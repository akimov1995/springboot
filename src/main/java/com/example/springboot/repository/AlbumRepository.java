package com.example.springboot.repository;

import com.example.springboot.model.Album;
import org.springframework.data.jdbc.repository.query.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface AlbumRepository extends CrudRepository<Album, Integer> {
    @Query("SELECT a FROM Album a WHERE a.name=:name")
    List<Album> findByName(@Param("name") String name);

    List<Album> findByGenre(String genre);
}
