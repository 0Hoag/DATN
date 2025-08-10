<script setup>
import { handleError, hideLoading, showLoading } from "@/api/functions/common";
import { CategoryService } from "@/api/service/CategoryService";
import { ProductService } from "@/api/service/ProductService";
import ProductList from "@/components/ProductList.vue";
import { BRANDS } from "@/constant";
import { onMounted, ref, watch } from "vue";
import { useRoute } from "vue-router";

const brands = ref(
  BRANDS.map((b) => ({
    value: b,
    label: b,
  }))
);

const selectedBrands = ref([]); 
const selectedCategories = ref([]); 

const list = ref([]);
const route = useRoute();
const keyword = route.query.q;
// Phân trang
const pagination = ref({
  current: 1,
  pageSize: 10,
  total: 0,
});

// Khoảng giá
const minPrice = ref("");
const maxPrice = ref("");

// get list category
const categories = ref([])
const getListCategory = async () => {
  categories.value = [];
  try {
    const res = await CategoryService.fetchListCategoryForUser();
    const raw = res.result || [];

    // Lọc category cha
    categories.value = raw
      .filter((c) =>  c.children.length == 0 )
      .map((c) => ({
        ...c,
        children: c.children || [],
      }));
  } catch (error) {
    console.log(error);
    handleError(error);
  }
};

//loc
const filterProduct = async () => {
  try {
    const params = new URLSearchParams();
    params.append('keyword',keyword)
    params.append("page", pagination.value.current);
    params.append("size", pagination.value.pageSize);
    if (minPrice.value) params.append("minPrice", minPrice.value);
    if (maxPrice.value) params.append("maxPrice", maxPrice.value);
    selectedBrands.value.forEach((brand) => params.append("brands", brand));
    selectedCategories.value.forEach((brand) => params.append("categoryIds", brand));

    const res = await ProductService.filter(params);
    list.value = res.result.data;
    pagination.value.total = res.result.totalElements;
  } catch (error) {
    handleError(error);
  }
};

// Gọi khi nhấn "Áp dụng bộ lọc"
const handleFilter = async () => {
  pagination.value.current = 1;
  await filterProduct();
};

// Gọi khi nhấn "Xóa bộ lọc"
const handleReset = async () => {
  minPrice.value = "";
  maxPrice.value = "";
  selectedBrands.value = [];
  selectedCategories.value = [];
  pagination.value.current = 1;
  await searchList(keyword);
};

//search
 async function searchList(q) {
  try {
   showLoading();
   // isSearching.value = true;

   const response = await ProductService.searchProductForUser({
    keyword: q,
    page: pagination.value.current,
    size: pagination.value.pageSize,
   });

   list.value = response.result.data;
   pagination.value.total = response.result.totalElements;
  } catch (error) {
   toast.error("Lỗi khi tìm kiếm dữ liệu");
  } finally {
   hideLoading();
  }
 }


watch(
  () => pagination.value.current,
  async () => {
    if(minPrice.value || maxPrice.value) await filterProduct();
  }
);

watch(
  () => route.query.q,
  (newQuery) => {
    if (newQuery) {
      searchList(newQuery);
    }
  },
  { immediate: true }
)
onMounted(async () => {
  await getListCategory();
});
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
            <h6 class="fw-bold">Khoảng giá (VNĐ)</h6>
            <div class="mb-2">
              <label>Giá thấp nhất</label>
              <input
                type="number"
                class="form-control"
                v-model="minPrice"
                placeholder="VD: 1000000"
              />
            </div>
            <div>
              <label>Giá cao nhất</label>
              <input
                type="number"
                class="form-control"
                v-model="maxPrice"
                placeholder="VD: 10000000"
              />
            </div>
          </div>

          <!-- Category Filter -->
          <div class="mb-4">
            <h6 class="fw-bold">Danh mục</h6>
           <div class="d-flex flex-wrap gap-2">
              <div class="form-check mb-2" v-for="c in categories">
                <input class="form-check-input" type="checkbox" :id="c.name" :value="c.id"  v-model="selectedCategories" />
                <label class="form-check-label" :for="c.label">
                  {{ c.name }}
                </label>
              </div>
           </div>
          </div>
          <!-- Brand Filter -->
          <div class="mb-4">
            <h6 class="fw-bold">Thương hiệu</h6>
           <div class="d-flex flex-wrap gap-2">
              <div class="form-check mb-2" v-for="b in brands">
                <input class="form-check-input" type="checkbox" :id="b.value" :value="b.value"  v-model="selectedBrands" />
                <label class="form-check-label" :for="b.label">
                  {{ b.value }}
                </label>
              </div>
           </div>
          </div>

          <div class="d-grid gap-2">
            <button class="btn btn-primary" @click="handleFilter">Áp dụng bộ lọc</button>
            <button class="btn btn-outline-secondary" @click="handleReset">
              Xóa bộ lọc
            </button>
          </div>
        </div>
      </div>
    </div>
    <div class="col-9">
      <ProductList :products="list" :title="'Tìm kiếm sản phẩm'" />
      <!-- Phân trang -->
      <div class="d-flex justify-content-center mt-3" v-if="list && list.length > 0">
        <a-pagination
          v-model:current="pagination.current"
          :total="pagination.total"
          simple
          :page-size="pagination.pageSize"
        />
      </div>
    </div>
  </div>
</template>
