<script setup ">
import { handleError } from "@/api/functions/common";
import { ProductService } from "@/api/service/ProductService";
import ProductList from "@/components/ProductList.vue";
import { BRANDS } from "@/constant";
import { onMounted, ref, watch } from "vue";
import { useRoute } from "vue-router";

const brands = ref(
  BRANDS.map((b) => ({
    value: b,
    label: b
  }))
);
const listProductBySlugCategory = ref([]);

const route = useRoute();

const getListProductBySlugCategory = async () => {
  listProductBySlugCategory.value = [];
  try {
    const res = await ProductService.fetchListProductBySlugCategory(route.params.slug);
    listProductBySlugCategory.value = res.result.data;
  } catch (error) {
    handleError(error);
  }
};
watch(
  () => route.params.slug, // theo dõi slug
  (newSlug, oldSlug) => {
    if (newSlug !== oldSlug) {
      getListProductBySlugCategory();
    }
  },
  { immediate: true } // gọi lần đầu tiên khi component mounted
);
onMounted(()=> {
  getListProductBySlugCategory();
})

</script>

<template>
  <div class="row my-3">
    <!-- Sidebar Filter -->
    <div class="col-lg-3">
      <div class="card border-0 shadow-sm">
        <div class="card-header bg-primary text-white">
          <h6 class="mb-0"><i class="fas fa-filter me-2"></i>Bộ lọc sản phẩm</h6>
        </div>
        <div class="card-body">
          <!-- Price Filter -->
          <div class="mb-4">
            <h6 class="fw-bold">Khoảng giá</h6>
            <div class="form-check mb-2">
              <input class="form-check-input" type="checkbox" id="price1" />
              <label class="form-check-label" for="price1">Dưới 5 triệu</label>
            </div>
            <div class="form-check mb-2">
              <input class="form-check-input" type="checkbox" id="price2" />
              <label class="form-check-label" for="price2">5 - 10 triệu</label>
            </div>
            <div class="form-check mb-2">
              <input class="form-check-input" type="checkbox" id="price3" />
              <label class="form-check-label" for="price3">10 - 20 triệu</label>
            </div>
            <div class="form-check mb-2">
              <input class="form-check-input" type="checkbox" id="price4" />
              <label class="form-check-label" for="price4">20 - 30 triệu</label>
            </div>
            <div class="form-check">
              <input class="form-check-input" type="checkbox" id="price5" />
              <label class="form-check-label" for="price5">Trên 30 triệu</label>
            </div>
          </div>

          <!-- Brand Filter -->
          <div class="mb-4">
            <h6 class="fw-bold">Thương hiệu</h6>
            <div class="form-check mb-2" v-for="b in brands">
              <input class="form-check-input" type="checkbox" :id="b.value" />
              <label class="form-check-label" :for="b.label">
                {{ b.value }}
              </label>
            </div>
          </div>

          

          <div class="d-grid gap-2">
            <button class="btn btn-primary">Áp dụng bộ lọc</button>
            <button class="btn btn-outline-secondary">Xóa bộ lọc</button>
          </div>
        </div>
      </div>
    </div>
    <div class="col-9">
    <ProductList :products="listProductBySlugCategory" />
    </div>
  </div>
</template>

