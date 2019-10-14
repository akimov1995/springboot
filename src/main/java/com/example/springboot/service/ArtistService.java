package com.example.springboot.service;

import com.example.springboot.model.Artist;
import com.example.springboot.repository.ArtistRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Optional;

@Service
public class ArtistService {

    @Autowired
    private ArtistRepository artistRepository;

    public List<Artist> findAll() {
        List<Artist> artists = new ArrayList<>();
        Iterator<Artist> iterator = artistRepository.findAll().iterator();
        while (iterator.hasNext()) {
            artists.add(iterator.next());
        }
        return artists;
    }

    public void addArtist(Artist artist) {
        artistRepository.save(artist);
    }

    public boolean updateArtist(Artist artist) {
        if (artistRepository.existsById(artist.getId())) {
            artistRepository.save(artist);
            return true;
        } else {
            return false;
        }
    }

    public boolean deleteArtist(int id) {
        if (artistRepository.existsById(id)) {
            artistRepository.deleteById(id);
            return true;
        } else {
            return false;
        }
    }

    public Optional<Artist> findById(int id) {
        return artistRepository.findById(id);
    }
}

