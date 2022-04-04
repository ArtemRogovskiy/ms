package com.example.ms.resourceservice.service;

import java.io.InputStream;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import com.amazonaws.AmazonServiceException;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.model.DeleteObjectsRequest;
import com.amazonaws.services.s3.model.ObjectMetadata;
import com.amazonaws.services.s3.model.PutObjectRequest;
import com.amazonaws.services.s3.model.S3Object;
import com.amazonaws.services.s3.model.S3ObjectInputStream;
import com.example.ms.resourceservice.domain.Resource;

@Service
public class FileStore {

    @Value("${config.aws.s3.bucket-name}")
    private String bucketName;

    private final AmazonS3 amazonS3;

    private final ResourceService resourceService;

    @Autowired
    public FileStore(AmazonS3 amazonS3, ResourceService resourceService) {
        this.amazonS3 = amazonS3;
        this.resourceService = resourceService;
    }

    public void upload(String key,
                       InputStream inputStream,
                       Optional<Map<String, String>> optionalMetaData) {
        ObjectMetadata objectMetadata = new ObjectMetadata();
        optionalMetaData.ifPresent(map -> {
            if (!map.isEmpty()) {
                map.forEach(objectMetadata::addUserMetadata);
            }
        });
        try {
            PutObjectRequest request = new PutObjectRequest(bucketName, key, inputStream, objectMetadata);
            request.getRequestClientOptions().setReadLimit(800000000);
            amazonS3.putObject(request);
        } catch (AmazonServiceException e) {
            throw new IllegalStateException("Failed to upload the file", e);
        }
    }

    public InputStream download(int resourceId) {
        String key = resourceService.getKeyById(resourceId);
        try {
            S3Object object = amazonS3.getObject(bucketName, key);
            S3ObjectInputStream objectContent = object.getObjectContent();
            return objectContent.getDelegateStream();
        } catch (AmazonServiceException e) {
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
