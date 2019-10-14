package com.example.springboot.controller;

import com.example.springboot.model.Track;
import com.example.springboot.service.TrackService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping(value = "/trackRestController")
public class TrackController {

    @Autowired
    private TrackService trackService;

    @GetMapping(value = "/tracks", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<List<Track>> listTracks() {
        return new ResponseEntity<List<Track>>(trackService.findAll(), HttpStatus.OK);
    }

    @PostMapping(value = "/addTrack", consumes = MediaType.APPLICATION_JSON_VALUE,
            produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Track> addTrack(@RequestBody Track track) {
        trackService.addTrack(track);
        return new ResponseEntity<Track>(track, HttpStatus.CREATED);
    }

    @DeleteMapping(value = "/deleteTrack", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity deleteTrack(@RequestParam int id) {
        if (trackService.deleteTrack(id)) {
            return new ResponseEntity(HttpStatus.ACCEPTED);
        } else {
            return new ResponseEntity(HttpStatus.NOT_FOUND);
        }
    }

    @GetMapping(value = "/track", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Track> getTrackById(@RequestParam int id) {
        Track track = null;
        if (trackService.findById(id).isPresent()) {
            track = trackService.findById(id).get();
            return new ResponseEntity<Track>(track, HttpStatus.OK);
        } else {
            return new ResponseEntity<Track>(track, HttpStatus.NOT_FOUND);
        }
    }

    @PutMapping(value = "/updateTrack", consumes = MediaType.APPLICATION_JSON_VALUE,
            produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Track> updateTrack(@RequestBody Track track) {
        if (trackService.updateTrack(track)) {
            return new ResponseEntity<Track>(track, HttpStatus.OK);
        } else {
            return new ResponseEntity<Track>(track, HttpStatus.NOT_FOUND);
        }
    }
}
