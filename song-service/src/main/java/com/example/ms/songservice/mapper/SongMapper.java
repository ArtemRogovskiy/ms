package com.example.ms.songservice.mapper;

import org.springframework.stereotype.Component;

import com.example.ms.songservice.domain.Song;
import com.example.ms.songservice.dto.SongDto;

@Component
public class SongMapper {

    public SongDto toDto(Song song) {
        return new SongDto(song.getName(), song.getArtist(), song.getAlbum(), song.getLength(), song.getResourceId(), song.getYear());
    }

    public Song toSong(SongDto songDto) {
        return new Song(songDto.getName(), songDto.getArtist(), songDto.getAlbum(), songDto.getLength(), songDto.getResourceId(), songDto.getYear());
    }
}
