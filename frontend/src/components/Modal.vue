<template>
 <div class="modal fade" tabindex="-1" ref="modalRef" aria-hidden="true">
  <div :class="['modal-dialog', sizeClass]">
   <div class="modal-content">
    <div class="modal-header">
     <h5 class="modal-title"><slot name="header">Modal title</slot></h5>
     <button type="button" class="btn-close" @click="close"></button>
    </div>
    <div class="modal-body">
     <slot name="body">Modal body</slot>
    </div>
   </div>
  </div>
 </div>
</template>

<script setup>
import { ref, onMounted, onBeforeUnmount, computed } from "vue";
import { Modal } from "bootstrap";

const modalRef = ref(null);
let modalInstance = null;

const props = defineProps({
 size: {
  type: String,
  default: "lg",
  validator: (value) => ["", "sm", "lg", "xl"].includes(value),
 },
});
const sizeClass = computed(() => {
 return props.size ? `modal-${props.size}` : "";
});
onMounted(() => {
 if (modalRef.value) {
  modalInstance = new Modal(modalRef.value, {
   backdrop: "static",
   keyboard: true,
  });
 }
});

onBeforeUnmount(() => {
 modalInstance?.dispose();
});

// Hàm mở modal
function open() {
 modalInstance?.show();
}

// Hàm đóng modal
function close() {
 modalInstance?.hide();
}

// expose 2 hàm ra ngoài để component cha có thể gọi
defineExpose({ open, close });
</script>
