<template>
  <a-layout-content style="margin: 24px 16px; padding: 24px; background: #fff">
    <router-view />
  </a-layout-content>
</template>

<script>
import { Transition } from "vue";

export default {
  name: "AppContent",
};
</script>
