package com.fpl.datn.controller;

import com.fpl.datn.models.UploadImage;
import com.fpl.datn.service.Product.UploadImageService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.Map;

@RestController
@RequestMapping("/upload-image")
@RequiredArgsConstructor
public class UploadImageController {

    private final UploadImageService uploadImageService;

    @PostMapping("/")
    public ResponseEntity<?> upload(@RequestParam("url") MultipartFile file) {
        UploadImage image = uploadImageService.upload(file);
        return ResponseEntity.ok(Map.of(
                "id", image.getId(),
                "url", image.getUrl()
        ));
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> get(@PathVariable Integer id) {
        UploadImage image = uploadImageService.getById(id);
        return ResponseEntity.ok(image);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> delete(@PathVariable Integer id) {
        uploadImageService.delete(id);
        return ResponseEntity.ok(Map.of("message", "Deleted successfully"));
    }
}