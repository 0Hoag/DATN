<script setup>
import Header from "./Header.vue";
import Content from "./Content.vue";
import Footer from "./Footer.vue";
import Header2 from "./Header2.vue";
</script>

<template>
  <a-layout >
    <!-- Header -->
    <!-- <Header /> -->
    <Header2 />

    <!-- Nội dung chiếm toàn bộ phần còn lại -->
  
    <Content />



    <!-- Footer -->
    <Footer />
  </a-layout>
</template>
