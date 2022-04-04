package com.example.ms.songservice.web;

import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.StreamSupport;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.example.ms.songservice.domain.Song;
import com.example.ms.songservice.dto.SongDto;
import com.example.ms.songservice.exceptions.SongNotFoundException;
import com.example.ms.songservice.mapper.SongMapper;
import com.example.ms.songservice.repo.SongRepository;

@RestController
@RequestMapping(path = "/songs")
public class SongController {

    SongRepository songRepository;
    SongMapper songMapper;

    @Autowired
    public SongController(SongRepository songRepository, SongMapper songMapper) {
        this.songRepository = songRepository;
        this.songMapper = songMapper;
    }

    @PostMapping
    public Map<String, Integer> createSongMeta(@RequestBody SongDto songDto) {
        Song song = songMapper.toSong(songDto);
        songRepository.save(song);
        return Map.of("id", song.getId());
    }

    @GetMapping(value = "/{songId}")
    public SongDto getSongMeta(@PathVariable(value = "songId") int songId) {
        Optional<Song> songOpt = songRepository.findById(songId);
        return songMapper.toDto(songOpt.orElseThrow(SongNotFoundException::new));
    }

    @DeleteMapping
    public Map<String, List<Integer>> deleteSongMeta(@RequestParam List<Integer> id) {
        Iterable<Song> songs = songRepository.findAllById(id);
        songRepository.deleteAll(songs);
        List<Integer> deletedIds = StreamSupport.stream(songs.spliterator(), false).map(Song::getId).toList();
        return Map.of("ids", deletedIds);
    }
}
