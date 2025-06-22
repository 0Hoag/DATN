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
            Map<?, ?> result = cloudinary.uploader().upload(
                    file.getBytes(),
                    ObjectUtils.asMap("folder", "product-images")
            );

            UploadImage image = UploadImage.builder()
                    .url((String) result.get("secure_url"))
                    .publicId((String) result.get("public_id"))
                    .fileName(file.getOriginalFilename())
                    .build();

            return uploadImageRepo.save(image);
        } catch (IOException e) {
            log.error("Upload failed", e);
            throw new RuntimeException("Upload to Cloudinary failed");
        }
    }

    public UploadImage getById(Integer id) {
        return uploadImageRepo.findById(id)
                .orElseThrow(() -> new RuntimeException("Image not found"));
    }

    public void delete(Integer id) {
        UploadImage image = getById(id);
        try {
            cloudinary.uploader().destroy(image.getPublicId(), ObjectUtils.emptyMap());
        } catch (IOException e) {
            log.error("Cloudinary deletion failed", e);
        }
        uploadImageRepo.delete(image);
    }
}
