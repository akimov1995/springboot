package com.example.springboot.repository;

import com.example.springboot.model.Track;
import org.springframework.data.repository.CrudRepository;

public interface TrackRepository extends CrudRepository<Track, Integer> {
}
