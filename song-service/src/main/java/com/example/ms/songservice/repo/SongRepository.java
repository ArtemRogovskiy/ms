package com.example.ms.songservice.repo;

import org.springframework.data.repository.CrudRepository;
import org.springframework.data.rest.core.annotation.RestResource;

import com.example.ms.songservice.domain.Song;

@RestResource(exported = false)
public interface SongRepository extends CrudRepository<Song, Integer> {
}
