package com.example.ms.songservice.controller;

import java.util.List;
import java.util.Map;

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
import com.example.ms.songservice.service.SongService;

@RestController
@RequestMapping(path = "/songs")
public class SongController {

    SongService songService;

    @Autowired
    public SongController(SongService songService) {
        this.songService = songService;
    }

    @PostMapping
    public Map<String, Integer> createSongMeta(@RequestBody SongDto songDto) {
        Song song = songService.createSongMeta(songDto);
        return Map.of("id", song.getId());
    }

    @GetMapping(value = "/{songId}")
    public SongDto getSongMeta(@PathVariable(value = "songId") int songId) {
        return songService.getSongMeta(songId);
    }

    @DeleteMapping
    public Map<String, List<Integer>> deleteSongMeta(@RequestParam List<Integer> id) {
        List<Integer> deletedIds = songService.deleteSongMeta(id);
        return Map.of("ids", deletedIds);
    }
}
