<script setup>
import { handleError, hideLoading, showLoading, showPromtDelete } from "@/api/functions/common";
import { AccountService } from "@/api/service/AccountService";
import { RoleService } from "@/api/service/RoleService";
import Modal from "@/components/Modal.vue";
import dayjs from "dayjs";
import { computed, onMounted, ref, watch } from "vue";
import { toast } from "vue3-toastify";
const activeType = ref("STAFF");
const addModalRef = ref(null);
const editModalRef = ref(null);
const listAccount = ref([]);
const listRole = ref([]);
const accountModel = ref({
  fullName: "",
  password: "",
  email: "",
  phone: "",
  isEnable: true,
  roles: [],
});

const accountModelEdit = ref({
  roles: [],
});

const pagination = ref({
  current: 1,
  pageSize: 10,
  total: 0,
});

const keyword = ref("");
const timer = ref(null);

function onInput(event) {
  const val = event.target.value;
  if (timer.value) clearTimeout(timer.value);
  timer.value = setTimeout(() => {
    keyword.value = val;
    pagination.value.current = 1;
  }, 1000);
}

const filteredList = computed(() => {
  if (!keyword.value) return listAccount.value;
  return listAccount.value.filter((item) => item.fullName.toLowerCase().includes(keyword.value.toLowerCase()));
});
// Phân trang
const filterPagination = computed(() => {
  const start = (pagination.value.current - 1) * pagination.value.pageSize;
  const end = start + pagination.value.pageSize;
  return filteredList.value.slice(start, end);
});
async function fetchListAccount() {
  try {
    showLoading();
    const response = await AccountService.fetchListAccount();

    const data = response.result;
    if (activeType.value == "STAFF") {
      const staffRoles = ["MANAGER", "SHIFT_STAFF"];
      listAccount.value = data.filter((user) => user.roles.some((role) => staffRoles.includes(role.name)));
    } else {
      listAccount.value = data.filter((user) => user.roles.some((role) => role.name === "CUSTOMER"));
    }

    pagination.value.total = listAccount.value.length;
  } catch (error) {
    toast.error("Lỗi khi tải dữ liệu");
    console.log(error);
  } finally {
    hideLoading();
  }
}
async function fetchListRole() {
  try {
    const response = await RoleService.fetchListRole();
    listRole.value = response.result;
    console.log(listRole.value);
  } catch (error) {
    toast.error("Lỗi khi tải dữ liệu");
    console.log(error);
  }
}

function openModalAdd() {
  resetForm();
  addModalRef.value.open();
}
function openModalEdit(user) {
  accountModelEdit.value = {
    ...user,
    roles: user.roles.map((role) => role.name),
  };
  editModalRef.value.open();
}
function openModalDelete(user) {
  showPromtDelete(() => {
    submitFormDelete(user);
  });
}
const closeModal = () => {
  addModalRef.value?.close();
  editModalRef.value?.close();
  resetForm();
};

function resetForm() {
  accountModel.value = {
    fullName: "",
    password: "",
    email: "",
    phone: "",
    isEnable: true,
    roles: [],
  };
}

function getFormAdd() {
  return { ...accountModel.value };
}
function getFormEdit() {
  return { ...accountModelEdit.value };
}
async function submitFormAdd() {
  try {
    showLoading();
    await AccountService.createAccount(getFormAdd());
    await fetchListAccount();
    toast.success("Thêm tài khoản thành công!");
    closeModal();
  } catch (error) {
    handleError(error);
  } finally {
    hideLoading();
  }
}
async function submitFormEdit() {
  try {
    showLoading();
    const data = getFormEdit();
    await AccountService.updateAccount(data.id, data);
    await fetchListAccount();
    toast.success("Thêm tài khoản thành công!");
    closeModal();
  } catch (error) {
    handleError(error);
  } finally {
    hideLoading();
  }
}
async function submitFormDelete(user) {
  try {
    showLoading();
    await AccountService.deleteAccount(user.id);
    await fetchListAccount();
    toast.success("Thêm tài khoản thành công!");
    closeModal();
  } catch (error) {
    handleError(error);
  } finally {
    hideLoading();
  }
}

watch(activeType, () => {
  pagination.value.current = 1;
  fetchListAccount();
});

onMounted(() => {
  fetchListAccount();
  fetchListRole();
});

async function handlePageChange(page) {
  pagination.value.current = page;
  await fetchListAccount();
}
</script>

<template>
  <h1>Quản lý người dùng</h1>
  <div class="d-flex justify-content-end align-items-center mb-3">
    <button class="btn btn-success p-2 fs-5" @click="openModalAdd">
      <font-awesome-icon icon="plus" />
      Thêm người dùng
    </button>
  </div>

  <div class="d-flex justify-content-between">
    <div class="btn-group mb-3">
      <button class="btn" :class="{ 'btn-primary': activeType === 'STAFF', 'btn-outline-primary': activeType !== 'STAFF' }" @click="activeType = 'STAFF'">
        Nhân viên
      </button>
      <button class="btn" :class="{ 'btn-primary': activeType === 'GUEST', 'btn-outline-primary': activeType !== 'GUEST' }" @click="activeType = 'GUEST'">
        Khách hàng
      </button>
    </div>
    <div class="">
      <input type="text" class="form-control w-100" placeholder="Tìm kiếm user..." :value="keyword" @input="onInput" />
    </div>
  </div>
  <table class="table table-hover text-center align-middle my-3">
    <thead>
      <tr>
        <th>STT</th>
        <th>Họ tên</th>
        <th>Email</th>
        <th>Số điện thoại</th>
        <th>Vai trò</th>
        <th>Trạng thái</th>
        <th>Ngày tham gia</th>
        <th>Thao tác</th>
      </tr>
    </thead>
    <tbody>
      <tr v-for="(account, index) in filterPagination" :key="account.id">
        <td>{{ (pagination.current - 1) * pagination.pageSize + index + 1 }}</td>
        <td>{{ account.fullName || 'Chưa có tên' }}</td>
        <td>{{ account.email }}</td>
        <td>{{ account.phone  || 'Chưa có số điện thoại'}}</td>
        <td>{{ account.roles.map((role) => role.name).join(", ") }}</td>
        <td>{{ account.isEnable ? "Hoạt động" : "Khóa" }}</td>
        <td>{{ dayjs(account.createdAt).format("DD-MM-YYYY") }}</td>
        <td>
          <button class="btn btn-primary mx-2" @click="openModalEdit(account)">
            <font-awesome-icon icon="pen-to-square" />
          </button>
          <button class="btn btn-danger" @click="openModalDelete(account)">
            <font-awesome-icon icon="trash" />
          </button>
        </td>
      </tr>
      <tr v-if="filterPagination.length === 0">
        <td colspan="8" class="text-center py-4">
          <font-awesome-icon icon="circle-exclamation" size="2x" class="text-secondary mb-2" />
          <div>Không có dữ liệu</div>
        </td>
      </tr>
    </tbody>
  </table>

  <div class="d-flex justify-content-end mt-3">
    <a-pagination v-model:current="pagination.current" :total="pagination.total" simple :page-size="pagination.pageSize" @change="handlePageChange" />
  </div>

  <Modal ref="addModalRef">
    <template #header> Thêm người dùng </template>
    <template #body>
      <form @submit.prevent="submitFormAdd">
        <div class="row">
          <div class="col-6 mb-3">
            <label for="addFullName" class="form-label fw-bold">Họ tên</label>
            <input type="text" class="form-control" id="addFullName" placeholder="Họ tên" required v-model="accountModel.fullName" />
          </div>
          <div class="col-6 mb-3">
            <label for="addEmail" class="form-label fw-bold">Email</label>
            <input type="email" class="form-control" id="addEmail" placeholder="Email" required v-model="accountModel.email" />
          </div>
          <div class="col-6 mb-3">
            <label for="addPhone" class="form-label fw-bold">Số điện thoại</label>
            <input type="text" class="form-control" id="addPhone" placeholder="Số điện thoại" required v-model="accountModel.phone" />
          </div>
          <div class="col-6 mb-3">
            <label for="addPassword" class="form-label fw-bold">Mật khẩu</label>
            <input type="password" class="form-control" id="addPassword" placeholder="Mật khẩu" required v-model="accountModel.password" />
          </div>
          <div id="addListRole" class="col-12 row">
            <div class="col-3">
              <p class="fw-bold">Vai trò</p>
            </div>
            <div class="col-9 row">
              <div class="col-4 mb-3 form-check" v-for="(role, index) in listRole" :key="'add-' + role.name">
                <input class="form-check-input" type="checkbox" v-model="accountModel.roles" :value="role.name" :id="`add-role-${role.name}`" />
                <label class="form-check-label" :for="`add-role-${role.name}`">
                  {{ role.name }}
                </label>
              </div>
            </div>
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
    <template #header> Chỉnh sửa tài khoản </template>
    <template #body>
      <form @submit.prevent="submitFormEdit">
        <div class="row">
          <div class="col-6 mb-3">
            <label for="editFullName" class="form-label fw-bold">Họ tên</label>
            <input type="text" class="form-control" id="editFullName" placeholder="Họ tên" required v-model="accountModelEdit.fullName" />
          </div>
          <div class="col-6 mb-3">
            <label for="editEmail" class="form-label fw-bold">Email</label>
            <input type="email" class="form-control" id="editEmail" placeholder="Email" required v-model="accountModelEdit.email" />
          </div>
          <div class="col-6 mb-3">
            <label for="editPhone" class="form-label fw-bold">Số điện thoại</label>
            <input type="text" class="form-control" id="editPhone" placeholder="Số điện thoại" required v-model="accountModelEdit.phone" />
          </div>
          <div id="editListRole" class="col-12 row">
            <div class="col-3">
              <p class="fw-bold">Vai trò</p>
            </div>
            <div class="col-9 row">
              <div class="col-4 mb-3 form-check" v-for="(role, index) in listRole" :key="'edit-' + role.name">
                <input class="form-check-input" type="checkbox" v-model="accountModelEdit.roles" :value="role.name" :id="`edit-role-${role.name}`" />
                <label class="form-check-label" :for="`edit-role-${role.name}`">
                  {{ role.name }}
                </label>
              </div>
            </div>
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
