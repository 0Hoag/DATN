<script setup>
import { ref, computed, watch, onMounted } from "vue";
// import { Modal } from "bootstrap";
import Modal from "@/components/Modal.vue";
import { toast } from "vue3-toastify";
import { CategoryService } from "@/api/service/CategoryService";
import {
  handleError,
  hideLoading,
  showLoading,
  showPromtDelete,
} from "@/api/functions/common";

const keyword = ref("");
const timer = ref(null);

const pagination = ref({
  current: 1,
  pageSize: 10,
  total: 0,
});

const listCategory = ref([
  // {
  //   id: 1,
  //   name: "Xiaomi",
  //   slug: "xiaomi",
  //   parent: null,
  // },
]);

const categoryData = ref({
  id: "",
  name: "",
  slug: "",
  parent: null,
  description: "",
  isShow: false,
});

const errors = ref({
  name: "",
  slug: "",
});

// Ref cho 2 modal
const addModalRef = ref(null);
const editModalRef = ref(null);

// Reset trang về 1 mỗi khi keyword thay đổi
watch(
  () => pagination.value.current,
  () => {
    if (keyword.value.trim()) {
      searchList();
    } else {
      fetchListCategory();
    }
  }
);

// tư động tạo slug dựa trên tên
watch(
  () => categoryData.value.name,
  (newValue) => {
    categoryData.value.slug = generateSlug(newValue);
  }
);

async function fetchListCategory() {
  try {
    const response = await CategoryService.fetchListCategory({
      page: pagination.value.current,
      size: pagination.value.pageSize,
    });
    // const rawList = response.result;

    // // Tạo Map để tra cứu nhanh
    // const idToNameMap = new Map();
    // rawList.forEach((cat) => {
    //   idToNameMap.set(cat.id, cat.name);
    // });

    // // Thêm parentName
    // listCategory.value = rawList.map((cat) => ({
    //   ...cat,
    //   parentName: cat.parent ? idToNameMap.get(cat.parent) || null : null,
    // }));
    // pagination.value.total = listCategory.value.length;
    listCategory.value = response.result.data;
    pagination.value.total = response.result.totalElements;
  } catch (error) {
    toast.error("Lỗi khi tải danh sách danh mục");
    console.log(error);
  }
}
async function searchList() {
  try {
    showLoading();
    const response = await CategoryService.searchCategory({
      keyword: keyword.value.trim(),
      page: pagination.value.current,
      size: pagination.value.pageSize,
    });

    const rawList = response.result.data;

    // Tạo Map để tra cứu nhanh
    const idToNameMap = new Map();
    rawList.forEach((cat) => {
      idToNameMap.set(cat.id, cat.name);
    });

    // Thêm parentName
    listCategory.value = rawList.map((cat) => ({
      ...cat,
      parentName: cat.parent ? idToNameMap.get(cat.parent) || null : null,
    }));
    pagination.value.total = response.result.totalElements;
  } catch (error) {
    toast.error("Lỗi khi tìm kiếm danh mục");
  } finally {
    hideLoading();
  }
}

async function handleSearch() {
  if (timer.value) clearTimeout(timer.value);

  timer.value = setTimeout(async () => {
    pagination.value.current = 1;

    if (!keyword.value.trim()) {
      await fetchListCategory(); // Gọi API fetch bình thường
    } else {
      await searchList(); // Gọi API search
    }
  }, 1000); // debounce 1000ms
}
const openAddModal = () => {
  resetForm();
  addModalRef.value?.open();
};
const openEditModal = (categroy) => {
  let c = null;

  if (categroy.parent) {
    c = listCategory.value.find((item) => item.id === categroy.parent);
  }

  categoryData.value = {
    ...categroy,
    parent: c?.id ?? null,
  };

  editModalRef.value?.open();
};
const closeModal = () => {
  addModalRef.value?.close();
  editModalRef.value?.close();
};

function showModalDelete(category) {
  showPromtDelete(() => {
    submitFormDelete(category);
  });
}

function getFormEdit() {
  return {
    ...categoryData.value,
  };
}
function getFormAdd() {
  return {
    ...categoryData.value,
  };
}

function validateForm() {
  const existedName = listCategory.value.some(
    (item) =>
      item.name.toLowerCase() === categoryData.value.name.trim().toLowerCase() &&
      item.id !== categoryData.value.id
  );
  errors.value.name = existedName ? "Tên danh mục đã tồn tại" : "";
  const existedSlug = listCategory.value.some(
    (item) =>
      item.slug.toLowerCase() === categoryData.value.slug.trim().toLowerCase() &&
      item.id !== categoryData.value.id
  );
  errors.value.slug = existedSlug ? "Đường dẫn tĩnh đã tồn tại" : "";

  return !!(errors.value.name || errors.value.slug);
}
async function submitFormAdd() {
  try {
    showLoading();
    if (validateForm()) return;

    const response = await CategoryService.createCategory(getFormAdd());
    if (response.result) {
      await fetchListCategory();

      toast.success(`Thêm danh mục "${categoryData.value.name}" thành công!`);

      // Đóng modal sau khi hiển thị thông báo
      setTimeout(() => {
        closeModal();
      }, 500);
    }
  } catch (error) {
    handleError(error);
  } finally {
    hideLoading();
  }
}
async function submitFormEdit() {
  try {
    showLoading();
    if (validateForm()) return;
    const data = getFormEdit();
    await CategoryService.updateCategory(data.id, data);
    toast.success("Cập nhật danh mục thành công!");
    await fetchListCategory();

    // Đóng modal sau khi hiển thị thông báo
    setTimeout(() => {
      closeModal();
    }, 500);
  } catch (error) {
    handleError(error);
  } finally {
    hideLoading();
  }
}

async function submitFormDelete(category) {
  try {
    showLoading();
    await CategoryService.deleteCategory(category.id);
    listCategory.value = listCategory.value.filter((item) => item !== category);
    toast.success("Xóa danh mục thành công!");
  } catch (error) {
    handleError(error);
  } finally {
    hideLoading();
  }
}

const resetForm = () => {
  categoryData.value = {
    name: "",
    slug: "",
    parent: null,
    description: "",
    isShow: false,
  };
  (errors.value.name = ""), (errors.value.slug = "");
};

function generateSlug(text) {
  return text
    .toLowerCase() // chuyển hết sang chữ thường
    .normalize("NFD") // tách các ký tự có dấu thành ký tự gốc + dấu
    .replace(/[\u0300-\u036f]/g, "") // xóa các dấu
    .replace(/đ/g, "d") // thay đ -> d
    .replace(/[^a-z0-9\s-]/g, "") // xóa ký tự đặc biệt
    .trim() // xóa khoảng trắng đầu cuối
    .replace(/\s+/g, "-") // thay khoảng trắng bằng -
    .replace(/-+/g, "-"); // gộp nhiều dấu - liên tiếp
}

onMounted(() => {
  fetchListCategory();
});
</script>

<template>
  <div class="d-flex justify-content-between align-items-center mb-3">
    <h1>Quản lý danh mục</h1>
    <button class="btn btn-success p-2 fs-5" @click="openAddModal">
      <font-awesome-icon icon="plus" />
      Thêm danh mục
    </button>
  </div>
  <div class="d-flex justify-content-end">
    <input
      type="text"
      class="form-control w-25"
      placeholder="Tìm kiếm danh mục..."
      v-model="keyword"
      @input="handleSearch"
      @keydown.enter="handleSearch"
    />
  </div>
  <table class="table table-hover text-center align-middle my-3">
    <thead>
      <tr>
        <th scope="col">STT</th>
        <th scope="col">Tên</th>
        <th scope="col">Đường dẫn tĩnh</th>
        <th scope="col">Danh mục cha</th>
        <th scope="col">Thao tác</th>
      </tr>
    </thead>
    <tbody>
      <tr v-for="(category, index) in listCategory" :key="index">
        <td scope="row">
          {{ (pagination.current - 1) * pagination.pageSize + index + 1 }}
        </td>
        <td>{{ category.name }}</td>
        <td>{{ category.slug }}</td>
        <td>{{ category.nameParent ?? "Không có danh mục cha" }}</td>
        <td>
          <button class="btn btn-primary mx-2" @click="openEditModal(category)">
            <font-awesome-icon icon="pen-to-square" />
          </button>
          <button class="btn btn-danger" @click="showModalDelete(category)">
            <font-awesome-icon icon="trash" />
          </button>
        </td>
      </tr>

      <tr v-if="listCategory.length === 0">
        <td colspan="5" class="text-center py-4">
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

  <!-- Add modal -->
  <Modal ref="addModalRef">
    <template #header> Thêm danh mục </template>
    <template #body>
      <form @submit.prevent="submitFormAdd">
        <div class="mb-3">
          <label for="nameModalAdd" class="form-label fw-bold">Tên danh mục</label>
          <input
            type="text"
            class="form-control"
            id="nameModalAdd"
            placeholder="Tên danh mục"
            v-model="categoryData.name"
            :class="{ 'is-invalid': errors.name }"
            required
          />
          <div class="invalid-feedback">{{ errors.name }}</div>
        </div>
        <div class="mb-3">
          <label for="slugModalAdd" class="form-label fw-bold">Đường dẫn tĩnh</label>
          <input
            type="text"
            class="form-control"
            id="slugModalAdd"
            placeholder="Đường dẫn tĩnh"
            v-model="categoryData.slug"
            :class="{ 'is-invalid': errors.slug }"
            required
          />
          <div class="invalid-feedback">{{ errors.slug }}</div>
        </div>
        <div class="mb-3">
          <label for="parentModalAdd" class="form-label fw-bold">Danh mục cha</label>
          <select class="form-select" id="parentModalAdd" v-model="categoryData.parent">
            <option :value="null">Chọn danh mục</option>
            <option
              :value="category.id"
              v-for="category in listCategory.filter(
                (item) =>
                  item.parent === null &&
                  item.children.length == 0 &&
                  item.products.length == 0
              )"
              :key="category.id"
            >
              {{ category.name }}
            </option>
          </select>
        </div>
        <div class="mb-3">
          <label for="descriptionModalAdd" class="form-label fw-bold">Mô tả</label>
          <textarea
            class="form-control"
            placeholder="Mô tả"
            id="descriptionModalAdd"
            v-model="categoryData.description"
          ></textarea>
        </div>
        <div class="mb-3 form-check">
          <input
            type="checkbox"
            class="form-check-input"
            id="isHomeModalAdd"
            v-model="categoryData.isShow"
          />
          <label class="form-check-label" for="isHomeModalAdd">Hiển thị trang home</label>
        </div>
        <div class="d-flex justify-content-end">
          <button type="submit" class="btn btn-primary mx-2">Lưu</button>
        </div>
      </form>
    </template>
  </Modal>

  <!-- Edit modal -->
  <Modal ref="editModalRef">
    <template #header> Chỉnh sửa danh mục </template>
    <template #body>
      <form @submit.prevent="submitFormEdit">
        <div class="mb-3">
          <label for="nameModalEdit" class="form-label fw-bold">Tên danh mục</label>
          <input
            type="text"
            class="form-control"
            id="nameModalEdit"
            placeholder="Tên danh mục"
            :class="{ 'is-invalid': errors.name }"
            v-model="categoryData.name"
            required
          />
          <div class="invalid-feedback">{{ errors.name }}</div>
        </div>
        <div class="mb-3">
          <label for="slugModalEdit" class="form-label fw-bold">Đường dẫn tĩnh</label>
          <input
            type="text"
            class="form-control"
            id="slugModalEdit"
            placeholder="Đường dẫn tĩnh"
            :class="{ 'is-invalid': errors.slug }"
            v-model="categoryData.slug"
            required
          />
          <div class="invalid-feedback">{{ errors.slug }}</div>
        </div>
        <div class="mb-3">
          <label for="parentModalEdit" class="form-label fw-bold">Danh mục cha</label>
          <select class="form-select" id="parentModalEdit" v-model="categoryData.parent">
            <option :value="null">Chọn danh mục</option>
            <option
              :value="category.id"
              v-for="category in listCategory.filter(
                (item) =>
                  item.id !== categoryData.id &&
                  item.parent == null &&
                   item.products.length == 0 &&
                  (item.children.length === 0 || item.id === categoryData.parent)
              )"
              :key="category.id"
            >
              {{ category.name }}
            </option>
          </select>
        </div>
        <div class="mb-3">
          <label for="descriptionModalEdit" class="form-label fw-bold">Mô tả</label>
          <textarea
            class="form-control"
            placeholder="Mô tả"
            id="descriptionModalEdit"
            v-model="categoryData.description"
          ></textarea>
        </div>
        <div class="mb-3 form-check">
          <input
            type="checkbox"
            class="form-check-input"
            id="isHomeModalEdit"
            v-model="categoryData.isShow"
          />
          <label class="form-check-label" for="isHomeModalEdit"
            >Hiển thị trang home</label
          >
        </div>
        <div class="d-flex justify-content-end">
          <button type="submit" class="btn btn-primary mx-2">Lưu</button>
        </div>
      </form>
    </template>
  </Modal>
</template>
