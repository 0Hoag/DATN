<script setup>
import {
  handleError,
  hideLoading,
  showLoading,
  showPromtConfirm,
  showPromtDelete,
} from "@/api/functions/common";
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

const searchKeyword = ref("");
const roleSelected = ref("");
const timer = ref(null);

async function fetchListAccount() {
  try {
    showLoading();
    const response = await AccountService.fetchListAccount({
      page: pagination.value.current,
      size: pagination.value.pageSize,
      active: true,
    });

    listAccount.value = response.result.data;
    console.log(listAccount.value);
    pagination.value.total = response.result.totalElements;
  } catch (error) {
    toast.error("Lỗi khi tải dữ liệu");
    console.log(error);
  } finally {
    hideLoading();
  }
}

async function searchUser() {
  try {
    showLoading();
    const params = {
      keyword: searchKeyword.value,
      role: roleSelected.value,
      page: pagination.value.current,
      size: pagination.value.pageSize,
    };
    const response = await AccountService.searchUser(params);
    listAccount.value = response.result.data;
    pagination.value.total = response.result.totalElements;
  } catch (error) {
    handleError(error);
  } finally {
    hideLoading();
  }
}

async function filterByRole() {
  try {
    showLoading();
    const params = {
      keyword: '',
      role: roleSelected.value,
      page: pagination.value.current,
      size: pagination.value.pageSize,
    };
    const response = await AccountService.searchUser(params);
    listAccount.value = response.result.data;
    pagination.value.total = response.result.totalElements;
  } catch (error) {
    handleError(error);
  } finally {
    hideLoading();
  }
}

async function handleSearch() {
  if (timer.value) clearTimeout(timer.value);

  timer.value = setTimeout(async () => {
    if (pagination.value.current !== 1) {
      pagination.value.current = 1; // watcher sẽ tự gọi searchUser/fetchListAccount
    } else {
      if (!searchKeyword.value.trim()) {
        await fetchListAccount();
      } else {
        await searchUser();
      }
    }
  }, 1000);
}
async function fetchListRole() {
  try {
    const response = await RoleService.fetchListRole();
    listRole.value = response.result.filter((role) => role.name !== "GUEST");
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
  console.log(accountModelEdit.value);
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
function validateForm() {
  const regexPhone = /^0\d{9}$/;
  const regexPassword = /^(?=.*[A-Z]).{8,}$/;
  if (!regexPhone.test(accountModel.value.phone)) {
    toast.error("Số điện thoại phải bắt đầu bằng 0 và có 10 chữ số");
    return false;
  }
  if (!regexPassword.test(accountModel.value.password)) {
    toast.error("Mật khẩu ít nhất 8 ký tự và 1 chữ in hoa");
    return false;
  }
  if (accountModel.value.roles.length === 0) {
    toast.error("Vui lòng chọn ít nhất 1 vai trò");
    return false;
  }
  return true;
}
function validateFormEdit() {
  const regexPhone = /^0\d{9}$/;
  const regexPassword = /^(?=.*[A-Z]).{8,}$/;
  if (!regexPhone.test(accountModelEdit.value.phone)) {
    toast.error("Số điện thoại phải bắt đầu bằng 0 và có 10 chữ số");
    return false;
  }
  if (!regexPassword.test(accountModelEdit.value.password)) {
    toast.error("Mật khẩu ít nhất 8 ký tự và 1 chữ in hoa");
    return false;
  }
  if (accountModelEdit.value.roles.length === 0) {
    toast.error("Vui lòng chọn ít nhất 1 vai trò");
    return false;
  }
  return true;
}
async function submitFormAdd() {
  try {
    if (!validateForm()) return; // Kiểm tra tính hợp lệ của form
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
    if (!validateFormEdit()) return; // Kiểm tra tính hợp lệ của form
    showLoading();
    const data = getFormEdit();
    await AccountService.updateAccount(data.id, data);
    await fetchListAccount();
    toast.success("Cập nhật tài khoản thành công!");
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
    toast.success("Xóa tài Fkhoản thành công!");
    closeModal();
  } catch (error) {
    handleError(error);
  } finally {
    hideLoading();
  }
}

watch(
  () => pagination.value.current,
  (newVal, oldVal) => {
    if (newVal !== oldVal) {
      if (searchKeyword.value.trim()) {
        searchUser();
      } else {
        fetchListAccount();
      }
    }
  }
);


onMounted(() => {
  fetchListAccount();
  fetchListRole();
});




</script>

<template>
  <h1>Quản lý người dùng</h1>
  <div class="d-flex justify-content-end align-items-center mb-3">
    <button class="btn btn-success p-2 fs-5" @click="openModalAdd">
      <font-awesome-icon icon="plus" />
      Thêm người dùng
    </button>
  </div>
  <div class="d-flex justify-content-end align-items-center mb-3 gap-2">
  <button class="btn btn-outline-secondary" @click="filterByRole">Lọc</button>
    <select class="form-select w-auto" v-model="roleSelected">
      <option :value="''">Vai trò</option>
      <option :value="role.name" v-for="role in listRole" :key="role.id">
        {{ role.name }}
      </option>
    </select>
    <div>
      <input
        type="text"
        class="form-control"
        placeholder="Tìm kiếm user..."
        v-model="searchKeyword"
        @input="handleSearch"
        @keydown.enter="handleSearch"
      />
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
      <tr v-for="(account, index) in listAccount" :key="account.id">
        <td>{{ (pagination.current - 1) * pagination.pageSize + index + 1 }}</td>
        <td>{{ account.fullName || "Chưa có tên" }}</td>
        <td>{{ account.email }}</td>
        <td>{{ account.phone || "Chưa có số điện thoại" }}</td>
        <td>{{ account.roles.map((role) => role.name).join(", ") }}</td>
        <td>{{ account.deletedAt == null ? "Hoạt động" : "Khóa" }}</td>
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
      <tr v-if="listAccount.length === 0">
        <td colspan="8" class="text-center py-4">
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

  <div class="d-flex justify-content-end mt-3">
    <a-pagination
      v-model:current="pagination.current"
      :total="pagination.total"
      simple
      :page-size="pagination.pageSize"
    />
  </div>

  <Modal ref="addModalRef">
    <template #header> Thêm người dùng </template>
    <template #body>
      <form @submit.prevent="submitFormAdd">
        <div class="row">
          <div class="col-6 mb-3">
            <label for="addFullName" class="form-label fw-bold">Họ tên</label>
            <input
              type="text"
              class="form-control"
              id="addFullName"
              placeholder="Họ tên"
              required
              v-model="accountModel.fullName"
            />
          </div>
          <div class="col-6 mb-3">
            <label for="addEmail" class="form-label fw-bold">Email</label>
            <input
              type="email"
              class="form-control"
              id="addEmail"
              placeholder="Email"
              required
              v-model="accountModel.email"
            />
          </div>
          <div class="col-6 mb-3">
            <label for="addPhone" class="form-label fw-bold">Số điện thoại</label>
            <input
              type="text"
              class="form-control"
              id="addPhone"
              placeholder="Số điện thoại"
              required
              v-model="accountModel.phone"
            />
          </div>
          <div class="col-6 mb-3">
            <label for="addPassword" class="form-label fw-bold">Mật khẩu</label>
            <input
              type="password"
              class="form-control"
              id="addPassword"
              placeholder="Mật khẩu"
              required
              v-model="accountModel.password"
            />
          </div>
          <div id="addListRole" class="col-12 row">
            <div class="col-3">
              <p class="fw-bold">Vai trò</p>
            </div>
            <div class="col-9 row">
              <div
                class="col-4 mb-3 form-check"
                v-for="(role, index) in listRole"
                :key="'add-' + role.name"
              >
                <input
                  class="form-check-input"
                  type="checkbox"
                  v-model="accountModel.roles"
                  :value="role.name"
                  :id="`add-role-${role.name}`"
                />
                <label class="form-check-label" :for="`add-role-${role.name}`">
                  {{ role.name }}
                </label>
              </div>
            </div>
          </div>
          <div class="col-12 d-flex justify-content-end">
            <button type="submit" class="btn btn-primary">Lưu</button>
            <button type="button" class="btn btn-secondary" @click="closeModal">
              Hủy
            </button>
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
            <input
              type="text"
              class="form-control"
              id="editFullName"
              placeholder="Họ tên"
              required
              v-model="accountModelEdit.fullName"
            />
          </div>
          <div class="col-6 mb-3">
            <label for="editEmail" class="form-label fw-bold">Email</label>
            <input
              type="email"
              class="form-control"
              id="editEmail"
              placeholder="Email"
              required
              v-model="accountModelEdit.email"
            />
          </div>
          <div class="col-6 mb-3">
            <label for="editPhone" class="form-label fw-bold">Số điện thoại</label>
            <input
              type="text"
              class="form-control"
              id="editPhone"
              placeholder="Số điện thoại"
              required
              v-model="accountModelEdit.phone"
            />
          </div>
          <div id="editListRole" class="col-12 row">
            <div class="col-3">
              <p class="fw-bold">Vai trò</p>
            </div>
            <div class="col-9 row">
              <div
                class="col-4 mb-3 form-check"
                v-for="(role, index) in listRole"
                :key="'edit-' + role.name"
              >
                <input
                  class="form-check-input"
                  type="checkbox"
                  v-model="accountModelEdit.roles"
                  :value="role.name"
                  :id="`edit-role-${role.name}`"
                />
                <label class="form-check-label" :for="`edit-role-${role.name}`">
                  {{ role.name }}
                </label>
              </div>
            </div>
          </div>
          <div class="col-12 d-flex justify-content-end">
            <button type="submit" class="btn btn-primary">Lưu</button>
            <button type="button" class="btn btn-secondary" @click="closeModal">
              Hủy
            </button>
          </div>
        </div>
      </form>
    </template>
  </Modal>
</template>
