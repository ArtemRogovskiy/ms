package com.example.ms.resourceservice.web;

import java.io.IOException;
import java.io.InputStream;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;

import org.apache.tomcat.util.http.fileupload.IOUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.example.ms.resourceservice.domain.Resource;
import com.example.ms.resourceservice.repo.ResourceRepository;
import com.example.ms.resourceservice.service.FileStore;
import com.example.ms.resourceservice.service.ResourceService;

import javax.servlet.http.HttpServletResponse;

@RestController
@RequestMapping(path = "/resources")
public class ResourceController {
    ResourceService resourceService;
    ResourceRepository resourceRepository;
    FileStore fileStore;

    @Autowired
    public ResourceController(ResourceService resourceService, ResourceRepository resourceRepository, FileStore fileStore) {
        this.resourceService = resourceService;
        this.fileStore = fileStore;
        this.resourceRepository = resourceRepository;
    }

    protected ResourceController() {
    }

    @RequestMapping(method = RequestMethod.POST)
    public Map<String, Integer> uploadResource(InputStream dataStream) {
        String uuid = UUID.randomUUID().toString();
        fileStore.upload(uuid, dataStream, Optional.empty());
        Resource resource = new Resource(uuid);
        resourceRepository.save(resource);
        return Map.of("id", resource.getId());
    }

    @RequestMapping(value = "/{resourceId}", method = RequestMethod.GET)
    public void getResource(@PathVariable(value = "resourceId") int resourceId, HttpServletResponse response) throws IOException {
        InputStream inputStream = fileStore.download(resourceId);
        IOUtils.copy(inputStream, response.getOutputStream());
        response.flushBuffer();
    }

    @DeleteMapping
    public Map<String, List<Integer>> deleteResource(@RequestParam Integer[] id) {
        return Map.of("ids", fileStore.delete(id));
    }
}
