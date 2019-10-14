package com.example.springboot;

import com.example.springboot.model.Album;
import com.example.springboot.model.Artist;
import com.example.springboot.model.Track;
import com.example.springboot.repository.AlbumRepository;
import com.example.springboot.repository.ArtistRepository;
import com.example.springboot.repository.TrackRepository;
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
public class TrackControllerIntegrationTest {
    @Autowired
    private ArtistRepository artistRepository;

    @Autowired
    private AlbumRepository albumRepository;

    @Autowired
    private TrackRepository trackRepository;

    @Autowired
    private TestRestTemplate restTemplate;

    @Test
    public void createTrackTest() {
        Artist artist = new Artist();
        artist.setName("name");
        artist.setLabelName("labelName");

        Artist createdArtist = createArtist(artist);
        Album album = new Album();
        album.setName("album");
        album.setGenre("genre");
        album.setArtist(createdArtist);
        Album createdAlbum = createAlbum(album);

        Track track = new Track();
        track.setName("track");
        track.setYear(2019);
        track.setAlbum(createdAlbum);
        createTrack(track);

        ResponseEntity<Track> response = restTemplate
                .postForEntity("/trackRestController/addTrack", track, Track.class);

        assertThat(response.getStatusCode(), is(HttpStatus.CREATED));
        assertThat(response.getBody().getId(), notNullValue());
        assertThat(response.getBody().getName(), is("track"));
        assertThat(response.getBody().getYear(), is(2019));
        assertThat(response.getBody().getAlbum(), is(createdAlbum));
    }

    @Test
    public void updateTrackTest() {
        Artist artist = new Artist();
        artist.setName("name");
        artist.setLabelName("labelName");

        Artist createdArtist = createArtist(artist);
        Album album = new Album();
        album.setName("album");
        album.setGenre("genre");
        album.setArtist(createdArtist);
        Album createdAlbum = createAlbum(album);

        Track track = new Track();
        track.setName("track");
        track.setYear(2019);
        track.setAlbum(createdAlbum);
        Track createdTrack = createTrack(track);

        track.setName("test new name");

        HttpEntity<Track> entity = new HttpEntity<Track>(track);

        ResponseEntity<Track> response = restTemplate.exchange("/trackRestController/updateTrack",
                HttpMethod.PUT, entity, Track.class);

        assertThat(response.getStatusCode(), is(HttpStatus.OK));
        assertThat(response.getBody().getId(), notNullValue());
        assertThat(response.getBody().getName(), is("test new name"));
        assertThat(response.getBody().getYear(), is(2019));
        assertThat(response.getBody().getAlbum(), is(createdAlbum));
    }

    @Test
    public void getTrackTest() {
        Artist artist = new Artist();
        artist.setName("name");
        artist.setLabelName("labelName");

        Artist createdArtist = createArtist(artist);
        Album album = new Album();
        album.setName("album");
        album.setGenre("genre");
        album.setArtist(createdArtist);
        Album createdAlbum = createAlbum(album);

        Track track = new Track();
        track.setName("track");
        track.setYear(2019);
        track.setAlbum(createdAlbum);
        int id = createTrack(track).getId();

        Track createdTrack = restTemplate.getForObject("/trackRestController/track?id={id}", Track.class, id);
        assertThat(createdTrack.getName(), is("track"));
        assertThat(createdTrack.getYear(), is(2019));
        assertThat(createdTrack.getAlbum(), is(track.getAlbum()));
    }

    @Test
    public void getTrackListTest() {
        Artist artist = new Artist();
        artist.setName("name");
        artist.setLabelName("labelName");

        Artist createdArtist = createArtist(artist);
        Album album = new Album();
        album.setName("album");
        album.setGenre("genre");
        album.setArtist(createdArtist);
        Album createdAlbum = createAlbum(album);

        Track track = new Track();
        track.setName("track");
        track.setYear(2019);
        track.setAlbum(createdAlbum);
        int id = createTrack(track).getId();

        ResponseEntity<List<Track>> response = restTemplate.exchange("/trackRestController/tracks",
                HttpMethod.GET, null, new ParameterizedTypeReference<List<Track>>() {
                });
        List<Track> tracks = response.getBody();

        assertThat(tracks, hasSize(1));
        assertThat(tracks.get(0).getId(), is(id));
        assertThat(tracks.get(0).getName(), is("track"));
        assertThat(tracks.get(0).getYear(), is(2019));
        assertThat(tracks.get(0).getAlbum(), is(track.getAlbum()));
    }

    @Test
    public void deleteTrackTest() {
        Artist artist = new Artist();
        artist.setName("name");
        artist.setLabelName("labelName");

        Artist createdArtist = createArtist(artist);
        String name = "test name";
        Album album = new Album();
        album.setName(name);
        album.setGenre("genre");
        album.setArtist(createdArtist);
        Album createdAlbum = createAlbum(album);

        Track track = new Track();
        track.setName("track");
        track.setYear(2019);
        track.setAlbum(createdAlbum);
        int id = createTrack(track).getId();

        ResponseEntity response = restTemplate.exchange("/trackRestController/deleteTrack?id={id}",
                HttpMethod.DELETE, null, String.class, id);
        assertThat(response.getStatusCode(), is(HttpStatus.ACCEPTED));
    }

    private Artist createArtist(Artist artist) {
        return artistRepository.save(artist);
    }

    private Album createAlbum(Album album) {
        return albumRepository.save(album);
    }

    private Track createTrack(Track track) {
        return trackRepository.save(track);
    }

    @After
    public void resetDb() {
        trackRepository.deleteAll();
        albumRepository.deleteAll();
        artistRepository.deleteAll();
    }
}
