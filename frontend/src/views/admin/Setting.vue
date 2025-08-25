<template>
  <h1>Nhật ký hoạt động</h1>
  <nav>
    <div class="nav nav-tabs" id="nav-tab" role="tablist">
      <button
        class="nav-link active"
        id="nav-home-tab"
        data-bs-toggle="tab"
        data-bs-target="#nav-home"
        type="button"
        role="tab"
        aria-controls="nav-home"
        aria-selected="true"
      >
        Hoạt động người dùng
      </button>
      <button
        class="nav-link"
        id="nav-profile-tab"
        data-bs-toggle="tab"
        data-bs-target="#nav-profile"
        type="button"
        role="tab"
        aria-controls="nav-profile"
        aria-selected="false"
      >
        Hoạt động giao dịch
      </button>
    </div>
  </nav>
  <div class="tab-content" id="nav-tabContent">
    <div
      class="tab-pane fade show active"
      id="nav-home"
      role="tabpanel"
      aria-labelledby="nav-home-tab"
      tabindex="0"
    >
      <div
        class="border rounded p-3 shadow"
        style="height: 500px; overflow-y: auto"
        ref="scrollContainer"
      >
        <p class="mb-1" v-for="log in activiLyLogs">
          {{ [dayjs(log.createAt).format("DD-MM-YYYY HH:mm:ss")] }} {{ [log.action] }}
          {{ [log.module] }} {{ `UserId: ${log.userId}` }} -
          {{ log.description }}
          {{ `(Object ID): ${log.objectID}` }}
        </p>
        <div ref="loadMoreTrigger" class="flex-shrink-0" style="height: 20px"></div>
        <p v-if="isLoading" class="mt-2">Đang tải...</p>
      </div>
    </div>
    <div
      class="tab-pane fade"
      id="nav-profile"
      role="tabpanel"
      aria-labelledby="nav-profile-tab"
      tabindex="0"
    >
      <table class="table table-hover">
        <thead>
          <tr>
            <td>STT</td>
            <td>Trạng thái</td>
            <td>Tổng tiền</td>
            <td>Kiểu</td>
            <td>Mã đơn hàng</td>
            <td>Mô tả</td>
            <td>Ngày tạo</td>
          </tr>
        </thead>
        <tbody></tbody>
        <tr v-for="(item, index) in transactionLogs">
          <td>
            {{
              (paginationTransactionLogs.current - 1) *
                paginationTransactionLogs.pageSize +
              index +
              1
            }}
          </td>
          <td>{{ item.status }}</td>
          <td>{{ item.amount }}</td>
          <td>{{ item.actionType }}</td>
          <td>{{ item.orderId }}</td>
          <td>{{ item.message }}</td>
          <td>{{ item.createdAt }}</td>
        </tr>
      </table>
      <div class="d-flex justify-content-end mt-3">
        <a-pagination
          v-model:current="paginationTransactionLogs.current"
          :total="paginationTransactionLogs.total"
          simple
          :page-size="paginationTransactionLogs.pageSize"
        />
      </div>
    </div>
  </div>
</template>
<script setup>
import { handleError } from "@/api/functions/common";
import { LogService } from "@/api/service/LogService";
import dayjs from "dayjs";
import { onMounted, onUnmounted, ref, watch } from "vue";

const activiLyLogs = ref([]);
const transactionLogs = ref([]);

const loadMoreTrigger = ref(null);
const scrollContainer = ref(null);
const isLoading = ref(false);
const paginationActivityLogs = ref({
  current: 1,
  pageSize: 30,
  total: 0,
  totalPages: null,
});
const paginationTransactionLogs = ref({
  current: 1,
  pageSize: 10,
  total: 0,
});
let observer = null;

const fetchActivityLogs = async () => {
  if (isLoading.value) return;
  if (
    paginationActivityLogs.value.totalPages &&
    paginationActivityLogs.value.current > paginationActivityLogs.value.totalPages
  ) {
    return; // hết dữ liệu
  }
  isLoading.value = true;
  try {
    const res = await LogService.fetchActivityLogs({
      size: paginationActivityLogs.value.pageSize,
      page: paginationActivityLogs.value.current,
    });
    activiLyLogs.value.push(...res.result.data);
    paginationActivityLogs.value.totalPages = res.result.totalPages;
    paginationActivityLogs.value.current++;
  } catch (error) {
    handleError(error);
  } finally {
    isLoading.value = false;
  }
};
const fetchTransactionLogs = async () => {
  try {
    const res = await LogService.fetchTransactionLogs({
      size: paginationTransactionLogs.value.pageSize,
      page: paginationTransactionLogs.value.current,
    });
    transactionLogs.value = res.result.data;
    paginationTransactionLogs.value.total = res.result.totalElements;
  } catch (error) {
    handleError(error);
  }
};
watch(
  () => paginationTransactionLogs.value.current,
  () => {
    fetchTransactionLogs();
  }
);
onMounted(() => {
  fetchActivityLogs();
  fetchTransactionLogs();
  observer = new IntersectionObserver(
    (entries) => {
      if (entries[0].isIntersecting) {
        fetchActivityLogs();
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
