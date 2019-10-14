package com.example.springboot.controller;

import com.example.springboot.model.Album;
import com.example.springboot.service.AlbumService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping(value = "/albumRestController")
public class AlbumController {

    @Autowired
    private AlbumService albumService;

    @GetMapping(value = "/albums", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<List<Album>> listAlbums() {
        return new ResponseEntity<List<Album>>(albumService.findAll(), HttpStatus.OK);
    }

    @PostMapping(value = "/addAlbum", consumes = MediaType.APPLICATION_JSON_VALUE,
            produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Album> addAlbum(@RequestBody Album album) {
        albumService.addAlbum(album);
        return new ResponseEntity<Album>(album, HttpStatus.CREATED);
    }

    @DeleteMapping(value = "/deleteAlbum", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity deleteAlbum(@RequestParam int id) {
        if (albumService.deleteAlbum(id)) {
            return new ResponseEntity(HttpStatus.ACCEPTED);
        } else {
            return new ResponseEntity(HttpStatus.NOT_FOUND);
        }
    }

    @GetMapping(value = "/album", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Album> getAlbumById(@RequestParam int id) {
        Album album = null;
        if (albumService.findById(id).isPresent()) {
            album = albumService.findById(id).get();
            return new ResponseEntity<Album>(album, HttpStatus.OK);
        } else {
            return new ResponseEntity<Album>(album, HttpStatus.NOT_FOUND);
        }
    }

    @PutMapping(value = "/updateAlbum", consumes = MediaType.APPLICATION_JSON_VALUE,
            produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Album> updateAlbum(@RequestBody Album album) {
        if (albumService.updateAlbum(album)) {
            return new ResponseEntity<Album>(album, HttpStatus.OK);
        } else {
            return new ResponseEntity<Album>(album, HttpStatus.NOT_FOUND);
        }
    }

    @GetMapping(value = "/albumByGenre", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<List<Album>> getAlbumByGenre(@RequestParam String genre) {
        return new ResponseEntity<List<Album>>(albumService.findByGenre(genre), HttpStatus.OK);
    }

    @GetMapping(value = "/albumByName", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<List<Album>> getAlbumByName(@RequestParam String name) {
        return new ResponseEntity<List<Album>>(albumService.findByName(name), HttpStatus.OK);
    }
}
