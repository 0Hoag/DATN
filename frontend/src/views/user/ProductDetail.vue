<template>
  <div class="container">
    <!-- Product Info Section -->
    <div class="row">
      <!-- Image Gallery -->
      <div class="col-lg-5">
        <div class="border rounded mb-3 bg-white p-3 text-center">
          <img :src="selectedImage" class="img-fluid" height="50px" width="350px" />
        </div>
        <div class="d-flex gap-2 justify-content-center">
          <img
            v-for="(img, index) in selectedVariantDetail?.images"
            :key="index"
            :src="img.imageUrl"
            class="thumb-img"
            style="height: 50px; object-fit: cover"
            @click="selectedImage = img.imageUrl"
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
        <p>Mô tả ngắn: {{ productDetail?.description }}</p>
        <div v-if="selectedVariantDetail?.quantity > 0">
          Tồn kho: {{ selectedVariantDetail?.quantity }}
        </div>
        <div v-else><strong class="text-danger fs-4">Hết hàng</strong></div>
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
        <div class="d-flex gap-2 mt-4" v-if="selectedVariantDetail?.quantity > 0">
          <button class="btn-buy" @click="buyNow">Mua ngay</button>
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
      <div v-if="listReview && listReview.length === 0" class="text-muted">
        Chưa có đánh giá nào cho sản phẩm này.
      </div>
      <div v-else class="spec-box overflow-auto" style="max-height:500px ;" >
        <div
          v-for="(review, index) in listReview"
          :key="index"
          class="border-bottom py-3"
        >
          <div class="d-flex justify-content-start align-items-center mb-2">
            <img
              :src="avatarUrl(review.userFullName)"
              alt="avatar"
              class="rounded-circle me-2"
              width="40"
              height="40"
            />
            <div class="fw-bold">{{ review.userFullName }} - {{ review.userEmail }}</div>
          </div>
          <div class="text-warning mb-1">
            <i class="fa fa-star" v-for="n in review.rating" :key="n"></i>
          </div>
          <div>{{ review.content }}</div>
        </div>
        <div class="text-center mt-3" v-if="hasMore()">
          <button class="btn btn-primary" @click="loadMore" :disabled="isLoading">
            <span v-if="!isLoading">Tải thêm</span>
            <span v-else>Đang tải...</span>
          </button>
        </div>
      </div>
    </div>

    <!-- Related Products -->
    <div class="mt-5" v-if="relatedProducts && relatedProducts.length > 0">
      <h5 class="fw-bold mb-3">Sản phẩm liên quan</h5>
      <div class="row g-3 mt-1">
        <div class="col-md-2 d-flex mb-3" v-for="p in relatedProducts">
          <router-link :to="{ name: 'product', params: { slug: p.slug } }">
            <div class="card product-card shadow-sm d-flex flex-column h-100 w-100">
              <div class="product-img-wrapper">
                <img :src="p.imageUrl" :alt="p.name" class="img-fluid" />
              </div>
              <div
                class="card-body flex-grow-1 d-flex flex-column justify-content-between m-auto w-100"
              >
                <h6 class="card-title text-left">
                  {{ p.name }}
                </h6>
                <div
                  class="mb-1 text-warning d-flex justify-content-start align-items-center mt-auto"
                  style="min-height: 28px"
                >
                  <font-awesome-icon icon="fa-solid fa-star" />
                  <span class="text-muted small ms-1">({{ p.averageRating }})</span>
                </div>
                <p class="text-danger fw-bold mb-1 text-start">
                  {{
                    p.minSalePrice
                      ? formatPrice(p.minSalePrice)
                      : formatPrice(p.minOriginPrice)
                  }}<br />
                  <del class="text-muted" v-if="p.minSalePrice">{{
                    formatPrice(p.minOriginPrice)
                  }}</del>
                </p>
              </div>
            </div>
          </router-link>
        </div>
      </div>
    </div>
  </div>
</template>

<script setup>
import { handleError } from "@/api/functions/common";
import { CartService } from "@/api/service/CartService";
import { CategoryService } from "@/api/service/CategoryService";
import { ProductService } from "@/api/service/ProductService";
import { ReviewService } from "@/api/service/ReviewService";
import router from "@/router";
import { useCartStore } from "@/store/cartStore";
import { ref, computed, onMounted } from "vue";
import { useRoute } from "vue-router";
// sản phẩm liên quan
const relatedProducts = ref([]);
const categorySlug = ref(null);
const getRelatedProduct = async () => {
  relatedProducts.value = [];
  try {
    const res = await ProductService.fetchListProductBySlugCategory(categorySlug.value, {
      page: 1,
      size: 12,
    });
    relatedProducts.value = res.result.data.filter(
      (item) => productDetail.value.id != item.productId
    );
    console.log("relatedProducts", relatedProducts.value);
  } catch (error) {
    console.log(error);
    handleError(error);
  }
};

// review
const avatarUrl = (name) => {
  const displayName = name || "Unknown";
  return `https://ui-avatars.com/api/?name=${encodeURIComponent(
    displayName
  )}&background=random`;
};

const currentPage = ref(1);
const pageSize = 10;
const total = ref(0);
const isLoading = ref(false);
const listReview = ref([]);
const getListReviewByProduct = async () => {
  if (isLoading.value) return;
  try {
    const response = await ReviewService.fetchListReviewByProduct(productDetail.value.id, {
      page: currentPage.value,
      size: pageSize,
    });
    listReview.value.push(...response.result.data);
    total.value = response.result.totalElements;
  } catch (error) {
    handleError(error);
  } finally {
    isLoading.value = false;
  }
};
const loadMore = async () => {
  currentPage.value++;
  await getListReviewByProduct();
};

const hasMore = () => {
  return listReview.value.length < total.value;
};

// const splitSpecs = (text) => {
//   return text
//     .split(".")
//     .map((s) => s.trim())
//     .filter((s) => s.length > 0);
// };

//
const selectedImage = ref("");

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
        selectedImage.value = selectedVariantDetail.value.images[0].imageUrl;
        console.log("selected img", selectedImage.value);
        categorySlug.value = productDetail.value.category.slug;
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

const addToCart = async () => {
  await cartStore.addToCart(selectedVariantDetail.value.id, 1);
  await cartStore.getCart();
  console.log("cart item from detail", cartStore.cartItem);
};

const buyNow = async () => {
  await cartStore.addToCart(selectedVariantDetail.value.id, 1);
  router.push({ name: "cart" });
  console.log("cart item from detail", cartStore.cartItem);
};
onMounted(async () => {
  await cartStore.getCart();
  await getDetailProduct();
  await getRelatedProduct();
  await getListReviewByProduct();
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
