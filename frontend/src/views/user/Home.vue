<script setup>
import BannerCarousel from "@/components/BannerCarousel.vue";
import ProductCarousel from "@/components/ProductCarousel.vue";
import Banner from "@/components/Banner.vue";

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
const banners = [
  { image: "https://picsum.photos/1200/400?random=1" },
  { image: "https://picsum.photos/1200/400?random=2" },
  { image: "https://picsum.photos/1200/400?random=3" },
];
</script>
<template>
  <BannerCarousel :banners="banners" />

  <ProductCarousel :products="hotProducts" :title="'Sản phẩm 1'" />

  <Banner image="https://picsum.photos/1200/400?random=1" />

  <ProductCarousel :products="hotProducts" :title="'Sản phẩm 2'" />
</template>
