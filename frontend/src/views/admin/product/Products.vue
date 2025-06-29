<template>
 <div class="d-flex justify-content-between align-items-center mb-3">
  <h1>Quản lý sản phẩm</h1>
  <router-link :to="{ name: 'product-create' }">
   <button class="btn btn-success p-2 fs-5">
    <font-awesome-icon icon="plus" />
    Thêm sản phẩm
   </button>
  </router-link>
 </div>
 <div class="d-flex justify-content-end">
  <input
   type="text"
   class="form-control w-25"
   placeholder="Tìm kiếm sản phẩm.."
   @input="handleSearch"
   v-model="searchKeyword"
  />
 </div>
 <table class="table table-hover text-center align-middle my-3">
  <thead>
   <tr>
    <th>STT</th>
    <th>Hình ảnh</th>
    <th>Tên sản phẩm</th>
    <th>Danh mục</th>
    <th>Thương hiệu</th>
    <!-- <th>Giá bán</th> -->
    <!-- <th>Tồn kho</th>
        <th>Trạng thái</th> -->
    <th>Thao tác</th>
   </tr>
  </thead>
  <tbody>
   <tr v-for="(product, index) in list" :key="index">
    <td>{{ (pagination.current - 1) * pagination.pageSize + index + 1 }}</td>
    <td>
     <img :src="product.thumbnail" alt="" width="150" height="100" />
    </td>
    <td>{{ product.name }}</td>
    <td>{{ product.category.name }}</td>
    <td>{{ product.brand }}</td>
    <!-- <td>{{ product.price }}</td> -->
    <!-- <td>...</td>
        <td>...</td> -->
    <td>
     <router-link class="btn btn-primary mx-2" :to="{ name: 'product-edit', params: { id: product.id } }">
      <font-awesome-icon icon="pen-to-square" />
     </router-link>
     <button class="btn btn-danger" @click="showModalDelete(product)">
      <font-awesome-icon icon="trash" />
     </button>
    </td>
   </tr>
   <tr v-if="list.length === 0">
    <td colspan="9" class="text-center py-4">
     <font-awesome-icon icon="circle-exclamation" size="2x" class="text-secondary mb-2" />
     <div>Không có dữ liệu</div>
    </td>
   </tr>
  </tbody>
 </table>

 <div class="d-flex justify-content-end mt-3">
  <a-pagination
   v-model:current="pagination.current"
   :total="pagination.total"
   simple
   :page-size="pagination.pageSize"
  />
 </div>
</template>
<script setup>
 import { handleError, hideLoading, showLoading, showPromtDelete } from "@/api/functions/common";

 import { ProductService } from "@/api/service/ProductService";
 import { onBeforeMount, ref, watch } from "vue";
 import { toast } from "vue3-toastify";
 // search
 const searchKeyword = ref("");
 const timer = ref(null);

 const list = ref([]);
 const pagination = ref({
  current: 1,
  pageSize: 10,
  total: 0,
 });
 async function fetchList() {
  try {
   const response = await ProductService.fetchListProduct({
    page: pagination.value.current,
    size: pagination.value.pageSize,
   });
   list.value = response.result.data;
   pagination.value.total = response.result.totalElements;
   console.log(list.value);
  } catch (error) {
   console.log(error);
  }
 }
 // search
 async function searchList() {
  try {
   showLoading();
   // isSearching.value = true;

   const response = await ProductService.searchProduct({
    keyword: searchKeyword.value.trim(),
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

 async function handleSearch() {
  if (timer.value) clearTimeout(timer.value);

  timer.value = setTimeout(async () => {
   pagination.value.current = 1;

   if (!searchKeyword.value.trim()) {
    await fetchList(); // Gọi API fetch bình thường
   } else {
    await searchList(); // Gọi API search
   }
  }, 1000); // debounce 500ms
 }

 function showModalDelete(product) {
  showPromtDelete(() => {
   submitFormDelete(product);
  });
 }

 async function submitFormDelete(product) {
  try {
   showLoading();
   console.log(product.id);
   await ProductService.deleteProduct(product.id);
   list.value = list.value.filter((item) => item !== product);
   toast.success("Xóa sản phẩm thành công!");
  } catch (error) {
   handleError(error);
  } finally {
   hideLoading();
  }
 }
 watch(
  () => pagination.value.current,
  () => {
   fetchList();
  }
 );
 onBeforeMount(async () => {
  await fetchList();
 });
</script>
