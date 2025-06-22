package com.fpl.datn.service.Product;

import com.cloudinary.Cloudinary;
import com.cloudinary.utils.ObjectUtils;
import com.fpl.datn.models.UploadImage;
import com.fpl.datn.repository.UploadImageRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.Map;

@Service
@RequiredArgsConstructor
@Slf4j
public class UploadImageService {

    private final Cloudinary cloudinary;
    private final UploadImageRepository uploadImageRepo;

    public UploadImage upload(MultipartFile file) {
        try {
            Map<?, ?> uploadResult = cloudinary.uploader().upload(
                    file.getBytes(),
                    ObjectUtils.asMap("folder", "product-images")
            );

            String url = (String) uploadResult.get("secure_url");

            UploadImage image = UploadImage.builder()
                    .url(url)
                    .fileName(file.getOriginalFilename())
                    .build();

            return uploadImageRepo.save(image);
        } catch (IOException e) {
            log.error("Upload image failed: {}", e.getMessage());
            throw new RuntimeException("Failed to upload image to Cloudinary");
        }
    }

    public UploadImage getById(Integer id) {
        return uploadImageRepo.findById(id)
                .orElseThrow(() -> new RuntimeException("UploadImage not found"));
    }

    public void delete(Integer id) {
        UploadImage image = getById(id);
        uploadImageRepo.delete(image);
    }
}