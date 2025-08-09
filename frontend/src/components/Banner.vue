<template>
  <div class="container my-4">
    <div class="position-relative w-100" style="aspect-ratio: 3 / 1">
      <img
        :src="image"
        class="position-absolute top-0 start-0 w-100 h-100 object-fit-cover rounded shadow"
        :alt="alt"
      />
    </div>
  </div>
</template>

<script setup>
defineProps({
  image: {
    type: String,
    required: true
  },
  alt: {
    type: String,
    default: 'Banner'
  }
})
</script>
