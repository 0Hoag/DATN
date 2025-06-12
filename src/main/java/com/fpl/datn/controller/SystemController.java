package com.fpl.datn.controller;

import java.io.IOException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;
import java.util.Map;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.fpl.datn.dto.ApiResponse;

@RestController
@RequestMapping("/system")
public class SystemController {

    @PostMapping("/backup")
    public ApiResponse<Map<String, String>> backupDatabase(
            @RequestParam(defaultValue = "datn") String dbName,
            @RequestParam(defaultValue = "root") String dbUser,
            @RequestParam(defaultValue = "123456") String dbPassword) {

        Map<String, String> result = new HashMap<>();

        try {
            String timestamp = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyyMMdd_HHmmss"));
            String backupFile = "backup_" + dbName + "_" + timestamp + ".sql";

            // Phát hiện hệ điều hành
            String os = System.getProperty("os.name").toLowerCase();
            String[] command;

            if (os.contains("win")) {
                // Windows
                command = new String[]{
                        "cmd", "/c",
                        String.format("mysqldump -u %s -p%s --routines --triggers --single-transaction %s > %s",
                                dbUser, dbPassword, dbName, backupFile)
                };
            } else {
                // Linux/Mac
                command = new String[]{
                        "sh", "-c",
                        String.format("mysqldump -u %s -p%s --routines --triggers --single-transaction %s > %s",
                                dbUser, dbPassword, dbName, backupFile)
                };
            }

            // Chạy lệnh
            Process process = Runtime.getRuntime().exec(command);
            int exitCode = process.waitFor();

            if (exitCode == 0) {
                result.put("status", "SUCCESS");
                result.put("message", "Database backup completed successfully");
                result.put("backupFile", backupFile);
                result.put("timestamp", timestamp);
                result.put("os", os);
            } else {
                result.put("status", "FAILED");
                result.put("message", "Database backup failed. Exit code: " + exitCode);
                result.put("os", os);
            }

        } catch (IOException | InterruptedException e) {
            result.put("status", "ERROR");
            result.put("message", "Error: " + e.getMessage());
            result.put("suggestion", "Try using GET /system/backup-command to get manual commands");
        }

        return ApiResponse.<Map<String, String>>builder()
                .result(result)
                .build();
    }

    @GetMapping("/backup-command")
    public ApiResponse<Map<String, String>> getBackupCommand(
            @RequestParam(defaultValue = "datn_db") String dbName,
            @RequestParam(defaultValue = "root") String dbUser) {

        Map<String, String> result = new HashMap<>();
        String timestamp = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyyMMdd_HHmmss"));
        String backupFile = "backup_" + dbName + "_" + timestamp + ".sql";
        String os = System.getProperty("os.name").toLowerCase();

        if (os.contains("win")) {
            result.put("backupCommand", "mysqldump -u " + dbUser + " -p " + dbName + " > " + backupFile);
            result.put("compressCommand", "powershell Compress-Archive " + backupFile + " " + backupFile + ".zip");
            result.put("restoreCommand", "mysql -u " + dbUser + " -p " + dbName + " < " + backupFile);
            result.put("platform", "Windows");
            result.put("note", "Run these commands in Command Prompt (cmd)");
        } else {
            result.put("backupCommand", "mysqldump -u " + dbUser + " -p " + dbName + " > " + backupFile);
            result.put("compressCommand", "gzip " + backupFile);
            result.put("restoreCommand", "mysql -u " + dbUser + " -p " + dbName + " < " + backupFile);
            result.put("platform", "Linux/Mac");
            result.put("note", "Run these commands in Terminal");
        }

        result.put("backupFile", backupFile);

        return ApiResponse.<Map<String, String>>builder()
                .result(result)
                .message("Copy and run these commands manually")
                .build();
    }

    @GetMapping("/info")
    public ApiResponse<Map<String, Object>> getSystemInfo() {
        Map<String, Object> systemInfo = new HashMap<>();

        systemInfo.put("osName", System.getProperty("os.name"));
        systemInfo.put("osVersion", System.getProperty("os.version"));
        systemInfo.put("javaVersion", System.getProperty("java.version"));
        systemInfo.put("serverTime", LocalDateTime.now());
        systemInfo.put("userDir", System.getProperty("user.dir"));

        return ApiResponse.<Map<String, Object>>builder()
                .result(systemInfo)
                .build();
    }
}
