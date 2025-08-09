<!-- src/views/NotFound.vue -->
<template>
   <div class="flex-grow-1 d-flex align-items-center justify-content-center vh-100">
        <div class="text-center error-container">

            <h1 class="display-1 fw-bold text-danger">404</h1>
            <h2 class="mb-3">Không tìm thấy trang</h2>
            <p class="lead mb-4 text-muted">
                Rất tiếc, trang bạn tìm kiếm không tồn tại.
                <br>
                Vui lòng kiểm tra lại địa chỉ hoặc quay về trang chủ.
            </p>
           
            <a href="/" class="btn btn-primary btn-lg mt-3">
                Về trang chủ
            </a>
        </div>
    </div>
</template>

<script setup>
// Không cần gì đặc biệt nếu chỉ là hiển thị
</script>

<style scoped>
.not-found {
  text-align: center;
  padding: 50px;
}
</style>
