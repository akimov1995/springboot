package com.example.springboot.service;

import com.example.springboot.model.Album;
import com.example.springboot.repository.AlbumRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Optional;

@Service
public class AlbumService {

    @Autowired
    private AlbumRepository albumRepository;

    public List<Album> findAll() {
        List<Album> albums = new ArrayList<>();
        Iterator<Album> iterator = albumRepository.findAll().iterator();
        while (iterator.hasNext()) {
            albums.add(iterator.next());
        }
        return albums;
    }

    public void addAlbum(Album album) {
        albumRepository.save(album);
    }

    public boolean updateAlbum(Album album) {
        if (albumRepository.existsById(album.getId())) {
            albumRepository.save(album);
            return true;
        } else {
            return false;
        }
    }

    public boolean deleteAlbum(int id) {
        if (albumRepository.existsById(id)) {
            albumRepository.deleteById(id);
            return true;
        } else {
            return false;
        }
    }

    public Optional<Album> findById(int id) {
        return albumRepository.findById(id);
    }

    public List<Album> findByGenre(String genre) {
        return albumRepository.findByGenre(genre);
    }

    public List<Album> findByName(String name) {
        return albumRepository.findByName(name);
    }
}
