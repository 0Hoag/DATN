<template>
  <a-layout-header style="background: #fff; padding: 0">
    <div class="d-flex justify-content-between align-items-center px-3">
      <!-- Trái: Trang chủ -->
      <div class="d-flex align-items-center gap-2 fs-5">
        <font-awesome-icon icon="house" />
        <router-link :to="{name: 'home'}" class="text-dark">Trang chủ</router-link>
      </div>

      <!-- Phải: Admin và Đăng xuất -->
      <div class="d-flex align-items-center gap-3 fs-5">
        <div class="d-flex align-items-center gap-2">
          <font-awesome-icon icon="user" />
          <span>{{ store.userInfo?.fullName }}</span>
        </div>
        <div class="d-flex align-items-center gap-2" style="cursor: pointer">
          <font-awesome-icon icon="arrow-right-from-bracket" />
          <a @click="handleLogout">Đăng xuất</a>
        </div>
      </div>
    </div>
  </a-layout-header>
</template>

<script setup>
import { useAuth } from "@/composable/useAuth";
import { useUserStore } from "@/store/userStore";
import { onMounted, watchEffect } from "vue";
import { useRouter } from "vue-router";
const router = useRouter();

const {  logout } = useAuth();
const store = useUserStore();

const handleLogout = async () => {
  await logout();
  router.push("/admin/login");
};



</script>
