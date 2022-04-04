package com.example.ms.songservice.service;

import java.util.List;
import java.util.Optional;
import java.util.stream.StreamSupport;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.ms.songservice.domain.Song;
import com.example.ms.songservice.dto.SongDto;
import com.example.ms.songservice.exception.SongNotFoundException;
import com.example.ms.songservice.mapper.SongMapper;
import com.example.ms.songservice.repository.SongRepository;

@Service
public class SongService {

    SongRepository songRepository;
    SongMapper songMapper;

    @Autowired
    public SongService(SongRepository songRepository, SongMapper songMapper) {
        this.songRepository = songRepository;
        this.songMapper = songMapper;
    }

    public Song createSongMeta(SongDto songDto) {
        Song song = songMapper.toSong(songDto);
        songRepository.save(song);
        return song;
    }

    public SongDto getSongMeta(int songId) {
        Optional<Song> songOpt = songRepository.findById(songId);
        return songMapper.toDto(songOpt.orElseThrow(SongNotFoundException::new));
    }

    public List<Integer> deleteSongMeta(List<Integer> ids) {
        Iterable<Song> songs = songRepository.findAllById(ids);
        songRepository.deleteAll(songs);
        return StreamSupport.stream(songs.spliterator(), false).map(Song::getId).toList();
    }
}
