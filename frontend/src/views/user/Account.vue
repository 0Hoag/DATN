<script setup>
import { ref, computed, onMounted, watch } from "vue";
import { useRouter } from "vue-router";
import { useAuth } from "@/composable/useAuth";
import {
  handleError,
  hideLoading,
  showLoading,
  showPromtConfirm,
} from "@/api/functions/common";
import { AccountService } from "@/api/service/AccountService";
import { AddressService } from "@/api/service/AddressService";
import { OrderService } from "@/api/service/OrderService";
import { toast } from "vue3-toastify";
import { useUserStore } from "@/store/userStore";
import { VoucherService } from "@/api/service/VoucherService";
import { ReviewService } from "@/api/service/ReviewService";

const store = useUserStore();
console.log("store", store.userInfo);

const { logout } = useAuth();
const router = useRouter();

const confirmLogout = () =>
  showPromtConfirm("Bạn có chắc chắn muốn đăng xuất không?", () => handleLogout());

const handleLogout = async () => {
  await logout();
  router.push("/");
};

const avatarUrl = computed(() => {
  const name = store.userInfo?.fullName || "Unknown";
  return `https://ui-avatars.com/api/?name=${encodeURIComponent(name)}&background=random`;
});

// Xử lý chọn tab
const activeTab = ref("overview");

// Đổi mật khẩu
const showPasswordForm = ref(false);
const currentPassword = ref("");
const newPassword = ref("");
const confirmPassword = ref("");
const showPassword = ref(false);
const changePasswordErrors = ref({
  newPassword: "",
  confirmPassword: "",
});
function validateForm() {
  const regexPassword = /^(?=.*[A-Z]).{8,}$/;
  changePasswordErrors.value.newPassword = regexPassword.test(newPassword.value)
    ? ""
    : "Mật khẩu mới ít nhất 8 ký tự và 1 chữ in hoa";
  changePasswordErrors.value.confirmPassword =
    newPassword.value == confirmPassword.value ? "" : "Mật khẩu xác nhận không khớp";

  return !!(
    changePasswordErrors.value.newPassword || changePasswordErrors.value.confirmPassword
  );
}

const changePassword = async () => {
  try {
    showLoading();

    if (validateForm()) return;

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
    console.error("Error changing password:", error);
    handleError(error);
  } finally {
    hideLoading();
  }
};

const togglePassword = () => {
  showPassword.value = !showPassword.value;
};

// Xử lý địa chỉ
const addresses = ref([]);

const getAddresses = async () => {
  try {
    const userId = store.userInfo?.id;
    const res = await AddressService.fetchAddressByUser(userId);
    addresses.value = res.result || [];
  } catch (error) {
    handleError(error);
  }
};

// Tạo địa chỉ mới
const fullName = ref("");
const phone = ref("");
const addressLine = ref("");
const isDefault = ref(false);
const addressErrors = ref({
  fullName: "",
  phone: "",
  address: "",
});
const validateFormSaveAddress = () => {
  const regexPhone = /^0\d{9}$/;

  addressErrors.value.fullName = fullName.value ? "" : "Vui lòng nhập họ và tên";
  addressErrors.value.address = addressLine.value ? "" : "Vui lòng nhập địa chỉ";
  addressErrors.value.phone = regexPhone.test(phone.value)
    ? ""
    : "Số điện thoại phải bắt đầu bằng 0 và có 10 chữ số";

  return (
    addressErrors.value.fullName ||
    addressErrors.value.phone ||
    addressErrors.value.address
  );
};

const saveAddress = async () => {
  try {
    showLoading();
    if (validateFormSaveAddress()) return;
    const userId = store.userInfo?.id;
    const payload = {
      fullName: fullName.value,
      phone: phone.value,
      addressLine: addressLine.value,
      isDefault: isDefault.value ? 1 : 0,
      userId,
    };

    await AddressService.createAddressByUser(userId, payload);
    toast.success("Thêm địa chỉ thành công!");

    // Reset form
    fullName.value = "";
    phone.value = "";
    addressLine.value = "";
    isDefault.value = false;

    // const modal = bootstrap.Modal.getInstance(document.getElementById("addAddressModal"));
    // modal.hide();
    document.querySelector("#addAddressModal .btn-close")?.click();

    await getAddresses();
  } catch (err) {
    handleError(err);
  } finally {
    hideLoading();
  }
};

const deleteAddress = async (id) => {
  try {
    await AddressService.deleteAddressByUser(id);
    toast.success("Xóa địa chỉ thành công!");
    await getAddresses();
  } catch (err) {
    handleError(err);
  }
};

//chi tiet don hàng
const selectedOrder = ref(null);

// Đơn hàng
const listOrder = store.userInfo?.orders;
const orderRecent = ref([]);
const orders = ref([]);
const totalOrders = ref(0);
const activeTypeOrder = ref("");

const getOrderRecentByUser = async () => {
  try {
    showLoading();
    const userId = store.userInfo?.id;
    const res = await OrderService.fetchOrdersByUser(userId, { size: 4 });
    orderRecent.value = res.result.data;
    totalOrders.value = res.result.totalElements;
    console.log("order recent", orderRecent.value);
  } catch (error) {
    handleError(error);
  } finally {
    hideLoading();
  }
};
async function updateStatusReceived(order) {
  try {
    showLoading();

    await OrderService.updateStatus(order.id, {
      orderStatus: "RECEIED",
      paymentStatus: "PAID",
    });
    toast.success("Cập nhật trạng thái đơn hàng thành công!");
    await filterOrderByStatus();
  } catch (error) {
    toast.error("Lỗi khi tìm kiếm dữ liệu");
  } finally {
    hideLoading();
  }
}
// lấy thông tin user
const nameUser = ref(store.userInfo?.fullName);
const emailUser = ref(store.userInfo?.email);
const phoneUser = ref(store.userInfo?.phone);
const updateProfileErrors = ref({
  fullName: "",
  phone: "",
});
const validateFormUpdateProfile = () => {
  const regexPhone = /^0\d{9}$/;

  updateProfileErrors.value.fullName = nameUser.value ? "" : "Vui lòng nhập họ và tên";
  updateProfileErrors.value.phone = regexPhone.test(phoneUser.value)
    ? ""
    : "Số điện thoại phải bắt đầu bằng 0 và có 10 chữ số";

  return updateProfileErrors.value.fullName || updateProfileErrors.value.phone;
};
const updateProfile = async () => {
  try {
    showLoading();
    if (validateFormUpdateProfile()) return;
    const userId = store.userInfo?.id;
    const params = {
      fullName: nameUser.value,
      phone: phoneUser.value,
    };
    await AccountService.updateProfile(userId, params);
    toast.success("Cập nhật thành công");
  } catch (error) {
    handleError(error);
  } finally {
    hideLoading();
  }
};

const paginationOrder = ref({
  current: 1,
  pageSize: 5,
  total: 0,
});
const getAllOrdersByUser = async () => {
  try {
    showLoading();
    const userId = store.userInfo?.id;
    const res = await OrderService.fetchOrdersByUser(userId, {
      page: paginationOrder.value.current,
      size: paginationOrder.value.pageSize,
    });

    orders.value = res.result.data;
    paginationOrder.value.total = res.result.totalElements;
  } catch (error) {
    handleError(error);
  } finally {
    hideLoading();
  }
};

const filterOrderByStatus = async () => {
  console.log("type order", activeTypeOrder.value);
  try {
    showLoading();
    const userId = store.userInfo?.id;
    const res = await OrderService.searchOrder({
      orderStatus: activeTypeOrder.value,
      userId: userId,
      page: paginationOrder.value.current,
      size: paginationOrder.value.pageSize,
    });

    orders.value = res.result.data;
    console.log("filter by order", orders.value);
    paginationOrder.value.total = res.result.totalElements;
  } catch (error) {
    handleError(error);
  } finally {
    hideLoading();
  }
};

watch([() => paginationOrder.value.current, () => activeTypeOrder.value], async () => {
  await filterOrderByStatus();
});

const translateStatus = (status) => {
  const map = {
    PENDING: "Chờ xác nhận",
    CONFIRMED: "Đã xác nhận",
    SHIPPED: "Đang giao",
    DELIVERED: "Đã giao",
    RECEIED: "Đã nhận",
    CANCELLED: "Đã hủy",
  };
  return map[status];
};

const changeStatus = (status = "") => {
  activeTypeOrder.value = status;
  paginationOrder.value.current = 1;
};
// watch(activeTypeOrder, async () => {
//   paginationOrder.value.current = 1;
//   await filterOrderByStatus();
// });
const getBadgeOrderStatus = (status) => {
  switch (status) {
    case "PENDING":
      return "secondary";
    case "CONFIRMED":
      return "primary";
    case "SHIPPED":
      return "warning";
    case "DELIVERED":
    case "RECEIED":
      return "success";
    case "CANCELLED":
      return "danger";
    default:
      return "secondary";
  }
};

//reivew
const showReviewForm = ref(false);
const selectedProduct = ref(null);
const selectedRating = ref(0);
const hoverRating = ref(0);
const contentReview = ref("");

const resetFormRating = () => {
  selectedProduct.value = null;
  selectedRating.value = null;
  contentReview.value = "";
  showReviewForm.value = false;
};
const openModalRating = (order) => {
  resetFormRating();
  selectedOrder.value = order;
};
const openReviewForm = (product) => {
  showReviewForm.value = true;
  selectedProduct.value = product;
  console.log(selectedProduct.value);
};

const submitFormReview = async () => {
  try {
    showLoading();
    const formReview = {
      orderDetailId: selectedProduct.value.id,
      rating: selectedRating.value,
      content: contentReview.value,
    };
    const res = await ReviewService.createReview(formReview);
    if (res.result) {
      //reset form
      resetFormRating();

      document.querySelector("#ratingProductModal .btn-close")?.click();
      toast.success("Đánh giá sản phẩm thành công");
      await filterOrderByStatus();
    }
  } catch (error) {
    handleError(error);
  } finally {
    hideLoading();
  }
};

//helper
function formatDate(isoString) {
  if (!isoString) return "";
  return new Date(isoString).toISOString().slice(0, 10);
}

function formatPrice(price) {
  if (typeof price !== "number") return "";
  return new Intl.NumberFormat("vi-VN", {
    style: "currency",
    currency: "VND",
  }).format(price);
}
function getDiscountAmount(order) {
  if (!order?.voucher) return 0;
  const value = order.voucher.discountValue;
  return value <= 100 ? order.totalAmount * (value / 100) : value;
}

//voucher
const listVoucherCanUse = ref([]);
const totalVoucher = computed(() => listVoucherCanUse.value?.length);
const paginationVoucher = ref({
  current: 1,
  pageSize: 10,
  total: 0,
});
const getVoucherUserCanUse = async () => {
  try {
    const res = await VoucherService.getVoucherCanUse({
      size: paginationVoucher.value.pageSize,
      page: paginationVoucher.value.current,
    });
    listVoucherCanUse.value = res.result.data;
    console.log("res", res.result);
    paginationVoucher.value.total = res.result.totalElements;
  } catch (error) {
    handleError(error);
  }
};

watch(
  () => paginationVoucher.value.current,
  () => {
    getVoucherUserCanUse();
  }
);
async function init() {
  await store.loadUser();
  await getOrderRecentByUser();
  await getAllOrdersByUser();
  await getAddresses();
  await getVoucherUserCanUse();
}

// hủy đơn hàng
const cancelOrderReason = ref("Khác");
const openModalCancelOrder = (order) => {
  cancelOrderReason.value = "Khác";
  selectedOrder.value = order;
};
const confirmCancelOrder = () => {
  showPromtConfirm("Xác nhận hủy đơn hàng", () => submitFormCancelOrder());
};
const submitFormCancelOrder = async () => {
  try {
    showLoading();
    const res = await OrderService.cancelOrder(selectedOrder.value.id, {
      reason: cancelOrderReason.value || "Khác",
    });
    if (res.code == 1000) {
      toast.success("Hủy đơn hàng");
      document.querySelector("#cancelOrderModal .btn-close")?.click();
      await filterOrderByStatus();
    }
  } catch (error) {
    handleError(error);
  } finally {
    hideLoading();
  }
};
onMounted(() => {
  init();
});
</script>

<template>
  <div class="container my-4">
    <div class="row">
      <!-- Sidebar Menu -->
      <div class="col-lg-3">
        <div class="card border-0 shadow-sm">
          <div class="card-body p-0">
            <!-- User Info -->
            <div class="p-4 bg-primary text-white text-center">
              <div
                class="bg-white text-primary rounded-circle d-inline-flex align-items-center justify-content-center mb-3"
              >
                <img :src="avatarUrl" class="rounded-circle" />
              </div>
              <h5 class="mb-1" id="userName">{{ store.userInfo?.fullName }}</h5>
              <!-- <small class="opacity-75">Thành viên VIP</small> -->
            </div>

            <!-- Menu Items -->
            <nav class="p-3">
              <a
                class="nav-link d-flex align-items-center py-3 px-3 rounded mb-2"
                :class="{ active: activeTab === 'overview' }"
                href="#"
                @click.prevent="activeTab = 'overview'"
              >
                <font-awesome-icon
                  :icon="['fas', 'tachometer-alt']"
                  class="me-3 text-primary"
                />
                <span>Tổng quan</span>
              </a>
              <a
                class="nav-link d-flex align-items-center py-3 px-3 rounded mb-2"
                :class="{ active: activeTab === 'profile' }"
                href="#"
                @click.prevent="activeTab = 'profile'"
              >
                <font-awesome-icon
                  :icon="['fas', 'user-edit']"
                  class="me-3 text-success"
                />
                <span>Thông tin cá nhân</span>
              </a>
              <a
                class="nav-link d-flex align-items-center py-3 px-3 rounded mb-2"
                :class="{ active: activeTab === 'orders' }"
                href="#"
                @click.prevent="activeTab = 'orders'"
              >
                <font-awesome-icon
                  :icon="['fas', 'shopping-bag']"
                  class="me-3 text-warning"
                />
                <span>Đơn hàng của tôi</span>
              </a>
              <a
                class="nav-link d-flex align-items-center py-3 px-3 rounded mb-2"
                :class="{ active: activeTab === 'addresses' }"
                href="#"
                @click.prevent="activeTab = 'addresses'"
              >
                <font-awesome-icon
                  :icon="['fas', 'map-marker-alt']"
                  class="me-3 text-info"
                />
                <span>Sổ địa chỉ</span>
              </a>
              <a
                class="nav-link d-flex align-items-center py-3 px-3 rounded mb-2"
                :class="{ active: activeTab === 'vouchers' }"
                href="#"
                @click.prevent="activeTab = 'vouchers'"
              >
                <font-awesome-icon icon="fa-solid fa-ticket" class="text-danger me-3" />
                <span>Voucher</span>
              </a>
              <a
                class="nav-link d-flex align-items-center py-3 px-3 rounded mb-2"
                :class="{ active: activeTab === 'security' }"
                href="#"
                @click.prevent="activeTab = 'security'"
              >
                <font-awesome-icon
                  :icon="['fas', 'shield-alt']"
                  class="me-3 text-success"
                />
                <span>Đổi mật khẩu</span>
              </a>
              <hr />
              <a
                class="nav-link d-flex align-items-center py-3 px-3 rounded text-danger"
                href="javascript:void(0)"
                @click.prevent="confirmLogout"
              >
                <i class="fas fa-sign-out-alt me-3"></i>
                <span>Đăng xuất</span>
              </a>
            </nav>
          </div>
        </div>
      </div>

      <!-- Main Content -->
      <div class="col-lg-9">
        <div v-if="activeTab === 'overview'">
          <!-- Overview Section -->
          <div class="card border-0 shadow-sm mb-4">
            <div class="card-body">
              <h4 class="card-title">
                Chào mừng trở lại,
                <span id="welcomeName">{{ store.userInfo?.fullName }}</span
                >!
              </h4>
              <p class="text-muted">
                Quản lý thông tin tài khoản và theo dõi đơn hàng của bạn
              </p>
            </div>
          </div>
          <!-- Quick Stats -->
          <div class="row mb-4">
            <div class="col-md-6 mb-3">
              <div class="card border-0 shadow-sm text-center h-100">
                <div class="card-body">
                  <div
                    class="bg-primary text-white rounded-circle d-inline-flex align-items-center justify-content-center mb-3"
                    style="width: 60px; height: 60px"
                  >
                    <font-awesome-icon :icon="['fas', 'shopping-bag']" size="2x" />
                  </div>
                  <h4 class="mb-1" id="totalOrders">{{ totalOrders }}</h4>
                  <small class="text-muted">Đơn hàng</small>
                </div>
              </div>
            </div>
            <div class="col-md-6 mb-3">
              <div class="card border-0 shadow-sm text-center h-100">
                <div class="card-body">
                  <div
                    class="bg-success text-white rounded-circle d-inline-flex align-items-center justify-content-center mb-3"
                    style="width: 60px; height: 60px"
                  >
                    <font-awesome-icon :icon="['fas', 'gift']" size="2x" />
                  </div>
                  <h4 class="mb-1">{{ totalVoucher }}</h4>
                  <small class="text-muted">Voucher</small>
                </div>
              </div>
            </div>
          </div>
          <!-- Recent Orders -->
          <div class="card border-0 shadow-sm">
            <div class="card-header d-flex justify-content-between align-items-center">
              <h6 class="mb-0">Đơn hàng gần đây</h6>
              <a
                href="#"
                @click.prevent="activeTab = 'orders'"
                class="btn btn-sm btn-outline-primary"
                >Xem tất cả</a
              >
            </div>
            <div class="card-body">
              <div class="row" id="recentOrders">
                <!-- Recent orders will be loaded here -->
                <div class="col-md-3 mb-3 d-flex" v-for="order in orderRecent">
                  <div class="card border d-flex flex-column h-100 w-100">
                    <div
                      class="card-body flex-grow-1 d-flex flex-column justify-content-between m-auto w-100"
                    >
                      <div class="d-flex justify-content-between align-items-start mb-2">
                        <span class="fw-bold">#{{ order.id }}</span>
                        <span
                          :class="`badge text-bg-${getBadgeOrderStatus(
                            order.orderStatus
                          )}`"
                          >{{ translateStatus(order.orderStatus) }}</span
                        >
                      </div>
                      <div>
                        <p
                          class="mb-1 text-truncate"
                          v-for="product in order.orderDetails.slice(0, 2)"
                          :key="product.id"
                        >
                          {{ product.name }} - {{ product.variantName }}
                        </p>
                        <p v-if="order.orderDetails.length > 2">...</p>
                      </div>
                      <small class="text-muted mt-auto"
                        >{{ formatPrice(order.totalAmount) }} •
                        {{ formatDate(order.createdAt) }}</small
                      >
                    </div>
                  </div>
                </div>
              </div>
              <div class="text-center py-2" v-if="listOrder?.length == 0">
                <font-awesome-icon
                  :icon="['fas', 'shopping-bag']"
                  size="3x"
                  class="text-muted mb-3"
                />
                <h5>Chưa có đơn hàng nào</h5>
                <p class="text-muted">Hãy bắt đầu mua sắm để tạo đơn hàng đầu tiên</p>
                <a href="category.html" class="btn btn-primary">Mua sắm ngay</a>
              </div>
            </div>
          </div>
        </div>
        <div v-else-if="activeTab === 'profile'">
          <!-- Profile Section -->
          <div class="card border-0 shadow-sm">
            <div class="card-header">
              <h5 class="mb-0">Thông tin cá nhân</h5>
            </div>
            <div class="card-body">
              <form @submit.prevent="updateProfile">
                <div class="row">
                  <div class="col-md-6 mb-3">
                    <label class="form-label fw-semibold">Họ và tên</label>
                    <input
                      type="text"
                      class="form-control"
                      :class="{ 'is-invalid': updateProfileErrors.fullName }"
                      id="profileFullName"
                      v-model="nameUser"
                      required
                    />
                    <div class="invalid-feedback">
                      {{ updateProfileErrors.fullName }}
                    </div>
                  </div>
                  <div class="col-md-6 mb-3">
                    <label class="form-label fw-semibold">Email</label>
                    <input
                      type="email"
                      class="form-control"
                      id="profileEmail"
                      v-model="emailUser"
                      readonly
                    />
                  </div>
                </div>
                <div class="row">
                  <div class="col-md-6 mb-3">
                    <label class="form-label fw-semibold">Số điện thoại</label>
                    <input
                      type="tel"
                      class="form-control"
                      :class="{ 'is-invalid': updateProfileErrors.phone }"
                      id="profilePhone"
                      v-model="phoneUser"
                      required
                    />
                    <div class="invalid-feedback">
                      {{ updateProfileErrors.phone }}
                    </div>
                  </div>
                </div>
                <button type="submit" class="btn btn-primary">
                  <i class="fas fa-save me-2"></i>Cập nhật thông tin
                </button>
              </form>
            </div>
          </div>
        </div>
        <div v-else-if="activeTab === 'orders'">
          <!-- Orders Section -->
          <div class="card border-0 shadow-sm">
            <div class="card-header">
              <h5 class="mb-0">Đơn hàng của tôi</h5>
            </div>
            <div class="card-body">
              <!-- Order Tabs -->
              <ul class="nav nav-pills mb-4">
                <li class="nav-item">
                  <a
                    class="nav-link"
                    :class="{ active: activeTypeOrder == '' }"
                    href="javascript:void(0)"
                    @click.prevent="changeStatus()"
                  >
                    Tất cả
                  </a>
                </li>
                <li class="nav-item">
                  <a
                    class="nav-link"
                    :class="{ active: activeTypeOrder == 'PENDING' }"
                    href="javascript:void(0)"
                    @click.prevent="changeStatus('PENDING')"
                  >
                    Chờ xác nhận
                  </a>
                </li>
                <li class="nav-item">
                  <a
                    class="nav-link"
                    :class="{ active: activeTypeOrder == 'CONFIRMED' }"
                    href="javascript:void(0)"
                    @click.prevent="changeStatus('CONFIRMED')"
                  >
                    Đã xác nhận
                  </a>
                </li>
                <li class="nav-item">
                  <a
                    class="nav-link"
                    :class="{ active: activeTypeOrder == 'SHIPPED' }"
                    href="javascript:void(0)"
                    @click.prevent="changeStatus('SHIPPED')"
                  >
                    Đang giao
                  </a>
                </li>
                <li class="nav-item">
                  <a
                    class="nav-link"
                    :class="{ active: activeTypeOrder == 'DELIVERED' }"
                    href="javascript:void(0)"
                    @click.prevent="changeStatus('DELIVERED')"
                  >
                    Hoàn thành
                  </a>
                </li>
                <li class="nav-item">
                  <a
                    class="nav-link"
                    :class="{ active: activeTypeOrder == 'RECEIED' }"
                    href="javascript:void(0)"
                    @click.prevent="changeStatus('RECEIED')"
                  >
                    Đã nhận hàng
                  </a>
                </li>
                <li class="nav-item">
                  <a
                    class="nav-link"
                    :class="{ active: activeTypeOrder == 'CANCELLED' }"
                    href="javascript:void(0)"
                    @click.prevent="changeStatus('CANCELLED')"
                  >
                    Đã hủy
                  </a>
                </li>
              </ul>
              <!-- Order List -->
              <div id="ordersList">
                <div class="row" v-if="orders?.length > 0">
                  <div class="col-md-12 mb-3" v-for="order in orders" :key="order.id">
                    <div class="card border shadow-sm">
                      <div class="card-body">
                        <div
                          class="d-flex justify-content-between align-items-center mb-2"
                        >
                          <div>
                            <strong>#{{ order.id }}</strong> •
                            {{ formatDate(order.createdAt) }}
                            <div class="text-muted small">
                              {{ order.address }}
                            </div>
                          </div>
                          <span
                            :class="`badge text-bg-${getBadgeOrderStatus(
                              order.orderStatus
                            )}`"
                          >
                            {{ translateStatus(order.orderStatus) }}
                          </span>
                        </div>
                        <div class="mb-2">
                          <div
                            v-for="item in order.orderDetails"
                            :key="item.id"
                            class="d-flex align-items-center mb-1"
                          >
                            <img
                              :src="item.imageUrl"
                              alt="..."
                              class="me-2 rounded"
                              style="width: 60px; height: 60px; object-fit: cover"
                            />
                            <div>
                              <div>
                                <strong>{{ item.name }}</strong> -
                                {{ item.variantName }}
                              </div>
                              <div class="text-muted small">
                                {{ item.quantity }} x
                                {{ formatPrice(item.price) }}
                              </div>
                            </div>
                          </div>
                        </div>
                        <div class="text-end">
                          <strong>Tổng: {{ formatPrice(order.totalAmount) }}</strong>
                        </div>
                        <div class="text-end mt-2">
                          <button
                            class="btn btn-outline-primary btn-sm mx-2"
                            data-bs-toggle="modal"
                            data-bs-target="#orderDetailModal"
                            @click="
                              selectedOrder = order;
                              console.log('selected order', selectedOrder);
                            "
                          >
                            Xem chi tiết
                          </button>
                          <button
                            v-if="order.orderStatus == 'DELIVERED'"
                            class="btn btn-outline-success btn-sm mx-2"
                            @click="updateStatusReceived(order)"
                          >
                            Đã nhận được hàng
                          </button>
                          <button
                            v-if="order.orderStatus == 'RECEIED'"
                            class="btn btn-warning btn-sm"
                            data-bs-toggle="modal"
                            data-bs-target="#ratingProductModal"
                            @click="openModalRating(order)"
                          >
                            Đánh giá
                          </button>
                          <button
                            v-if="order.orderStatus == 'PENDING'"
                            class="btn btn-danger btn-sm"
                            data-bs-toggle="modal"
                            data-bs-target="#cancelOrderModal"
                            @click="openModalCancelOrder(order)"
                          >
                            Hủy đơn hàng
                          </button>
                        </div>
                      </div>
                    </div>
                  </div>
                </div>
                <div class="text-center py-5" v-else>
                  <font-awesome-icon
                    :icon="['fas', 'shopping-bag']"
                    size="3x"
                    class="text-muted mb-3"
                  />
                  <h5>Chưa có đơn hàng nào</h5>
                  <p class="text-muted">Hãy bắt đầu mua sắm để tạo đơn hàng đầu tiên</p>
                  <a href="category.html" class="btn btn-primary">Mua sắm ngay</a>
                </div>
              </div>
              <!-- Pagination -->
              <div class="d-flex justify-content-center mt-3">
                <a-pagination
                  v-model:current="paginationOrder.current"
                  :total="paginationOrder.total"
                  simple
                  :page-size="paginationOrder.pageSize"
                />
              </div>
            </div>
          </div>
        </div>
        <div v-else-if="activeTab === 'addresses'">
          <!-- Addresses Section -->
          <div class="card border-0 shadow-sm">
            <div class="card-header d-flex justify-content-between align-items-center">
              <h5 class="mb-0">Sổ địa chỉ</h5>
              <button
                class="btn btn-primary"
                data-bs-toggle="modal"
                data-bs-target="#addAddressModal"
              >
                <i class="fas fa-plus me-1"></i>Thêm địa chỉ
              </button>
            </div>
            <div class="card-body">
              <div class="row" v-if="addresses.length > 0">
                <div class="col-md-6 mb-3" v-for="addr in addresses" :key="addr.id">
                  <div class="card shadow-sm h-100">
                    <div class="card-body">
                      <h6 class="fw-bold mb-1">{{ addr.name }}</h6>
                      <div class="d-flex justify-content-between align-items-center mb-1">
                        <div>
                          <b class="mb-1">Name: </b>
                          <span>:{{ addr.fullName }}</span>
                        </div>
                        <span v-if="addr.isDefault" class="badge bg-success"
                          >Mặc định</span
                        >
                      </div>
                      <b class="mb-1">Số điện thoại:</b>
                      <span>{{ addr.phone }}</span> <br />
                      <b class="mb-1">Địa chỉ: </b>
                      <span>{{ addr.addressLine }}</span>

                      <div class="mt-2 text-end">
                        <button
                          class="btn btn-sm btn-outline-danger"
                          @click="deleteAddress(addr.id)"
                        >
                          <i class="fas fa-trash me-1"></i> Xóa
                        </button>
                      </div>
                    </div>
                  </div>
                </div>
              </div>

              <div class="col-12 text-center py-4" v-else>
                <font-awesome-icon
                  :icon="['fas', 'map-marker-alt']"
                  size="3x"
                  class="text-muted mb-3"
                />
                <h6>Chưa có địa chỉ nào</h6>
                <p class="text-muted">Thêm địa chỉ để thuận tiện cho việc đặt hàng</p>
              </div>
            </div>
          </div>
        </div>
        <div v-else-if="activeTab === 'vouchers'">
          <!-- Vouchers Section -->
          <div class="card border-0 shadow-sm">
            <div class="card-header">
              <h5 class="mb-0">Voucher của tôi</h5>
            </div>
            <div class="card-body">
              <!-- Voucher List -->
              <div id="voucherList">
                <div class="row" v-if="listVoucherCanUse?.length > 0">
                  <div
                    class="col-md-12 mb-3"
                    v-for="voucher in listVoucherCanUse"
                    :key="voucher.id"
                  >
                    <div class="card border shadow-sm">
                      <div class="card-body">
                        <div
                          class="d-flex justify-content-between align-items-center mb-2"
                        >
                          <div>
                            <font-awesome-icon
                              icon="fa-solid fa-ticket"
                              class="text-danger me-3"
                            />
                            <strong
                              >#{{ voucher.code }} - giảm
                              {{
                                voucher.discountValue <= 100
                                  ? voucher.discountValue + "%"
                                  : formatPrice(voucher.discountValue)
                              }}</strong
                            >
                            <div class="text-muted small">
                              Mô tả: {{ voucher.description }}
                            </div>
                            <div class="text-muted small">
                              Hạn sử dụng: {{ formatDate(voucher.endAt) }}
                            </div>
                            <div class="text-muted small">
                              Đơn hàng tối thiểu {{ formatPrice(voucher.minOrderValue) }}
                            </div>
                          </div>
                        </div>
                      </div>
                    </div>
                  </div>
                </div>
                <div class="text-center py-5" v-else>
                  <font-awesome-icon
                    :icon="['fas', 'ticket']"
                    size="3x"
                    class="text-muted mb-3"
                  />
                  <h5>Chưa có voucher nào</h5>
                </div>
              </div>
              <!-- Pagination -->
              <div class="d-flex justify-content-center mt-3">
                <a-pagination
                  v-model:current="paginationVoucher.current"
                  :total="paginationVoucher.total"
                  simple
                  :page-size="paginationVoucher.pageSize"
                />
              </div>
            </div>
          </div>
        </div>
        <div v-else-if="activeTab === 'security'">
          <!-- Security Section -->
          <div class="card border-0 shadow-sm">
            <div class="card-header">
              <h5 class="mb-0">Bảo mật tài khoản</h5>
            </div>
            <div class="card-body">
              <div class="mb-4">
                <h6>Đổi mật khẩu</h6>
                <form id="changePasswordForm" @submit.prevent="changePassword">
                  <!-- Mật khẩu hiện tại -->
                  <div class="mb-3">
                    <label class="form-label d-block">Mật khẩu hiện tại</label>
                    <div class="input-group">
                      <input
                        :type="showPassword ? 'text' : 'password'"
                        class="form-control"
                        v-model="currentPassword"
                        required
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
                        required
                        :class="{ 'is-invalid': changePasswordErrors.newPassword }"
                      />
                      <button
                        class="btn btn-outline-secondary"
                        type="button"
                        @click="togglePassword"
                      >
                        <font-awesome-icon :icon="showPassword ? 'eye-slash' : 'eye'" />
                      </button>
                      <div class="invalid-feedback">
                        {{ changePasswordErrors.newPassword }}
                      </div>
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
                        required
                        :class="{ 'is-invalid': changePasswordErrors.confirmPassword }"
                      />
                      <button
                        class="btn btn-outline-secondary"
                        type="button"
                        @click="togglePassword"
                      >
                        <font-awesome-icon :icon="showPassword ? 'eye-slash' : 'eye'" />
                      </button>
                      <div class="invalid-feedback">
                        {{ changePasswordErrors.confirmPassword }}
                      </div>
                    </div>
                  </div>
                  <button type="submit" class="btn btn-primary">Đổi mật khẩu</button>
                </form>
              </div>
            </div>
          </div>
        </div>
      </div>
    </div>
  </div>

  <!-- Add Address Modal -->
  <div class="modal fade" id="addAddressModal" tabindex="-1">
    <div class="modal-dialog">
      <div class="modal-content">
        <div class="modal-header">
          <h5 class="modal-title">Thêm địa chỉ mới</h5>
          <button type="button" class="btn-close" data-bs-dismiss="modal"></button>
        </div>
        <div class="modal-body">
          <form id="addAddressForm" @submit.prevent="saveAddress">
            <div class="row">
              <div class="col-md-6 mb-3">
                <label class="form-label">Họ và tên</label>
                <input
                  type="text"
                  class="form-control"
                  :class="{ 'is-invalid': addressErrors.fullName }"
                  v-model="fullName"
                  required
                />
                <div class="invalid-feedback">
                  {{ addressErrors.fullName }}
                </div>
              </div>
              <div class="col-md-6 mb-3">
                <label class="form-label">Số điện thoại</label>
                <input
                  type="tel"
                  class="form-control"
                  :class="{ 'is-invalid': addressErrors.phone }"
                  v-model="phone"
                  required
                />
                <div class="invalid-feedback">
                  {{ addressErrors.phone }}
                </div>
              </div>
            </div>
            <div class="mb-3">
              <label class="form-label">Địa chỉ chi tiết</label>
              <textarea
                class="form-control"
                rows="3"
                :class="{ 'is-invalid': addressErrors.address }"
                v-model="addressLine"
                required
              ></textarea>
              <div class="invalid-feedback">
                {{ addressErrors.address }}
              </div>
            </div>
            <div class="form-check">
              <input
                class="form-check-input"
                type="checkbox"
                id="defaultAddress"
                v-model="isDefault"
              />
              <label class="form-check-label" for="defaultAddress">
                Đặt làm địa chỉ mặc định
              </label>
            </div>

            <div class="modal-footer">
              <button type="button" class="btn btn-secondary" data-bs-dismiss="modal">
                Hủy
              </button>
              <button type="submit" class="btn btn-primary">Lưu địa chỉ</button>
            </div>
          </form>
        </div>
      </div>
    </div>
  </div>

  <!-- Order Detail Modal -->
  <div
    class="modal fade"
    id="orderDetailModal"
    tabindex="-1"
    aria-labelledby="orderDetailModalLabel"
    aria-hidden="true"
  >
    <div class="modal-dialog modal-lg modal-dialog-scrollable">
      <div class="modal-content">
        <div class="modal-header">
          <h5 class="modal-title" id="orderDetailModalLabel">Chi tiết đơn hàng</h5>
          <button
            type="button"
            class="btn-close"
            data-bs-dismiss="modal"
            aria-label="Close"
          ></button>
        </div>
        <div class="modal-body">
          <div class="row mb-4">
            <div class="col-md-6">
              <h5 class="mb-3">Thông tin đơn hàng</h5>
              <p><strong>Mã đơn hàng:</strong> #{{ selectedOrder?.id }}</p>
              <p><strong>Ngày đặt:</strong> {{ formatDate(selectedOrder?.createdAt) }}</p>
              <p>
                <strong>Trạng thái đơn hàng:</strong>
                <span class="ms-2 fw-bold text-uppercase text-success">
                  {{ selectedOrder?.orderStatus }}
                </span>
              </p>
              <p>
                <strong>Trạng thái Thanh toán:</strong>
                <span class="ms-2 fw-bold text-uppercase text-success">
                  {{ selectedOrder?.paymentStatus }}
                </span>
              </p>
              <p v-if="selectedOrder?.orderStatus === 'CANCELLED'">
                <strong>Lý do hủy đơn hàng:</strong>
                <span class="ms-2 fw-bold text-success">
                  {{ selectedOrder?.reason || "Khác" }}
                </span>
              </p>
            </div>
            <div class="col-md-6">
              <h5 class="mb-3">Thông tin khách hàng</h5>
              <p><strong>Tên khách hàng:</strong> {{ selectedOrder?.fullName }}</p>
              <p><strong>Số điện thoại:</strong> {{ selectedOrder?.phone }}</p>
              <p><strong>Địa chỉ:</strong> {{ selectedOrder?.address }}</p>
            </div>
          </div>

          <hr class="my-4" />

          <h5 class="mb-3">Sản phẩm trong đơn hàng</h5>
          <div class="table-responsive mb-4">
            <table class="table table-striped table-hover">
              <thead>
                <tr>
                  <th>Sản phẩm</th>
                  <th class="text-center">Số lượng</th>
                  <th class="text-end">Đơn giá</th>
                  <th class="text-end">Thành tiền</th>
                </tr>
              </thead>
              <tbody>
                <tr v-for="item in selectedOrder?.orderDetails" :key="item.id">
                  <td>{{ item.name }} - {{ item.variantName }}</td>
                  <td class="text-center">{{ item.quantity }}</td>
                  <td class="text-end">{{ formatPrice(item.price) }}</td>
                  <td class="text-end">{{ formatPrice(item.price * item.quantity) }}</td>
                </tr>
              </tbody>
            </table>
          </div>

          <hr class="my-4" />

          <div class="row">
            <div class="col-md-6">
              <h5 class="mb-3">Thông tin thanh toán</h5>
              <p>
                <strong>Phương thức:</strong> Thanh toán
                <span v-if="selectedOrder?.paymentMethod === 'VNPAY'">VNPay</span>
                <span v-else-if="selectedOrder?.paymentMethod === 'COD'"
                  >khi nhận hàng (COD)</span
                >
                <span v-else>{{ selectedOrder?.paymentMethod }}</span>
              </p>
            </div>
            <div class="col-md-6 text-end">
              <p class="d-flex justify-content-between">
                <span><strong>Phí vận chuyển:</strong></span>
                <span>{{ formatPrice(selectedOrder?.shippingFee || 0) }}</span>
              </p>
              <p class="d-flex justify-content-between">
                <span><strong>Giảm giá:</strong></span>
                <span>{{ formatPrice(getDiscountAmount(selectedOrder)) }}</span>
              </p>
              <p class="d-flex justify-content-between fs-5 fw-bold mt-3 pt-2 border-top">
                <span>Tổng thanh toán:</span>
                <span>{{ formatPrice(selectedOrder?.totalAmount) }}</span>
              </p>
            </div>
          </div>
        </div>
        <div class="modal-footer">
          <button type="button" class="btn btn-secondary" data-bs-dismiss="modal">
            Đóng
          </button>
        </div>
      </div>
    </div>
  </div>

  <!-- Đánh giá sản phẩm -->
  <div
    class="modal fade"
    id="ratingProductModal"
    tabindex="-1"
    aria-labelledby="orderDetailModalLabel"
    aria-hidden="true"
  >
    <div class="modal-dialog modal-lg modal-dialog-scrollable">
      <div class="modal-content">
        <div class="modal-header">
          <h5 class="modal-title" id="orderDetailModalLabel">Đánh giá sản phẩm</h5>
          <button
            type="button"
            class="btn-close"
            data-bs-dismiss="modal"
            aria-label="Close"
          ></button>
        </div>
        <div class="modal-body">
          <h5 class="mb-3" v-if="!showReviewForm">Sản phẩm cần đánh giá</h5>
          <div class="table-responsive mb-4">
            <table class="table table-hover align-middle" v-if="!showReviewForm">
              <thead>
                <tr>
                  <th>STT</th>
                  <th>Sản phẩm</th>
                  <th>Hành động</th>
                </tr>
              </thead>
              <tbody>
                <!--hiển thị danh sách sản phẩm -->
                <tr v-for="(item, index) in selectedOrder?.orderDetails" :key="item.id">
                  <td>{{ index + 1 }}</td>
                  <td>
                    <div class="mb-2 d-flex justify-content-start">
                      <img
                        :src="item.imageUrl"
                        alt="..."
                        class="me-2 rounded"
                        style="width: 60px; height: 60px; object-fit: cover"
                      />
                      <div>
                        <div>
                          <strong>{{ item.name }}</strong> -
                          {{ item.variantName }}
                        </div>
                      </div>
                    </div>
                  </td>
                  <td>
                    <button
                      class="btn btn-sm btn-outline-warning"
                      @click="openReviewForm(item)"
                      v-if="!item.isReviewed"
                    >
                      Đánh giá
                    </button>
                    <span class="badge bg-primary" v-else>Đã đánh giá</span>
                  </td>
                </tr>
              </tbody>
            </table>
            <!-- Hiển thị form đánh giá -->
            <div class="w-100" v-else>
              <p class="fs-5">
                Đánh giá sản phẩm: <strong>{{ selectedProduct.variantName }}</strong>
              </p>
              <div class="star-rating d-flex gap-1 m-2">
                <font-awesome-icon
                  v-for="star in 5"
                  :key="star"
                  :icon="
                    star <= (hoverRating || selectedRating)
                      ? ['fas', 'star']
                      : ['far', 'star']
                  "
                  class="text-warning"
                  @mouseover="hoverRating = star"
                  @mouseleave="hoverRating = 0"
                  @click="selectedRating = star"
                  style="cursor: pointer; font-size: 24px"
                />
              </div>
              <div class="form-floating m-2">
                <textarea
                  class="form-control w-100 h-auto my-2"
                  placeholder="Đánh giá sản phẩm"
                  id="commentReviewProduct"
                  v-model="contentReview"
                ></textarea>
                <label for="commentReviewProduct">Nội dung đánh giá</label>
              </div>
              <div class="m-2">
                <button class="btn btn-secondary" @click="showReviewForm = false">
                  Hủy
                </button>
                <button class="btn btn-primary mx-2" @click="submitFormReview">
                  Gửi
                </button>
              </div>
            </div>
          </div>
        </div>
        <div class="modal-footer">
          <button type="button" class="btn btn-secondary" data-bs-dismiss="modal">
            Đóng
          </button>
        </div>
      </div>
    </div>
  </div>

  <!-- Hủy đơn hàng -->
  <div
    class="modal fade"
    id="cancelOrderModal"
    tabindex="-1"
    aria-labelledby="cancelOrderModal"
    aria-hidden="true"
  >
    <div class="modal-dialog modal-lg modal-dialog-scrollable">
      <div class="modal-content">
        <div class="modal-header">
          <h5 class="modal-title" id="cancelOrderModal">Hủy đơn hàng</h5>
          <button
            type="button"
            class="btn-close"
            data-bs-dismiss="modal"
            aria-label="Close"
          ></button>
        </div>
        <div class="modal-body">
          <div class="form-floating mb-3">
            <textarea
              class="form-control"
              placeholder="Leave a comment here"
              id="cancelOrder"
              v-model="cancelOrderReason"
            ></textarea>
            <label for="cancelOrder">Nhập lý do hủy đơn ở đây</label>
          </div>
        </div>
        <div class="modal-footer">
          <button class="btn btn-primary" @click="confirmCancelOrder(selectedOrder)">
            Xác nhận
          </button>
          <button type="button" class="btn btn-secondary" data-bs-dismiss="modal">
            Đóng
          </button>
        </div>
      </div>
    </div>
  </div>
</template>

<style scoped>
.active {
  background-color: #f8f9fa;
  color: #007bff;
}
</style>
