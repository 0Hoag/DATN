<template>
  <div class="container-fluid vh-100 d-flex align-items-center justify-content-center bg-light">
    <div class="row w-100">
      <div class="col-md-4 mx-auto">
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
                <label for="email" class="form-label fw-semibold"> <i class="fas fa-envelope me-2"></i>Email </label>
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
                <label for="password" class="form-label fw-semibold"> <i class="fas fa-lock me-2"></i>Mật khẩu </label>
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
                  <button class="btn btn-outline-secondary" type="button" @click="togglePassword">
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
                  <input class="form-check-input" type="checkbox" id="rememberMe" v-model="loginForm.rememberMe" />
                  <label class="form-check-label" for="rememberMe"> Ghi nhớ đăng nhập </label>
                </div>
                <router-link :to="{ name: 'ForgotPassword' }" class="text-decoration-none text-primary"> Quên mật khẩu? </router-link>
              </div>

              <!-- Login Button -->
              <div class="d-grid mb-3">
                <button type="submit" class="btn btn-primary btn-lg" :disabled="isLoading">
                  <span v-if="isLoading" class="spinner-border spinner-border-sm me-2" role="status"></span>
                  <i v-else class="fas fa-sign-in-alt me-2"></i>
                  {{ isLoading ? "Đang đăng nhập..." : "Đăng nhập" }}
                </button>
              </div>
            </form>

            <!-- Alert for errors -->
            <div v-if="generalError" class="alert alert-danger mt-3" role="alert">
              <i class="fas fa-exclamation-triangle me-2"></i>
              {{ generalError }}
            </div>
          </div>
        </div>
      </div>
    </div>
  </div>
</template>

<script setup>
import { jwtDecode } from "jwt-decode";
import { ref, reactive } from "vue";
import { AccountService } from "@/api/service/AccountService";
import { useRouter } from "vue-router";

// Form data
const loginForm = reactive({
  email: "",
  password: "",
  rememberMe: false,
});
const router = useRouter();
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

    const response = await AccountService.login(loginForm); // <-- thay thế bằng service thực tế
    const token = response.result.token;

    // Giải mã token
    const decoded = jwtDecode(token); 

    const role = decoded?.scope; // hoặc decoded?.user?.role tùy backend

    // Lưu token vào localStorage hoặc cookies nếu cần
    localStorage.setItem("token", token);
    localStorage.setItem("role", role);

    const roles = ["ROLE_ADMIN", "ROLE_MANAGER", "ROLE_SHIFT_STAFF"];
    if (roles.some((r) => role.includes(r))) {
      router.push("/admin/dashboard");
    } else {
      router.push("/login");
    }
  } catch (error) {
    generalError.value = "Đăng nhập thất bại. Vui lòng kiểm tra lại thông tin.";
    console.error("Login error:", error);
  } finally {
    isLoading.value = false;
  }
};
</script>

<style scoped>
.card {
  border: none;
  border-radius: 15px;
}

.form-control:focus {
  border-color: #0d6efd;
  box-shadow: 0 0 0 0.2rem rgba(13, 110, 253, 0.25);
}

.btn-primary {
  background: linear-gradient(45deg, #0d6efd, #0b5ed7);
  border: none;
  border-radius: 10px;
  font-weight: 600;
  transition: all 0.3s ease;
}

.btn-primary:hover {
  transform: translateY(-2px);
  box-shadow: 0 5px 15px rgba(13, 110, 253, 0.3);
}

.bg-light {
  background: linear-gradient(135deg, #f8f9fa 0%, #e9ecef 100%) !important;
}

.text-primary {
  color: #0d6efd !important;
}

.form-control-lg {
  border-radius: 10px;
}

.input-group .btn {
  border-radius: 0 10px 10px 0;
}
</style>
