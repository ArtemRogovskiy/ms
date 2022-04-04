package com.example.ms.resourceservice.service;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.ms.resourceservice.domain.Resource;
import com.example.ms.resourceservice.exception.ResourceNotFoundException;
import com.example.ms.resourceservice.repository.ResourceRepository;

@Service
public class ResourceService {

    private final ResourceRepository resourceRepository;

    @Autowired
    public ResourceService(ResourceRepository resourceRepository) {
        this.resourceRepository = resourceRepository;
    }

    public Resource createResource(String key) {
        return resourceRepository.save(new Resource(key));
    }

    public Resource getResource(Integer id) {
        return resourceRepository.findById(id)
                .orElseThrow(ResourceNotFoundException::new);
    }

    public List<Resource> getResources(Integer[] ids) {
        return Arrays.stream(ids).map(resourceRepository::findById)
                .filter(Optional::isPresent)
                .map(Optional::get).toList();
    }

    public List<Integer> deleteResources(List<Resource> objectsToDelete) {
        List<Integer> deletedIds = objectsToDelete.stream()
                .map(Resource::getId).toList();
        resourceRepository.deleteAllById(deletedIds);
        return deletedIds;
    }

    public String getKeyById(Integer resourceId) {
        Optional<Resource> resource = resourceRepository.findById(resourceId);
        return resource.orElseThrow(ResourceNotFoundException::new).getKey();
    }

    public void deleteResource(Integer id) {
        resourceRepository.deleteById(id);
    }
}
