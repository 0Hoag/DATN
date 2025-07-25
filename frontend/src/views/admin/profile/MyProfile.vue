<template>
  <div class="container mt-4">
    <div class="card p-4 shadow">
      <div class="text-center mb-4">
        <img :src="avatarUrl" class="rounded-circle" width="100" height="100" />
        <h4 class="mt-3">{{ store.userInfo?.fullName }}</h4>
        <p class="text-muted">{{ roleDisplayNames }}</p>
      </div>

      <ul class="list-group list-group-flush mb-4">
        <li class="list-group-item"><strong>Email:</strong> {{ store.userInfo?.email }}</li>
        <li class="list-group-item"><strong>Phone:</strong> {{ store.userInfo?.phone }}</li>
      </ul>

      <div class="form-check form-switch mb-3">
        <input
          class="form-check-input"
          type="checkbox"
          id="togglePassword"
          v-model="showPasswordForm"
        />
        <label class="form-check-label" for="togglePassword">Đổi mật khẩu</label>
      </div>

      <div v-if="showPasswordForm" class="border-top pt-3">
        <!-- Mật khẩu hiện tại -->
        <div class="mb-3">
          <label class="form-label d-block">Mật khẩu hiện tại</label>
          <div class="input-group">
            <input
              :type="showPassword ? 'text' : 'password'"
              class="form-control"
              v-model="currentPassword"
            />
            <button
              class="btn btn-outline-secondary"
              type="button"
              @click="togglePassword"
            >
              <font-awesome-icon :icon="showPassword ? 'eye-slash' : 'eye'" />
            </button>
          </div>
        </div>

        <!-- Mật khẩu mới -->
        <div class="mb-3">
          <label class="form-label d-block">Mật khẩu mới</label>
          <div class="input-group">
            <input
              :type="showPassword ? 'text' : 'password'"
              class="form-control"
              v-model="newPassword"
            />
            <button
              class="btn btn-outline-secondary"
              type="button"
              @click="togglePassword"
            >
              <font-awesome-icon :icon="showPassword ? 'eye-slash' : 'eye'" />
            </button>
          </div>
        </div>

        <!-- Xác nhận mật khẩu -->
        <div class="mb-3">
          <label class="form-label d-block">Xác nhận mật khẩu</label>
          <div class="input-group">
            <input
              :type="showPassword ? 'text' : 'password'"
              class="form-control"
              v-model="confirmPassword"
            />
            <button
              class="btn btn-outline-secondary"
              type="button"
              @click="togglePassword"
            >
              <font-awesome-icon :icon="showPassword ? 'eye-slash' : 'eye'" />
            </button>
          </div>
        </div>

        <button class="btn btn-primary" @click="changePassword">Xác nhận</button>
      </div>
    </div>
  </div>
</template>
<script setup>
import { handleError, hideLoading, showLoading } from "@/api/functions/common";
import { AccountService } from "@/api/service/AccountService";
import { useAuth } from "@/composable/useAuth";
import { useUserStore } from "@/store/userStore";
import { computed, onBeforeMount, ref, watchEffect } from "vue";
import { toast } from "vue3-toastify";

const store = useUserStore();

const roleDisplayNames = computed(() => {
  if (store.userInfo && Array.isArray(store.userInfo.roles)) {
    return store.userInfo.roles
      .map((role) => role.displayName || role.name || "")
      .join(", ");
  }
  return "";
});

// Dùng dịch vụ UI Avatar
const avatarUrl = computed(() => {
  const name = store.userInfo?.fullName || "Unknown";
  return `https://ui-avatars.com/api/?name=${encodeURIComponent(name)}&background=random`;
});

// Toggle đổi mật khẩu
const showPasswordForm = ref(false);
const currentPassword = ref("");
const newPassword = ref("");
const confirmPassword = ref("");
const showPassword = ref(false);

function validateForm() {
  const regexPassword = /^(?=.*[A-Z]).{8,}$/;
  if (!currentPassword.value || !newPassword.value || !confirmPassword.value) {
    toast.error("Vui lòng điền đầy đủ thông tin!");
    return false;
  }
  if (!regexPassword.test(newPassword.value)) {
    toast.error("Mật khẩu mới ít nhất 8 ký tự và 1 chữ in hoa");
    return false;
  }
  if (newPassword.value !== confirmPassword.value) {
    toast.error("Mật khẩu xác nhận không khớp!");
    return false;
  }

  return true;
}

// Giả lập xử lý đổi mật khẩu
const changePassword = async () => {
  try {
    showLoading();

    if (!validateForm()) return;

    const res = await AccountService.changePassword({
      email: store.userInfo?.email,
      oldPassword: currentPassword.value,
      newPassword: newPassword.value,
      confirmNewPassword: confirmPassword.value,
    });

    if (res.result) {
      toast.success("Đổi mật khẩu thành công!");
      currentPassword.value = "";
      newPassword.value = "";
      confirmPassword.value = "";
      showPasswordForm.value = false;
    }
  } catch (error) {
    handleError(error);
  } finally {
    hideLoading();
  }
};
// Toggle password visibility
const togglePassword = () => {
  showPassword.value = !showPassword.value;
};
</script>
