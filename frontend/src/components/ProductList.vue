<script setup>
const props = defineProps({
  products: {
    type: Array,
    required: true,
  },
  title: {
    type: String,
    default: "Sản phẩm nổi bật",
  },

});

function formatPrice(price) {
  if (typeof price !== "number") return "";
  return new Intl.NumberFormat("vi-VN", {
    style: "currency",
    currency: "VND",
  }).format(price);
}

function getDisplayPrice(variants) {
  if (!variants || variants.length === 0) return "";

  const prices = variants.map((v) => (v.salePrice != null ? v.salePrice : v.price));
  const min = Math.min(...prices);
  const max = Math.max(...prices);

  if (min === max) {
    return formatPrice(min);
  } else {
    return `${formatPrice(min)} - ${formatPrice(max)}`;
  }
}
</script>

<template>
  <div class="container">
    <!-- Sort Options -->
    <div class="d-flex justify-content-between align-items-center mb-4">
      <div>
        <h4 class="mb-1">{{ title }}</h4>
        <small class="text-muted">Hiển thị 1-12 trong 156 sản phẩm</small>
      </div>
      <div class="d-flex align-items-center gap-3">
        <div class="d-flex align-items-center">
          <label class="form-label me-2 mb-0">Sắp xếp:</label>
          <select class="form-select form-select-sm" style="width: auto">
            <option>Nổi bật</option>
            <option>Giá thấp đến cao</option>
            <option>Giá cao đến thấp</option>
            <option>Mới nhất</option>
            <option>Bán chạy nhất</option>
            <option>Đánh giá cao nhất</option>
          </select>
        </div>
      </div>
    </div>
    <div class="row g-3 mt-1">
      <div class="col-md-3 d-flex mb-3" v-for="product in products" :key="product.id">
        {{ console.log(product) }}
        <router-link :to="{name: 'product', params: {slug: product.slug}}">
          <div class="card product-card shadow-sm d-flex flex-column h-100 w-100">
            <div class="product-img-wrapper">
              <img :src="product.imageUrl" :alt="product.name" class="img-fluid" style=" height: 200px; object-fit: cover"/>
            </div>
            <div
              class="card-body flex-grow-1 d-flex flex-column justify-content-between m-auto w-100"
            >
              <h6 class="card-title text-left" :title="product.name">
                {{ product.name }}
              </h6>
              <div
                class="mb-1 text-warning d-flex justify-content-start align-items-center mt-auto"
                style="min-height: 28px"
              >
                <font-awesome-icon icon="star" class="text-warning" />
                <span class="text-muted small ms-1">({{ product.averageRating }})</span>
              </div>
              <p class="text-danger fw-bold mb-1 text-start">
                {{
                  product.minSalePrice
                    ? formatPrice(product.minSalePrice)
                    : formatPrice(product.minOriginPrice)
                }}<br />
                <del class="text-muted" v-if="product.minSalePrice">{{
                  formatPrice(product.minOriginPrice)
                }}</del>
              </p>
            </div>
          </div>
        </router-link>
      </div>
    </div>
    <div class="text-center py-2" v-if="products.length == 0">
      <font-awesome-icon :icon="['fas', 'box']" size="3x" class="text-muted mb-3" />
      <h5>Không có dữ liệu</h5>
    </div>
  </div>
</template>

<style scoped>
.product-card {
  transition: transform 0.2s ease;
}
.product-card:hover {
  transform: translateY(-4px);
}
</style>
