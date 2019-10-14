package com.example.springboot;

import com.example.springboot.model.Album;
import com.example.springboot.model.Artist;
import com.example.springboot.repository.AlbumRepository;
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
public class AlbumControllerIntegrationTest {
    @Autowired
    private ArtistRepository artistRepository;

    @Autowired
    private AlbumRepository albumRepository;

    @Autowired
    private TestRestTemplate restTemplate;

    @Test
    public void createAlbumTest() {
        Artist artist = new Artist();
        artist.setName("name");
        artist.setLabelName("labelName");

        Artist createdArtist = createArtist(artist);
        Album album = new Album();
        album.setName("album");
        album.setGenre("genre");
        album.setArtist(createdArtist);
        createAlbum(album);

        ResponseEntity<Album> response = restTemplate
                .postForEntity("/albumRestController/addAlbum", album, Album.class);

        assertThat(response.getStatusCode(), is(HttpStatus.CREATED));
        assertThat(response.getBody().getId(), notNullValue());
        assertThat(response.getBody().getName(), is("album"));
        assertThat(response.getBody().getGenre(), is("genre"));
        assertThat(response.getBody().getArtist(), is(createdArtist));
    }

    @Test
    public void updateAlbumTest() {
        Artist artist = new Artist();
        artist.setName("name");
        artist.setLabelName("labelName");

        Artist createdArtist = createArtist(artist);
        Album album = new Album();
        album.setName("album");
        album.setGenre("genre");
        album.setArtist(createdArtist);
        createAlbum(album);

        album.setName("test new name");

        HttpEntity<Album> entity = new HttpEntity<Album>(album);

        ResponseEntity<Album> response = restTemplate.exchange("/albumRestController/updateAlbum",
                HttpMethod.PUT, entity, Album.class);

        assertThat(response.getStatusCode(), is(HttpStatus.OK));
        assertThat(response.getBody().getId(), notNullValue());
        assertThat(response.getBody().getName(), is("test new name"));
        assertThat(response.getBody().getGenre(), is("genre"));
        assertThat(response.getBody().getArtist(), is(createdArtist));
    }

    @Test
    public void getAlbumTest() {
        Artist artist = new Artist();
        artist.setName("name");
        artist.setLabelName("labelName");

        Artist createdArtist = createArtist(artist);
        Album album = new Album();
        album.setName("album");
        album.setGenre("genre");
        album.setArtist(createdArtist);
        int id = createAlbum(album).getId();

        Album createdAlbum = restTemplate.getForObject("/albumRestController/album?id={id}", Album.class, id);
        assertThat(createdAlbum.getName(), is("album"));
        assertThat(createdAlbum.getGenre(), is("genre"));
        assertThat(createdAlbum.getArtist(), is(album.getArtist()));
    }

    @Test
    public void getAlbumListTest() {
        Artist artist = new Artist();
        artist.setName("name");
        artist.setLabelName("labelName");

        Artist createdArtist = createArtist(artist);
        Album album = new Album();
        album.setName("album");
        album.setGenre("genre");
        album.setArtist(createdArtist);
        int id = createAlbum(album).getId();

        ResponseEntity<List<Album>> response = restTemplate.exchange("/albumRestController/albums",
                HttpMethod.GET, null, new ParameterizedTypeReference<List<Album>>() {
                });
        List<Album> albums = response.getBody();

        assertThat(albums, hasSize(1));
        assertThat(albums.get(0).getId(), is(id));
        assertThat(albums.get(0).getName(), is("album"));
        assertThat(albums.get(0).getGenre(), is("genre"));
        assertThat(albums.get(0).getArtist(), is(album.getArtist()));
    }

    @Test
    public void getAlbumByGenreTest() {
        Artist artist = new Artist();
        artist.setName("name");
        artist.setLabelName("labelName");

        Artist createdArtist = createArtist(artist);
        String genre = "rock";
        Album album = new Album();
        album.setName("album");
        album.setGenre(genre);
        album.setArtist(createdArtist);
        int id = createAlbum(album).getId();

        ResponseEntity<List<Album>> response = restTemplate.exchange("/albumRestController/albumByGenre?genre={genre}",
                HttpMethod.GET, null, new ParameterizedTypeReference<List<Album>>() {}, genre);
        List<Album> albums = response.getBody();
        assertThat(albums, hasSize(1));
        assertThat(albums.get(0).getId(), is(id));
        assertThat(albums.get(0).getName(), is("album"));
        assertThat(albums.get(0).getGenre(), is(genre));
        assertThat(albums.get(0).getArtist(), is(album.getArtist()));
    }

    @Test
    public void getAlbumByNameTest() {
        Artist artist = new Artist();
        artist.setName("name");
        artist.setLabelName("labelName");

        Artist createdArtist = createArtist(artist);
        String name = "test name";
        Album album = new Album();
        album.setName(name);
        album.setGenre("genre");
        album.setArtist(createdArtist);
        int id = createAlbum(album).getId();

        ResponseEntity<List<Album>> response = restTemplate.exchange("/albumRestController/albumByName?name={name}",
                HttpMethod.GET, null, new ParameterizedTypeReference<List<Album>>() {}, name);
        List<Album> albums = response.getBody();
        assertThat(albums, hasSize(1));
        assertThat(albums.get(0).getId(), is(id));
        assertThat(albums.get(0).getName(), is(name));
        assertThat(albums.get(0).getGenre(), is("genre"));
        assertThat(albums.get(0).getArtist(), is(album.getArtist()));
    }

    @Test
    public void deleteAlbumTest() {
        Artist artist = new Artist();
        artist.setName("name");
        artist.setLabelName("labelName");

        Artist createdArtist = createArtist(artist);
        String name = "test name";
        Album album = new Album();
        album.setName(name);
        album.setGenre("genre");
        album.setArtist(createdArtist);
        int id = createAlbum(album).getId();

        ResponseEntity response = restTemplate.exchange("/albumRestController/deleteAlbum?id={id}",
                HttpMethod.DELETE, null, String.class, id);
        assertThat(response.getStatusCode(), is(HttpStatus.ACCEPTED));
    }

    private Artist createArtist(Artist artist) {
        return artistRepository.save(artist);
    }

    private Album createAlbum(Album album) {
        return albumRepository.save(album);
    }

    @After
    public void resetDb() {
        albumRepository.deleteAll();
        artistRepository.deleteAll();
    }
}
