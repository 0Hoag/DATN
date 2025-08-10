<template>
  <div
    class="d-flex justify-content-center align-items-center"
    style="min-height: 500px"
    v-if="listCart && listCart.length == 0"
  >
    <div class="text-center py-2">
      <font-awesome-icon
        :icon="['fas', 'shopping-bag']"
        size="3x"
        class="text-muted mb-3"
      />
      <h5>Chưa có sản phẩm nào trong giỏ hàng</h5>
      <p class="text-muted">Hãy bắt đầu mua sắm để tạo đơn hàng đầu tiên</p>
    </div>
  </div>
  <div v-else>
    <!-- Địa điểm giao hàng -->
    <div class="bg-white p-3 rounded shadow-sm mb-3" v-if="isDisplayOnCart">
      <!-- <h5 class="mb-3">Thông tin giao hàng</h5>
      <div class="d-flex align-items-center mb-2">
        <input type="radio" name="deliveryOption" checked class="form-check-input me-2" />
        <label class="form-check-label me-4">Giao tận nơi</label>
        <input type="radio" name="deliveryOption" class="form-check-input me-2" />
        <label class="form-check-label">Nhận tại siêu thị</label>
      </div> -->

      <div class="mt-3 p-3 bg-light rounded">
        <div class="d-flex justify-content-between align-items-center">
          <div v-if="addresses.length > 0">
            <div class="fw-bold">
              Người nhận: {{ selectedAddress?.fullName }} - {{ selectedAddress?.phone }}
            </div>
            <div class="text-muted small">
              <i class="fas fa-map-marker-alt me-1"></i>
              Địa chỉ: {{ selectedAddress?.addressLine }}
            </div>
          </div>
          <div v-else>
            <div class="fw-bold">Chưa có địa chỉ vui lòng tạo địa chỉ</div>
          </div>
          <button class="btn btn-outline-secondary btn-sm" @click="showModalAddress">
            <font-awesome-icon icon="fa-solid fa-pen-to-square" />
          </button>

          <a-modal
            v-model:open="openModalAddress"
            title="Danh sách địa chỉ"
            @ok="handleOkAddress"
          >
            <template #footer>
              <a-button key="back" @click="handleCancelAddress">Hủy</a-button>
              <a-button
                key="submit"
                type="primary"
                :loading="loading"
                @click="handleOkAddress"
                >Xác nhận</a-button
              >
            </template>
            <!-- Danh sách địa chỉ -->
            <div
              v-for="(addr, index) in addresses"
              :key="index"
              class="d-flex align-items-start border p-3 mb-2 rounded"
              :class="{ 'border-primary': selectedIndex === index }"
              @click="selectedIndex = index"
              style="cursor: pointer"
            >
              <input
                type="radio"
                class="form-check-input me-3 mt-1"
                :checked="selectedIndex === index"
                @change="selectedIndex = index"
              />
              <div>
                <div>
                  <span class="fw-bold">{{ addr.fullName }}</span> - {{ addr.phone }}
                </div>
                <div>{{ addr.addressLine }}</div>
                <div>
                  <span class="badge text-bg-success">{{
                    addr.isDefault ? "Mặc định" : ""
                  }}</span>
                </div>
              </div>
            </div>

            <!-- Thêm địa chỉ -->
            <div class="text-center mt-4">
              <button class="btn btn-outline-success" @click="showForm = !showForm">
                {{ showForm ? "Hủy thêm địa chỉ" : "➕ Thêm địa chỉ mới" }}
              </button>
            </div>

            <!-- Form thêm địa chỉ -->
            <form
              v-if="showForm"
              class="mt-3 border-top pt-3"
              @submit.prevent="addAddress"
            >
              <div class="row g-2">
                <div class="col-md-6">
                  <input
                    v-model="newAddress.fullName"
                    class="form-control"
                    placeholder="Họ tên người nhận"
                    required
                  />
                </div>
                <div class="col-md-6">
                  <input
                    v-model="newAddress.phone"
                    class="form-control"
                    placeholder="Số điện thoại người nhận"
                    required
                  />
                </div>
                <div class="col-12">
                  <input
                    v-model="newAddress.addressLine"
                    class="form-control"
                    placeholder="Địa chỉ cụ thể"
                    required
                  />
                </div>
              </div>
              <div class="mt-3 text-end">
                <button class="btn btn-success" @click="addNewAddress">
                  Thêm địa chỉ
                </button>
              </div>
            </form>
          </a-modal>
        </div>
      </div>
    </div>

    <!-- Danh sách sản phẩm -->
    <div class="bg-white p-3 rounded shadow-sm mb-3">
      <div class="d-flex my-3" v-for="item in listCart">
        <img
          :src="getThumnailForVariant(item).imageUrl"
          :alt="getThumnailForVariant(item).altText"
          style="width: 80px; height: 80px; object-fit: cover; border-radius: 8px"
          class="me-3"
        />

        <div class="flex-grow-1">
          <h6 class="mb-1">{{ item.productVariant.variantName }}</h6>
          <!-- <div class="mb-1">
            <span class="badge bg-secondary">Màu đen</span>
          </div>
          <div>
            <a href="#" class="text-primary small">8 Khuyến mãi</a>
          </div> -->
        </div>

        <div class="text-end">
          <div class="text-danger fw-bold mb-2">
            {{
              item.productVariant.salePrice
                ? formatPrice(item.productVariant.salePrice)
                : formatPrice(item.productVariant.price)
            }}
          </div>
          <del
            class="text-danger fw-bold mb-2 text-muted"
            v-if="item.productVariant.salePrice"
            >{{ formatPrice(item.productVariant.price) }}</del
          >
          <div class="d-flex align-items-center justify-content-end">
            <button
              class="btn btn-outline-secondary btn-sm me-1"
              @click="decreaseQuantity(item)"
            >
              −
            </button>
            <input
              type="text"
              class="form-control form-control-sm text-center"
              :value="item.quantity"
              style="width: 50px"
              readonly
            />
            <button
              class="btn btn-outline-secondary btn-sm ms-1"
              @click="increaseQuantity(item)"
            >
              +
            </button>
          </div>
          <div class="mt-2 text-end">
            <button
              class="btn btn-link text-danger btn-sm"
              @click="deleteItem(item.productVariant.id)"
            >
              Xóa
            </button>
          </div>
        </div>
      </div>
    </div>

    <!-- Voucher -->
    <div class="bg-white p-3 rounded shadow-sm mb-3" v-if="isDisplayOnCart">
      <div class="d-flex justify-content-between align-items-center mb-3">
        <h6 class="mb-0">
          <i class="fas fa-gift text-primary me-2"></i>Sử dụng mã giảm giá
        </h6>
        <button class="btn btn-outline-secondary btn-sm" @click="showModalVoucher">
          +
        </button>

        <a-modal
          v-model:open="openModalVoucher"
          title="Danh sách voucher của bạn"
          @ok="handleOk"
        >
          <template #footer>
            <a-button key="back" @click="handleCancel">Hủy</a-button>
            <a-button key="submit" type="primary" :loading="loading" @click="handleOk"
              >Áp dụng</a-button
            >
          </template>
          <div class="mb-3">
            <div class="mb-3">
              <input
                type="text"
                class="form-control w-100"
                placeholder="Tìm kiếm voucher.."
                @input="handleSearch"
                v-model="searchKeyword"
              />
            </div>
            <div class="row g-2">
              <div
                v-for="voucher in listVoucherCanUse"
                :key="voucher.code"
                class="col-12"
              >
                <div
                  class="border rounded p-2 hover-bg-light"
                  style="cursor: pointer"
                  :class="{
                    'border-primary bg-light': selectedVouchers == voucher,
                  }"
                  @click="selectedVouchers = voucher"
                >
                  <div class="d-flex justify-content-between align-items-center">
                    <div>
                      <span class="badge bg-warning text-dark me-2">{{
                        voucher.code
                      }}</span>
                      <span class="small">{{ voucher.description }}</span>
                    </div>
                    <div class="text-success fw-bold">
                      -{{
                        voucher.type === "percent"
                          ? voucher.discountValue + "%"
                          : formatPrice(voucher.discountValue)
                      }}
                    </div>
                  </div>
                </div>
              </div>
            </div>
          </div>
        </a-modal>
      </div>

      <div>
        <!-- Voucher có sẵn -->

        <!-- Voucher đã áp dụng -->
        <div v-if="selectedVouchers" class="mt-3">
          <div class="small text-muted mb-2">Voucher đã áp dụng:</div>
          <div class="d-flex flex-wrap gap-2">
            <span

              class="badge bg-success d-flex align-items-center"
            >
              {{ selectedVouchers?.code }} - {{ formatPrice(discountAmount) }}
              
            </span>
          </div>
        </div>
      </div>
    </div>

    <!-- Ghi chú -->
    <div class="bg-white p-3 rounded shadow-sm mb-3" v-if="isDisplayOnCart">
      <h6 class="fw-bold mb-3">Ghi chú</h6>

      <div class="form-floating">
        <textarea
          class="form-control"
          placeholder="Ghi chú"
          id="floatingTextarea2"
          style="height: 100px"
          v-model="noteForOrder"
        ></textarea>
        <label for="floatingTextarea2">Ghi chú</label>
      </div>
    </div>
    <!-- Hình thức thanh toán -->
    <div class="bg-white p-3 rounded shadow-sm mb-3" v-if="isDisplayOnCart">
      <h6 class="fw-bold mb-3">Hình thức thanh toán</h6>

      <div
        class="form-check mb-2 p-2 border rounded"
        v-for="medthod in listpaymentMethod"
      >
        <input
          class="form-check-input"
          type="radio"
          name="paymentMethod"
          id="cash"
          v-model="selectedPaymentMethod"
          :value="medthod.id"
        />
        <label class="form-check-label d-flex align-items-center" for="cash">
          <font-awesome-icon
            :icon="medthod.name === 'COD' ? 'money-bill-wave' : 'mobile-alt'"
            class="me-2"
            :class="medthod.name === 'COD' ? 'text-success' : 'text-primary'"
          />
          {{ medthod.description }}
        </label>
      </div>
    </div>

    <!-- Tóm tắt đơn hàng -->
    <div class="bg-white p-3 rounded shadow-sm mt-3">
      <h6 class="mb-3">Tóm tắt đơn hàng</h6>

      <ul class="list-group list-group-flush mb-3">
        <li class="list-group-item d-flex justify-content-between">
          Tạm tính: <strong>{{ formatPrice(subtotal) }}</strong>
        </li>
        <li class="list-group-item d-flex justify-content-between">
          Giảm giá: <span class="text-success">{{ formatPrice(discountAmount) }}</span>
        </li>
        <li class="list-group-item d-flex justify-content-between">
          Phí giao hàng: <span class="text-muted">Miễn phí</span>
        </li>
        <li class="list-group-item d-flex justify-content-between fw-bold fs-6">
          Tổng cộng: <span class="text-danger">{{ formatPrice(totalPrice) }}</span>
        </li>
      </ul>

      <div class="d-grid">
        <button @click="checkPermissionToCreateOrder" class="btn btn-primary btn-lg">
          <i class="fas fa-credit-card me-2"></i>Tiến hành đặt hàng
        </button>
      </div>
    </div>
  </div>
  <!-- <button @click="paymentSuccessful.open()">test</button> -->
  <!-- Modal thanh toan thanh cong -->
  <Modal ref="paymentSuccessful" :size="'sm'">
    <template #body>
      <div class="text-center">
        <font-awesome-icon
          icon="fa-solid fa-circle-check"
          size="2xl"
          class="text-success"
        />
        <h5 class="mt-3">Thanh toán thành công!</h5>
        <p class="mb-1">Cảm ơn bạn đã thanh toán.</p>
        <p><strong>Mã đơn hàng:</strong> #{{ orderId ?? "" }}</p>
        <button type="button" class="btn btn-success mt-3" data-bs-dismiss="modal">
          Đóng
        </button>
      </div>
    </template>
  </Modal>
</template>
<script setup>
import { handleError, hideLoading, showLoading } from "@/api/functions/common";
import { AddressService } from "@/api/service/AddressService";
import { CartService } from "@/api/service/CartService";
import { OrderService } from "@/api/service/OrderService";
import { PaymentService } from "@/api/service/PaymentService";
import { VoucherService } from "@/api/service/VoucherService";
import { useCartStore } from "@/store/cartStore";
import { useUserStore } from "@/store/userStore";
import { storeToRefs } from "pinia";
import { computed, onBeforeMount, onMounted, ref } from "vue";
import { useRoute, useRouter } from "vue-router";
import { toast } from "vue3-toastify";
import Modal from "@/components/Modal.vue";
const paymentSuccessful = ref(null);

const router = useRouter();
const route = useRoute();
const cartStore = useCartStore();
const userStore = useUserStore();

const searchKeyword = ref("");
const timer = ref(null);

const isDisplayOnCart = ref(!!userStore.token);

const increaseQuantity = async (item) => {
  const maxQuantity = item.productVariant.quantity;
  const newQty = item.quantity + 1;

  if (newQty > maxQuantity) {
    toast.warn("Không được vượt quá số lượng sản phẩm");
    return;
  }
  item.quantity = newQty;

  console.log("update quantity", newQty);
  await cartStore.updateCart(item.productVariant.id, newQty);
  // await cartStore.getCart();
  await getCart();
};
const decreaseQuantity = async (item) => {
  const newQty = item.quantity - 1;

  if (newQty < 1) {
    toast.warn("Số lượng tối thiểu là 1");
    return;
  }

  console.log("update quantity", newQty);
  await cartStore.updateCart(item.productVariant.id, newQty);
  // await cartStore.getCart();
  await getCart();
};
const listCart = ref([]);
const getCart = async () => {
  await cartStore.getCart();
  listCart.value = cartStore.cartItem;
};
const deleteItem = async (id) => {
  await cartStore.deleteItem(id); // đợi xóa xong
  // await cartStore.getCart(); // rồi mới gọi lại giỏ hàng
  await getCart();
};
const getThumnailForVariant = (variant) => {
  return variant.productVariant.images.find((v) => v.isThumbnail);
};
function formatPrice(price) {
  if (typeof price !== "number" || isNaN(price)) return "0đ";
  return new Intl.NumberFormat("vi-VN", {
    style: "currency",
    currency: "VND",
  }).format(price);
}

const subtotal = computed(() => {
  return cartStore.cartItem.reduce((acc, item) => {
    const price =
      item.productVariant.salePrice > 0
        ? item.productVariant.salePrice
        : item.productVariant.price;
    return acc + price * item.quantity;
  }, 0);
});

const discountAmount = computed(() => {
  const discountValue = selectedVouchers.value?.discountValue;
  return discountValue >= 100 ? discountValue : (subtotal.value * discountValue) / 100;
});

const totalPrice = computed(() => {
  return subtotal.value - (discountAmount.value || 0);
});

const checkPermissionToCreateOrder = async () => {
  if (!userStore.token) {
    toast.warn("Vui lòng đăng nhập để đặt hàng");
    return;
  }
  await checkout();

  toast.success("Đơn hàng đã tạo thành công");

  await getCart();
};

//voucher
const loading = ref(false);
const openModalVoucher = ref(false);
const showModalVoucher = () => {
  openModalVoucher.value = true;
};
const handleOk = () => {
  loading.value = true;
  console.log("slectedVoucher", selectedVouchers.value);
  setTimeout(() => {
    loading.value = false;
    openModalVoucher.value = false;
  }, 500);
};
const handleCancel = () => {
  selectedVouchers.value = null;
  openModalVoucher.value = false;
};
const availableVouchers = ref([
  {
    code: "TGDD100K",
    description: "Giảm 100K cho đơn hàng từ 2 triệu",
    discount: 100000,
    type: "fixed",
    minOrder: 2000000,
  },
  {
    code: "NEWUSER15",
    description: "Giảm 15% cho khách hàng mới (tối đa 500K)",
    discount: 15,
    type: "percent",
    maxDiscount: 500000,
  },
  {
    code: "FREESHIP",
    description: "Miễn phí giao hàng",
    discount: 30000,
    type: "fixed",
  },
  {
    code: "STUDENT10",
    description: "Giảm 10% cho sinh viên (tối đa 300K)",
    discount: 10,
    type: "percent",
    maxDiscount: 300000,
  },
]);

const selectedPaymentMethod = ref("1");
const selectedVouchers = ref(null);

//payment
const listpaymentMethod = ref([]);
async function fetchPaymentMethods() {
  try {
    const response = await PaymentService.fetchPaymentMethods();
    listpaymentMethod.value = response.result;
    console.log(listpaymentMethod.value);
  } catch (error) {
    console.log(error);
    handleError(error);
  }
}

//adresss
const addresses = ref([]);
const selectedIndex = ref(null);
const selectedAddress = ref(null);
const showForm = ref(false);
const newAddress = ref({});
const openModalAddress = ref(false);
const showModalAddress = () => {
  openModalAddress.value = true;
};
const handleOkAddress = () => {
  if (selectedIndex.value !== null) {
    selectedAddress.value = addresses.value[selectedIndex.value];
    openModalAddress.value = false;
  }
};
const handleCancelAddress = () => {
  openModalAddress.value = false;
};

const fetchAddressByUser = async () => {
  try {
    const res = await AddressService.fetchAddressByUser(userStore.userInfo.id);
    addresses.value = res.result;

    const index = addresses.value.findIndex((addr) => addr.isDefault);
    selectedIndex.value = index >= 0 ? index : 0;
    selectedAddress.value = addresses.value[selectedIndex.value];
  } catch (error) {
    handleError(error);
  }
};
function validateFormAdress() {
  const regexPhone = /^0\d{9}$/;
  const phone = newAddress.value.phone;
  const addressLine = newAddress.value.addressLine;
  const fullName = newAddress.value.fullName;
  if (!fullName || !phone || !addressLine) {
    toast.error("Vui lòng điền đầy đủ thông tin");
    return false;
  }

  if (!regexPhone.test(phone)) {
    toast.error("Số điện thoại phải bắt đầu bằng 0 và có 10 chữ số");
    return false;
  }

  return true;
}
const addNewAddress = async () => {
  try {
    if (!validateFormAdress()) return;
    const newDataAdress = { ...newAddress.value };
    const res = await AddressService.createAddressByUser(
      userStore.userInfo.id,
      newDataAdress
    );
    Object.keys(newAddress.value).forEach((k) => (newAddress.value[k] = ""));
    showForm.value = false;
    toast.success("Thêm địa chỉ mới thành công");
    await fetchAddressByUser();
  } catch (error) {
    console.log(error);
    handleError(error);
  }
};

//đặt hàng
const noteForOrder = ref("");
const paymentUrl = ref("");

const checkout = async () => {
  try {
    showLoading();
    console.log("addressid", selectedAddress.value.id);
    console.log("selectedPaymentMethod", selectedPaymentMethod.value);
    const orderData = {
      addressId: selectedAddress.value.id,
      paymentMethodId: selectedPaymentMethod.value,
      orderStatus: "PENDING",
      voucherId: selectedVouchers.value?.id ?? null,
      note: noteForOrder.value,
    };
    const res = await OrderService.createOrderFromUser(orderData);
    console.log('result',res.result);
    paymentUrl.value = res.result.paymentUrl;
    console.log("payemnt url", paymentUrl.value);
    localStorage.setItem("orderId", JSON.stringify(res.result.id));
    if (paymentUrl.value) {
      toast.success(
        "Đơn hàng đã tạo thành công và đang chuyển hướng đến trang thanh toán"
      );
      setTimeout(() => {
        window.location.href = paymentUrl.value;
      }, 1000);
    }
  } catch (error) {
    handleError(error);
  } finally {
    hideLoading();
  }
};

const paginationVoucher = ref({
  current: 1,
  pageSize: 10,
  total: 0,
});
const listVoucherCanUse = ref([]);
//voucher
const getVoucherUserCanUse = async () => {
  try {
    const res = await VoucherService.getVoucherCanUse({
      size: paginationVoucher.value.pageSize,
      page: paginationVoucher.value.current,
    });
    listVoucherCanUse.value = res.result.data.filter(item => subtotal.value > item.minOrderValue).map((item) => ({
      ...item,
      type: item.discountValue <= 100 ? "percent" : "fixed",
    }));
    console.log("res", res.result);
    paginationVoucher.value.total = res.result.totalElements;
  } catch (error) {
    handleError(error);
  }
};

//update order status
const updateOrderStatus = async (orderId) => {
  try {
    await OrderService.updateStatus(orderId, {
      orderStatus: "PENDING",
      paymentStatus: "PAID",
    });
  } catch (error) {
    handleError(error);
  }
};

const orderId = ref(null);
onMounted(() => {
  const responseCode = route.query.vnp_ResponseCode;

  if (responseCode === "00") {
    orderId.value = JSON.parse(localStorage.getItem("orderId"));
    paymentSuccessful.value?.open();
    updateOrderStatus(orderId.value);
    localStorage.removeItem("orderId");
    toast.success("Thanh toán thành công!");
  } else if (responseCode) {
    toast.error("Thanh toán thất bại hoặc bị hủy");
  }
});
onBeforeMount(async () => {
  await getCart();
  if (userStore.token) {
    await fetchAddressByUser();
    await fetchPaymentMethods();
    await getVoucherUserCanUse();
  }
});
</script>
