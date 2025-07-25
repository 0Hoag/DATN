<template>
  <div class="container my-5">
    <div class="row justify-content-center">
      <div class="col-md-8 col-lg-6">
        <div class="card border-0 shadow-lg">
          <div class="card-body p-5">
            <div class="text-center mb-4">
              <font-awesome-icon
                :icon="['fas', 'user-plus']"
                class="fa-3x text-primary mb-3"
              />
              <h3 class="fw-bold">Đăng ký tài khoản</h3>
              <p class="text-muted">
                Tạo tài khoản để trải nghiệm mua sắm tốt nhất tại TechZone
              </p>
            </div>

            <!-- Register Form -->
            <div id="registerForm">
              <div class="row">
                <div class="col-md-6 mb-3">
                  <label class="form-label fw-semibold">Họ và tên *</label>
                  <input
                    type="text"
                    class="form-control"
                    v-model="fullName"
                    required
                    placeholder="Nhập họ và tên"
                  />
                </div>
                <div class="col-md-6 mb-3">
                  <label class="form-label fw-semibold">Số điện thoại *</label>
                  <input
                    type="tel"
                    class="form-control"
                    v-model="phone"
                    required
                    placeholder="Nhập số điện thoại"
                  />
                </div>
              </div>
              <div class="mb-3">
                <label class="form-label fw-semibold">Email *</label>
                <input
                  type="email"
                  class="form-control"
                  v-model="email"
                  required
                  placeholder="Nhập địa chỉ email"
                />
              </div>
              <div class="row">
                <div class="col-md-6 mb-3">
                  <label class="form-label fw-semibold">Mật khẩu *</label>
                  <div class="input-group">
                    <input
                      :type="showPassword ? 'text' : 'password'"
                      class="form-control"
                      v-model="password"
                      required
                      placeholder="Nhập mật khẩu"
                    />
                    <button
                      class="btn btn-outline-secondary"
                      type="button"
                      @click="showPassword = !showPassword"
                      tabindex="-1"
                    >
                      <font-awesome-icon :icon="['fas', showPassword ? 'eye-slash' : 'eye']" />
                    </button>
                  </div>
                  <div class="form-text">Tối thiểu 8 ký tự, có ít nhất 1 chữ hoa</div>
                </div>
                <div class="col-md-6 mb-3">
                  <label class="form-label fw-semibold">Xác nhận mật khẩu *</label>
                  <div class="input-group">
                    <input
                      :type="showPassword ? 'text' : 'password'"
                      class="form-control"
                      v-model="confirmPassword"
                      required
                      placeholder="Nhập lại mật khẩu"
                    />
                    <button
                      class="btn btn-outline-secondary"
                      type="button"
                      @click="showPassword = !showPassword"
                      tabindex="-1"
                    >
                      <font-awesome-icon :icon="['fas', showPassword ? 'eye-slash' : 'eye']" />
                    </button>
                  </div>
                </div>
              </div>

              <div class="d-grid">
                <button type="button" @click="submitForm" class="btn btn-primary btn-lg">
                  <font-awesome-icon :icon="['fas', 'user-plus']" class="me-2" />Đăng ký
                  tài khoản
                </button>
              </div>
            </div>

            <!-- Login Link -->
            <div class="text-center mt-2">
              <span class="text-muted">Đã có tài khoản? </span>
              <router-link
                :to="{ name: 'user-login' }"
                class="text-decoration-none fw-semibold"
                >Đăng nhập ngay</router-link
              >
            </div>
          </div>
        </div>
      </div>
    </div>
  </div>
</template>

<script setup>
import { handleError, hideLoading, showLoading } from "@/api/functions/common";
import { AccountService } from "@/api/service/AccountService";
import { ref } from "vue";
import { toast } from "vue3-toastify";

const fullName = ref("");
const email = ref("");
const password = ref("");
const confirmPassword = ref("");
const phone = ref("");
const showPassword = ref(false);


const validateForm = () => {
  const regexPhone = /^0\d{9}$/;
  const regexPassword = /^(?=.*[A-Z]).{8,}$/;

  if (
    !fullName.value ||
    !email.value ||
    !password.value ||
    !confirmPassword.value ||
    !phone.value
  ) {
    toast.error("Vui lòng điền đầy đủ thông tin");
    return false;
  }
  if (!regexPhone.test(phone.value)) {
    toast.error("Số điện thoại phải bắt đầu bằng 0 và có 10 chữ số");
    return false;
  }
  if (!regexPassword.test(password.value)) {
    toast.error("Mật khẩu ít nhất 8 ký tự và 1 chữ in hoa");
    return false;
  }
  if (password.value !== confirmPassword.value) {
    toast.error("Mật khẩu xác nhận không khớp!");
    return false;
  }

  return true;
};
const resetForm = () => {
  fullName.value = '';
  phone.value = '';
  password.value = '';
  confirmPassword.value = '';
  email.value = '';
}
const submitForm = async () => {
  try {
    showLoading();
    if(!validateForm()) return ;
    const res = await AccountService.registerAccount({
      fullName: fullName.value,
      password: password.value,
      phone: phone.value,
      email: email.value,
    });
    if(res.result){
      toast.success("Đăng ký tài khoản thành công")
      resetForm();
    }
  } catch (error) {
    handleError(error);
  }finally{
    hideLoading();
  }
}
</script>
