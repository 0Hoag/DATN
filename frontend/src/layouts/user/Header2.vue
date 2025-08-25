<template>
  <!-- Top Scrolling Bar -->
  <div class="top-category-bar py-2">
    <div class="container">
      <div class="scroll-text">
        <a href="#">🔥 Deal sốc cuối tuần!</a>
        <a href="#">🎁 Tặng quà khi mua iPhone</a>
        <a href="#">⚡ Laptop gaming giảm 20%</a>
        <a href="#">💥 Giảm giá đồng hồ thông minh</a>
        <a href="#">🛍️ Miễn phí vận chuyển toàn quốc</a>
      </div>
    </div>
  </div>

  <!-- Header -->
  <div class="main-header">
    <div class="container d-flex align-items-center justify-content-between">
      <!-- Logo -->
      <div class="logo">
        <router-link :to="{ name: 'home' }"
          ><img src="../../assets/image/PHoneZone.png" alt="Logo"
        /></router-link>
      </div>

      <!-- Search Bar -->
      <div class="search-bar flex-grow-1 mx-4">
        <div class="input-group">
          <input
            type="text"
            class="form-control"
            placeholder="Bạn cần tìm gì hôm nay?"
            @keyup.enter="handleSearch"
            v-model="searchKeyword"
          />
          <button @click="handleSearch">
            <font-awesome-icon icon="fa-solid fa-magnifying-glass" />
          </button>
        </div>
      </div>

      <!-- Icons -->
      <!-- <div class="header-icons d-flex align-items-center">
        <a href="/login" class="me-3">
          <i class="fas fa-user" style="cursor: pointer;"></i>
        </a>

        <a href="/cart">
          <i class="fas fa-cart-shopping" style="cursor: pointer;"></i>
        </a>
      </div> -->
      <!-- Tài khoản + Giỏ hàng -->
      <div class="icon-group d-flex align-items-center gap-3 text-white">
        <a href="#" class="d-flex align-items-center gap-1">
          <font-awesome-icon :icon="['fas', 'user']" />
          <a-dropdown>
            <a class="ant-dropdown-link" @click.prevent>
              {{ store.isLoggedIn ? store.userInfo?.fullName : "Tài khoản" }}
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
        <router-link :to="{ name: 'cart' }" class="d-flex align-items-center gap-1">
          <font-awesome-icon :icon="['fas', 'cart-shopping']" />
          <span class="d-none d-md-inline">Giỏ hàng</span>
        </router-link>
        <router-link
          :to="{ name: 'dashboard' }"
          class="d-flex align-items-center gap-1"
          v-if="hasScope(['ROLE_ADMIN', 'ROLE_MANAGER', 'ROLE_SHIFT_STAFF'])"
        >
          <font-awesome-icon :icon="['fas', 'dashboard']" />
          <span class="d-none d-md-inline">Dashboard</span>
        </router-link>
      </div>
    </div>

    <!-- menu -->
    <div class="container mt-2 d-flex justify-content-center">
      <ul class="menu">
        <li v-for="item in categories" @mouseenter="adjustPosition($event)">
          <router-link
            v-if="item.children.length === 0"
            :to="{ name: 'category', params: { slug: item.slug } }"
          >
            {{ item.name }}
          </router-link>
          <span v-else class="d-inline-flex align-items-center">
            {{ item.name }}
            <font-awesome-icon icon="fa-solid fa-caret-down" class="ms-2" />
          </span>

          <ul
            v-if="item.children.length > 0"
            class="sub-menu mt-4 bg-white shadow rounded"
            :class="item.children.length <= 4 ? 'center' : 'full'"
          >
            <li v-for="child in item.children" :key="child.id">
              <router-link :to="{ name: 'category', params: { slug: child.slug } }">
                {{ child.name }}
              </router-link>
            </li>
          </ul>
        </li>
      </ul>
    </div>
  </div>
</template>

<script setup>
import { ref } from "vue";
import { onMounted, nextTick } from "vue";
import { Dropdown } from "bootstrap";
import { useUserStore } from "@/store/userStore";
import { CategoryService } from "@/api/service/CategoryService";
import { handleError } from "@/api/functions/common";
import { useRouter } from "vue-router";
import { useAuth } from "@/composable/useAuth";
const { hasScope } = useAuth();
const store = useUserStore();
const router = useRouter();

const categories = ref([
  // { id: 1, name: "Điện thoại", slug: "dien-thoai", children: [] },
  // {
  //   id: 2,
  //   name: "Máy tính bảng",
  //   slug: "may-tinh-bang",
  //   children: [
  //     { id: 3, name: "iPad", slug: "ipad" },
  //     { id: 4, name: "Android", slug: "android" },
  //   ],
  // },
  // { id: 5, name: "Laptop", slug: "laptop", children: [] },
]);

const getListCategory = async () => {
  categories.value = [];
  try {
    const res = await CategoryService.fetchListCategoryForUser();
    const raw = res.result || [];

    // Lọc category cha
    categories.value = raw
      .filter((c) => c.parent === null)
      .map((c) => ({
        ...c,
        children: c.children || [],
      }));
  } catch (error) {
    console.log(error);
    handleError(error);
  }
};

function goToCategory(slug) {
  router.push({ name: "category", params: { slug } });
}

function adjustPosition(event) {
  const subMenu = event.currentTarget.querySelector(".sub-menu");
  if (!subMenu) return;

  // reset
  subMenu.style.left = "";
  subMenu.style.right = "";
  subMenu.style.transform = "";

  const rect = subMenu.getBoundingClientRect();
  console.log(rect);
  console.log(window.innerWidth);

  if (rect.left < 0) {
    subMenu.style.left = "0";
    subMenu.style.transform = "translateY(-10px)"; // giữ hiệu ứng
  } else if (rect.right > window.innerWidth) {
    subMenu.style.right = "0";
    subMenu.style.transform = "translateY(-10px)";
  }
}
//search
const searchKeyword = ref("");
function handleSearch() {
  const value = searchKeyword.value.trim();
  if (!value) return;

  router.push({
    name: "search",
    query: { q: value },
  });
  searchKeyword.value = "";
}
onMounted(() => {
  getListCategory();
});
</script>

<style scoped>
.dropdown-item img {
  width: 20px;
  height: 20px;
  object-fit: contain;
}

/* Top Category Bar */
.top-category-bar {
  background-color: #d70018;
  color: white;
  font-size: 13px;
  overflow: hidden;
}

.top-category-bar .scroll-text {
  white-space: nowrap;
  display: inline-block;
  padding-left: 100%;
  animation: scroll-left 20s linear infinite;
}

@keyframes scroll-left {
  0% {
    transform: translateX(0);
  }

  100% {
    transform: translateX(-100%);
  }
}

.top-category-bar a {
  color: white;
  text-decoration: none;
  margin-right: 25px;
}

/* Header */
.main-header {
  background-color: rgb(255, 255, 255);
  padding: 10px 0;
  box-shadow: 0 1px 4px rgba(0, 0, 0, 0.1);
}

.main-header .logo img {
  height: 50px;
}

.search-bar input {
  border-radius: 20px;
  padding-left: 15px;
}

.search-bar button {
  background-color: #d70018;
  border-radius: 0 20px 20px 0;
  border: none;
  padding: 0 15px;
  color: white;
}

.header-icons i {
  font-size: 20px;
  color: #333;
  margin-left: 20px;
  cursor: pointer;
}

a {
  text-decoration: none;
  color: black;
}

/* menu */
.menu {
  display: flex;
  list-style: none;
  flex-wrap: wrap;
  max-width: 700px;
  box-sizing: border-box;
}

.menu > li {
  padding: 0.5rem 1rem;
  cursor: pointer;
  position: relative;
  border-bottom: 2px solid transparent;
}

.menu > li:hover {
  border-bottom-color: blue;
}

/* Ẩn menu con mặc định */
.sub-menu {
  position: absolute;
  top: 100%;
  padding: 0.5rem;
  list-style: none;

  display: inline-flex;

  gap: 1rem;
  opacity: 0;
  visibility: hidden;
  transform: translateY(-10px);
  transition: all 0.3s ease;
  z-index: 10;
}

/* Hiển thị khi hover */
.menu > li:hover > .sub-menu {
  opacity: 1;
  visibility: visible;
  transform: translateY(0);
}

.sub-menu > li:hover {
  color: black;
}

.sub-menu > li {
  flex: 1 1 150px;
  text-align: center;
  padding: 0.5rem;
  color: black;
}

/* ít item -> center ngay dưới cha */
.sub-menu.center {
  left: 0;
  flex-wrap: nowrap;
}

/* nhiều item -> full width menu */
.sub-menu.full {
  min-width: 600px;
  justify-content: flex-start;
  flex-wrap: wrap;
}
</style>
