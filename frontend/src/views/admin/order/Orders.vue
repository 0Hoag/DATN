<template>
  <div class="d-flex justify-content-between align-items-center mb-3">
    <h1>Quản lý đơn hàng</h1>
    <router-link :to="{ name: 'order-create' }" class="btn btn-success">
      <font-awesome-icon icon="plus" />
      Thêm đơn hàng
    </router-link>
  </div>
  <div class="d-flex justify-content-between">
    <div class="btn-group mb-3">
      <button class="btn" :class="{ 'btn-primary': activeType === 'ALL', 'btn-outline-primary': activeType !== 'ALL' }" @click="activeType = 'ALL'">Tất cả</button>
      <button class="btn" :class="{ 'btn-primary': activeType === 'PENDING', 'btn-outline-primary': activeType !== 'PENDING' }" @click="activeType = 'PENDING'">
        Chờ xác nhận
      </button>
      <button class="btn" :class="{ 'btn-primary': activeType === 'CONFIRMED', 'btn-outline-primary': activeType !== 'CONFIRMED' }" @click="activeType = 'CONFIRMED'">
        Đã xác nhận
      </button>
      <button class="btn" :class="{ 'btn-primary': activeType === 'SHIPPED', 'btn-outline-primary': activeType !== 'SHIPPED' }" @click="activeType = 'SHIPPED'">
        Đang giao
      </button>
      <button class="btn" :class="{ 'btn-primary': activeType === 'DELIVERED', 'btn-outline-primary': activeType !== 'DELIVERED' }" @click="activeType = 'DELIVERED'">
        Đã giao
      </button>
      <button class="btn" :class="{ 'btn-primary': activeType === 'CANCELLED', 'btn-outline-primary': activeType !== 'CANCELLED' }" @click="activeType = 'CANCELLED'">
        Đã hủy
      </button>
    </div>
    <div class="">
      <input type="text" class="form-control w-100" placeholder="Tìm kiếm đơn hàng..." />
    </div>
  </div>

  <table class="table table-hover text-center align-middle my-3">
    <thead>
      <tr>
        <th>STT</th>
        <th>Mã đơn hàng</th>
        <th>Khách hàng</th>
        <th>Trạng thái đơn hàng</th>
        <th>Trạng thái thanh toán</th>
        <th>Ngày tạo</th>
        <th>Thao tác</th>
      </tr>
    </thead>
    <tbody>
      <tr v-for="(order, index) in list" :key="index">
        <td>{{ (pagination.current - 1) * pagination.pageSize + index + 1 }}</td>
        <td>{{ order.id }}</td>
        <td>{{ order.user.fullName || "Chưa có tên" }}</td>
        <td>{{ order.orderStatus }}</td>
        <td>{{ order.paymentStatus }}</td>
        <td>{{ order.createdAt }}</td>
        <td>
          <button class="btn btn-success mx-2" v-if="order.orderStatus === 'PENDING'" @click="updateStatusOrder(order)">
            <font-awesome-icon icon="check" />
          </button>
          <router-link class="btn btn-primary mx-2" :to="{ name: 'order-edit', params: { id: order.id } }">
            <font-awesome-icon icon="pen-to-square" />
          </router-link>
          <button class="btn btn-danger mx-2" @click="showModalDelete(order)">
            <font-awesome-icon icon="trash" />
          </button>
        </td>
      </tr>
      <tr v-if="list.length === 0">
        <td colspan="7" class="text-center py-4">
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
import { handleError, hideLoading, showLoading, showPromtDelete } from "@/api/functions/common";
import { OrderService } from "@/api/service/OrderService";
import { onBeforeMount, ref, watch } from "vue";
import { toast } from "vue3-toastify";

const activeType = ref("ALL");

const list = ref([]);
// Phân trang
const pagination = ref({
  current: 1,
  pageSize: 10,
  total: 0,
});
async function fetchList() {
  try {
    const response = await OrderService.fetchListOrder({
      page: pagination.value.current,
      size: pagination.value.pageSize,
    });
    if (activeType.value === "ALL") {
      list.value = response.result.data;
    } else {
      list.value = response.result.data.filter((item) => item.orderStatus === activeType.value);
    }
    pagination.value.total = response.result.totalElements; // Phân trang: cập nhật tổng số phần tử trên trang
    console.log(list.value);
  } catch (error) {
    console.log(error);
  }
}

async function updateStatusOrder(order) {
  try {
    showLoading();

    await OrderService.updateStatus(order.id, { orderStatus: "CONFIRMED", paymentStatus: order.paymentStatus });
    toast.success("Cập nhật trạng thái đơn hàng thành công!");
    await fetchList();
  } catch (error) {
    toast.error("Lỗi khi tìm kiếm dữ liệu");
  } finally {
    hideLoading();
  }
}

function showModalDelete(order) {
  showPromtDelete(() => {
    submitFormDelete(order);
  });
}

async function submitFormDelete(order) {
  try {
    showLoading();
    await OrderService.deleteOrder(order.id);
    list.value = list.value.filter((item) => item !== order);
    toast.success("Xóa danh mục thành công!");
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

watch(activeType, async () => {
  pagination.value.current = 1;
  console.log(activeType.value);
  await fetchList();
});

onBeforeMount(async () => {
  await fetchList();
});
</script>
