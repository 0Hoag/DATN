<template>
  <h1>Nhật ký hoạt động</h1>
  <div
    class="border rounded p-3 shadow"
    style="height: 500px; overflow-y: auto"
    ref="scrollContainer"
  >
    <p class="mb-1" v-for="log in logs">{{ [log.action] }} {{ log.description }}</p>
    <div ref="loadMoreTrigger" class="flex-shrink-0" style="height: 20px"></div>
    <p v-if="isLoading" class="mt-2">Đang tải...</p>
  </div>
</template>
<script setup>
import { handleError } from "@/api/functions/common";
import { ActivityLogService } from "@/api/service/ActivityLogService";
import { onMounted, onUnmounted, ref } from "vue";

const logs = ref([]);

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
