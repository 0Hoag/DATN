<template>
  <div class="container ">
    <!-- Product Info Section -->
    <div class="row">
      <!-- Image Gallery -->
      <div class="col-lg-5">
        <div class="border rounded mb-3 bg-white p-3 text-center">
          <img :src="selectedImage" class="img-fluid" height="50px" width="350px"/>
        </div>
        <div class="d-flex gap-2 justify-content-center">
          <img
            v-for="(img, index) in selectedVariantDetail?.images"
            :key="index"
            :src="img.imageUrl"
            class="thumb-img"
            @click="selectedImage = img"
          />
        </div>
      </div>

      <!-- Product Details -->
      <div class="col-lg-7">
        <h1 class="product-title">{{ selectedVariantDetail?.variantName }}</h1>
        <div class="text-muted mb-2 fs-6">
          Thương hiệu: <strong>{{ productDetail?.brand }}</strong>
        </div>

        <!-- Price -->
        <div class="price-box">
          <span class="price">{{
            formatPrice(selectedVariantDetail?.salePrice || selectedVariantDetail?.price)
          }}</span>
          <span v-if="selectedVariantDetail?.salePrice" class="price-old">
            {{ formatPrice(selectedVariantDetail?.price) }}
          </span>
        </div>

        <!-- Color -->
        <!-- <div class="mb-3">
          <label class="option-label">Chọn màu:</label>
          <div class="d-flex flex-wrap gap-2 mt-1">
            <button
              v-for="color in availableColors"
              :key="color"
              @click="selectColor(color)"
              :class="['color-btn', selectedColor === color ? 'active' : '']"
            >
              {{ color }}
            </button>
          </div>
        </div> -->

        <!-- Storage -->
        <!-- <div class="mb-3">
          <label class="option-label">Dung lượng:</label>
          <div class="d-flex flex-wrap gap-2 mt-1">
            <button
              v-for="ram in availableRams"
              :key="ram"
              @click="selectRam(ram)"
              :class="['color-btn', selectedRam === ram ? 'active' : '']"
            >
              {{ ram }}
            </button>
          </div>
        </div> -->

        <!-- Variant list -->
        <div class="d-flex flex-wrap gap-2 my-3">
          <div
            class="card border-2"
            style="cursor: pointer"
            :class="{ 'border-danger': selectedVariant2 === variant.id }"
            v-for="variant in listVariant"
            @click="selectVariant(variant.id)"
          >
            <div class="d-flex align-items-center p-2">
              <img
                :src="getThumnailForVariant(variant).imageUrl"
                class="me-2"
                style="height: 30px"
                :alt="getThumnailForVariant(variant).altText"
              />
              <span class="text-danger fw-bold">{{ variant.variantName }}</span>
            </div>
          </div>
        </div>

        <!-- Promotions -->
        <div class="promo-box">
          <h6 class="text-danger fw-bold mb-2">Ưu đãi</h6>
          <ul class="mb-0 ps-3">
            <li>Giảm thêm 1 triệu khi thanh toán qua VNPAY</li>
            <li>Trả góp 0% qua thẻ tín dụng</li>
            <li>1 đổi 1 trong 30 ngày nếu lỗi</li>
          </ul>
        </div>

        <!-- Buttons -->
        <div class="d-flex gap-2 mt-4">
          <button class="btn-buy" >Mua ngay</button>
          <button class="btn-cart" @click="addToCart">Thêm vào giỏ</button>
        </div>
      </div>
    </div>

    <!-- Specs -->
    <div class="spec-text mt-5">
      <h5 class="fw-bold mb-3">Thông số kỹ thuật</h5>
      <div class="spec-box" v-html="productDetail.content"></div>
    </div>

    <!-- Reviews -->
    <div class="mt-5">
      <h5 class="fw-bold mb-3">Đánh giá & Nhận xét</h5>
      <div
        v-if="productDetail.productReviews && productDetail.productReviews.length === 0"
        class="text-muted"
      >
        Chưa có đánh giá nào cho sản phẩm này.
      </div>
      <div v-else class="review-list">
        <div
          v-for="(review, index) in productDetail.productReviews"
          :key="index"
          class="border-bottom py-3"
        >
          <div class="fw-bold">{{ review.name }}</div>
          <div class="text-warning mb-1">
            <i class="fa fa-star" v-for="n in review.rating" :key="n"></i>
          </div>
          <div>{{ review.comment }}</div>
        </div>
      </div>
      <div class="mt-4">
        <h6 class="fw-bold">Viết đánh giá của bạn</h6>
        <div class="mb-2">
          <select v-model="newReview.rating" class="form-select">
            <option disabled value="">Chọn số sao</option>
            <option v-for="n in 5" :key="n" :value="n">{{ n }} sao</option>
          </select>
        </div>
        <div class="mb-2">
          <textarea
            v-model="newReview.comment"
            class="form-control"
            rows="3"
            placeholder="Nội dung đánh giá..."
          ></textarea>
        </div>
        <button @click="submitReview" class="btn btn-outline-primary">
          Gửi đánh giá
        </button>
      </div>
    </div>

    <!-- Related Products -->
    <div class="mt-5">
      <h5 class="fw-bold mb-3">Sản phẩm liên quan</h5>
      <div class="row g-3">
        <div v-for="p in relatedProducts" :key="p.id" class="col-6 col-md-3">
          <div class="card h-100">
            <img :src="p.thumbnail" class="card-img-top related-thumb" />
            <div class="card-body p-2">
              <div class="fw-semibold small text-dark">{{ p.name }}</div>
              <div class="text-danger fw-bold">
                {{ formatPrice(p.salePrice || p.price) }}
              </div>
              <div
                v-if="p.salePrice"
                class="text-muted text-decoration-line-through small"
              >
                {{ formatPrice(p.price) }}
              </div>
            </div>
          </div>
        </div>
      </div>
    </div>
  </div>
</template>

<script setup>
import { handleError } from "@/api/functions/common";
import { CartService } from "@/api/service/CartService";
import { ProductService } from "@/api/service/ProductService";
import { useCartStore } from "@/store/cartStore";
import { ref, computed, onMounted } from "vue";
import { useRoute } from "vue-router";

const relatedProducts = [
  {
    id: 1,
    name: "iPhone 15 Pro Max",
    price: 34990000,
    salePrice: 31990000,
    thumbnail: "https://cdn.example.com/iphone15.png",
  },
  {
    id: 2,
    name: "Xiaomi 14 Ultra",
    price: 25990000,
    thumbnail: "https://cdn.example.com/xiaomi14.png",
  },
  // ...
];
const product = {
  name: "Samsung Galaxy S25 Ultra 5G",
  brand: "Samsung",
  images: [
    "https://cdn.example.com/s25-front.png",
    "https://cdn.example.com/s25-back.png",
  ],
  specText:
    "Màn hình: 6,9 inch QHD+ AMOLED 120Hz. Camera: 108MP + 12MP + 10MP + 10MP. Pin: 5000mAh, sạc nhanh 45W. Chipset: Snapdragon 8 Gen 3. RAM: 12GB. ROM: 256GB.",
};

// const splitSpecs = (text) => {
//   return text
//     .split(".")
//     .map((s) => s.trim())
//     .filter((s) => s.length > 0);
// };

const selectedImage = ref('');


const newReview = ref({ name: "", rating: "", comment: "" });
const reviews = ref([
  {
    name: "Nguyễn Văn A",
    rating: 5,
    comment: "Sản phẩm rất tốt, giao hàng nhanh chóng.",
  },
  { name: "Trần Thị B", rating: 4, comment: "Thiết kế đẹp, hiệu năng ổn." },
]);

function submitReview() {
  if (!newReview.value.name || !newReview.value.rating || !newReview.value.comment)
    return;
  reviews.value.push({ ...newReview.value });
  newReview.value = { name: "", rating: "", comment: "" };
}

const formatPrice = (val) => {
  return new Intl.NumberFormat("vi-VN", { style: "currency", currency: "VND" }).format(
    val
  );
};

const route = useRoute();
const productDetail = ref([]);
const listVariant = ref([]);
const selectedVariant2 = ref(null);
const getDetailProduct = async () => {
  try {
    const res = await ProductService.detailProductBySlug(route.params.slug);
    if (res.result.id) {
      productDetail.value = res.result;
      listVariant.value = productDetail.value.productVariants;
      if (listVariant.value.length > 0) {
        selectedVariant2.value = listVariant.value[0].id;
        selectedImage.value = selectedVariantDetail.value.images[0].imageUrl
      }
    }
    console.log(productDetail.value);
    console.log(listVariant.value);
  } catch (error) {
    handleError(error);
  }
};
const getThumnailForVariant = (variant) => {
  return variant.images.find((v) => v.isThumbnail);
};
const selectVariant = (id) => {
  selectedVariant2.value = id;
  console.log(selectedVariantDetail.value);
};
const selectedVariantDetail = computed(() => {
  return listVariant.value.find((v) => v.id === selectedVariant2.value);
});
const cartStore = useCartStore();
const addToCart = () => {
    cartStore.addToCart(selectedVariantDetail.value.id, 1);
}
onMounted(() => {
  getDetailProduct();
});
</script>

<style scoped>
.container {
  background-color: #f8f9fa;
}

.product-title {
  font-size: 1.8rem;
  font-weight: bold;
}

.price-box {
  margin-bottom: 1rem;
}

.price {
  color: #d70018;
  font-size: 1.8rem;
  font-weight: 700;
}

.price-old {
  text-decoration: line-through;
  margin-left: 10px;
  color: #777;
  font-size: 1rem;
}

.option-label {
  font-weight: 500;
}

.color-btn {
  border: 1px solid #ccc;
  background-color: white;
  padding: 6px 14px;
  border-radius: 6px;
  cursor: pointer;
  font-weight: 500;
}

.color-btn.active {
  background-color: #d70018;
  color: white;
  border-color: #d70018;
}

.thumb-img {
  width: 50px;
  height: 50px;
  object-fit: cover;
  border: 1px solid #ddd;
  border-radius: 6px;
  cursor: pointer;
}

.promo-box {
  background-color: #fff3f3;
  padding: 15px;
  border-radius: 8px;
  border: 1px solid #ffd1d1;
}

.btn-buy {
  background-color: #d70018;
  color: white;
  padding: 10px 24px;
  border: none;
  border-radius: 6px;
  font-weight: 600;
  font-size: 1rem;
}

.btn-cart {
  background-color: white;
  border: 1px solid #ccc;
  padding: 10px 24px;
  border-radius: 6px;
  font-weight: 600;
  font-size: 1rem;
}

.spec-box {
  background-color: #fff;
  padding: 16px;
  border-radius: 8px;
  border: 1px solid #eee;
}

.spec-line {
  padding: 8px 0;
  border-bottom: 1px solid #f0f0f0;
  font-size: 1rem;
  color: #333;
}

.spec-line:last-child {
  border-bottom: none;
}
</style>
