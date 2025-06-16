<template>
  <div class="d-flex justify-content-between align-items-center mb-3">
    <h1>Quản lý các thuộc tính</h1>
    <button class="btn btn-success p-2 fs-5" @click="openAddModal">
      <font-awesome-icon icon="plus" />
      Thêm thuộc tính
    </button>
  </div>
  <a-collapse v-model:activeKey="activeKey" accordion>
    <a-collapse-panel v-for="(attribute, index) in attributeList" :key="index">
      <template #header>
        <div class="d-flex justify-content-between align-items-center">
          <span>{{ attribute.name }}</span>
          <div class="d-flex gap-2">
            <button class="btn btn-sm btn-primary" @click.stop="addValue(attribute)"><font-awesome-icon icon="plus" /> Thêm giá trị</button>
            <button class="btn btn-sm btn-danger" @click.stop="showModalDeleteAttribute(attribute)"><font-awesome-icon icon="trash" /> Xóa</button>
          </div>
        </div>
      </template>
      <div class="input-group mb-3" v-for="(value, index) in attribute.values" :key="value.id">
        <input type="text" class="form-control" placeholder="Giá trị" :value="value.value" />
        <button class="btn btn-danger" @click="showModalDeleteValue(value)">
          <font-awesome-icon icon="trash" />
        </button>
      </div>
    </a-collapse-panel>
  </a-collapse>
  <div v-if="attributeList.length === 0" class="text-center py-4">
    <font-awesome-icon icon="circle-exclamation" size="2x" class="text-secondary mb-2" />
    <div>Không có dữ liệu</div>
  </div>

  <Modal ref="addModalRef">
    <template #header> Thêm thuộc tính </template>
    <template #body>
      <form @submit.prevent="submitFormAdd">
        <div class="mb-3">
          <label for="name" class="form-label fw-bold">Tên</label>
          <input type="text" class="form-control" id="name" placeholder="Tên thuộc tính" v-model="atrributeModel.name" required />
        </div>
        <div class="mb-3">
          <label for="values" class="form-label fw-bold">Giá trị</label>
          <input type="text" class="form-control" id="values" placeholder="VD: Đỏ, Vàng, Xanh" v-model="valuesInput" required />
        </div>
        <button type="submit" class="btn btn-primary">Lưu</button>
      </form>
    </template>
  </Modal>
</template>

<script setup>
import { handleError, hideLoading, showLoading, showPromtDelete } from "@/api/functions/common";
import { AttributeService } from "@/api/service/AttributeService";
import Modal from "@/components/Modal.vue";
import { onMounted, ref, watch } from "vue";
import { toast } from "vue3-toastify";
const activeKey = ref([]);
const attributeList = ref([]);
async function fetchListAttribute() {
  try {
    showLoading();
    const response = await AttributeService.fetchListAttribute();
    attributeList.value = response.result;
  } catch (error) {
    handleError(error);
    toast.error("Lỗi lấy dữ liệu");
    console.log(error);
  } finally {
    hideLoading();
  }
}

const atrributeModel = ref({
  name: "",
  values: [], // đây sẽ là mảng được backend nhận
});

const valuesInput = ref(""); // đây là input người dùng nhập (chuỗi)
const isEdit = ref(false);
const addModalRef = ref(null);

watch(valuesInput, (newVal) => {
  atrributeModel.value.values = newVal
    .split(",")
    .map((val) => val.trim())
    .filter((val) => val.length > 0);
});

const openAddModal = () => {
  resetForm();
  addModalRef.value?.open();
};
function getForm() {
  return { ...atrributeModel.value };
}
function setForm(attribute) {
  // atrributeModel.value = {
  //   ...attribute,
  //   values: attribute.values.join(", "),
  // };
}
function resetForm() {
  atrributeModel.value.name = "";
  valuesInput.value = "";
}
async function submitFormAdd() {
  try {
    showLoading();
    await AttributeService.createAttribute(getForm());
    await fetchListAttribute();
    toast.success("Thêm thuộc tính thành công!");
    resetForm();
  } catch (error) {
    handleError(error);
  } finally {
    hideLoading();
  }
}
function addValue(attribute) {
  openAddModal();
  atrributeModel.value.name = attribute.name;
}
function showModalDeleteValue(value) {
  showPromtDelete(() => {
    submitFormDeleteValue(value);
  });
}
async function submitFormDeleteValue(value) {
  try {
    showLoading();
    console.log(value);
    await AttributeService.deleteAttributeValues(value.id);
    await fetchListAttribute();
    toast.success("Xóa thuộc tính thành công!");
  } catch (error) {
    handleError(error);
  } finally {
    hideLoading();
  }
}
function showModalDeleteAttribute(attribute) {
  showPromtDelete(() => {
    submitFormDeleteAttribute(attribute);
  });
}
async function submitFormDeleteAttribute(attribute) {
  try {
    showLoading();

    await AttributeService.deleteAttribute(attribute.id);
    await fetchListAttribute();
    toast.success("Xóa thuộc tính thành công!");
  } catch (error) {
    handleError(error);
  } finally {
    hideLoading();
  }
}
onMounted(async () => {
  await fetchListAttribute();
});
</script>
