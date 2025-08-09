<template>
    <!-- Progress Steps -->
    <div class="bg-light py-3">
        <div class="container">
            <div class="row">
                <div class="col-12">
                    <div class="d-flex justify-content-center align-items-center">
                        <div class="d-flex align-items-center me-4">
                            <div class="bg-success text-white rounded-circle d-flex align-items-center justify-content-center me-2" style="width: 30px; height: 30px;">
                                <i class="fas fa-check"></i>
                            </div>
                            <span class="text-success fw-semibold">Giỏ hàng</span>
                        </div>
                        <div class="border-top flex-grow-1 me-4" style="max-width: 100px;"></div>
                        <div class="d-flex align-items-center me-4">
                            <div class="bg-primary text-white rounded-circle d-flex align-items-center justify-content-center me-2" style="width: 30px; height: 30px;">
                                <i class="fas fa-credit-card"></i>
                            </div>
                            <span class="text-primary fw-semibold">Thanh toán</span>
                        </div>
                        <div class="border-top flex-grow-1 me-4" style="max-width: 100px;"></div>
                        <div class="d-flex align-items-center">
                            <div class="bg-secondary text-white rounded-circle d-flex align-items-center justify-content-center me-2" style="width: 30px; height: 30px;">
                                <i class="fas fa-check"></i>
                            </div>
                            <span class="text-muted">Hoàn thành</span>
                        </div>
                    </div>
                </div>
            </div>
        </div>
    </div>

    <div class="container my-4">
        <div class="row">
            <div class="col-lg-8">
                <!-- Customer Information -->
                <div class="card border-0 shadow-sm mb-4">
                    <div class="card-header bg-primary text-white">
                        <h5 class="mb-0"><i class="fas fa-user me-2"></i>Thông tin khách hàng</h5>
                    </div>
                    <div class="card-body">
                        <form id="checkoutForm">
                            <div class="row">
                                <div class="col-md-6 mb-3">
                                    <label class="form-label fw-semibold">Họ và tên *</label>
                                    <input type="text" class="form-control" id="fullName" required placeholder="Nhập họ và tên">
                                </div>
                                <div class="col-md-6 mb-3">
                                    <label class="form-label fw-semibold">Số điện thoại *</label>
                                    <input type="tel" class="form-control" id="phone" required placeholder="Nhập số điện thoại">
                                </div>
                            </div>
                            <div class="mb-3">
                                <label class="form-label fw-semibold">Email</label>
                                <input type="email" class="form-control" id="email" placeholder="Nhập email để nhận thông báo đơn hàng">
                            </div>
                            <div class="mb-3">
                                <label class="form-label fw-semibold">Địa chỉ giao hàng *</label>
                                <textarea class="form-control" id="address" rows="3" required placeholder="Nhập địa chỉ chi tiết (số nhà, tên đường...)"></textarea>
                            </div>
                            <div class="row">
                                <div class="col-md-4 mb-3">
                                    <label class="form-label fw-semibold">Tỉnh/Thành phố *</label>
                                    <select class="form-select" id="province" required>
                                        <option value="">Chọn tỉnh/thành</option>
                                        <option value="hcm">Hồ Chí Minh</option>
                                        <option value="hn">Hà Nội</option>
                                        <option value="dn">Đà Nẵng</option>
                                        <option value="ct">Cần Thơ</option>
                                        <option value="hp">Hải Phòng</option>
                                    </select>
                                </div>
                                <div class="col-md-4 mb-3">
                                    <label class="form-label fw-semibold">Quận/Huyện *</label>
                                    <select class="form-select" id="district" required>
                                        <option value="">Chọn quận/huyện</option>
                                    </select>
                                </div>
                                <div class="col-md-4 mb-3">
                                    <label class="form-label fw-semibold">Phường/Xã *</label>
                                    <select class="form-select" id="ward" required>
                                        <option value="">Chọn phường/xã</option>
                                    </select>
                                </div>
                            </div>
                            <div class="mb-3">
                                <label class="form-label fw-semibold">Ghi chú</label>
                                <textarea class="form-control" id="note" rows="2" placeholder="Ghi chú thêm cho đơn hàng (tùy chọn)"></textarea>
                            </div>
                        </form>
                    </div>
                </div>

              

                <!-- Payment Method -->
                <div class="card border-0 shadow-sm mb-4">
                    <div class="card-header bg-warning text-dark">
                        <h5 class="mb-0"><i class="fas fa-credit-card me-2"></i>Phương thức thanh toán</h5>
                    </div>
                    <div class="card-body">
                        <div class="form-check mb-3 p-3 border rounded" v-for="payment in paymentMethod">
                            <input class="form-check-input" type="radio" name="payment" id="cod" value="cod" checked>
                            <label class="form-check-label w-100" :for="payment.name">
                                <div class="d-flex align-items-center">

                                    <div>
                                        <strong>{{payment.description}}</strong>
                                        <div class="text-muted small">{{ payment.description }} <span v-if="payment.name == 'COD'">khi nhận hàng</span></div>
                                    </div>
                                </div>
                            </label>
                        </div>
                      
                    </div>
                </div>
            </div>

            <div class="col-lg-4">
                <!-- Order Summary -->
                <div class="card border-0 shadow-sm sticky-top" style="top: 20px;">
                    <div class="card-header bg-info text-white">
                        <h5 class="mb-0">Đơn hàng của bạn</h5>
                    </div>
                    <div class="card-body">
                        <div id="orderItems">
                            <!-- Order items will be loaded here -->
                        </div>
                        
                        <hr>
                        
                        <div class="d-flex justify-content-between mb-2">
                            <span>Tạm tính:</span>
                            <span id="subtotal">0₫</span>
                        </div>
                        <div class="d-flex justify-content-between mb-2">
                            <span>Phí vận chuyển:</span>
                            <span id="shippingFee" class="text-success">Miễn phí</span>
                        </div>
                        <div class="d-flex justify-content-between mb-2">
                            <span>Thuế VAT (10%):</span>
                            <span id="tax">0₫</span>
                        </div>
                        <hr>
                        <div class="d-flex justify-content-between fw-bold fs-5">
                            <span>Tổng cộng:</span>
                            <span id="total" class="text-danger">0₫</span>
                        </div>
                        
                        <div class="mt-4">
                            <div class="form-check mb-3">
                                <input class="form-check-input" type="checkbox" id="terms" required>
                                <label class="form-check-label" for="terms">
                                    Tôi đồng ý với <a href="#" class="text-decoration-none">điều khoản và điều kiện</a> của TechZone
                                </label>
                            </div>
                            
                            <div class="d-grid gap-2">
                                <button class="btn btn-primary btn-lg" onclick="placeOrder()">
                                    <i class="fas fa-check me-2"></i>Đặt hàng
                                </button>
                                <a href="cart" class="btn btn-outline-secondary">
                                    <i class="fas fa-arrow-left me-2"></i>Quay lại giỏ hàng
                                </a>
                            </div>
                        </div>
                    </div>
                </div>
            </div>
        </div>
    </div>
</template>
<script setup>
import { handleError } from '@/api/functions/common';
import { PaymentService } from '@/api/service/PaymentService';
import { onMounted, ref } from 'vue';
const paymentMethod = ref([]);
async function fetchPaymentMethods() {
  try {
    const response = await PaymentService.fetchPaymentMethods();
    paymentMethod.value = response.result;
    console.log(paymentMethod.value);
  } catch (error) {
    console.log(error);
    handleError(error);
  }
}
onMounted(async()=>{
    await fetchPaymentMethods();
})
</script>