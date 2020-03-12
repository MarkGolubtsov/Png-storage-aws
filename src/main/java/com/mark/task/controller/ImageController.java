package com.mark.task.controller;

import com.mark.task.controller.exception.NotValidImageException;
import com.mark.task.service.ImageService;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequestMapping("/images")
public class ImageController {
    private static final MediaType MEDIA_TYPE_PNG = MediaType.IMAGE_PNG;
    private static final String NOT_PNG_FILE = "Suffix image isn't png.";
    private static final String VERY_SMALL_IMAGE = "Sorry but size of image is very small.";
    private static final String THERE_IS_NO_IMAGE = "There is no image.";
    private static final String SUFFIX_IMAGE = ".png";
    private final ImageService imageService;
    private final long minSizeImage;

    public ImageController(ImageService imageService, @Value("${min-image-size}") long minSizeImage) {
        this.imageService = imageService;
        this.minSizeImage = minSizeImage;
    }

    @PostMapping
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void saveImage(@RequestParam MultipartFile image) {
        validateImage(image);
        imageService.saveImage(image.getResource());
    }

    @GetMapping("/{imageName}")
    public ResponseEntity<byte[]> readImage(@PathVariable String imageName) {
        byte[] content = imageService.readImage(imageName);
        return ResponseEntity.ok()
                .contentType(MEDIA_TYPE_PNG)
                .body(content);
    }

    @GetMapping("/random")
    public ResponseEntity<byte[]> readRandomImage() {
        byte[] content = imageService.readRandomImage();
        return ResponseEntity.ok()
                .contentType(MEDIA_TYPE_PNG)
                .body(content);
    }

    private void validateImage(MultipartFile image) {
        if (image.isEmpty()) {
            throw new NotValidImageException(THERE_IS_NO_IMAGE);
        }
        if (!image.getOriginalFilename().endsWith(SUFFIX_IMAGE)) {
            throw new NotValidImageException(NOT_PNG_FILE);
        }
        if (image.getSize() < minSizeImage) {
            throw new NotValidImageException(VERY_SMALL_IMAGE);
        }
    }
}
