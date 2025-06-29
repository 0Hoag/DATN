<template>
  <div class="d-flex justify-content-between align-items-center mb-3">
    <h1>Quản lý voucher</h1>
    <button class="btn btn-success p-2 fs-5" @click="openModalAdd">
      <font-awesome-icon icon="plus" />
      Thêm voucher
    </button>
  </div>
  <div class="d-flex justify-content-end">
    <input type="text" class="form-control w-25" placeholder="Tìm kiếm voucher.." @input="handleSearch" v-model="searchKeyword" />
  </div>
  <table class="table table-hover text-center align-middle my-3">
    <thead>
      <tr>
        <th>STT</th>
        <th>Mã voucher</th>
        <th>Mô tả voucher</th>
        <th>Giá trị</th>
        <th>Đã sử dụng</th>
        <th>Hạn sử dụng</th>
        <th>Trạng thái</th>
        <th>Thao tác</th>
      </tr>
    </thead>
    <tbody>
      <tr v-for="(voucher, index) in listVoucher" :key="index">
        <td>{{ (pagination.current - 1) * pagination.pageSize + index + 1 }}</td>
        <td>{{ voucher.code }}</td>
        <td>{{ voucher.description }}</td>
        <td>{{ voucher.discountValue <=100 ? voucher.discountValue + "%" : voucher.discountValue + "đ" }}</td>
        <td>{{ voucher.usageCount }}</td>
        <td>{{ voucher.displayEndAt }}</td>
        <td>{{ voucher.isActive ? "Hoạt động" : "Hết hạn" }}</td>
        <td>
          <button class="btn btn-primary mx-2" @click="openModalEdit(voucher)">
            <font-awesome-icon icon="pen-to-square" />
          </button>
          <button class="btn btn-danger" @click="openModalDelete(voucher)">
            <font-awesome-icon icon="trash" />
          </button>
        </td>
      </tr>
      <tr v-if="listVoucher.length === 0">
        <td colspan="8" class="text-center py-4">
          <font-awesome-icon icon="circle-exclamation" size="2x" class="text-secondary mb-2" />
          <div>Không có dữ liệu</div>
        </td>
      </tr>
    </tbody>
  </table>

  <div class="d-flex justify-content-end mt-3">
    <a-pagination v-model:current="pagination.current" :total="pagination.total" simple :page-size="pagination.pageSize" />
  </div>

  <Modal ref="addModalRef">
    <template #header> Thêm voucher </template>
    <template #body>
      <form @submit.prevent="submitFormAdd">
        <div class="row">
          <div class="col-12 mb-3">
            <label for="addCode" class="form-label fw-bold">Mã voucher</label>
            <input type="text" class="form-control" id="addCode" placeholder="Mã voucher" v-model="voucherModel.code" required />
          </div>
          <div class="col-6 mb-3">
            <label for="addDescription" class="form-label fw-bold">Mô tả</label>
            <input type="text" class="form-control" id="addDescription" placeholder="Mô tả" v-model="voucherModel.description" required />
          </div>
          <div class="col-6 mb-3">
            <label for="addDiscountValue" class="form-label fw-bold">Giá trị</label>
            <input type="number" step="any" class="form-control" id="addDiscountValue" placeholder="Giá trị" v-model="voucherModel.discountValue" required />
          </div>
          <div class="col-6 mb-3">
            <label for="addMinOrderValue" class="form-label fw-bold">Giá trị đơn hàng tối thiểu</label>
            <input type="number" class="form-control" id="addMinOrderValue" placeholder="Giá trị đơn hàng tối thiểu" v-model="voucherModel.minOrderValue" required />
          </div>
          <div class="col-6 mb-3">
            <label for="addMaxUsageCount" class="form-label fw-bold">Số lượt dùng tối đa</label>
            <input type="number" class="form-control" id="addMaxUsageCount" placeholder="Số lượt dùng tối đa" v-model="voucherModel.quantity" required />
          </div>
          <div class="col-6 mb-3">
            <label for="addStart_at" class="form-label fw-bold">Bắt đầu từ</label>
            <input type="date" class="form-control" id="addStart_at" v-model="voucherModel.startAt" required />
          </div>
          <div class="col-6 mb-3">
            <label for="addEnd_at" class="form-label fw-bold">Hạn sử dụng</label>
            <input type="date" class="form-control" id="addEnd_at" v-model="voucherModel.endAt" required />
          </div>

          <div class="col-12 d-flex justify-content-end">
            <button type="submit" class="btn btn-primary">Lưu</button>
            <button type="button" class="btn btn-secondary" @click="closeModal">Hủy</button>
          </div>
        </div>
      </form>
    </template>
  </Modal>

  <Modal ref="editModalRef">
    <template #header> Chỉnh sửa voucher </template>
    <template #body>
      <form @submit.prevent="submitFormEdit">
        <div class="row">
          <div class="col-12 mb-3">
            <label for="editCode" class="form-label fw-bold">Mã voucher</label>
            <input type="text" class="form-control" id="editCode" placeholder="Mã voucher" v-model="voucherModelEdit.code" required />
          </div>
          <div class="col-6 mb-3">
            <label for="editDescription" class="form-label fw-bold">Mô tả</label>
            <input type="text" class="form-control" id="editDescription" placeholder="Mô tả" v-model="voucherModelEdit.description" required />
          </div>
          <div class="col-6 mb-3">
            <label for="editDiscountValue" class="form-label fw-bold">Giá trị</label>
            <input type="number" step="any" class="form-control" id="editDiscountValue" placeholder="Giá trị" v-model="voucherModelEdit.discountValue" required />
          </div>
          <div class="col-6 mb-3">
            <label for="editMinOrderValue" class="form-label fw-bold">Giá trị đơn hàng tối thiểu</label>
            <input type="number" class="form-control" id="editMinOrderValue" placeholder="Giá trị đơn hàng tối thiểu" v-model="voucherModelEdit.minOrderValue" required />
          </div>
          <div class="col-6 mb-3">
            <label for="editMaxUsageCount" class="form-label fw-bold">Số lượt dùng tối đa</label>
            <input type="number" class="form-control" id="editMaxUsageCount" placeholder="Số lượt dùng tối đa" v-model="voucherModelEdit.quantity" required />
          </div>
          <div class="col-6 mb-3">
            <label for="editStart_at" class="form-label fw-bold">Bắt đầu từ</label>
            <input type="date" class="form-control" id="editStart_at" v-model="voucherModelEdit.startAt" required />
          </div>
          <div class="col-6 mb-3">
            <label for="editEnd_at" class="form-label fw-bold">Hạn sử dụng</label>
            <input type="date" class="form-control" id="editEnd_at" v-model="voucherModelEdit.endAt" required />
          </div>

          <div class="col-12 d-flex justify-content-end">
            <button type="submit" class="btn btn-primary">Lưu</button>
            <button type="button" class="btn btn-secondary" @click="closeModal">Hủy</button>
          </div>
        </div>
      </form>
    </template>
  </Modal>
</template>
<script setup>
import { handleError, hideLoading, showLoading, showPromtDelete } from "@/api/functions/common";
import { VoucherService } from "@/api/service/VoucherService";
import Modal from "@/components/Modal.vue";
import { onBeforeMount, ref, watch } from "vue";
import { toast } from "vue3-toastify";
import dayjs from "dayjs";
const voucherModel = ref({
  code: "", // Mã giảm giá
  description: "", // Mô tả
  discountValue: 0, // Giá trị giảm (kiểu số - BigDecimal)
  minOrderValue: 0, // Giá trị đơn hàng tối thiểu để áp dụng
  quantity: 0, // Tổng số lượng mã giảm giá
  usageCount: 0, // Số lần đã sử dụng
  startAt: "", // Thời gian bắt đầu (ISO String hoặc Date object)
  endAt: "", // Thời gian kết thúc (ISO String hoặc Date object)
  isActive: true, // Có đang hoạt động hay không
});
const voucherModelEdit = ref({});

const addModalRef = ref(null);
const editModalRef = ref(null);

const listVoucher = ref([]);
const pagination = ref({
  current: 1,
  pageSize: 10,
  total: 0,
});
const searchKeyword = ref("");

const timer = ref(null);

function openModalAdd() {
  addModalRef.value.open();
}
function openModalEdit(voucher) {
  voucherModelEdit.value = {
    ...voucher,
    startAt: formatDateForInput(voucher.startAt),
    endAt: formatDateForInput(voucher.endAt),
  };

  console.log(voucherModelEdit.value);
  editModalRef.value.open();
}
function openModalDelete(voucher) {
  showPromtDelete(() => {
    submitFormDelete(voucher);
  });
}
const closeModal = () => {
  addModalRef.value?.close();
  editModalRef.value?.close();
  resetForm();
};

function resetForm() {
  voucherModel.value = {
    code: "",
    description: "",
    discountValue: 0,
    minOrderValue: 0,
    quantity: 0,
    usageCount: 0,
    startAt: "",
    endAt: "",
    isActive: true,
  };
}
function getFormAdd() {
  return {
    ...voucherModel.value,
    startAt: toStartOfDay(voucherModel.value.startAt),
    endAt: toEndOfDay(voucherModel.value.endAt),
  };
}
function getFormEdit() {
  return { ...voucherModelEdit.value, startAt: toStartOfDay(voucherModelEdit.value.startAt), endAt: toEndOfDay(voucherModelEdit.value.endAt) };
}

function isValidDateRange(start, end) {
  return dayjs(end).isAfter(dayjs(start)) || dayjs(end).isSame(dayjs(start));
}

async function submitFormAdd() {
  try {
    showLoading();
    if (!isValidDateRange(voucherModel.value.startAt, voucherModel.value.endAt)) {
      toast.error("Ngày kết thúc phải sau ngày bắt đầu");
      return;
    }
    console.log(getFormAdd());
    await VoucherService.createVoucher(getFormAdd());
    toast.success("Thêm voucher thành công!");
    await fetchList();
    closeModal();
  } catch (error) {
    handleError(error);
    console.log(error);
  } finally {
    hideLoading();
  }
}
async function submitFormEdit() {
  try {
    showLoading();
    if (!isValidDateRange(voucherModelEdit.value.startAt, voucherModelEdit.value.endAt)) {
      toast.error("Ngày kết thúc phải sau ngày bắt đầu");
      return;
    }
    const data = getFormEdit();
    await VoucherService.updateVoucher(data.id, data);
    await fetchList();
    toast.success("Thêm voucher thành công!");
    closeModal();
    console.log(getFormEdit());
  } catch (error) {
    handleError(error);
    console.log(error);
  } finally {
    hideLoading();
  }
}
async function submitFormDelete(voucher) {
  try {
    showLoading();
    await VoucherService.deleteVoucher(voucher.id);
    toast.success("Xóa voucher thành công!");
    await fetchList();
    closeModal();
    console.log(getFormAdd());
  } catch (error) {
    handleError(error);
    console.log(error);
  } finally {
    hideLoading();
  }
}
async function fetchList() {
  try {
    showLoading();
    const response = await VoucherService.fetchListVoucher({
      page: pagination.value.current,
      size: pagination.value.pageSize,
      sort: true,
    });

    const now = dayjs();

    listVoucher.value = response.result.data.map((item) => {
      const end = dayjs(item.endAt);
      return {
        ...item,
        displayStartAt: dayjs(item.startAt).format("DD-MM-YYYY HH:mm:ss"),
        displayEndAt: end.format("DD-MM-YYYY HH:mm:ss"),
        isActive: end.isAfter(now),
      };
    });

    pagination.value.total = response.result.totalElements;
  } catch (error) {
    console.log(error);
    toast.error("Lỗi khi lấy dữ liệu");
  } finally {
    hideLoading();
  }
}

async function searchList() {
  try {
    showLoading();
    // isSearching.value = true;

    const response = await VoucherService.searchVoucher({
      keyword: searchKeyword.value.trim(),
      page: pagination.value.current,
      size: pagination.value.pageSize,
      desc: true,
    });

    const now = dayjs();

    listVoucher.value = response.result.data.map((item) => {
      const end = dayjs(item.endAt);
      return {
        ...item,
        displayStartAt: dayjs(item.startAt).format("DD-MM-YYYY HH:mm:ss"),
        displayEndAt: end.format("DD-MM-YYYY HH:mm:ss"),
        isActive: end.isAfter(now),
      };
    });

    pagination.value.total = response.result.totalElements;
  } catch (error) {
    toast.error("Lỗi khi tìm kiếm dữ liệu");
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

function formatDateForInput(isoString) {
  if (!isoString) return "";
  return new Date(isoString).toISOString().slice(0, 10); // "2025-06-16"
}
function toStartOfDay(dateStr) {
  return `${dateStr}T00:00:00`;
}

function toEndOfDay(dateStr) {
  return `${dateStr}T23:59:59`;
}

onBeforeMount(async () => {
  await fetchList();
});
</script>
