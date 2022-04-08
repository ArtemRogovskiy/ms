package com.example.ms.songservice.dto;

import lombok.Data;

@Data
public class SongDto {

    private final String name;
    private final String artist;
    private final String album;
    private final String length;
    private final String resourceId;
    private final String year;
}
