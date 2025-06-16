<template>
  <h3 class="mb-4">Thêm đơn hàng</h3>

  <!-- Thông tin cơ bản của đơn hàng -->
  <div class="card shadow-sm mb-5">
    <div class="card-body">
      <h4 class="card-title mb-3">Thông tin cơ bản</h4>
      <div class="row">
        <!-- Thêm khách hàng -->
        <div class="col-6 mb-3">
          <label for="customer" class="form-label fw-bold">Khách hàng</label>
          <a-select
            id="customer"
            v-model:value="selectedCustomer"
            show-search
            placeholder="Chọn khách hàng"
            :options="customer"
            :filter-option="filterCustomer"
            @change="onChangeCustomer"
            style="width: 100%"
          />
        </div>
        <!-- Chọn phương thức thanh toán -->
        <div class="col-6 mb-3">
          <label for="customer" class="form-label fw-bold">Phương thức thanh toán</label>
          <a-select
            id="customer"
            v-model:value="selectedPaymentMethod"
            show-search
            placeholder="Phương thức thanh toán"
            :options="paymentMethod"
            :filter-option="filterAPaymentMethod"
            @change="onChangePaymentMethod"
            style="width: 100%"
          />
        </div>
        <!-- Chọn địa chỉ -->
        <div class="col-6 mb-3">
          <label for="address" class="form-label fw-bold">Địa chỉ</label>
          <a-select
            id="address"
            v-model:value="selectedAddress"
            show-search
            placeholder="Chọn địa chỉ"
            :options="address"
            :filter-option="filterAddress"
            @change="onChangeAddress"
            style="width: 100%"
          />
        </div>
        <!-- Hiển thị trạng thái đơn hàng -->
        <div class="col-6 mb-3">
          <label for="customer" class="form-label fw-bold">Trạng thái đơn hàng</label>
          <a-select
            id="customer"
            v-model:value="selectedOrderStatus"
            show-search
            placeholder="Trạng thái đơn hàng"
            :options="orderStatus"
            :filter-option="filterOrderStatus"
            @change="onChangeOrderStatus"
            style="width: 100%"
          />
        </div>
        <!-- Địa chỉ tự nhập -->
        <div class="col-sm-6 mb-3" v-if="selectedAddress === 'custom'">
          <label for="customAddress" class="form-label fw-bold">Nhập địa chỉ</label>
          <a-input id="customAddress" v-model:value="customAddress" placeholder="Nhập địa chỉ cụ thể..." style="width: 100%" />
        </div>
        <!-- Ghi chú -->
        <div class="col-sm-12 mb-3">
          <label for="note" class="form-label fw-bold">Ghi chú</label>
          <a-textarea id="note" v-model:value="note" placeholder="Ghi chú..." style="width: 100%" allow-clear />
        </div>
      </div>
    </div>
  </div>

  <!-- Danh sách sản phẩm được chọn -->
  <div class="card shadow-sm mb-3">
    <div class="card-body">
      <h4 class="card-title mb-3">Danh sách sản phẩm đã chọn</h4>
      <table class="table table-hover text-center align-middle my-3">
        <thead>
          <tr>
            <th>STT</th>
            <th>Tên sản phẩm</th>
            <th>Giá</th>
            <th>Số lượng</th>
            <th>Thành tiền</th>
            <th>Thao tác</th>
          </tr>
        </thead>
        <tbody>
          <tr v-for="(item, index) in cart" :key="index">
            <td>{{ index + 1 }}</td>
            <td>{{ item.name }}</td>
            <td>{{ item.price }}</td>
            <td>
              <input type="number" min="1" @input="handleQuantityChange(item)" v-model="item.quantity" class="form-control d-inline-block" style="width: 200px" />
            </td>
            <td>{{ formatCurrency(item.total) }}</td>
            <td>
              <button class="btn btn-danger">
                <font-awesome-icon icon="trash" />
              </button>
            </td>
          </tr>
        </tbody>
        <tfoot>
          <tr>
            <td colspan="4" class="text-end fw-bold">Tổng tiền</td>
            <td class="text-success fw-bold">{{ formatCurrency(totalCart) }}</td>
            <td></td>
          </tr>
        </tfoot>
      </table>
    </div>
  </div>

  <!-- Danh sách các sản phẩm -->
  <div class="card shadow-sm">
    <div class="card-body">
      <h4 class="card-title mb-3">Danh sách sản phẩm</h4>

      <!-- Thanh tìm kiếm -->
      <div class="d-flex justify-content-end my-3">
        <input type="text" class="form-control w-25" placeholder="Tìm kiếm sản phẩm..." />
      </div>

      <!-- Danh sách sản phẩm -->
      <div class="row row-cols-1 row-cols-sm-2 row-cols-md-3 row-cols-lg-5 g-4">
        <!-- Một sản phẩm -->
        <div class="col" v-for="(product, index) in listProduct" :key="index">
          <div class="card h-100">
            <img :src="product.productImages[0]" class="card-img-top" alt="..." />
            <div class="card-body text-center">
              <p class="card-text">{{ product.variantName }}</p>
              <p class="card-text text-danger">{{ product.price }}</p>
              <p class="card-text">
                <strong>Còn lại:</strong> <span>{{ product.quantity }}</span>
              </p>
              <button class="btn btn-primary" @click="addProductToCart(product)">Thêm</button>
            </div>
          </div>
        </div>

        <!-- Thêm nhiều col tương tự... -->
      </div>
      <!-- Phân trang -->
      <div class="d-flex justify-content-center mt-3">
        <a-pagination v-model:current="pagination.current" :total="pagination.total" simple :page-size="pagination.pageSize" />
      </div>
    </div>
  </div>

  <!-- Nút lưu -->
  <div class="d-flex justify-content-start mt-3">
    <button class="btn btn-primary" type="button" @click="submitFormAdd">Lưu</button>
  </div>
</template>

<script setup>
import { hideLoading, showLoading } from "@/api/functions/common";
import { AccountService } from "@/api/service/AccountService";
import { OrderService } from "@/api/service/OrderService";
import { ProductService } from "@/api/service/ProductService";
import { computed, onMounted, ref, watch } from "vue";
import { toast } from "vue3-toastify";
// Dữ liệu khách hàng
const customer = ref([
  { value: "jack", label: "Jack" },
  { value: "lucy", label: "Lucy" },
  { value: "tom", label: "Tom" },
]);

// Dữ liệu địa chỉ (bao gồm option tự nhập)
const address = ref([
  { value: "custom", label: "Tự nhập địa chỉ" },
  { value: "dia chi 1", label: "Địa chỉ 1" },
  { value: "dia chi 2", label: "Địa chỉ 2" },
  { value: "dia chi 3", label: "Địa chỉ 3" },
]);

// Dữ liệu phương thức thanh toán
const paymentMethod = ref([
  { value: "1", label: "Thanh toán khi nhận hàng" },
  { value: "2", label: "Thanh toán qua VNPay" },
]);
// Dữ liệu trạng thái đơn hàng
const orderStatus = ref([
  { value: "PENDING", label: "Chờ xác nhận" },
  { value: "CONFIRMED", label: "Đã xác nhận" },
  { value: "SHIPPED", label: "Đã giao" },
  { value: "DELIVERED", label: "Đã nhận" },
  { value: "CANCELLED", label: "Đã hủy" },
]);

const listUser = ref([]);
const listProduct = ref([]);

const cart = ref([]);

// Biến riêng cho mỗi select
const selectedCustomer = ref(undefined);
const selectedAddress = ref(undefined);
const selectedPaymentMethod = ref("1");
const selectedOrderStatus = ref("PENDING");
const customAddress = ref(""); // input khi người dùng tự nhập
const note = ref(""); // input khi người dùng tự nhập
const pagination = ref({
  current: 1,
  pageSize: 5,
  total: 0,
});
// Filter và handler
const onChangeCustomer = (value) => {
  console.log("Khách hàng:", value);
};

const onChangeAddress = (value) => {
  console.log("Địa chỉ:", value);
};
const onChangePaymentMethod = (value) => {
  console.log("Địa chỉ:", value);
};
const onChangeOrderStatus = (value) => {
  console.log("Địa chỉ:", value);
};

const filterCustomer = (input, option) => option.value.toLowerCase().includes(input.toLowerCase());

const filterAddress = (input, option) => option.value.toLowerCase().includes(input.toLowerCase());

const filterAPaymentMethod = (input, option) => option.value.toLowerCase().includes(input.toLowerCase());

const filterOrderStatus = (input, option) => option.value.toLowerCase().includes(input.toLowerCase());

async function fecthListUser() {
  try {
    showLoading();
    const response = await AccountService.fetchListAccount();

    listUser.value = response.result.filter((user) => user.roles.some((role) => role.name === "CUSTOMER"));
    customer.value = listUser.value.map((item) => ({ value: item.id, label: item.fullName, address: item.addresses }));
    // address.value = listUser.value.map((item) => ({ value: item.addresses.id, label: item.addresses.fullName }));
    pagination.value.total = listUser.value.length;
    console.log(listUser.value);
    console.log(customer.value);
  } catch (error) {
    toast.error("Lỗi khi tải dữ liệu");
    console.log(error);
  } finally {
    hideLoading();
  }
}
async function fetchList() {
  try {
    const response = await ProductService.fetchListProductVariant({ page: pagination.value.current, size: pagination.value.pageSize });
    listProduct.value = response.result.data;
    pagination.value.total = response.result.totalElements;
    console.log(listProduct.value);
  } catch (error) {
    console.log(error);
  }
}

function addProductToCart(product) {
  const existingProduct = cart.value.find((item) => item.id === product.id);

  if (product.quantity === 0) {
    toast.info("Sản phẩm đã hết hàng");
    return;
  }

  if (existingProduct && existingProduct.quantity >= product.quantity) {
    toast.info("Số lượng sản phẩm không đủ");
    return;
  }
  if (existingProduct) {
    existingProduct.quantity++;
  } else {
    const total = product.price * product.quantity;
    cart.value.push({
      id: product.id,
      name: product.variantName,
      price: product.price,
      quantity: 1,
      total,
      maxQuantity: product.quantity,
    });
  }
  console.log("cart", cart.value);
}
function formatCurrency(value) {
  return value?.toLocaleString("vi-VN") + " ₫";
}

function handleQuantityChange(item) {
  // Nếu người dùng nhập nhỏ hơn 1 thì gán về 1
  if (item.quantity < 1) {
    item.quantity = 1;
  }

  // Nếu người dùng nhập lớn hơn tồn kho thì gán về tồn kho
  if (item.quantity > item.maxQuantity) {
    item.quantity = item.maxQuantity;
    toast.info("Số lượng vượt quá tồn kho");
  }

  // Cập nhật lại thành tiền
  item.total = item.quantity * item.price;
}
function getFormAdd() {
  return {
    userId: selectedCustomer.value,
    addressId: selectedAddress.value,
    paymentMethodId: selectedPaymentMethod.value,
    orderStatus: selectedOrderStatus.value,
    voucherId: null,
    note: note.value,
    customAddress: customAddress.value,
    items: cart.value.map((item) => ({
      productVariantId: item.id,
      quantity: item.quantity,
    })),
  };
}
function validateForm() {
  let errorMessage = "";
  if (!selectedCustomer.value) {
    errorMessage = "Vui lòng chọn khách hàng";
  } else if (!selectedAddress.value) {
    errorMessage = "Vui lòng chọn địa chỉ";
  } else if (!selectedPaymentMethod.value) {
    errorMessage = "Vui lòng chọn phương thức thanh toán";
  } else if (!selectedOrderStatus.value) {
    errorMessage = "Vui lòng chọn trạng thái đơn hàng";
  } else if (!cart.value.length) {
    errorMessage = "Vui lòng chọn sản phẩm";
  }
  if (errorMessage) {
    toast.error(errorMessage);
    return false;
  }
  return true;
}
async function submitFormAdd() {
  try {
    showLoading();
    if (!validateForm()) return;

    console.log(getFormAdd());
    await OrderService.createOrder(getFormAdd());
    toast.success("Thêm đơn hàng thành công!");
    resetForm();
  } catch (error) {
    handleError(error);
  } finally {
    hideLoading();
  }
}
function resetForm() {
  selectedCustomer.value = undefined;
  selectedAddress.value = undefined;
  selectedPaymentMethod.value = "1";
  selectedOrderStatus.value = "PENDING";
  customAddress.value = "";
  note.value = "";
  cart.value = [];
}
watch(
  () => selectedCustomer.value,
  (newVal) => {
    const selected = customer.value.find((cus) => cus.value === newVal);
    if (selected && Array.isArray(selected.address)) {
      address.value = selected.address.map((addr) => ({
        value: addr.id,
        label: addr.addressLine,
      }));
    } else {
      address.value = []; // fallback nếu không tìm thấy hoặc không có địa chỉ
    }
  }
);

watch(
  cart,
  (newCart) => {
    newCart.forEach((item) => {
      item.total = item.quantity * item.price;
    });
  },
  { deep: true }
);

const totalCart = computed(() => cart.value.reduce((sum, item) => sum + item.price * item.quantity, 0));
onMounted(() => {
  fecthListUser();
  fetchList();
});
</script>
