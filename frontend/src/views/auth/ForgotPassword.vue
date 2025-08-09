<template>
  <!-- Main Container -->
  <div
    class="container-fluid vh-100 d-flex align-items-center justify-content-center bg-light"
  >
    <div class="row w-100">
      <div class="col-md-4 mx-auto">
        <div class="card shadow">
          <div class="card-body p-5">
            <!-- Header -->
            <div class="text-center mb-4">
              <div class="mb-3">
                <i class="fas fa-key text-primary" style="font-size: 3rem"></i>
              </div>
              <h2 class="card-title fw-bold text-primary">Quên mật khẩu</h2>
              <p class="text-muted" v-if="!isSuccess && !isResetPassword">
                Nhập email của bạn và chúng tôi sẽ gửi mã OTP đặt lại mật khẩu
              </p>
            </div>

            <!-- Success Message -->
            <div v-if="isSuccess" class="text-center" role="alert">
              <i class="fas fa-check-circle me-2"></i>
              <strong>Thành công!</strong><br />
              Chúng tôi đã gửi mã OTP đặt lại mật khẩu đến email của bạn.
              <form class="mt-3" @submit.prevent="verifyOtp">
                <input
                  type="text"
                  class="form-control mt-2"
                  placeholder="Nhập mã OTP"
                  v-model="otpCode"
                  :class="{ 'is-invalid': errors.otpCode }"
                  required
                />
                <div v-if="errors.otpCode" class="invalid-feedback">
                  {{ errors.otpCode }}
                </div>
                <button type="submit" class="btn btn-primary mt-2" :disabled="isLoading">
                  <span
                    v-if="isLoading"
                    class="spinner-border spinner-border-sm me-2"
                    role="status"
                  ></span>
                  <i v-else class="fas fa-paper-plane me-2"></i>
                  {{ isLoading ? "Đang xác nhận OTP" : "Xác nhận mã OTP" }}
                </button>
                <button type="button" class="btn btn-secondary mt-2" @click="resetOtp">
                  <span
                    v-if="isLoadingResetOtp"
                    class="spinner-border spinner-border-sm me-2"
                    role="status"
                  ></span>
                  <i v-else class="fas fa-paper-plane me-2"></i>
                  {{ isLoadingResetOtp ? "Đang gửi" : "Gửi lại OTP" }}
                </button>
              </form>
            </div>
            <!-- Reset password -->
            <div v-else-if="isResetPassword" class="" role="alert">
              <div class="mt-3">
                <div class="mb-3">
                  <label for="newPassword">Mật khẩu mới:</label>
                  <input
                    id="newPassword"
                    type="password"
                    class="form-control mt-2"
                    :class="{ 'is-invalid': errors.newPassword }"
                    placeholder="Nhập mật khẩu mới"
                    v-model="newPassword"
                    required
                  />
                  <div v-if="errors.newPassword" class="invalid-feedback d-block">
                    {{ errors.newPassword }}
                  </div>
                </div>
                <div class="mb-3">
                  <label for="confirmNewPassword">Xác nhận mật khẩu</label>
                  <input
                    id="confirmNewPassword"
                    type="password"
                    class="form-control mt-2"
                    placeholder="Xác nhận mật khẩu mới"
                    v-model="confirmNewPassword"
                    :class="{ 'is-invalid': errors.confirmNewPassword }"
                    required
                  />
                  <div v-if="errors.confirmNewPassword" class="invalid-feedback">
                    {{ errors.confirmNewPassword }}
                  </div>
                </div>
                <button
                  class="btn btn-primary mt-2"
                  @click="handleResetPassword"
                  :disabled="isLoading"
                >
                  Gửi
                </button>
              </div>
              <div class="alert alert-success text-center mt-2" v-if="isRessetSuccess">
                <i class="fas fa-check-circle me-2"></i>
                Mật khẩu đã được đặt lại thành công. Bạn có thể đăng nhập bằng mật khẩu mới.
              </div>
              <div class="text-center">
                <router-link
                  :to="{ name: 'user-login' }"
                  class="text-decoration-none text-primary"
                >
                  <i class="fas fa-arrow-left me-2"></i>
                  Quay lại đăng nhập
                </router-link>
              </div>
            </div>
            <!-- Form -->
            <form v-else @submit.prevent="verifyEmail">
              <!-- Email Input -->
              <div class="mb-4">
                <label for="email" class="form-label fw-semibold">
                  <i class="fas fa-envelope me-2"></i>Địa chỉ email
                </label>
                <input
                  type="email"
                  class="form-control form-control-lg"
                  id="email"
                  v-model="email"
                  placeholder="Nhập địa chỉ email của bạn"
                  required
                  :class="{ 'is-invalid': errors.email }"
                />
                <div v-if="errors.email" class="invalid-feedback">
                  {{ errors.email }}
                </div>
                <div class="form-text">
                  <i class="fas fa-info-circle me-1"></i>
                  Chúng tôi sẽ gửi mã OTP đặt lại mật khẩu đến email này
                </div>
              </div>

              <!-- Submit Button -->
              <div class="d-grid mb-4">
                <button
                  type="submit"
                  class="btn btn-primary btn-lg"
                  :disabled="isLoading"
                >
                  <span
                    v-if="isLoading"
                    class="spinner-border spinner-border-sm me-2"
                    role="status"
                  ></span>
                  <i v-else class="fas fa-paper-plane me-2"></i>
                  {{ isLoading ? "Đang gửi..." : "Gửi mã đặt lại mật khẩu" }}
                </button>
              </div>

              <!-- Back to Login -->
              <div class="text-center">
                <router-link
                  :to="{ name: 'user-login' }"
                  class="text-decoration-none text-primary"
                >
                  <i class="fas fa-arrow-left me-2"></i>
                  Quay lại đăng nhập
                </router-link>
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
import { ForgotPasswordService } from "@/api/service/ForgotPassword";
import router from "@/router";
import { ref } from "vue";
import { toast } from "vue3-toastify";

// Form data
const email = ref("");
const otpCode = ref("");
const newPassword = ref("");
const confirmNewPassword = ref("");
// State management
const isLoading = ref(false);
const isRessetSuccess = ref(false);
const isLoadingResetOtp = ref(false);
const isSuccess = ref(false);
const isResetPassword = ref(false);
const generalError = ref("");

// Form validation errors
const errors = ref({
  email: "",
  otpCode: "",
  confirmNewPassword: "",
  newPassword: "",
});

// Validate email
const validateEmail = () => {
  errors.value.email = "";

  if (!email.value.trim()) {
    errors.value.email = "Vui lòng nhập địa chỉ email";
    return false;
  }

  // Email regex validation
  const emailRegex = /^[^\s@]+@[^\s@]+\.[^\s@]+$/;
  if (!emailRegex.test(email.value)) {
    errors.value.email = "Vui lòng nhập địa chỉ email hợp lệ";
    return false;
  }

  return true;
};

// Handle forgot password
const handleForgotPassword = async () => {
  // Clear previous errors
  generalError.value = "";

  // Validate email
  if (!validateEmail()) {
    return;
  }

  try {
    isLoading.value = true;

    // Simulate API call
    await new Promise((resolve) => setTimeout(resolve, 2000));

    // Here you would typically call your forgot password API
    console.log("Forgot password for email:", email.value);

    // Success
    isSuccess.value = true;
  } catch (error) {
    generalError.value = "Có lỗi xảy ra. Vui lòng thử lại sau.";
    console.error("Forgot password error:", error);
  } finally {
    isLoading.value = false;
  }
};

// Reset form to send another email
const resetForm = () => {
  email.value = "";
  isSuccess.value = false;
  generalError.value = "";
  errors.value.email = "";
};

const verifyEmail = async () => {
  // Reset errors
  errors.value.email = "";
  try {
    isLoading.value = true;
    // Validate email
    if (!validateEmail()) {
      return false;
    }

    const res = await ForgotPasswordService.verifyEmail(email.value);

    isSuccess.value = true;
  } catch (error) {
    errors.value.email =
      error.response?.data?.message || "Có lỗi xảy ra. Vui lòng thử lại sau.";
    // isLoading.value = false;
  } finally {
    isLoading.value = false;
  }
};
const resetOtp = async () => {
  // Reset errors
  errors.value.email = "";
  try {
    isLoadingResetOtp.value = true;
    // Validate email
    if (!validateEmail()) {
      return false;
    }

    const res = await ForgotPasswordService.verifyEmail(email.value);

    isSuccess.value = true;
  } catch (error) {
    errors.value.email =
      error.response?.data?.message || "Có lỗi xảy ra. Vui lòng thử lại sau.";
    // isLoadingResetOtp.value = false;
  } finally {
    isLoadingResetOtp.value = false;
  }
};
const verifyOtp = async () => {
  // Reset errors
  errors.value.email = "";
  try {
    isLoading.value = true;
    // Validate email
    if (!validateEmail()) {
      return false;
    }

    const res = await ForgotPasswordService.verifyOtp(otpCode.value, email.value);
    if (res.code == 1000) {
      isResetPassword.value = true;
      isSuccess.value = false;
      otpCode.value = "";
      errors.otpCode.value = "";
    }
  } catch (error) {
    errors.value.otpCode = error.response?.data?.message;
    // isLoading.value = false;
  } finally {
    isLoading.value = false;
  }
};

function validateForm() {
  let valid = true;
  errors.value.newPassword = "";
  errors.value.confirmNewPassword = "";

  const regexPassword = /^(?=.*[A-Z]).{8,}$/;

  if (!newPassword.value.trim()) {
    errors.value.newPassword = "Vui lòng nhập mật khẩu mới";
    valid = false;
  } else if (!regexPassword.test(newPassword.value)) {
    errors.value.newPassword = "Mật khẩu mới ít nhất 8 ký tự và 1 chữ in hoa";
    valid = false;
  }

  if (!confirmNewPassword.value.trim()) {
    errors.value.confirmNewPassword = "Vui lòng xác nhận mật khẩu mới";
    valid = false;
  } else if (newPassword.value !== confirmNewPassword.value) {
    errors.value.confirmNewPassword = "Mật khẩu xác nhận không khớp";
    valid = false;
  }

  return valid;
}
const handleResetPassword = async () => {
  // Reset errors
  errors.value.email = "";
  errors.value.otpCode = "";
  errors.value.confirmNewPassword = "";

  // Validate form
  if (!validateForm()) {
    return;
  }

  try {
    isLoading.value = true;
    const res = await ForgotPasswordService.resetPassword(email.value, {
      newPassword: newPassword.value,
      confirmNewPassword: confirmNewPassword.value,
    });

    if (res.code == 1000) {
      isRessetSuccess.value = true;
      isSuccess.value = false;
      otpCode.value = "";
      newPassword.value = "";
      confirmNewPassword.value = "";
      errors.otpCode.value = "";
      errors.newPassword.value = "";
      errors.confirmNewPassword.value = "";
    }
  } catch (error) {
    errors.value.confirmNewPassword = error.response?.data?.message;
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

.btn-outline-primary {
  border-radius: 10px;
  font-weight: 600;
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

.alert {
  border-radius: 10px;
}

.card-body .row .col-4 i {
  transition: transform 0.3s ease;
}

.card-body .row .col-4:hover i {
  transform: scale(1.1);
}

a {
  transition: all 0.3s ease;
}

a:hover {
  transform: translateX(5px);
}
</style>
