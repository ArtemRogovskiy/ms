package com.example.ms.resourceservice.service;

import java.io.IOException;
import java.io.InputStream;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.amazonaws.AmazonServiceException;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.model.DeleteObjectsRequest;
import com.amazonaws.services.s3.model.ObjectMetadata;
import com.amazonaws.services.s3.model.PutObjectRequest;
import com.amazonaws.services.s3.model.S3Object;
import com.example.ms.resourceservice.domain.Resource;
import com.example.ms.resourceservice.dto.FileWithMeta;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

@Service
public class FileStoreService {

    @Value("${config.aws.s3.bucket-name}")
    private String bucketName;

    private final AmazonS3 amazonS3;
    private final ResourceService resourceService;
    private final QueueService queueService;

    @Autowired
    public FileStoreService(AmazonS3 amazonS3, ResourceService resourceService, QueueService queueService) {
        this.amazonS3 = amazonS3;
        this.resourceService = resourceService;
        this.queueService = queueService;
    }

    public Integer upload(MultipartFile file, Optional<Map<String, String>> optionalMetaData) {
        String key = UUID.randomUUID().toString();
        ObjectMetadata objectMetadata = new ObjectMetadata();
        optionalMetaData.ifPresent(map -> {
            if (!map.isEmpty()) {
                map.forEach(objectMetadata::addUserMetadata);
            }
        });
        try {
            PutObjectRequest request = new PutObjectRequest(bucketName, key, file.getInputStream(), objectMetadata);
            request.getRequestClientOptions().setReadLimit(800000000);
            amazonS3.putObject(request);
        } catch (AmazonServiceException e) {
            throw new IllegalStateException("Failed to upload the file", e);
        } catch (IOException e) {
            throw new IllegalStateException("Failed to get file Input Stream", e);
        }
        Resource resource = resourceService.createResource(key);
        queueService.sendResourceMeta(resource.getId());
        return resource.getId();
    }

    public FileWithMeta download(int resourceId) {
        String key = resourceService.getKeyById(resourceId);
        try {
            S3Object object = amazonS3.getObject(bucketName, key);
            InputStream inputStream = object.getObjectContent().getDelegateStream();
            ObjectMetadata metadata = object.getObjectMetadata();
            ObjectMapper mapper = new ObjectMapper();
            String jsonMetaData = mapper.writerWithDefaultPrettyPrinter()
                    .writeValueAsString(metadata.getUserMetadata());
            return new FileWithMeta(inputStream, jsonMetaData);
        } catch (AmazonServiceException | JsonProcessingException e) {
            throw new IllegalStateException("Failed to download the file");
        }
    }

    public List<Integer> delete(Integer[] id) {
        List<Resource> objectsToDelete = resourceService.getResources(id);

        String[] keys = objectsToDelete.stream()
                .map(Resource::getKey).toArray(String[]::new);
        try {
            DeleteObjectsRequest request = new DeleteObjectsRequest(bucketName).withKeys(keys);
            amazonS3.deleteObjects(request);
        } catch (AmazonServiceException e) {
            throw new IllegalStateException("Failed to delete files");
        }
        return resourceService.deleteResources(objectsToDelete);
    }
}
