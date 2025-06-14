package com.fpl.datn.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.io.File;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;

@Slf4j
@Service
public class DatabaseBackupService {

    private static final String MYSQLDUMP_PATH = "C:\\Program Files\\MySQL\\MySQL Server 8.0\\bin\\mysqldump.exe";
    private static final String MYSQL_PATH = "C:\\Program Files\\MySQL\\MySQL Server 8.0\\bin\\mysql.exe";
    private static final String BACKUP_DIR = "backup";

    public Map<String, String> backupDatabase(String dbName, String dbUser, String dbPassword) {
        Map<String, String> result = new HashMap<>();
        try {
            // Tạo thư mục
            new File(BACKUP_DIR).mkdirs();

            String timestamp = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyyMMdd_HHmmss"));
            String backupFileName = dbName + "_" + timestamp + ".sql";
            String backupFilePath = BACKUP_DIR + File.separator + backupFileName;

            String command = String.format("\"%s\" -u%s -p%s %s -r \"%s\"",
                    MYSQLDUMP_PATH, dbUser, dbPassword, dbName, backupFilePath);

            Process process = Runtime.getRuntime().exec(command);
            int exitCode = process.waitFor();

            if (exitCode == 0) {
                result.put("status", "SUCCESS");
                result.put("message", "Sao lưu thành công: " + backupFileName);
                result.put("fileName", backupFileName);
            } else {
                result.put("status", "FAILED");
                result.put("message", "Sao lưu thất bại");
            }
        } catch (Exception e) {
            result.put("status", "ERROR");
            result.put("message", "Lỗi: " + e.getMessage());
        }
        return result;
    }

    public List<Map<String, Object>> getBackupList() {
        List<Map<String, Object>> list = new ArrayList<>();
        File folder = new File(BACKUP_DIR);
        File[] files = folder.listFiles((dir, name) -> name.endsWith(".sql"));

        if (files != null) {
            for (File file : files) {
                Map<String, Object> fileInfo = new HashMap<>();
                fileInfo.put("fileName", file.getName());
                fileInfo.put("size", file.length());
                fileInfo.put("lastModified", new Date(file.lastModified()));
                list.add(fileInfo);
            }
        }
        return list;
    }

    public Map<String, String> restoreDatabase(String fileName, String dbName, String dbUser, String dbPassword) {
        Map<String, String> result = new HashMap<>();
        try {
            String backupFilePath = BACKUP_DIR + File.separator + fileName;
            File backupFile = new File(backupFilePath);

            if (!backupFile.exists()) {
                result.put("status", "FAILED");
                result.put("message", "File không tồn tại: " + fileName);
                return result;
            }

            String command = String.format("\"%s\" -u%s -p%s %s < \"%s\"",
                    MYSQL_PATH, dbUser, dbPassword, dbName, backupFilePath);

            String[] finalCommand = {"cmd.exe", "/c", command};
            Process process = Runtime.getRuntime().exec(finalCommand);
            int exitCode = process.waitFor();

            if (exitCode == 0) {
                result.put("status", "SUCCESS");
                result.put("message", "Phục hồi thành công từ: " + fileName);
            } else {
                result.put("status", "FAILED");
                result.put("message", "Phục hồi thất bại");
            }
        } catch (Exception e) {
            result.put("status", "ERROR");
            result.put("message", "Lỗi: " + e.getMessage());
        }
        return result;
    }

    public Map<String, String> deleteBackup(String fileName) {
        Map<String, String> result = new HashMap<>();
        File file = new File(BACKUP_DIR, fileName);

        if (file.exists() && file.delete()) {
            result.put("status", "SUCCESS");
            result.put("message", "Đã xóa: " + fileName);
        } else {
            result.put("status", "FAILED");
            result.put("message", "Không thể xóa: " + fileName);
        }
        return result;
    }

    public Map<String, Object> getSystemInfo() {
        Map<String, Object> info = new HashMap<>();
        info.put("javaVersion", System.getProperty("java.version"));
        info.put("osName", System.getProperty("os.name"));
        info.put("serverTime", LocalDateTime.now());
        info.put("backupDirectory", new File(BACKUP_DIR).getAbsolutePath());

        // Kiểm tra MySQL
        File mysqldump = new File(MYSQLDUMP_PATH);
        info.put("mysqlAvailable", mysqldump.exists());

        // Đếm backup files
        File backupDir = new File(BACKUP_DIR);
        File[] files = backupDir.listFiles((dir, name) -> name.endsWith(".sql"));
        info.put("totalBackups", files != null ? files.length : 0);

        return info;
    }
}