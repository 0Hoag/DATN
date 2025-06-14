package com.fpl.datn.controller;

import java.io.File;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.FileSystemResource;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.fpl.datn.dto.ApiResponse;
import com.fpl.datn.service.DatabaseBackupService;

@RestController
@RequestMapping("/system")
public class SystemController {

    @Autowired
    private DatabaseBackupService backupService;

    @PostMapping("/backup")
    public ApiResponse<Map<String, String>> backupDatabase(
            @RequestParam(defaultValue = "datn") String dbName,
            @RequestParam(defaultValue = "root") String dbUser,
            @RequestParam(defaultValue = "123456") String dbPassword) {

        Map<String, String> result = backupService.backupDatabase(dbName, dbUser, dbPassword);
        return ApiResponse.<Map<String, String>>builder()
                .code(1000)
                .message("Kết quả sao lưu cơ sở dữ liệu")
                .result(result)
                .build();
    }

    @GetMapping("/backups")
    public ApiResponse<List<Map<String, Object>>> getBackupList() {
        List<Map<String, Object>> backups = backupService.getBackupList();
        return ApiResponse.<List<Map<String, Object>>>builder()
                .code(1000)
                .message("Danh sách các bản sao lưu")
                .result(backups)
                .build();
    }

    @GetMapping("/download/{fileName}")
    public ResponseEntity<Resource> downloadBackup(@PathVariable String fileName) {
        try {
            File file = new File("backup", fileName);

            if (!file.exists()) {
                return ResponseEntity.notFound().build();
            }

            Resource resource = new FileSystemResource(file);

            return ResponseEntity.ok()
                    .contentType(MediaType.APPLICATION_OCTET_STREAM)
                    .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + fileName + "\"")
                    .body(resource);
        } catch (Exception e) {
            return ResponseEntity.internalServerError().build();
        }
    }

    @PostMapping("/restore")
    public ApiResponse<Map<String, String>> restoreDatabase(@RequestBody Map<String, String> request) {

        String fileName = request.get("fileName");
        String dbName = request.getOrDefault("dbName", "datn");
        String dbUser = request.getOrDefault("dbUser", "root");
        String dbPassword = request.getOrDefault("dbPassword", "123456");

        // Kiểm tra fileName
        if (fileName == null || fileName.trim().isEmpty()) {
            Map<String, String> errorResult = Map.of(
                    "status", "ERROR",
                    "message", "Vui lòng nhập tên file backup"
            );
            return ApiResponse.<Map<String, String>>builder()
                    .code(1000)
                    .message("Kết quả phục hồi dữ liệu")
                    .result(errorResult)
                    .build();
        }

        Map<String, String> result = backupService.restoreDatabase(fileName, dbName, dbUser, dbPassword);
        return ApiResponse.<Map<String, String>>builder()
                .code(1000)
                .message("Kết quả phục hồi dữ liệu")
                .result(result)
                .build();
    }

    @PostMapping("/delete/{fileName}")
    public ApiResponse<Map<String, String>> deleteBackup(@PathVariable String fileName) {
        Map<String, String> result = backupService.deleteBackup(fileName);
        return ApiResponse.<Map<String, String>>builder()
                .code(1000)
                .message("Kết quả xóa bản sao lưu")
                .result(result)
                .build();
    }

    @GetMapping("/info")
    public ApiResponse<Map<String, Object>> getSystemInfo() {
        Map<String, Object> systemInfo = backupService.getSystemInfo();
        return ApiResponse.<Map<String, Object>>builder()
                .code(1000)
                .message("Thông tin hệ thống")
                .result(systemInfo)
                .build();
    }
}