<template>
  <div class="container my-4">
    <swiper
      class="banner-swiper"
      :modules="[Autoplay, Navigation, Pagination]"
      :slides-per-view="1"
      :loop="true"
      :autoplay="{ delay: 4000, disableOnInteraction: false }"
      pagination
      navigation
    >
      <swiper-slide v-for="(banner, index) in banners" :key="index">
        <div class="row justify-content-center">
          <div class="col-12">
            <img
              :src="banner.image"
              class="img-fluid d-block w-100 rounded shadow"
              :alt="'Banner ' + (index + 1)"
            />
          </div>
        </div>
      </swiper-slide>
    </swiper>
  </div>
</template>

<script setup>
import { Swiper, SwiperSlide } from 'swiper/vue'
import { Autoplay, Navigation, Pagination } from 'swiper/modules'

import 'swiper/css';
import 'swiper/css/navigation';
import 'swiper/css/pagination';
import 'swiper/css/autoplay';

const props = defineProps({
  banners: {
    type: Array,
    required: true
  }
});

</script>

<style scoped>
.banner-swiper {
  position: relative;
}
</style>
