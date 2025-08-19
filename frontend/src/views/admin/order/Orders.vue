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
      <button
        class="btn"
        :class="{
          'btn-primary': activeType === '',
          'btn-outline-primary': activeType !== '',
        }"
        @click="changeStatus('')"
      >
        Tất cả
      </button>
      <button
        class="btn"
        :class="{
          'btn-primary': activeType === 'PENDING',
          'btn-outline-primary': activeType !== 'PENDING',
        }"
        @click="changeStatus('PENDING')"
      >
        Chờ xác nhận
      </button>
      <button
        class="btn"
        :class="{
          'btn-primary': activeType === 'CONFIRMED',
          'btn-outline-primary': activeType !== 'CONFIRMED',
        }"
        @click="changeStatus('CONFIRMED')"
      >
        Đã xác nhận
      </button>
      <button
        class="btn"
        :class="{
          'btn-primary': activeType === 'SHIPPED',
          'btn-outline-primary': activeType !== 'SHIPPED',
        }"
        @click="changeStatus('SHIPPED')"
      >
        Đang giao
      </button>
      <button
        class="btn"
        :class="{
          'btn-primary': activeType === 'DELIVERED',
          'btn-outline-primary': activeType !== 'DELIVERED',
        }"
        @click="changeStatus('DELIVERED')"
      >
        Đã giao
      </button>
      <button
        class="btn"
        :class="{
          'btn-primary': activeType === 'RECEIED',
          'btn-outline-primary': activeType !== 'RECEIED',
        }"
        @click="changeStatus('RECEIED')"
      >
        Đã nhận
      </button>
      <button
        class="btn"
        :class="{
          'btn-primary': activeType === 'CANCELLED',
          'btn-outline-primary': activeType !== 'CANCELLED',
        }"
        @click="changeStatus('CANCELLED')"
      >
        Đã hủy
      </button>
    </div>
    <div class="d-flex justify-content-end">
      <input
        type="text"
        class="form-control w-100"
        placeholder="Tìm kiếm đơn hàng.."
        @input="handleSearch"
        v-model="searchKeyword"
      />
    </div>
  </div>

  <div class="table-responsisve-sm table-responsive-md">
    <table class="table table-hover text-center align-middle my-3">
      <thead>
        <tr>
          <th>STT</th>
          <th>Mã đơn hàng</th>
          <th>Khách hàng</th>
          <th>Trạng thái đơn hàng</th>
          <th>Trạng thái thanh toán</th>
          <th>Phương thức thanh toán</th>
          <th>Ngày tạo</th>
          <th>Thao tác</th>
        </tr>
      </thead>
      <tbody>
        <tr v-for="(order, index) in list" :key="index">
          <td>{{ (pagination.current - 1) * pagination.pageSize + index + 1 }}</td>
          <td>{{ order.id }}</td>
          <td>{{ order.user.fullName || "Chưa có tên" }} - {{ order.user.email }}</td>
          <td>{{ order.orderStatus }}</td>
          <td>{{ order.paymentStatus }}</td>
          <td>{{ order.paymentMethod }}</td>
          <td>{{ order.createdAt }}</td>
          <td>
            <button
              class="btn btn-success mx-2"
              v-if="order.orderStatus === 'PENDING'"
              @click="updateStatusOrder(order)"
              title="Xác nhận đơn hàng"
            >
              <font-awesome-icon icon="check" />
            </button>
            <button
              class="btn btn-danger mx-2"
              v-if="order.orderStatus === 'PENDING'"
              @click="openModalCancelOrder(order)"
              title="Hủy đơn hàng"
            >
              <font-awesome-icon icon="xmark" />
            </button>
            <button
              class="btn btn-warning mx-2"
              v-if="order.orderStatus === 'CONFIRMED'"
              @click="updateStatusShiped(order)"
              title="Xác nhận giao hàng"
            >
              <font-awesome-icon icon="truck" />
            </button>
            <button
              class="btn btn-success mx-2"
              v-if="order.orderStatus === 'SHIPPED'"
              @click="updateStatusDeliverd(order)"
              title="Xác nhận giao thành công"
            >
              <font-awesome-icon icon="check" />
            </button>
            <button
              class="btn btn-success mx-2"
              v-if="order.orderStatus === 'DELIVERED'"
              @click="updateStatusReceived(order)"
              title="Xác nhận đơn hàng đã nhận"
            >
              <font-awesome-icon icon="box" />
            </button>
            <button
              class="btn btn-success mx-2"
              v-if="['CONFIRMED','DELIVERED','RECEIED'].includes(order.orderStatus) || order.paymentStatus == 'PAID'"
              @click="exportInvoice(order)"
              title="Xuất hóa đơn"
            >
              <font-awesome-icon icon="fa-solid fa-file-invoice" />
            </button>
            <router-link
              class="btn btn-primary mx-2"
              :to="{ name: 'order-edit', params: { id: order.id } }"
              title="Chỉnh sửa"
            >
              <font-awesome-icon icon="pen-to-square" />
            </router-link>
            <!-- <button class="btn btn-danger mx-2" @click="showModalDelete(order)" v-if="order.orderStatus === 'CANCELLED'">
              <font-awesome-icon icon="trash" />
            </button> -->
          </td>
        </tr>
        <tr v-if="list.length === 0">
          <td colspan="7" class="text-center py-4">
            <font-awesome-icon
              icon="circle-exclamation"
              size="2x"
              class="text-secondary mb-2"
            />
            <div>Không có dữ liệu</div>
          </td>
        </tr>
      </tbody>
    </table>
  </div>
  <div class="d-flex justify-content-end mt-3">
    <a-pagination
      v-model:current="pagination.current"
      :total="pagination.total"
      simple
      :page-size="pagination.pageSize"
    />
  </div>

  <!-- modal hủy đơn hàng -->
  <Modal ref="cancelOrderModalRef">
    <template #header> Lý do hủy đon hàng</template>
    <template #body>
      <form @submit.prevent="confirmCancelOrder">
        <div class="form-floating">
          <textarea
            id="cancelOrder"
            v-model="cancelOrderModel.reason"
            class="form-control"
            placeholder="Nhập lý do hủy đơn hàng"
            required
          ></textarea>
          <label for="cancelOrder">Nhập lý do hủy đơn hàng</label>
          <button class="btn btn-primary my-3" type="submit">Xác nhận</button>
        </div>
      </form>
    </template>
  </Modal>
</template>
<script setup>
import {
  handleError,
  hideLoading,
  showLoading,
  showPromtConfirm,
  showPromtDelete,
} from "@/api/functions/common";
import { OrderService } from "@/api/service/OrderService";
import { onBeforeMount, ref, watch } from "vue";
import { toast } from "vue3-toastify";
import Modal from "@/components/Modal.vue";
import instance from "@/api/plugin/axiosConfig";
import { API } from "@/api/domain";
//huy don hang
const cancelOrderModel = ref({});
const cancelOrderModalRef = ref(null);
const openModalCancelOrder = (order) => {
  cancelOrderModel.value.id = order.id;
  cancelOrderModel.value.reason = "";
  cancelOrderModalRef.value?.open();
};
const closeModalCancelOrder = () => {
  cancelOrderModel.value.id = "";
  cancelOrderModel.value.reason = "";
  cancelOrderModalRef.value?.close();
};
// tìm kiếm
const searchKeyword = ref("");
const timer = ref(null);
const startDate = ref(null);
const endDate = ref(null);
const orderStatus = ref(null);
const paymentSatus = ref(null);
const sort = ref(true);

const activeType = ref("");

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
      list.value = response.result.data.filter(
        (item) => item.orderStatus === activeType.value
      );
    }
    pagination.value.total = response.result.totalElements; // Phân trang: cập nhật tổng số phần tử trên trang
    console.log(list.value);
  } catch (error) {
    console.log(error);
  }
}
const filterOrderByStatus = async () => {
  console.log("type order", activeType.value);
  try {
    showLoading();
    const res = await OrderService.searchOrder({
      keyword: searchKeyword.value,
      orderStatus: activeType.value,
      page: pagination.value.current,
      size: pagination.value.pageSize,
    });

    list.value = res.result.data;
    console.log("filter by order", list.value);
    pagination.value.total = res.result.totalElements;
  } catch (error) {
    handleError(error);
  } finally {
    hideLoading();
  }
};
async function searchList() {
  try {
    showLoading();
    // isSearching.value = true;

    const response = await OrderService.searchOrder({
      keyword: searchKeyword.value.trim(),
      page: pagination.value.current,
      size: pagination.value.pageSize,
      orderStatus: activeType.value,
      // paymentStatus: searchKeyword.value.trim(),
      // startDate: searchKeyword.value.trim(),
      // endDate: searchKeyword.value.trim(),
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
      // await fetchList();
      await filterOrderByStatus();
    } else {
      await searchList();
    }
  }, 1000); // debounce
}

async function updateStatusShiped(order) {
  try {
    showLoading();

    await OrderService.updateStatus(order.id, {
      orderStatus: "SHIPPED",
      paymentStatus: order.paymentStatus,
    });
    toast.success("Cập nhật trạng thái đơn hàng thành công!");
    await filterOrderByStatus();
  } catch (error) {
    toast.error("Lỗi khi tìm kiếm dữ liệu");
  } finally {
    hideLoading();
  }
}
async function updateStatusDeliverd(order) {
  try {
    showLoading();

    await OrderService.updateStatus(order.id, {
      orderStatus: "DELIVERED",
      paymentStatus: order.paymentStatus,
    });
    toast.success("Cập nhật trạng thái đơn hàng thành công!");
    await filterOrderByStatus();
  } catch (error) {
    toast.error("Lỗi khi tìm kiếm dữ liệu");
  } finally {
    hideLoading();
  }
}
async function updateStatusReceived(order) {
  try {
    showLoading();

    await OrderService.updateStatus(order.id, {
      orderStatus: "RECEIED",
      paymentStatus: "PAID",
    });
    toast.success("Cập nhật trạng thái đơn hàng thành công!");
    await filterOrderByStatus();
  } catch (error) {
    toast.error("Lỗi khi tìm kiếm dữ liệu");
  } finally {
    hideLoading();
  }
}
async function updateStatusOrder(order) {
  try {
    showLoading();

    await OrderService.updateStatus(order.id, {
      orderStatus: "CONFIRMED",
      paymentStatus: order.paymentStatus,
    });
    toast.success("Cập nhật trạng thái đơn hàng thành công!");
    await filterOrderByStatus();
  } catch (error) {
    toast.error("Lỗi khi tìm kiếm dữ liệu");
  } finally {
    hideLoading();
  }
}
function confirmCancelOrder() {
  showPromtConfirm("Xác nhận hủy đơn hàng", () => cancelOrder());
}
async function cancelOrder() {
  try {
    showLoading();
    const { id, reason } = cancelOrderModel.value;
    await OrderService.cancelOrderByAdmin(id, { reason });
    await filterOrderByStatus();
    closeModalCancelOrder();
    toast.success("Hủy đơn hàng thành công!");
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

// watch(
//   () => pagination.value.current,
//   () => {
//     if (searchKeyword.value.trim()) {
//       searchList();
//     } else {
//       filterOrderByStatus();
//     }
//   }
// );
const changeStatus = (status = "") => {
  activeType.value = status;
  pagination.value.current = 1;
};

const exportInvoice = async (order) => {
  try {
    showLoading();
    const res = await instance.get(`${API.EXPORT_INVOICE}/${order.id}`, {
      responseType: "blob",
    });
    const blob = new Blob([res.data], { type: "application/pdf" });
    const url = window.URL.createObjectURL(blob);
    const link = document.createElement("a");
    link.href = url;
    link.setAttribute("download", "invoice.pdf");
    document.body.appendChild(link);
    link.click();
    link.remove();
    window.URL.revokeObjectURL(url);
  } catch (error) {
    handleError(error);
  } finally {
    hideLoading();
  }
};

watch([() => pagination.value.current, () => activeType.value], async () => {
  await filterOrderByStatus();
});

// watch(activeType, async () => {
//   pagination.value.current = 1;
//   console.log(activeType.value);
//   await fetchList();
// });

onBeforeMount(async () => {
  // await fetchList();
  await filterOrderByStatus();
});
</script>
