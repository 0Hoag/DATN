<template>
  <div class="flex-grow-1 d-flex align-items-center justify-content-center vh-100">
    <div class="text-center error-container">
      <h1 class="display-1 fw-bold text-warning">403</h1>
      <h2 class="mb-3">Truy cập bị từ chối</h2>
      <p class="lead mb-4 text-muted">
        Bạn không có quyền truy cập vào trang này.
        <br />
        Vui lòng liên hệ quản trị viên nếu bạn cho rằng đây là lỗi.
      </p>
      <a href="/" class="btn btn-primary btn-lg me-2 mt-3"> Về trang chủ </a>
      <a href="/contact" class="btn btn-outline-primary btn-lg me-2 mt-3"> Liên hệ hỗ trợ </a>
      <button @click="goBack" class="btn btn-outline-primary btn-lg mt-3"> Quay về trang trước đó</button>
    </div>
  </div>
</template>
<script setup>
import { useRouter } from 'vue-router';

const router = useRouter();
function goBack() {
  router.back();
}
</script>
