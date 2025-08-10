<template>
  <!-- <Login class="container-fluid vh-100 d-flex align-items-center justify-content-center bg-light"/> -->
 <div class="h-100 p-5 ">
    <div class="row ">
      <div class="col-md-6 d-flex align-items-center justify-content-center">
        <img :src="anh_login" alt="anh_login" class="img-fluid" />
      </div>
      <div class="col-md-6 ">
        <Login />
      </div>
  
    </div>
 </div>



</template>
<script setup>
import Login from '@/components/Login.vue';
import anh_login from '@/assets/images/anh_login.png';
</script>