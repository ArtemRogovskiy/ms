package com.example.ms.songservice.domain;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@NoArgsConstructor
public class Song {
    @Id
    @GeneratedValue
    private Integer id;

    @Column
    private String name;

    @Column
    private String artist;

    @Column
    private String album;

    @Column
    private String length;

    @Column(unique = true)
    private String resourceId;

    @Column
    private Integer year;

    public Song(String name, String artist, String album, String length, String resourceId, Integer year) {
        this.name = name;
        this.artist = artist;
        this.album = album;
        this.length = length;
        this.resourceId = resourceId;
        this.year = year;
    }
}
