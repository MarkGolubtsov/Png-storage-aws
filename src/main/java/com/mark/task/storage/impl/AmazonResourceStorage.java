package com.mark.task.storage.impl;

import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.model.AmazonS3Exception;
import com.amazonaws.services.s3.model.ObjectMetadata;
import com.mark.task.configuration.AWSConfigurationProperties;
import com.mark.task.storage.ResourceStorage;
import com.mark.task.storage.exception.AmazonResourceStorageException;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.time.LocalDateTime;

@Service
public class AmazonResourceStorage implements ResourceStorage {
    private static final Logger logger = LogManager.getLogger(AmazonResourceStorage.class);
    private static final String COULD_READ_FILE = "Could not read resource %s";
    private static final String COULD_GET_FILE_FROM_S3 = "Could not get resource %s from s3";
    private static final String OBJECT_METADATA_NAME = "Name";
    private static final String OBJECT_METADATA_SIZE = "Size";
    private static final String OBJECT_METADATA_SAVE_DATE = "Save date";
    private final AmazonS3 s3Client;
    private final String bucketName;

    public AmazonResourceStorage(AmazonS3 s3Client, AWSConfigurationProperties amazonProperties) {
        this.s3Client = s3Client;
        this.bucketName = amazonProperties.getBucketName();
    }

    @Override
    public String saveResource(Resource resource) {
        try {
            s3Client.putObject(bucketName, resource.getFilename(), resource.getInputStream(), getObjectMetaDataFromRecourse(resource));
        } catch (IOException e) {
            logger.info(e.getMessage());
            throw new AmazonResourceStorageException(e.getMessage());
        }
        return resource.getFilename();
    }

    @Override
    public byte[] readResource(String fileName) {
        try {
            return s3Client.getObject(bucketName, fileName)
                    .getObjectContent()
                    .readAllBytes();
        } catch (IOException e) {
            throw new AmazonResourceStorageException(String.format(COULD_READ_FILE, fileName));
        } catch (AmazonS3Exception e) {
            throw new AmazonResourceStorageException(String.format(COULD_GET_FILE_FROM_S3, fileName));
        }
    }

    private ObjectMetadata getObjectMetaDataFromRecourse(Resource resource) {
        ObjectMetadata objectMetadata = new ObjectMetadata();
        objectMetadata.addUserMetadata(OBJECT_METADATA_NAME, resource.getFilename());
        try {
            objectMetadata.addUserMetadata(OBJECT_METADATA_SIZE, String.valueOf(resource.contentLength()));
        } catch (IOException e) {
            logger.warn(e.getMessage());
        }
        objectMetadata.addUserMetadata(OBJECT_METADATA_SAVE_DATE, String.valueOf(LocalDateTime.now()));
        objectMetadata.getUserMetadata();
        return objectMetadata;
    }
}
