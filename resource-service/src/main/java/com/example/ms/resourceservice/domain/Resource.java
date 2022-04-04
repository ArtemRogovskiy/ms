package com.example.ms.resourceservice.domain;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@NoArgsConstructor
public class Resource {

    @Id
    @GeneratedValue
    private Integer id;

    @Column(unique = true)
    private String key;

    public Resource(String key) {
        this.key = key;
    }
}
