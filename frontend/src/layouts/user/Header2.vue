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
        <a href="/"><img src="../../assets/image/PHoneZone.png" alt="Logo" /></a>
      </div>

      <!-- Search Bar -->
      <div class="search-bar flex-grow-1 mx-4">
        <div class="input-group">
          <input type="text" class="form-control" placeholder="Bạn cần tìm gì hôm nay?" />
          <button><i class="fa fa-search"></i></button>
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
      </div>
    </div>
  </div>

 
<a-menu mode="horizontal" class="d-flex justify-content-center" >
  <template v-for="category in categories" :key="category.id + '-wrapper'">
   <a-sub-menu v-if="category.children" :key="category.id + '-submenu'">
  <template #title>
    <router-link
      :to="`/category/${category.slug}`"
      style="display: inline-block; width: 100%;"
    >
      {{ category.name }}
    </router-link>
  </template>

  <a-menu-item
    v-for="child in category.children"
    :key="child.id"
  >
    <router-link :to="`/category/${child.slug}`">
      {{ child.name }}
    </router-link>
  </a-menu-item>
</a-sub-menu>


    <a-menu-item
      v-else
      :key="category.id + '-item'"
    >
      <router-link :to="{name: 'category', params: {slug: category.slug}}">{{ category.name }}</router-link>
    </a-menu-item>
  </template>
</a-menu>



</template>

<script setup>
import { ref } from "vue";
import { onMounted, nextTick } from "vue";
import { Dropdown } from "bootstrap";
import { useUserStore } from "@/store/userStore";
import { CategoryService } from "@/api/service/CategoryService";
import { handleError } from "@/api/functions/common";
import { useRouter } from "vue-router";

const store = useUserStore();
// onMounted(() => {
//   document.querySelectorAll(".dropdown-toggle").forEach((el) => {
//     el.addEventListener("click", function (e) {
//       e.preventDefault();
//       const dropdown = new Dropdown(el);
//       dropdown.toggle();
//     });
//   });
// });

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

    // Đợi DOM cập nhật xong rồi mới init dropdown
    await nextTick();

    document.querySelectorAll(".dropdown-toggle").forEach((el) => {
      const dropdown = new Dropdown(el);
      // Không cần toggle ngay
    });
  } catch (error) {
    console.log(error);
    handleError(error);
  }
};
const router = useRouter();
function goToCategory(slug) {
    router.push({ name: 'category', params: { slug } });
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

/* Dropdown Menu */
.navbar-nav .nav-link {
  color: #333;
  font-weight: 500;
}

.navbar-nav .nav-link:hover {
  color: #d70018;
}

.dropdown-menu {
  border-radius: 0;
  border: none;
  box-shadow: 0 2px 10px rgba(0, 0, 0, 0.1);
}

.dropdown-item:hover {
  background-color: #f1f1f1;
  color: #d70018;
}

.navbar-nav .nav-link {
  font-size: 15px;
  color: #333 !important;
  transition: all 0.2s ease-in-out;
}

.navbar-nav .nav-link:hover {
  color: #d70018 !important;
}

.dropdown-menu {
  border-radius: 6px;
  border: none;
  margin-top: 0.5rem;
  box-shadow: 0 2px 12px rgba(0, 0, 0, 0.15);
  min-width: 200px;
  padding: 0.5rem 0;
}

.dropdown-item {
  padding: 8px 20px;
  font-size: 14px;
  color: #333;
  transition: all 0.2s;
}

.dropdown-item:hover {
  background-color: #f8f9fa;
  color: #d70018;
}

a {
  text-decoration: none;
  color: black;
}
</style>
