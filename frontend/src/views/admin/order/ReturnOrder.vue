<template>
  <h1>Danh sách trả hàng</h1>
  <table class="table table-hover text-center align-middle my-3">
    <thead>
      <tr>
        <th>STT</th>
        <th>Mã đơn hàng</th>
        <th>Khách hàng</th>
        <th>Lý do trả hàng</th>
        <th>Phương thức thanh toán</th>
        <th>Trạng thái trả hàng</th>
        <th>Ngày khởi tạo</th>
        <th>Thao tác</th>
      </tr>
    </thead>
    <tbody>
      <tr v-for="(order, index) in list" :key="index">
        <td>{{ (pagination.current - 1) * pagination.pageSize + index + 1 }}</td>
        <td>{{ order.order.id }}</td>
        <td>{{ order.fullName }}</td>
        <td>{{ order.reason }}</td>
        <td>{{ order.paymentMethod == 1 ? "VNPAY" : "COD" }}</td>
        <td>{{ order.status }}</td>
        <td>{{ order.createAt || "..." }}</td>
        <td>
          <button class="btn btn-success mx-2" v-if="order.orderStatus === 'PENDING'" @click="updateStatusOrder(order)">
            <font-awesome-icon icon="check" />
          </button>
          <button class="btn btn-primary mx-2">
            <font-awesome-icon icon="pen-to-square" />
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
    <a-pagination v-model:current="pagination.current" :total="pagination.total" simple :page-size="pagination.pageSize" />
  </div>
</template>
<script setup>
  import { OrderService } from "@/api/service/OrderService";
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
      const response = await OrderService.fetchListOrderReturn({
        page: pagination.value.current,
        size: pagination.value.pageSize,
      });
      list.value = response.result.data;
      pagination.value.total = response.result.totalElements;
      console.log(list.value);
    } catch (error) {
      toast.error("Lỗi khi tải đơn hàng");
      console.log(error);
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
