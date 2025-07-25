<template>
  <a-layout-header>
    <div class="container">
  
      <!-- Header top -->
      <div class="header-top">
        <!-- Logo -->
        <div class="logo"><router-link to="/">TechZone</router-link></div>
        <!-- Tìm kiếm -->
        <div class="search-container flex-grow-1 mx-3">
          <a-input-search placeholder="Bạn cần tìm gì..." allow-clear />
        </div>

        <!-- Tài khoản + Giỏ hàng -->
        <div class="icon-group d-flex align-items-center gap-3 text-white">
          <a  href="#" class="d-flex align-items-center gap-1">
            <font-awesome-icon :icon="['fas', 'user']" />
            <a-dropdown>
              <a class="ant-dropdown-link" @click.prevent>
               {{ store.isLoggedIn ? store.userInfo?.fullName : 'Tài khoản' }}
                <font-awesome-icon icon="angle-down" />
              </a>
              <template #overlay>
                <a-menu>
                  <a-menu-item v-if="!store.isLoggedIn">
                    <router-link :to="{ name: 'user-login' }">Đăng nhập</router-link>
                  </a-menu-item>
                  <a-menu-item v-if="!store.isLoggedIn">
                    <router-link :to="{ name: 'register' }">Đăng ký</router-link>
                  </a-menu-item>
                  <a-menu-item v-if="store.isLoggedIn">
                    <router-link :to="{ name: 'account' }">Hồ sơ</router-link>
                  </a-menu-item>
                </a-menu>
              </template>
            </a-dropdown>
          </a>
          <router-link :to="{name: 'cart'}" class="d-flex align-items-center gap-1">
            <font-awesome-icon :icon="['fas', 'cart-shopping']" />
            <span class="d-none d-md-inline">Giỏ hàng</span>
          </router-link>
        </div>
      </div>

      <!-- Menu danh mục -->
      <!-- <div class="d-flex justify-content-center">
        <template v-for="cat in categories" :key="cat.id">
          <div v-if="!cat.children" class="menu-class">
            {{ cat.name }}
          </div>

          <div v-else class="menu-class submenu-wrapper">
            <a-menu
              mode="horizontal"
              :style="{ lineHeight: '48px' }"
              :overflowed-indicator="null"
            >
              <a-sub-menu :key="cat.slug" :title="cat.name">
                <a-menu-item v-for="child in cat.children" :key="child.slug">
                  {{ child.name }}
                </a-menu-item>
              </a-sub-menu>
            </a-menu>
          </div>
        </template>
      </div> -->
      <a-menu
        mode="horizontal"
        :style="{ lineHeight: '48px' }"
        :overflowed-indicator="null"
        class="ant-menu-dark"
      >
        <template v-for="cat in categories" :key="cat.slug">
          <a-menu-item v-if="!cat.children || cat.children.length === 0">
            <router-link :to="{ name: 'category', params: { slug: cat.slug } }">
              {{ cat.name }}
            </router-link>
          </a-menu-item>

          <a-sub-menu v-else :key="cat.slug" :title="cat.name">
            <a-menu-item v-for="child in cat.children" :key="child.slug">
              <router-link :to="{ name: 'category', params: { slug: child.slug } }">
                {{ child.name }}
              </router-link>
            </a-menu-item>
          </a-sub-menu>
        </template>
      </a-menu>
    </div>
  </a-layout-header>
</template>

<script setup>
import { useUserStore } from "@/store/userStore";
import {  ref } from "vue";

// const { isLoggedIn, userInfo } = useAuth();
const store = useUserStore();
console.log("isLoggedIn:", store.isLoggedIn);
console.log("userInfo:", store.userInfo);
const categories = ref([
  { id: 1, name: "Điện thoại", slug: "dien-thoai", children: [] },
  {
    id: 2,
    name: "Máy tính bảng",
    slug: "may-tinh-bang",
    children: [
      { id: 3, name: "iPad", slug: "ipad" },
      { id: 4, name: "Android", slug: "android" },
    ],
  },
  { id: 5, name: "Laptop", slug: "laptop", children: [] },
]);

</script>

<style scoped>
.ant-layout-header {
  background-color: #000 !important;
  height: auto !important;
  padding: 0;
}

/* Header top */
.header-top {
  display: flex;
  align-items: center;
  justify-content: space-between;
  padding: 0 24px;
  height: 64px;
}

/* Search container để giữ kích thước ổn định */
.search-container {
  max-width: 600px;
  display: flex;
  align-items: center;
}

/* Icon group bên phải */
.icon-group a {
  text-decoration: none;
  font-size: 14px;
  color: #fff;
}
.icon-group a:hover {
  text-decoration: underline;
}

/* Logo */
.logo {
  width: 120px;
  height: 32px;
  background: rgba(255, 255, 255, 0.3);
}

/* Menu style */

:deep(.ant-menu-dark) {
  background-color: transparent !important;
}

:deep(.ant-menu-dark .ant-menu-item),
:deep(.ant-menu-dark .ant-menu-submenu) {
  color: #fff !important;
  font-weight: bold;
  position: relative;
}

/* Xóa gạch chân cho router-link trong menu */
:deep(.ant-menu-dark .ant-menu-item a),
:deep(.ant-menu-dark .ant-menu-submenu a) {
  text-decoration: none !important;
  color: inherit;
}

/* Mũi tên submenu bằng lớp giả */
:deep(.ant-menu-dark .ant-menu-submenu-title) {
  display: flex;
  align-items: center;
  justify-content: center;
}

:deep(.ant-menu-dark .ant-menu-submenu-title::after) {
  content: "";
  display: inline-block;
  margin-left: 8px;
  border: solid #fff;
  border-width: 0 2px 2px 0;
  width: 8px;
  height: 8px;
  transform: rotate(45deg);
  /* transition: border-color 0.2s, transform 0.2s; */
  align-self: center;
}

:deep(.ant-menu-dark .ant-menu-submenu:hover > .ant-menu-submenu-title::after),
:deep(.ant-menu-dark .ant-menu-submenu-active > .ant-menu-submenu-title::after),
:deep(.ant-menu-dark .ant-menu-submenu-open > .ant-menu-submenu-title::after) {
  transform: rotate(-135deg);
}

:deep(.ant-menu-dark .ant-menu-item:hover) {
  /* opacity: 1; */
  background-color: rgba(255, 255, 255, 0.15);
  transition: all 0.3s ease;
}

:deep(.ant-menu-dark .ant-menu-item-selected) {
  color: #fff !important;
  font-weight: bold;
  background-color: transparent !important;
}

/* Cách chắc chắn nhất: override trực tiếp popup menu của Ant Design Vue */
:global(.ant-menu-submenu-popup) {
  background: #fff !important;
}

:global(.ant-menu-submenu-popup .ant-menu) {
  background: #fff !important;
  color: #222 !important;
}

:global(.ant-menu-submenu-popup .ant-menu-item) {
  color: #222 !important;
  font-weight: 500;
  transition: background 0.3s, color 0.3s;
}

:global(.ant-menu-submenu-popup .ant-menu-item:hover) {
  background: #f0f0f0 !important;
  color: #ff5722 !important;
}

:deep(.ant-menu-horizontal) {
  justify-content: center;
  display: flex;
}
a {
  text-decoration: none;
  color: inherit;
}
</style>
