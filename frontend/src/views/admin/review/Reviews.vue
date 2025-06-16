<template>
  <h1>Quản lý đánh giá</h1>
  <div class="d-flex justify-content-end">
    <input type="text" class="form-control w-25" placeholder="Tìm kiếm đánh giá..." />
  </div>
  <table class="table table-hover text-center align-middle my-3">
    <thead>
      <tr>
        <th>STT</th>
        <th>Khách hàng</th>
        <th>Sản phẩm</th>
        <th>Đánh giá</th>
        <th>Nội dung</th>
        <th>Thao tác</th>
      </tr>
    </thead>
    <tbody>
      <tr v-for="(review, index) in list" :key="index">
        <td>{{ index + 1 }}</td>
        <td>{{ review.fullName }}</td>
        <td>{{ review.producName }}</td>
        <td>{{ review.rating }}</td>
        <td>{{ review.content }}</td>
        <td>
          <button class="btn btn-danger" @click="showModalDelete(review)">
            <font-awesome-icon icon="trash" />
          </button>
        </td>
      </tr>
      <tr v-if="list.length === 0">
        <td colspan="6" class="text-center py-4">
          <font-awesome-icon icon="circle-exclamation" size="2x" class="text-secondary mb-2" />
          <div>Không có dữ liệu</div>
        </td>
      </tr>
    </tbody>
  </table>
  <div class="d-flex justify-content-end mt-3">
    <a-pagination v-model:current="pagination.current" :total="pagination.total" simple :page-size="pagination.pageSize" />
  </div>
</template>
<script setup>
import { hideLoading, showLoading, showPromtDelete } from "@/api/functions/common";
import { ReviewService } from "@/api/service/ReviewService";
import { onBeforeMount, ref, watch } from "vue";

const list = ref([]);
const pagination = ref({
  current: 1,
  pageSize: 10,
  total: 0,
});
async function fetchList() {
  try {
    const response = await ReviewService.fetchListReview({
      page: pagination.value.current,
      size: pagination.value.pageSize,
    });
    list.value = response.result.data;
    console.log(list.value);
    pagination.value.total = response.result.totalElements;
  } catch (error) {
    console.log(error);
  }
}
function showModalDelete(review) {
  showPromtDelete(() => {
    submitFormDelete(review);
  });
}
async function submitFormDelete(review) {
  try {
    showLoading();
    await ReviewService.deleteReview(review.id);
    await fetchList();
    toast.success("Xóa đánh giá thành công!");
  } catch (error) {
    console.log(error);
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
