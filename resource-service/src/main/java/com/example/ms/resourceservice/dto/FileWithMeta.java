package com.example.ms.resourceservice.dto;

import java.io.InputStream;

import lombok.Data;

@Data
public class FileWithMeta {

    private final InputStream s3InputStream;
    private final String jsonMetaData;
}
