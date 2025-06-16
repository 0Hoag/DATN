<template>
  <!-- Main Container -->
  <div class="container-fluid vh-100 d-flex align-items-center justify-content-center bg-light">
    <div class="row w-100">
      <div class="col-md-4 mx-auto">
        <div class="card shadow">
          <div class="card-body p-5">
            <!-- Header -->
            <div class="text-center mb-4">
              <div class="mb-3">
                <i class="fas fa-key text-primary" style="font-size: 3rem;"></i>
              </div>
              <h2 class="card-title fw-bold text-primary">Quên mật khẩu</h2>
              <p class="text-muted">
                Nhập email của bạn và chúng tôi sẽ gửi link đặt lại mật khẩu
              </p>
            </div>

            <!-- Success Message -->
            <div v-if="isSuccess" class="alert alert-success text-center" role="alert">
              <i class="fas fa-check-circle me-2"></i>
              <strong>Thành công!</strong><br>
              Chúng tôi đã gửi link đặt lại mật khẩu đến email của bạn.
              <div class="mt-3">
                <button class="btn btn-outline-primary" @click="resetForm">
                  Gửi lại email khác
                </button>
              </div>
            </div>

            <!-- Form -->
            <form v-else @submit.prevent="handleForgotPassword">
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
                  Chúng tôi sẽ gửi link đặt lại mật khẩu đến email này
                </div>
              </div>

              <!-- Submit Button -->
              <div class="d-grid mb-4">
                <button
                  type="submit"
                  class="btn btn-primary btn-lg"
                  :disabled="isLoading"
                >
                  <span v-if="isLoading" class="spinner-border spinner-border-sm me-2" role="status"></span>
                  <i v-else class="fas fa-paper-plane me-2"></i>
                  {{ isLoading ? 'Đang gửi...' : 'Gửi link đặt lại mật khẩu' }}
                </button>
              </div>

              <!-- Back to Login -->
              <div class="text-center">
                <router-link :to="{ name: 'Login' }" class="text-decoration-none text-primary">
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

        <!-- Additional Info Card -->
        <div class="card mt-4 border-0 bg-transparent">
          <div class="card-body text-center">
            <h6 class="text-muted mb-3">
              <i class="fas fa-shield-alt me-2"></i>
              Bảo mật & An toàn
            </h6>
            <div class="row text-center">
              <div class="col-4">
                <i class="fas fa-lock text-primary mb-2" style="font-size: 1.5rem;"></i>
                <small class="d-block text-muted">Bảo mật</small>
              </div>
              <div class="col-4">
                <i class="fas fa-clock text-primary mb-2" style="font-size: 1.5rem;"></i>
                <small class="d-block text-muted">Nhanh chóng</small>
              </div>
              <div class="col-4">
                <i class="fas fa-user-shield text-primary mb-2" style="font-size: 1.5rem;"></i>
                <small class="d-block text-muted">Tin cậy</small>
              </div>
            </div>
          </div>
        </div>
      </div>
    </div>
  </div>
</template>

<script setup>
import { ref } from 'vue'

// Form data
const email = ref('')

// State management
const isLoading = ref(false)
const isSuccess = ref(false)
const generalError = ref('')

// Form validation errors
const errors = ref({
  email: ''
})

// Validate email
const validateEmail = () => {
  errors.value.email = ''
  
  if (!email.value.trim()) {
    errors.value.email = 'Vui lòng nhập địa chỉ email'
    return false
  }
  
  // Email regex validation
  const emailRegex = /^[^\s@]+@[^\s@]+\.[^\s@]+$/
  if (!emailRegex.test(email.value)) {
    errors.value.email = 'Vui lòng nhập địa chỉ email hợp lệ'
    return false
  }
  
  return true
}

// Handle forgot password
const handleForgotPassword = async () => {
  // Clear previous errors
  generalError.value = ''
  
  // Validate email
  if (!validateEmail()) {
    return
  }
  
  try {
    isLoading.value = true
    
    // Simulate API call
    await new Promise(resolve => setTimeout(resolve, 2000))
    
    // Here you would typically call your forgot password API
    console.log('Forgot password for email:', email.value)
    
    // Success
    isSuccess.value = true
    
  } catch (error) {
    generalError.value = 'Có lỗi xảy ra. Vui lòng thử lại sau.'
    console.error('Forgot password error:', error)
  } finally {
    isLoading.value = false
  }
}

// Reset form to send another email
const resetForm = () => {
  email.value = ''
  isSuccess.value = false
  generalError.value = ''
  errors.value.email = ''
}


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
