<template>
  <div class="w-auto">
    <div class="card shadow">
      <div class="card-body p-5">
        <!-- Header -->
        <div class="text-center mb-4">
          <h2 class="card-title fw-bold text-primary">Đăng nhập</h2>
        </div>

        <!-- Form -->
        <form @submit.prevent="handleLogin">
          <!-- Email/Username Input -->
          <div class="mb-3">
            <label for="email" class="form-label fw-semibold">
              <i class="fas fa-envelope me-2"></i>Email
            </label>
            <input
              type="text"
              class="form-control form-control-lg"
              id="email"
              v-model="loginForm.email"
              placeholder="Nhập email "
              required
              :class="{ 'is-invalid': errors.email }"
            />
            <div v-if="errors.email" class="invalid-feedback">
              {{ errors.email }}
            </div>
          </div>

          <!-- Password Input -->
          <div class="mb-3">
            <label for="password" class="form-label fw-semibold">
              <i class="fas fa-lock me-2"></i>Mật khẩu
            </label>
            <div class="input-group">
              <input
                :type="showPassword ? 'text' : 'password'"
                class="form-control form-control-lg"
                id="password"
                v-model="loginForm.password"
                placeholder="Nhập mật khẩu"
                required
                :class="{ 'is-invalid': errors.password }"
              />
              <button
                class="btn btn-outline-secondary"
                type="button"
                @click="togglePassword"
              >
                <font-awesome-icon :icon="showPassword ? 'eye-slash' : 'eye'" />
              </button>
              <div v-if="errors.password" class="invalid-feedback">
                {{ errors.password }}
              </div>
            </div>
          </div>

          <!-- Remember Me & Forgot Password -->
          <div class="d-flex justify-content-between align-items-center mb-4">
            <div class="form-check">
              <input
                class="form-check-input"
                type="checkbox"
                id="rememberMe"
                v-model="loginForm.rememberMe"
              />
              <label class="form-check-label" for="rememberMe"> Ghi nhớ đăng nhập </label>
            </div>
            <router-link
              :to="{ name: 'ForgotPassword' }"
              class="text-decoration-none text-primary"
            >
              Quên mật khẩu?
            </router-link>
          </div>

          <!-- Login Button -->
          <div class="d-grid mb-3">
            <button type="submit" class="btn btn-primary btn-lg" :disabled="isLoading">
              <span
                v-if="isLoading"
                class="spinner-border spinner-border-sm me-2"
                role="status"
              ></span>
              <i v-else class="fas fa-sign-in-alt me-2"></i>
              {{ isLoading ? "Đang đăng nhập..." : "Đăng nhập" }}
            </button>
          </div>
        </form>
        <!-- Register Link -->
        <div class="text-center" v-if="!route.path.startsWith('/admin')">
          <span class="text-muted">Chưa có tài khoản? </span>
          <router-link :to="{name: 'register'}" class="text-decoration-none fw-semibold"
            >Đăng ký ngay</router-link  >
        </div>
        <!-- Alert for errors -->
        <div v-if="generalError" class="alert alert-danger mt-3" role="alert">
          <i class="fas fa-exclamation-triangle me-2"></i>
          {{ generalError }}
        </div>
      </div>
    </div>
  </div>
</template>

<script setup>
import { jwtDecode } from "jwt-decode";
import { ref, reactive } from "vue";
import { AccountService } from "@/api/service/AccountService";
import { useRoute, useRouter } from "vue-router";
import { useAuth } from "@/composable/useAuth";
const { login, scope, hasScope } = useAuth();
// Form data
const loginForm = reactive({
  email: "",
  password: "",
  rememberMe: false,
});
const router = useRouter();
const route = useRoute();
// State management
const isLoading = ref(false);
const showPassword = ref(false);
const generalError = ref("");

// Form validation errors
const errors = reactive({
  email: "",
  password: "",
});

// Toggle password visibility
const togglePassword = () => {
  showPassword.value = !showPassword.value;
};

// Validate form
const validateForm = () => {
  // Reset errors
  errors.email = "";
  errors.password = "";

  let isValid = true;

  // Validate email
  if (!loginForm.email.trim()) {
    errors.email = "Vui lòng nhập email ";
    isValid = false;
  }

  // Validate password
  if (!loginForm.password.trim()) {
    errors.password = "Vui lòng nhập mật khẩu";
    isValid = false;
  } else if (loginForm.password.length < 6) {
    errors.password = "Mật khẩu phải có ít nhất 6 ký tự";
    isValid = false;
  }

  return isValid;
};

// Handle login
const handleLogin = async () => {
  // Clear previous errors
  generalError.value = "";

  // // Validate form
  // if (!validateForm()) {
  //   return;
  // }

  try {
    isLoading.value = true;
    const success = await login(loginForm);
    if (success) {
      setTimeout(() => {
        if (hasScope(['ROLE_CUSTOMER'])) {
          router.push({ name: "home" });
        } else {
          router.push({ name: "dashboard" });
        } 
        
      }, 2000);
    } else {
      generalError.value = "Đăng nhập thất bại. Vui lòng kiểm tra lại thông tin.";
      isLoading.value = false;
    }
  } catch (error) {
    generalError.value = "Đăng nhập thất bại. Vui lòng kiểm tra lại thông tin.";
    console.error("Login error:", error);
  }
};
</script>
