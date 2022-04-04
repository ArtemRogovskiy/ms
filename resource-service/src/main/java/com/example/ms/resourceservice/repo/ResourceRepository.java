package com.example.ms.resourceservice.repo;

import org.springframework.data.repository.CrudRepository;
import org.springframework.data.rest.core.annotation.RestResource;

import com.example.ms.resourceservice.domain.Resource;

@RestResource(exported = false)
public interface ResourceRepository extends CrudRepository<Resource, Integer> {
}
