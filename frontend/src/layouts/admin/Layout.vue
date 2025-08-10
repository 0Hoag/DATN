<template>
  <a-layout style="min-height: 100vh">
    <AppSidebar />
    <a-layout>
      <TheHeader />
      <AppContent>
        <!-- Nội dung của từng trang sẽ được render tại đây -->
        <router-view />
      </AppContent>
    </a-layout>
  </a-layout>
</template>

<script setup>
import TheHeader from './TheHeader.vue'
import AppSidebar from './Sidebar.vue'
import AppContent from './Content.vue'

</script>
