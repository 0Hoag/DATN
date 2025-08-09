<script setup>
import BannerCarousel from "@/components/BannerCarousel.vue";
import ProductCarousel from "@/components/ProductCarousel.vue";

const hotProducts = [
  {
    id: 1,
    name: "iPhone 15",
    price: 24000000,
    image: "https://picsum.photos/300/200?random=1",
  },
  {
    id: 2,
    name: "Samsung Galaxy S23 Samsung Galaxy S23 ",
    price: 21000000,
    image: "https://picsum.photos/300/200?random=2",
  },
  {
    id: 3,
    name: "Xiaomi 13",
    price: 12000000,
    image: "https://picsum.photos/300/200?random=3",
  },
  {
    id: 4,
    name: "Oppo Reno8",
    price: 9000000,
    image: "https://picsum.photos/300/200?random=4",
  },
  {
    id: 5,
    name: "Realme 10",
    price: 6500000,
    image: "https://picsum.photos/300/200?random=5",
  },
  {
    id: 6,
    name: "Nokia G21",
    price: 4500000,
    image: "https://picsum.photos/300/200?random=6",
  },
];
</script>
<template>
  <a-layout-content >
    <div class="container p-3">
      <!-- <a-breadcrumb style="margin: 16px 0">
        <a-breadcrumb-item>Home</a-breadcrumb-item>
        <a-breadcrumb-item>List</a-breadcrumb-item>
        <a-breadcrumb-item>App</a-breadcrumb-item>
      </a-breadcrumb> -->
     <router-view  />
    </div>
  </a-layout-content>
</template>
