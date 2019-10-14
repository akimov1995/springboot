package com.example.springboot;

import com.example.springboot.model.Artist;
import com.example.springboot.repository.ArtistRepository;
import org.junit.After;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.*;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.List;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.CoreMatchers.*;
import static org.hamcrest.core.IsNull.notNullValue;

@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class ArtistControllerIntegrationTest {
    @Autowired
    private ArtistRepository repository;

    @Autowired
    private TestRestTemplate restTemplate;

    @Test
    public void createArtistTest() {
        Artist artist = new Artist();
        artist.setName("name");
        artist.setLabelName("labelName");

        createArtist(artist);

        ResponseEntity<Artist> response = restTemplate
                .postForEntity("/artistRestController/addArtist", artist, Artist.class);

        assertThat(response.getStatusCode(), is(HttpStatus.CREATED));
        assertThat(response.getBody().getId(), notNullValue());
        assertThat(response.getBody().getName(), is("name"));
    }

    @Test
    public void updateArtistTest() {
        Artist artist = new Artist();
        artist.setName("name");
        artist.setLabelName("labelName");

        Artist artistToUpdate = createArtist(artist);
        artistToUpdate.setName("new name");
        HttpEntity<Artist> entity = new HttpEntity<Artist>(artistToUpdate);

        ResponseEntity<Artist> response = restTemplate.exchange("/artistRestController/updateArtist",
                HttpMethod.PUT, entity, Artist.class);
        assertThat(response.getStatusCode(), is(HttpStatus.OK));
        assertThat(response.getBody().getId(), is(artistToUpdate.getId()));
        assertThat(response.getBody().getName(), is("new name"));
        assertThat(response.getBody().getLabelName(), is("labelName"));
    }

    @Test
    public void getArtistTest() {
        Artist artist = new Artist();
        artist.setName("name");
        artist.setLabelName("label");

        int id = createArtist(artist).getId();

        Artist createdArtist = restTemplate.getForObject("/artistRestController/artist?id={id}", Artist.class, id);
        System.out.println(createdArtist);
        assertThat(createdArtist.getName(), is("name"));
        assertThat(createdArtist.getLabelName(), is("label"));
    }

    @Test
    public void getArtistListTest() {
        Artist artist1 = new Artist();
        artist1.setName("n1");
        artist1.setLabelName("l1");

        Artist artist2 = new Artist();
        artist2.setName("n2");
        artist2.setLabelName("l2");


        int id1 = createArtist(artist1).getId();
        int id2 = createArtist(artist2).getId();

        ResponseEntity<List<Artist>> response = restTemplate.exchange("/artistRestController/artists",
                HttpMethod.GET, null, new ParameterizedTypeReference<List<Artist>>() {
                });
        List<Artist> artists = response.getBody();

        assertThat(artists, hasSize(2));
        assertThat(artists.get(0).getId(), is(id1));
        assertThat(artists.get(0).getName(), is("n1"));
        assertThat(artists.get(0).getLabelName(), is("l1"));
        assertThat(artists.get(1).getId(), is(id2));
        assertThat(artists.get(1).getName(), is("n2"));
        assertThat(artists.get(1).getLabelName(), is("l2"));
    }

    @Test
    public void deleteArtistTest() {
        Artist artist = new Artist();
        artist.setName("name");
        artist.setLabelName("label");

        int id = createArtist(artist).getId();
        ResponseEntity response = restTemplate.exchange("/artistRestController/deleteArtist?id={id}",
                HttpMethod.DELETE, null, String.class, id);
        assertThat(response.getStatusCode(), is(HttpStatus.ACCEPTED));
    }

    private Artist createArtist(Artist artist) {
        return repository.save(artist);
    }

    @After
    public void resetDb() {
        repository.deleteAll();
    }
}
