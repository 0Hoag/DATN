package com.fpl.datn.controller;

import java.io.IOException;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import com.fpl.datn.dto.ApiResponse;
import com.fpl.datn.dto.PageResponse;
import com.fpl.datn.dto.response.Product.UploadImageResponse;
import com.fpl.datn.models.UploadImage;
import com.fpl.datn.service.Product.UploadImageService;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/upload-image")
@RequiredArgsConstructor
public class UploadImageController {

    private final UploadImageService uploadImageService;

    @PostMapping("/")
    public ApiResponse<List<UploadImageResponse>> upload(@RequestParam("files") List<MultipartFile> files) {
        if (files == null || files.isEmpty()) {
            throw new IllegalArgumentException("No file provided");
        }

        List<UploadImage> uploadedImages = (files.size() == 1)
                ? List.of(uploadImageService.upload(files.get(0)))
                : uploadImageService.uploadMultiple(files);

        List<UploadImageResponse> result = uploadedImages.stream()
                .map(img -> new UploadImageResponse(img.getId(), img.getUrl(), img.getFileName(), img.getPublicId()))
                .collect(Collectors.toList());

        return ApiResponse.<List<UploadImageResponse>>builder()
                .code(1000)
                .result(result)
                .build();
    }

    @GetMapping("/{id}")
    public ApiResponse<UploadImageResponse> get(@PathVariable Integer id) {
        UploadImage image = uploadImageService.getById(id);
        return ApiResponse.<UploadImageResponse>builder()
                .code(1000)
                .result(new UploadImageResponse(
                        image.getId(), image.getUrl(), image.getFileName(), image.getPublicId()))
                .build();
    }

    @DeleteMapping("/")
    public ApiResponse<String> deleteImages(@RequestBody List<Integer> id) throws IOException {
        uploadImageService.delete(id);
        return ApiResponse.<String>builder()
                .code(1000)
                .result("Xóa ảnh thành công")
                .build();
    }

    @GetMapping("/list")
    public ApiResponse<List<UploadImageResponse>> list() {
        return ApiResponse.<List<UploadImageResponse>>builder()
                .code(1000)
                .result(uploadImageService.list())
                .build();
    }

    @GetMapping("/get")
    public ApiResponse<PageResponse<UploadImageResponse>> getPage(
            @RequestParam(value = "page", required = false, defaultValue = "1") int page,
            @RequestParam(value = "size", required = false, defaultValue = "30") int size) {
        return ApiResponse.<PageResponse<UploadImageResponse>>builder()
                .code(1000)
                .result(uploadImageService.get(page, size))
                .build();
    }
}
