<template>
  <div class="container my-4">
    <div class="card border-0">
      <div class="card-body">
        <div class="d-flex justify-content-between align-items-center mb-3">
          <h3>{{ title }}</h3>
          <button class="btn btn-outline-primary">Xem tất cả</button>
        </div>
        <swiper
          class="product-swiper"
          :modules="[Navigation]"
          :slides-per-view="4"
          :space-between="20"
          :loop="true"
          navigation
          pagination
          :breakpoints="{
            0: { slidesPerView: 1 },
            576: { slidesPerView: 2 },
            768: { slidesPerView: 3 },
            992: { slidesPerView: 4 },
          }"
        >
          <swiper-slide
            v-for="product in products"
            :key="product.id"
            class="h-100"
          >
            <div class="card h-100 d-flex flex-column shadow-sm product-card">
              <img :src="product.image" class="card-img-top" :alt="product.name" />
              <div class="card-body d-flex flex-column flex-grow-1">
                <h5 class="card-title fw-semibold flex-grow-1 text-truncate text-wrap" style="min-height:2.6em;max-height:2.6em;overflow:hidden;" :title="product.name">
                  {{ product.name }}
                </h5>
                <p class="card-text text-danger fw-bold mt-auto">
                  {{ formatPrice(product.price) }}
                </p>
                <button class="btn btn-primary w-100 mt-2">Thêm vào giỏ</button>
              </div>
            </div>
          </swiper-slide>-
        </swiper>
      </div>
    </div>
  </div>
</template>

<script setup>
import { Swiper, SwiperSlide } from "swiper/vue";
import { Navigation, Pagination } from "swiper/modules";
import "swiper/css";
import "swiper/css/navigation";
import "swiper/css/pagination";
import "swiper/css/autoplay";

const props = defineProps({
  products: {
    type: Array,
    required: true,
  },
  title: {
    type: String,
    default: "Sản phẩm nổi bật",
    required: true,
  },
});

function formatPrice(price) {
  return new Intl.NumberFormat("vi-VN", {
    style: "currency",
    currency: "VND",
  }).format(price);
}
</script>

<style scoped>

.card-img-top {
  height: 180px;
  object-fit: cover;
}
.product-card:hover {
  color: blue;
  cursor: pointer;
}
</style>
