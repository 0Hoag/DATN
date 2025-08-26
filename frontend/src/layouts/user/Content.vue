<script setup>
import BannerCarousel from "@/components/BannerCarousel.vue";
import ProductCarousel from "@/components/ProductCarousel.vue";
import { Transition } from "vue";

const hotProducts = [];
</script>
<template>
  <a-layout-content>
    <div class="container p-3">
      <!-- <a-breadcrumb style="margin: 16px 0">
        <a-breadcrumb-item>Home</a-breadcrumb-item>
        <a-breadcrumb-item>List</a-breadcrumb-item>
        <a-breadcrumb-item>App</a-breadcrumb-item>
      </a-breadcrumb> -->
      <router-view />
    </div>
  </a-layout-content>
</template>
