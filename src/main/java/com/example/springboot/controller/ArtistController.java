package com.example.springboot.controller;

import com.example.springboot.model.Artist;
import com.example.springboot.service.ArtistService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping(value = "/artistRestController")
public class ArtistController {

    @Autowired
    ArtistService artistService;

    @GetMapping(value = "/artists", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<List<Artist>> listArtist() {
        List<Artist> artists = artistService.findAll();
        return new ResponseEntity<List<Artist>>(artists, HttpStatus.OK);
    }

    @PostMapping(value = "/addArtist", consumes = MediaType.APPLICATION_JSON_VALUE,
            produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Artist> addArtist(@RequestBody Artist artist) {
        artistService.addArtist(artist);
        return new ResponseEntity(artist, HttpStatus.CREATED);
    }

    @DeleteMapping(value = "/deleteArtist", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity deleteArtist(@RequestParam int id) {
        if (artistService.deleteArtist(id)) {
            return new ResponseEntity(HttpStatus.ACCEPTED);
        } else {
            return new ResponseEntity(HttpStatus.NOT_FOUND);
        }
    }

    @GetMapping(value = "/artist", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Artist> getArtistById(@RequestParam int id) {
        Artist artist = null;
        if (artistService.findById(id).isPresent()) {
            artist = artistService.findById(id).get();
            return new ResponseEntity<Artist>(artist, HttpStatus.OK);
        } else {
            return new ResponseEntity<Artist>(artist, HttpStatus.NOT_FOUND);
        }
    }

    @PutMapping(value = "/updateArtist", consumes = MediaType.APPLICATION_JSON_VALUE,
            produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Artist> updateArtist(@RequestBody Artist artist) {
        if (artistService.updateArtist(artist)) {
            return new ResponseEntity<Artist>(artist, HttpStatus.OK);
        } else {
            return new ResponseEntity<Artist>(artist, HttpStatus.NOT_FOUND);
        }
    }
}
