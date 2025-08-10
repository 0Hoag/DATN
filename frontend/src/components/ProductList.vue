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
      <div class="pt-3">
        <h4 class="">{{ title }}</h4>
      </div>
    </div>
    <div class="row row-cols-5 g-1  mt-1">
      <div class="col d-flex mb-3" v-for="product in products" :key="product.id">
        <router-link :to="{name: 'product', params: {slug: product.slug}}">
          <div class="card product-card shadow-sm d-flex flex-column h-100 w-100">
            <div class="product-img-wrapper" >
              <img :src="product.imageUrl" :alt="product.name" class="img-fluid"  />
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
      <h5>Không có sản phẩm</h5>
    </div>
  </div>
</template>

<style scoped>

.card {
  transition: all 0.3s ease-in-out;
}

.card:hover {
  transform: translateY(-5px);
  box-shadow: 0 10px 20px rgba(0, 0, 0, 0.15);
  cursor: pointer;
}

.card img {
  transition: transform 0.3s ease;
}

.card:hover img {
  transform: scale(1.05);
}

.product-card {
  height: 100%;
  display: flex;
  flex-direction: column;
  justify-content: space-between;
}

.product-img-wrapper {
  width: 100%;
  aspect-ratio: 4 / 3;
  display: flex;
  align-items: center;
  justify-content: center;
  overflow: hidden;
  padding: 5px;
}

.product-img-wrapper img {
  max-width: 100%;
  max-height: 100%;
  object-fit: cover;
}

.card-title {
  font-size: 14px;
  line-height: 1.2rem;
  font-weight: 600;
  margin-bottom: 0.5rem;
  min-height: 40px;
  white-space: normal;
}

</style>
