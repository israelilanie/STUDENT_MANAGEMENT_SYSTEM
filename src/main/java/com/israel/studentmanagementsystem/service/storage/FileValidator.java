package com.israel.studentmanagementsystem.service.storage;

import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@Component
public class FileValidator {
    private static final List<String> ALLOWED_IMAGE_TYPES = List.of(
            "image/jpeg",
            "image/jpg",
            "image/png",
            "image/webp"
    );

    private static final long MAX_AVATAR_SIZE = 5 * 1024 * 1024;

    public void validateAvatar(MultipartFile file) {

        if (file == null || file.isEmpty()) {
            throw new IllegalArgumentException("File cannot be empty");
        }

        if (file.getSize() > MAX_AVATAR_SIZE) {
            throw new IllegalArgumentException(
                    "File size exceeds 5MB limit. " +
                            "File size: " + (file.getSize() / 1024 / 1024) + "MB");
        }

        String contentType = file.getContentType();
        if (contentType == null ||
                !ALLOWED_IMAGE_TYPES.contains(contentType.toLowerCase())) {
            throw new IllegalArgumentException(
                    "Invalid file type. Allowed types: JPEG, PNG, WebP");
        }
    }
}
