<template>
  <div class="container mt-3">
    <div class="row">
      <!-- <div class="col-md-2">
        <div class="list-group list-group-flush category-menu">
          <a v-for="(cat, index) in categories" :key="index" class="list-group-item list-group-item-action" href="#">
            <i :class="cat.icon + ' me-2'"></i>{{ cat.label }}
          </a>
        </div>
      </div> -->
      <div class="col-md-3">
        <img
          v-for="(img, index) in rightBanners"
          :key="index"
          :src="img"
          class="img-fluid mb-2"
          alt="Banner"
        />
      </div>
      <div class="col-md-9">
        <img
          src="../../assets/image/banner2.jpg"
          class="img-fluid rounded"
          alt="Main Banner"
        />
      </div>

      <!-- <div class="col-md-3">
        <img v-for="(img, index) in rightBanners" :key="index" :src="img" class="img-fluid mb-2" alt="Banner" />
      </div> -->
    </div>

    <div class="row mt-3">
      <div class="col-12">
        <img
          src="../../assets/image/bannervoucher.png"
          class="img-fluid rounded"
          alt="Voucher Banner"
        />
      </div>
    </div>

    <!-- Flash Sale -->
    <div
      class="row mt-5 flash-sale-section p-3 rounded bg-warning-subtle"
      v-if="isActive"
    >
      <div class="d-flex justify-content-between align-items-center mb-3">
        <h5 class="text-danger fw-bold mb-0">
          <i class="fa-solid fa-bolt fa-shake text-warning me-2"></i> FLASH SALE
        </h5>
        <div class="countdown-timer text-danger fw-semibold small">
          Kết thúc trong:
          {{ h }}:{{ m }}:{{ s }}
        </div>
      </div>
      <Swiper :modules="[Navigation,Pagination]" :loop="true" :slides-per-view="5" navigation  pagination>
        <SwiperSlide v-for="(product, index) in flashSaleProducts" :key="index">
       <router-link :to="{name: 'product', params: {slug: product.slug}}">
            <div
              class="card flash-sale-card shadow-sm position-relative d-flex flex-column h-100"
              style="flex: 0 0 auto; width: 220px"
            >
              <span class="flash-badge">SALE</span>
  
              <div class="product-img-wrapper">
                <img :src="product.imageUrl" :alt="product.title" />
              </div>
  
              <div class="card-body d-flex flex-column justify-content-between flex-grow-1">
                <h6 class="card-title text-start text-truncate">
                  {{ product.name }}
                </h6>
  
                <div class="mb-1 text-warning d-flex align-items-center mt-auto">
                  <i class="fa fa-star"></i>
                  <span class="text-muted small ms-1">({{ product.averageRating }})</span>
                </div>
  
                <p class="text-danger fw-bold mb-1 text-start">
                  {{ formatPrice(product.minSalePrice) }}<br />
                  <del class="text-muted">{{ formatPrice(product.minOriginPrice) }}</del>
                </p>
              </div>
            </div>
       </router-link>
        </SwiperSlide>
      </Swiper>
    </div>
    <!-- Danh sách sản phẩm -->
    <div class="row mt-3">
      <div class="d-flex justify-content-between">
        <h5>ĐIỆN THOẠI NỔI BẬT NHẤT</h5>
        <router-link
          :to="{ name: 'category', params: { slug: 'dien-thoai' } }"
          class="btn btn-light border rounded-pill"
          >Xem tất cả</router-link
        >
      </div>
      <div class="col-12 d-flex flex-wrap gap-2">
        <!-- <button class="btn btn-light border rounded-pill " v-for="cate in listCategory">{{ cate.name }}</button> -->
      </div>

      <div class="row g-3 mt-1">
        <div class="col-md-2 d-flex mb-3" v-for="p in listProductMobile">
          <router-link :to="{ name: 'product', params: { slug: p.slug } }">
            <div class="card product-card shadow-sm d-flex flex-column h-100 w-100">
              <div class="product-img-wrapper">
                <img :src="p.imageUrl" :alt="p.name" class="img-fluid" />
              </div>
              <div
                class="card-body flex-grow-1 d-flex flex-column justify-content-between m-auto w-100"
              >
                <h6 class="card-title text-left">
                  {{ p.name }}
                </h6>
                <div
                  class="mb-1 text-warning d-flex justify-content-start align-items-center mt-auto"
                  style="min-height: 28px"
                >
                  <i class="fa fa-star"></i>
                  <span class="text-muted small ms-1">({{ p.averageRating }})</span>
                </div>
                <p class="text-danger fw-bold mb-1 text-start">
                  {{
                    p.minSalePrice
                      ? formatPrice(p.minSalePrice)
                      : formatPrice(p.minOriginPrice)
                  }}<br />
                  <del class="text-muted" v-if="p.minSalePrice">{{
                    formatPrice(p.minOriginPrice)
                  }}</del>
                </p>
              </div>
            </div>
          </router-link>
        </div>
      </div>
    </div>

    <!-- Laptop -->
    <div class="row mt-4">
      <div class="d-flex justify-content-between">
        <h5>LAPTOP NỔI BẬT NHẤT</h5>
        <router-link
          :to="{ name: 'category', params: { slug: 'laptop' } }"
          class="btn btn-light border rounded-pill"
          >Xem tất cả</router-link
        >
      </div>
      <!-- <div class="col-12 d-flex flex-wrap gap-2">
        <button class="btn btn-light border rounded-pill">MSI</button>
        <button class="btn btn-light border rounded-pill">ASUS</button>
        <button class="btn btn-light border rounded-pill">MACBOOK</button>
        <button class="btn btn-light border rounded-pill">DELL</button>
        <button class="btn btn-light border rounded-pill">HP</button>
        <button class="btn btn-light border rounded-pill">LENOVO</button>
        <button class="btn btn-light border rounded-pill">TUF</button>
        <button class="btn btn-light border rounded-pill">Xem tất cả</button>
      </div> -->

      <div class="row g-3 mt-1">
        <div class="col-md-2 d-flex mb-3" v-for="p in listProductLaptop">
          <router-link :to="{ name: 'product', params: { slug: p.slug } }">
            <div class="card product-card shadow-sm d-flex flex-column h-100 w-100">
              <div class="product-img-wrapper">
                <img :src="p.imageUrl" :alt="p.name" class="img-fluid" />
              </div>
              <div
                class="card-body flex-grow-1 d-flex flex-column justify-content-between m-auto w-100"
              >
                <h6 class="card-title text-left">
                  {{ p.name }}
                </h6>
                <div
                  class="mb-1 text-warning d-flex justify-content-start align-items-center mt-auto"
                  style="min-height: 28px"
                >
                  <i class="fa fa-star"></i>
                  <span class="text-muted small ms-1">({{ p.averageRating }})</span>
                </div>
                <p class="text-danger fw-bold mb-1 text-start">
                  {{
                    p.minSalePrice
                      ? formatPrice(p.minSalePrice)
                      : formatPrice(p.minOriginPrice)
                  }}<br />
                  <del class="text-muted" v-if="p.minSalePrice">{{
                    formatPrice(p.minOriginPrice)
                  }}</del>
                </p>
              </div>
            </div>
          </router-link>
        </div>
      </div>
    </div>

    <!-- Đồng hồ -->
    <div class="row mt-4">
      <div class="d-flex justify-content-between">
        <h5>ĐỒNG HỒ NỔI BẬT NHẤT</h5>
        <router-link
          :to="{ name: 'category', params: { slug: 'dong-ho' } }"
          class="btn btn-light border rounded-pill"
          >Xem tất cả</router-link
        >
      </div>
      <!-- <div class="col-12 d-flex flex-wrap gap-2">
        <button class="btn btn-light border rounded-pill">ROLEX</button>
        <button class="btn btn-light border rounded-pill">HUBLOT</button>
        <button class="btn btn-light border rounded-pill">CARTIER</button>
        <button class="btn btn-light border rounded-pill">CASIO</button>
        <button class="btn btn-light border rounded-pill">SEIKO</button>
        <button class="btn btn-light border rounded-pill">Xem tất cả</button>
      </div> -->

      <div class="row g-3 mt-1">
        <div class="col-md-2 d-flex mb-3" v-for="p in listProductWatch">
          <router-link :to="{ name: 'product', params: { slug: p.slug } }">
            <div class="card product-card shadow-sm d-flex flex-column h-100 w-100">
              <div class="product-img-wrapper">
                <img :src="p.imageUrl" :alt="p.name" class="img-fluid" />
              </div>
              <div
                class="card-body flex-grow-1 d-flex flex-column justify-content-between m-auto w-100"
              >
                <h6 class="card-title text-left">
                  {{ p.name }}
                </h6>
                <div
                  class="mb-1 text-warning d-flex justify-content-start align-items-center mt-auto"
                  style="min-height: 28px"
                >
                  <i class="fa fa-star"></i>
                  <span class="text-muted small ms-1">({{ p.averageRating }})</span>
                </div>
                <p class="text-danger fw-bold mb-1 text-start">
                  {{
                    p.minSalePrice
                      ? formatPrice(p.minSalePrice)
                      : formatPrice(p.minOriginPrice)
                  }}<br />
                  <del class="text-muted" v-if="p.minSalePrice">{{
                    formatPrice(p.minOriginPrice)
                  }}</del>
                </p>
              </div>
            </div>
          </router-link>
        </div>
      </div>
    </div>
  </div>
</template>

<script setup>
import { handleError } from "@/api/functions/common";
import { ProductService } from "@/api/service/ProductService";
import { onMounted, onUnmounted, ref } from "vue";
import { Swiper, SwiperSlide } from "swiper/vue";
import "swiper/css"; // style cơ bản
import "swiper/css/navigation";
import "swiper/css/autoplay";

const menus = ref([
  { name: "Điện thoại", items: ["iPhone", "Samsung", "Xiaomi", "OPPO"] },
  { name: "Laptop", items: ["MacBook", "Asus", "Acer", "MSI"] },
  { name: "Đồng hồ", items: ["Apple Watch", "Xiaomi", "Amazfit", "Huawei"] },
  { name: "Phụ kiện", items: ["Cáp sạc", "Ốp lưng", "Pin dự phòng", "Hub USB"] },
  { name: "Âm thanh", items: ["Tai nghe Bluetooth", "Loa Bluetooth", "Tai nghe Gaming"] },
]);

const categories = ref([
  { icon: "bi bi-phone", label: "Điện thoại, Tablet" },
  { icon: "bi bi-laptop", label: "Laptop" },
  { icon: "bi bi-headphones", label: "Âm thanh, Mic thu âm" },
  { icon: "bi bi-camera", label: "Đồng hồ, Camera" },
  { icon: "bi bi-controller", label: "Đồ gia dụng" },
  { icon: "bi bi-usb-symbol", label: "Phụ kiện" },
  { icon: "bi bi-pc-display-horizontal", label: "PC, Màn hình" },
  { icon: "bi bi-tv", label: "Tivi" },
  { icon: "bi bi-arrow-repeat", label: "Thu cũ đổi mới" },
  { icon: "bi bi-bag", label: "Hàng cũ" },
  { icon: "bi bi-tags", label: "Khuyến mãi" },
  { icon: "bi bi-newspaper", label: "Tin công nghệ" },
]);

import banner1 from "@/assets/image/banner1.png";
import banner3 from "@/assets/image/banner3.jpg";
import banner4 from "@/assets/image/banner4.jpg";
import { faL } from "@fortawesome/free-solid-svg-icons";
import { Navigation, Pagination } from "swiper/modules";

const rightBanners = ref([banner1, banner4, banner3]);
const flashSaleProducts = ref([
]);
// danh sach san pham new

const getListProductSale = async () => {
  try {
    const res = await ProductService.fetchListProductSale();
    flashSaleProducts.value = res.result || [];
    console.log(flashSaleProducts.value);
  } catch (error) {
    console.log(error);

    handleError(error);
  }
};

const listProductMobile = ref([]);
const getListProductMobile = async () => {
  listProductMobile.value = [];
  try {
    const res = await ProductService.fetchListProductBySlugCategory("dien-thoai", {
      page: 1,
      size: 12,
    });
    listProductMobile.value = res.result.data || [];
  } catch (error) {
    console.log(error);
    handleError(error);
  }
};

const listProductLaptop = ref([]);
const getListProductLaptop = async () => {
  listProductLaptop.value = [];
  try {
    const res = await ProductService.fetchListProductBySlugCategory("laptop", {
      page: 1,
      size: 12,
    });
    listProductLaptop.value = res.result.data || [];
  } catch (error) {
    console.log(error);
    handleError(error);
  }
};

const listProductWatch = ref([]);
const getListProductWatch = async () => {
  listProductWatch.value = [];
  try {
    const res = await ProductService.fetchListProductBySlugCategory("dong-ho", {
      page: 1,
      size: 12,
    });
    listProductWatch.value = res.result.data || [];
  } catch (error) {
    console.log(error);
    handleError(error);
  }
};

//helper
function formatPrice(price) {
  return new Intl.NumberFormat("vi-VN", {
    style: "currency",
    currency: "VND",
  }).format(price);
}

// count down
const h = ref("00");
const m = ref("00");
const s = ref("00");
const isActive = ref(true);
let startTime, endTime, timer;

// Hàm tính Thứ 7 & Chủ Nhật tuần tới (gọn nhất)
function getNextWeekendTimes() {
  const now = new Date();

  // Thứ 7 09:00
  // const start = new Date(now);
  // const diffToSat = (6 - start.getDay() + 7) % 7;
  // start.setDate(start.getDate() + diffToSat);
  // start.setHours(9, 0, 0, 0);

  // Chủ Nhật 23:59:59
  // const end = new Date(start);
  // end.setDate(start.getDate() + 1);
  // end.setHours(23, 59, 59, 999);

  // Test tự động tính toán
  const start = new Date(now.getTime() + 1 * 1000);
  const end = new Date(start.getTime() + 50 * 1000);

  return { start, end };
}

function tick() {
  const now = new Date();

  if (now < startTime) {
    isActive.value = false;
    h.value = m.value = s.value = "00";
    return;
  }

  isActive.value = true;
  let diff = Math.floor((endTime - now) / 1000);

  // reset lại countdown sang tuần sau
  if (diff < 0) {
    const next = getNextWeekendTimes();
    startTime = next.start;
    endTime = next.end;

    localStorage.setItem("flashsale-start", startTime.toISOString());
    localStorage.setItem("flashsale-end", endTime.toISOString());

    isActive.value = false;
    h.value = m.value = s.value = "00";
    return;
  }

  // tinh toan ra giờ phút giây
  const totalHours = Math.floor(diff / 3600);
  const minutes = Math.floor((diff % 3600) / 60);
  const seconds = diff % 60;

  // chuyển sang string, nếu mà giây chỉ có 1 chữ chố thì sẽ thêm 0 vào đằng trước
  h.value = String(totalHours).padStart(2, "0");
  m.value = String(minutes).padStart(2, "0");
  s.value = String(seconds).padStart(2, "0");
}

onMounted(() => {
  getListProductSale();
  getListProductMobile();
  getListProductLaptop();
  getListProductWatch();

  const startFromStorage = localStorage.getItem("flashsale-start");
  const endFromStorage = localStorage.getItem("flashsale-end");

  if (startFromStorage && endFromStorage) {
    startTime = new Date(startFromStorage);
    endTime = new Date(endFromStorage);
  } else {
    const times = getNextWeekendTimes();
    startTime = times.start;
    endTime = times.end;
    localStorage.setItem("flashsale-start", startTime.toISOString());
    localStorage.setItem("flashsale-end", endTime.toISOString());
  }

  tick();
  timer = setInterval(tick, 1000);
});

onUnmounted(() => clearInterval(timer));
</script>

<style scoped>
/* Các style tương tự như HTML gốc */
@import url("https://cdn.jsdelivr.net/npm/bootstrap@5.3.0/dist/css/bootstrap.min.css");
@import url("https://cdnjs.cloudflare.com/ajax/libs/font-awesome/6.4.0/css/all.min.css");
@import url("https://cdn.jsdelivr.net/npm/bootstrap-icons/font/bootstrap-icons.css");

body {
  font-family: "Segoe UI", Arial, sans-serif;
  background-color: #f3f3f3;
}

/* ===== Top Category Bar ===== */
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

/* ===== Main Header ===== */
.main-header {
  background-color: white;
}

.main-header .logo img {
  height: 50px;
}

.header-icons i {
  font-size: 20px;
  color: #333;
  margin-left: 20px;
  cursor: pointer;
}

/* ===== Search Bar ===== */
.search-bar .input-group input {
  border-radius: 20px 0 0 20px;
  padding-left: 15px;
}

.search-bar .input-group button {
  background-color: #d70018;
  color: white;
  border-radius: 0 20px 20px 0;
  border: none;
  padding: 0 15px;
}

/* ===== Navbar & Dropdown ===== */
.navbar-nav .nav-link {
  color: #333 !important;
  font-weight: 500;
  font-size: 15px;
  transition: all 0.2s ease-in-out;
}

.navbar-nav .nav-link:hover {
  color: #d70018 !important;
}

.dropdown-menu {
  border: none;
  border-radius: 6px;
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

/* ===== Category Hover ===== */
.category-menu a:hover {
  background-color: #f70b0b;
  text-decoration: none;
}

/* ===== Product Card ===== */
.card {
  transition: all 0.3s ease-in-out;
}

.card:hover {
  transform: translateY(-5px);
  box-shadow: 0 10px 20px rgba(0, 0, 0, 0.15);
  cursor: pointer;
}

.card img {
  transition: transform 0.3s ease;
}

.card:hover img {
  transform: scale(1.05);
}

.product-card {
  height: 100%;
  display: flex;
  flex-direction: column;
  justify-content: space-between;
}

.product-img-wrapper {
  width: 100%;
  aspect-ratio: 4 / 3;
  display: flex;
  align-items: center;
  justify-content: center;
  overflow: hidden;
  padding: 5px;
}

.product-img-wrapper img {
  max-width: 100%;
  max-height: 100%;
  object-fit: contain;
}

.card-title {
  font-size: 14px;
  line-height: 1.2rem;
  font-weight: 600;
  margin-bottom: 0.5rem;
  min-height: 40px;
  white-space: normal;
}

/* ===== Flash Sale Section ===== */
.flash-sale-section {
  background: #fff8f8;
  border-left: 6px solid #ff4d4f;
  padding: 10px;
}

.flash-sale-card {
  border: 1px solid #ffd6d6;
  transition: transform 0.3s ease;
}

.flash-sale-card:hover {
  transform: translateY(-5px);
  box-shadow: 0 4px 12px rgba(255, 77, 79, 0.3);
}

.flash-sale-card .product-img-wrapper {
  aspect-ratio: 1/1;
}

.flash-sale-card .product-img-wrapper img {
  width: 100%;
  height: auto;
  object-fit: contain;
}

/* ===== Sale Badge ===== */
.flash-badge {
  position: absolute;
  top: 10px;
  left: 10px;
  background-color: #dc3545;
  color: white;
  font-size: 0.75rem;
  padding: 2px 6px;
  border-radius: 4px;
  animation: flashPulse 1s infinite;
}

@keyframes flashPulse {
  0%,
  100% {
    opacity: 1;
  }

  50% {
    opacity: 0.5;
  }
}
</style>
