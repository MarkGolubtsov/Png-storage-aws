package com.mark.task.service.impl;

import com.mark.task.repository.ImageMetadataRepository;
import com.mark.task.repository.entity.ImageMetaData;
import com.mark.task.service.ImageService;
import com.mark.task.service.exception.RepeatImageException;
import com.mark.task.storage.ResourceStorage;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.time.LocalDateTime;

@Service
public class ImageServiceImpl implements ImageService {
    private static final Logger logger = LogManager.getLogger(ImageServiceImpl.class);
    private static final String IMAGE_EXIST = "Image %s is exist";
    private final ResourceStorage resourceStorage;
    private final ImageMetadataRepository imageMetadataRepository;

    public ImageServiceImpl(ResourceStorage resourceStorage, ImageMetadataRepository imageMetadataRepository) {
        this.resourceStorage = resourceStorage;
        this.imageMetadataRepository = imageMetadataRepository;
    }

    @Override
    public byte[] readImage(String name) {
        return resourceStorage.readResource(name);
    }

    @Override
    public String saveImage(Resource resource) {
        String imageName = resource.getFilename();
        if (imageMetadataRepository.existsByName(imageName)) {
            throw new RepeatImageException(String.format(IMAGE_EXIST, imageName));
        }
        String name = resourceStorage.saveResource(resource);
        saveMetadataFile(resource);
        return name;
    }

    @Override
    public byte[] readRandomImage() {
        ImageMetaData imageMetaData = imageMetadataRepository.findRandom();
        return resourceStorage.readResource(imageMetaData.getName());
    }

    private void saveMetadataFile(Resource resource) {
        ImageMetaData imageMetaData = new ImageMetaData();
        imageMetaData.setName(resource.getFilename());
        try {
            imageMetaData.setSize(resource.contentLength());
        } catch (IOException e) {
            logger.warn(e.getMessage());
        }
        imageMetaData.setCreationDate(LocalDateTime.now());
        imageMetadataRepository.save(imageMetaData);
    }
}
