package com.example.ms.resourceservice.controller;

import java.io.IOException;
import java.io.InputStream;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import org.apache.tomcat.util.http.fileupload.IOUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.example.ms.resourceservice.service.FileStoreService;

import javax.servlet.http.HttpServletResponse;

@RestController
@RequestMapping(path = "/resources")
public class ResourceController {

    private final FileStoreService fileStoreService;

    @Autowired
    public ResourceController(FileStoreService fileStoreService) {
        this.fileStoreService = fileStoreService;
    }

    @RequestMapping(method = RequestMethod.POST)
    public Map<String, Integer> uploadResource(InputStream dataStream) {
        Integer resourceId = fileStoreService.upload(dataStream, Optional.empty());
        return Map.of("id", resourceId);
    }

    @RequestMapping(value = "/{resourceId}", method = RequestMethod.GET)
    public void getResource(@PathVariable(value = "resourceId") int resourceId, HttpServletResponse response) throws IOException {
        InputStream inputStream = fileStoreService.download(resourceId);
        IOUtils.copy(inputStream, response.getOutputStream());
        response.flushBuffer();
    }

    @DeleteMapping
    public Map<String, List<Integer>> deleteResource(@RequestParam Integer[] id) {
        return Map.of("ids", fileStoreService.delete(id));
    }
}
