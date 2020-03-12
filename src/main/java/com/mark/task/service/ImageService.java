package com.mark.task.service;

import org.springframework.core.io.Resource;

public interface ImageService {
    byte[] readImage(String name);

    String saveImage(Resource image);

    byte[] readRandomImage();
}
