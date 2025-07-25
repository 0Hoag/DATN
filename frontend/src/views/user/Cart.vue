<template>
  <div v-if="cartStore.cartItem.length > 0">
    <!-- Địa điểm giao hàng -->
    <!-- <div class="bg-white p-3 rounded shadow-sm mb-3">
      <h5 class="mb-3">Thông tin giao hàng</h5>
      <div class="d-flex align-items-center mb-2">
        <input type="radio" name="deliveryOption" checked class="form-check-input me-2" />
        <label class="form-check-label me-4">Giao tận nơi</label>
        <input type="radio" name="deliveryOption" class="form-check-input me-2" />
        <label class="form-check-label">Nhận tại siêu thị</label>
      </div>
    </div> -->

    <!-- Danh sách sản phẩm -->
    <div class="bg-white p-3 rounded shadow-sm mb-3" >
      <div class="d-flex my-3" v-for="item in cartStore.cartItem">
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
          <div class="text-danger fw-bold mb-2" >{{item.productVariant.salePrice ? formatPrice(item.productVariant.salePrice ): formatPrice(item.productVariant.price ) }}</div>
          <del class="text-danger fw-bold mb-2 text-muted" v-if="item.productVariant.salePrice">{{formatPrice(item.productVariant.price ) }}</del>
          <div class="d-flex align-items-center justify-content-end">
            <button class="btn btn-outline-secondary btn-sm me-1" @click="decreaseQuantity(item)">−</button>
            <input
              type="text"
              class="form-control form-control-sm text-center"
              value="1"
              style="width: 50px"
              readonly
            />
            <button class="btn btn-outline-secondary btn-sm ms-1" @click="increaseQuantity(item)">+</button>
          </div>
          <div class="mt-2 text-end">
            <button class="btn btn-link text-danger btn-sm" @click="deleteItem(item.productVariant.id)">Xóa</button>
          </div>
        </div>
      </div>
    </div>

 

    <!-- Tóm tắt đơn hàng -->
    <div class="bg-white p-3 rounded shadow-sm mt-3">
      <h6 class="mb-3">Tóm tắt đơn hàng</h6>

      <ul class="list-group list-group-flush mb-3">
        <li class="list-group-item d-flex justify-content-between">
          Tạm tính: <strong>6.470.000đ</strong>
        </li>
        <li class="list-group-item d-flex justify-content-between">
          Giảm giá: <span class="text-success">0đ</span>
        </li>
        <li class="list-group-item d-flex justify-content-between">
          Phí giao hàng: <span class="text-muted">Miễn phí</span>
        </li>
        <li class="list-group-item d-flex justify-content-between fw-bold fs-6">
          Tổng cộng: <span class="text-danger">6.470.000đ</span>
        </li>
      </ul>

      <div class="mb-3">
        <div class="input-group input-group-sm">
          <input type="text" class="form-control" placeholder="Mã giảm giá" />
          <button class="btn btn-outline-secondary">Áp dụng</button>
        </div>
      </div>

      <div class="d-grid">
        <button class="btn btn-primary btn-lg">
          <i class="fas fa-credit-card me-2"></i>Tiến hành đặt hàng
        </button>
      </div>
    </div>
  </div>
  <div class="d-flex justify-content-center align-items-center" style="min-height: 500px;"  v-else>
    <div class="text-center py-2 ">
      <font-awesome-icon
        :icon="['fas', 'shopping-bag']"
        size="3x"
        class="text-muted mb-3"
      />
      <h5>Chưa có sản phẩm nào trong giỏ hàng</h5>
      <p class="text-muted">Hãy bắt đầu mua sắm để tạo đơn hàng đầu tiên</p>
    </div>
  </div>
</template>
<script setup>
import { CartService } from "@/api/service/CartService";
import { useCartStore } from "@/store/cartStore";
import { handleError, onBeforeMount, onMounted, ref } from "vue";

const cartStore = useCartStore();
const updateQuantity = ref([]);
const increaseQuantity =async (variant) => {
  const updateQuantity = ++variant.quantity;
  console.log("update quantity", updateQuantity);
  // await cartStore.updateCart(variant.productVariant.id, updateQuantity);
}
const decreaseQuantity =async (variant) => {
  const updateQuantity = --variant.quantity;
  console.log("update quantity", updateQuantity);
  // await cartStore.updateCart(variant.productVariant.id, updateQuantity);
}


const getCart =  () => {
  cartStore.getCart();
};
const deleteItem = async (id) => {
  await cartStore.deleteItem(id); // đợi xóa xong
  await cartStore.getCart();      // rồi mới gọi lại giỏ hàng
};
const getThumnailForVariant = (variant) => {
  return variant.productVariant.images.find((v) => v.isThumbnail);
};
function formatPrice(price) {
  return new Intl.NumberFormat("vi-VN", {
    style: "currency",
    currency: "VND",
  }).format(price);
}
onBeforeMount(() => {
 getCart();
});
</script>
