<template>
  <h1>Nhật ký hoạt động</h1>
  <div
    class="border rounded p-3 shadow"
    style="height: 500px; overflow-y: auto"
    ref="scrollContainer"
  >
    <p class="mb-1" v-for="log in logs">{{ [log.time] }} {{ log.message }}</p>
    <div ref="loadMoreTrigger" class="flex-shrink-0" style="height: 20px"></div>
    <p v-if="isLoading" class="mt-2">Đang tải...</p>
  </div>
</template>
<script setup>
import { handleError } from "@/api/functions/common";
import { ActivityLogService } from "@/api/service/ActivityLogService";
import { onMounted, onUnmounted, ref } from "vue";

const logs = ref([
  {
    time: "2025-08-25 14:05:33",
    level: "INFO",
    message: "User 'admin' logged in from 192.168.1.10",
  },
  {
    time: "2025-08-25 14:06:10",
    level: "ERROR",
    message: "Failed login attempt for user 'guest'",
  },
  { time: "2025-08-25 14:07:55", level: "WARNING", message: "Low disk space on server" },
  {
    time: "2025-08-25 14:10:21",
    level: "INFO",
    message: "User 'thanh' created a new post",
  },
  {
    time: "2025-08-25 14:12:44",
    level: "SUCCESS",
    message: "Order #10234 processed successfully",
  },
  { time: "2025-08-25 14:15:03", level: "ERROR", message: "Database connection timeout" },
  {
    time: "2025-08-25 14:18:17",
    level: "INFO",
    message: "User 'hoa' updated profile information",
  },
  { time: "2025-08-25 14:20:32", level: "INFO", message: "System backup completed" },
  {
    time: "2025-08-25 14:22:59",
    level: "WARNING",
    message: "High memory usage detected",
  },
  {
    time: "2025-08-25 14:25:18",
    level: "ERROR",
    message: "API request failed with status 500",
  },
  { time: "2025-08-25 14:27:41", level: "INFO", message: "User 'minh' logged out" },
  {
    time: "2025-08-25 14:30:08",
    level: "SUCCESS",
    message: "Email sent to user 'hoa@example.com'",
  },
  {
    time: "2025-08-25 14:32:27",
    level: "INFO",
    message: "User 'guest' viewed product page",
  },
  {
    time: "2025-08-25 14:34:52",
    level: "WARNING",
    message: "Unusual login attempt detected",
  },
  {
    time: "2025-08-25 14:36:19",
    level: "INFO",
    message: "Scheduled task 'cleanup' executed",
  },
  {
    time: "2025-08-25 14:38:44",
    level: "ERROR",
    message: "Failed to send email notification",
  },
  {
    time: "2025-08-25 14:40:11",
    level: "SUCCESS",
    message: "Password changed for user 'admin'",
  },
  {
    time: "2025-08-25 14:42:36",
    level: "INFO",
    message: "Session expired for user 'guest'",
  },
  {
    time: "2025-08-25 14:44:59",
    level: "WARNING",
    message: "CPU temperature is above normal",
  },
  { time: "2025-08-25 14:47:23", level: "INFO", message: "User 'admin' logged out" },
  {
    time: "2025-08-25 14:05:33",
    level: "INFO",
    message: "User 'admin' logged in from 192.168.1.10",
  },
  {
    time: "2025-08-25 14:06:10",
    level: "ERROR",
    message: "Failed login attempt for user 'guest'",
  },
  { time: "2025-08-25 14:07:55", level: "WARNING", message: "Low disk space on server" },
  {
    time: "2025-08-25 14:10:21",
    level: "INFO",
    message: "User 'thanh' created a new post",
  },
  {
    time: "2025-08-25 14:12:44",
    level: "SUCCESS",
    message: "Order #10234 processed successfully",
  },
  { time: "2025-08-25 14:15:03", level: "ERROR", message: "Database connection timeout" },
  {
    time: "2025-08-25 14:18:17",
    level: "INFO",
    message: "User 'hoa' updated profile information",
  },
  { time: "2025-08-25 14:20:32", level: "INFO", message: "System backup completed" },
  {
    time: "2025-08-25 14:22:59",
    level: "WARNING",
    message: "High memory usage detected",
  },
  {
    time: "2025-08-25 14:25:18",
    level: "ERROR",
    message: "API request failed with status 500",
  },
  { time: "2025-08-25 14:27:41", level: "INFO", message: "User 'minh' logged out" },
  {
    time: "2025-08-25 14:30:08",
    level: "SUCCESS",
    message: "Email sent to user 'hoa@example.com'",
  },
  {
    time: "2025-08-25 14:32:27",
    level: "INFO",
    message: "User 'guest' viewed product page",
  },
  {
    time: "2025-08-25 14:34:52",
    level: "WARNING",
    message: "Unusual login attempt detected",
  },
  {
    time: "2025-08-25 14:36:19",
    level: "INFO",
    message: "Scheduled task 'cleanup' executed",
  },
  {
    time: "2025-08-25 14:38:44",
    level: "ERROR",
    message: "Failed to send email notification",
  },
  {
    time: "2025-08-25 14:40:11",
    level: "SUCCESS",
    message: "Password changed for user 'admin'",
  },
  {
    time: "2025-08-25 14:42:36",
    level: "INFO",
    message: "Session expired for user 'guest'",
  },
  {
    time: "2025-08-25 14:44:59",
    level: "WARNING",
    message: "CPU temperature is above normal",
  },
  { time: "2025-08-25 14:47:23", level: "INFO", message: "User 'admin' logged out" },
  {
    time: "2025-08-25 14:05:33",
    level: "INFO",
    message: "User 'admin' logged in from 192.168.1.10",
  },
  {
    time: "2025-08-25 14:06:10",
    level: "ERROR",
    message: "Failed login attempt for user 'guest'",
  },
  { time: "2025-08-25 14:07:55", level: "WARNING", message: "Low disk space on server" },
  {
    time: "2025-08-25 14:10:21",
    level: "INFO",
    message: "User 'thanh' created a new post",
  },
  {
    time: "2025-08-25 14:12:44",
    level: "SUCCESS",
    message: "Order #10234 processed successfully",
  },
  { time: "2025-08-25 14:15:03", level: "ERROR", message: "Database connection timeout" },
  {
    time: "2025-08-25 14:18:17",
    level: "INFO",
    message: "User 'hoa' updated profile information",
  },
  { time: "2025-08-25 14:20:32", level: "INFO", message: "System backup completed" },
  {
    time: "2025-08-25 14:22:59",
    level: "WARNING",
    message: "High memory usage detected",
  },
  {
    time: "2025-08-25 14:25:18",
    level: "ERROR",
    message: "API request failed with status 500",
  },
  { time: "2025-08-25 14:27:41", level: "INFO", message: "User 'minh' logged out" },
  {
    time: "2025-08-25 14:30:08",
    level: "SUCCESS",
    message: "Email sent to user 'hoa@example.com'",
  },
  {
    time: "2025-08-25 14:32:27",
    level: "INFO",
    message: "User 'guest' viewed product page",
  },
  {
    time: "2025-08-25 14:34:52",
    level: "WARNING",
    message: "Unusual login attempt detected",
  },
  {
    time: "2025-08-25 14:36:19",
    level: "INFO",
    message: "Scheduled task 'cleanup' executed",
  },
  {
    time: "2025-08-25 14:38:44",
    level: "ERROR",
    message: "Failed to send email notification",
  },
  {
    time: "2025-08-25 14:40:11",
    level: "SUCCESS",
    message: "Password changed for user 'admin'",
  },
  {
    time: "2025-08-25 14:42:36",
    level: "INFO",
    message: "Session expired for user 'guest'",
  },
  {
    time: "2025-08-25 14:44:59",
    level: "WARNING",
    message: "CPU temperature is above normal",
  },
  { time: "2025-08-25 14:47:23", level: "INFO", message: "User 'admin' logged out" },
  {
    time: "2025-08-25 14:05:33",
    level: "INFO",
    message: "User 'admin' logged in from 192.168.1.10",
  },
  {
    time: "2025-08-25 14:06:10",
    level: "ERROR",
    message: "Failed login attempt for user 'guest'",
  },
  { time: "2025-08-25 14:07:55", level: "WARNING", message: "Low disk space on server" },
  {
    time: "2025-08-25 14:10:21",
    level: "INFO",
    message: "User 'thanh' created a new post",
  },
  {
    time: "2025-08-25 14:12:44",
    level: "SUCCESS",
    message: "Order #10234 processed successfully",
  },
  { time: "2025-08-25 14:15:03", level: "ERROR", message: "Database connection timeout" },
  {
    time: "2025-08-25 14:18:17",
    level: "INFO",
    message: "User 'hoa' updated profile information",
  },
  { time: "2025-08-25 14:20:32", level: "INFO", message: "System backup completed" },
  {
    time: "2025-08-25 14:22:59",
    level: "WARNING",
    message: "High memory usage detected",
  },
  {
    time: "2025-08-25 14:25:18",
    level: "ERROR",
    message: "API request failed with status 500",
  },
  { time: "2025-08-25 14:27:41", level: "INFO", message: "User 'minh' logged out" },
  {
    time: "2025-08-25 14:30:08",
    level: "SUCCESS",
    message: "Email sent to user 'hoa@example.com'",
  },
  {
    time: "2025-08-25 14:32:27",
    level: "INFO",
    message: "User 'guest' viewed product page",
  },
  {
    time: "2025-08-25 14:34:52",
    level: "WARNING",
    message: "Unusual login attempt detected",
  },
  {
    time: "2025-08-25 14:36:19",
    level: "INFO",
    message: "Scheduled task 'cleanup' executed",
  },
  {
    time: "2025-08-25 14:38:44",
    level: "ERROR",
    message: "Failed to send email notification",
  },
  {
    time: "2025-08-25 14:40:11",
    level: "SUCCESS",
    message: "Password changed for user 'admin'",
  },
  {
    time: "2025-08-25 14:42:36",
    level: "INFO",
    message: "Session expired for user 'guest'",
  },
  {
    time: "2025-08-25 14:44:59",
    level: "WARNING",
    message: "CPU temperature is above normal",
  },
  { time: "2025-08-25 14:47:23", level: "INFO", message: "User 'admin' logged out" },
]);

const loadMoreTrigger = ref(null);
const scrollContainer = ref(null);
const isLoading = ref(false);
const pagination = ref({
  current: 1,
  pageSize: 30,
  total: 0,
  totalPages: null,
});
let observer = null;

const fetchListLogs = async () => {
  if (isLoading.value) return;
  if (
    pagination.value.totalPages &&
    pagination.value.current > pagination.value.totalPages
  ) {
    return; // hết dữ liệu
  }
  isLoading.value = true;
  try {
    const res = await ActivityLogService.fetchListLogs({
      size: pagination.value.pageSize,
      page: pagination.value.current,
    });
    logs.value.push(...res.result.data);
    pagination.value.totalPages = res.result.totalPages;
    pagination.value.current++;
  } catch (error) {
    handleError(error);
  } finally {
    isLoading.value = false;
  }
};

onMounted(() => {
  fetchListLogs();
  observer = new IntersectionObserver(
    (entries) => {
      if (entries[0].isIntersecting) {
        // fetchListLogs();
      }
    },
    {
      root: scrollContainer.value,
      rootMargin: "100px",
      threshold: 0.1,
    }
  );
  if (loadMoreTrigger.value) {
    observer.observe(loadMoreTrigger.value);
  }
});
onUnmounted(() => {
  if (observer) {
    observer.disconnect();
  }
});
</script>
