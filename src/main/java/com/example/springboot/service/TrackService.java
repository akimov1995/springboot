package com.example.springboot.service;

import com.example.springboot.model.Track;
import com.example.springboot.repository.TrackRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Optional;

@Service
public class TrackService {

    @Autowired
    private TrackRepository trackRepository;

    public List<Track> findAll() {
        List<Track> tracks = new ArrayList<>();
        Iterator<Track> iterator = trackRepository.findAll().iterator();
        while (iterator.hasNext()) {
            tracks.add(iterator.next());
        }
        return tracks;
    }

    public void addTrack(Track track) {
        trackRepository.save(track);
    }

    public boolean updateTrack(Track track) {
        if (trackRepository.existsById(track.getId())) {
            trackRepository.save(track);
            return true;
        } else {
            return false;
        }
    }

    public boolean deleteTrack(int id) {
        if (trackRepository.existsById(id)) {
            trackRepository.deleteById(id);
            return true;
        } else {
            return false;
        }
    }

    public Optional<Track> findById(int id) {
        return trackRepository.findById(id);
    }
}

