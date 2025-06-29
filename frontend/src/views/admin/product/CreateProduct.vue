<template>
 <h1>Thêm sản phẩm</h1>
 <form @submit.prevent="submitFormAdd">
  <div class="row">
   <div class="col-9">
    <div class="card">
     <div class="card-body">
      <div>
       <div class="mb-3">
        <label for="name" class="form-label fw-bold">Tên sản phẩm</label>
        <input v-model="productModel.name" name="name" class="form-control" placeholder="Tên sản phẩm" required />
       </div>
       <div class="mb-3">
        <label for="slug" class="form-label fw-bold">Đường dẫn tĩnh</label>
        <input v-model="productModel.slug" name="slug" class="form-control" placeholder="Đường dẫn tĩnh" required />
       </div>
       <div class="mb-3">
        <label for="description" class="form-label fw-bold">Mô tả ngắn về sản phẩm</label>
        <input
         v-model="productModel.description"
         name="description"
         class="form-control"
         placeholder="Mô tả ngắn về sản phẩm"
         required
        />
       </div>
       <div class="mb-3">
        <label for="name" class="form-label fw-bold">Thông số kỹ thuật</label>
        <QuillEditor
         v-model:content="productModel.content"
         contentType="html"
         style="height: 500px"
         theme="snow"
         :toolbar="[
          [{ header: [1, 2, 3, 4, 5, 6, false] }],
          [{ font: [] }],
          [{ size: ['small', false, 'large', 'huge'] }],
          ['bold', 'italic', 'underline', 'strike'],
          [{ color: [] }, { background: [] }],
          [{ script: 'sub' }, { script: 'super' }],
          [{ list: 'ordered' }, { list: 'bullet' }],
          [{ indent: '-1' }, { indent: '+1' }],
          [{ align: [] }],
          ['blockquote', 'code-block'],
          ['link', 'image', 'video'],
          ['clean'],
         ]"
        />
       </div>
      </div>
     </div>
    </div>
   </div>

   <div class="col-3">
    <div class="card">
     <div class="card-body">
      <div class="mb-3">
       <label for="name" class="form-label fw-bold">Danh mục</label>
       <a-select
        v-model:value="selectedCategory"
        class="w-100"
        show-search
        placeholder="Chọn danh mục"
        :options="categories"
        :filter-option="filterCategory"
       ></a-select>
      </div>
      <div class="mb-3">
       <label for="name" class="form-label fw-bold">Thương hiệu</label>
       <a-select
        v-model:value="selectedBrand"
        class="w-100"
        show-search
        placeholder="Chọn thương hiệu"
        :options="barnds"
        :filter-option="filterBrand"
       ></a-select>
      </div>
      <div class="mb-3">
       <label for="thumbnail" class="form-label fw-bold">Thumnail</label>
       <br />
       <img
        :src="productModel.thumbnail || 'https://upload.wikimedia.org/wikipedia/commons/6/65/No-Image-Placeholder.svg'"
        class="img-thumbnail rounded"
        alt="..."
        @click="openThumbnailSelector"
        width="200"
        height="200"
       />
      </div>
      <div class="form-check mb-3">
       <label for="showHome" class="form-label fw-bold">Hiển thị ở trang chủ</label>
       <input type="checkbox" class="form-check-input" id="showHome" v-model="productModel.isHome" />
      </div>

      <button class="btn btn-primary" type="submit">Lưu</button>
     </div>
    </div>
   </div>

   <!-- test -->
   <div class="col-9 mt-3">
    <div class="card">
     <div class="card-body">
      <h4 class="mb-3">Tạo biến thể sản phẩm</h4>
      <!-- <a href="javascript:void(0)" @click="openMediaModal">Upload ảnh</a> -->

      <!-- Chọn các thuộc tính -->
      <a-select
       v-model:value="selectedAttributes"
       mode="multiple"
       style="width: 100%"
       placeholder="Chọn các thuộc tính (ví dụ: RAM, ROM)"
       @change="handleAttributeChange"
      >
       <a-select-option v-for="attr in allAttributes" :key="attr.name" :value="attr.name">
        {{ attr.name }}
       </a-select-option>
      </a-select>

      <!-- Chọn giá trị cho từng thuộc tính -->
      <div v-for="attr in selectedAttributes" :key="attr" class="mt-2">
       <label
        ><strong>{{ attr }}</strong></label
       >
       <a-select
        v-model:value="selectedValues[attr]"
        mode="multiple"
        style="width: 100%"
        :placeholder="`Chọn giá trị cho ${attr}`"
       >
        <a-select-option v-for="val in getAttributeValues(attr)" :key="val.value" :value="val.value">
         {{ val.value }}
        </a-select-option>
       </a-select>
      </div>

      <!-- Tạo biến thể -->
      <a-button type="primary" class="my-3" @click="generateVariants">Tạo biến thể</a-button>

      <!-- Danh sách biến thể -->
      <a-collapse accordion>
       <a-collapse-panel v-for="(variant, index) in variants" :key="index">
        <template #header>
         <div class="d-flex justify-content-between">
          <strong>{{ variant.title }}</strong>
          <a-button type="primary" danger @click="removeVariant(variant)">Xóa</a-button>
         </div>
        </template>
        <div class="row g-3">
         <a href="javascript:void(0)" @click="openMediaModal(index)">Upload ảnh</a>
         <div class="col-md-12">
          <p>Ảnh đại diện</p>
          <!-- Danh sách ảnh -->

          <img
           :src="img.imageUrl"
           class="img-thumbnail rounded me-2"
           width="150"
           height="150"
           alt="Xem ảnh"
           v-for="(img, index) in variant.images"
           :key="index"
          />
         </div>
         <div class="col-md-12">
          <label>Tên biến thể</label>
          <input v-model="variant.variantName" class="form-control" required />
         </div>
         <div class="col-md-6">
          <label>Giá</label>
          <input v-model="variant.price" type="number" class="form-control" required />
         </div>
         <div class="col-md-6">
          <label>Tồn kho</label>
          <input v-model="variant.quantity" type="number" class="form-control" required />
         </div>
        </div>
       </a-collapse-panel>
      </a-collapse>
     </div>
    </div>
   </div>
  </div>
 </form>

 <Modal ref="mediaModalRef" :size="'xl'">
  <template #header>Thư viện ảnh</template>
  <template #body>
   <a-tabs v-model:activeKey="activeKey">
    <a-tab-pane key="1" tab="Upload ảnh">
     <div class="container mt-5 d-flex justify-content-center">
      <div class="upload-box text-center p-5 border border-2 border-dashed rounded">
       <div class="mb-3">
        <font-awesome-icon icon="cloud-arrow-up" size="2xl" />
       </div>
       <p class="mb-2">Chọn tệp để upload hình ảnh</p>
       <label class="btn btn-outline-secondary">
        Chọn tệp
        <input type="file" hidden multiple @change="onFilesSelected" accept="image/*" />
       </label>
       <button class="btn btn-primary" @click="onUploadImage">Upload {{ selectedFiles.length > 0 ? selectedFiles.length + ' tệp' : '' }}</button>
      </div>
     </div>
    </a-tab-pane>
    <a-tab-pane key="2" tab="Thư viện">
     <h6>Danh sách ảnh</h6>
     <div class="overflow-auto" style="max-height: 600px">
      <div v-for="(img, index) in listImage" :key="index" :class="imageCss(index)" class="border mx-3">
       <font-awesome-icon icon="circle-check" v-if="isSelected(index)" />
       <img :src="img?.url" @click="onImageSelect(index)" alt="" class="" width="150" height="150" />
      </div>
     </div>

     <div class="my-3">
      <button
       class="btn btn-primary"
       @click="isGlobalThumbnail ? confirmImageGlobalThumbnail() : confirmImageSelection()"
      >
       Chọn {{ selectedIndexes.length > 0 ? selectedIndexes.length + " ảnh" : "" }}
      </button>
      <button class="btn btn-secondary mx-2" v-if="selectedIndexes.length > 0" @click="clearImageSelection">
       Hủy chọn tất cả
      </button>
     </div>
    </a-tab-pane>
   </a-tabs>
  </template>
 </Modal>
</template>

<script setup>
import { handleError, hideLoading, showLoading } from "@/api/functions/common";
import { computed, watch, ref, reactive, onMounted } from "vue";
import { ProductService } from "@/api/service/ProductService";
import { toast } from "vue3-toastify";
import { CategoryService } from "@/api/service/CategoryService";
import { AttributeService } from "@/api/service/AttributeService";
import Modal from "@/components/Modal.vue";
import { ImageService } from "@/api/service/ImageService";

const mediaModalRef = ref(null);
const selectedFiles = ref([]);
const activeKey = ref("2");
const isGlobalThumbnail = ref(false);

const selectedCategory = ref(undefined);
const selectedBrand = ref(undefined);
const productModel = ref({
 name: "",
 slug: "",
 description: "",
 brand: "",
 thumbnail: "",
 content: "",
 isHome: true,
 isActive: true,
 category: null,
 productVariants: [],
});

const categories = ref([]);
const barnds = ref([
 { value: "Xiaomi", label: "Xiaomi" },
 { value: "Apple", label: "Apple" },
 { value: "Acer", label: "Acer" },
]);

const listImage = ref([]);

const selectedIndexes = ref([]);
const currentVariantIndex = ref(null);


const onFilesSelected = (event) => {
 selectedFiles.value = Array.from(event.target.files);
 console.log(selectedFiles.value);
};

const onUploadImage = async () => {
 let formData = new FormData();
 selectedFiles.value.forEach((file) => formData.append("files", file));

 try {
  showLoading("Đang tiến hành upload");
  const res = await ImageService.uploadImage(formData);
  toast.success('Upload thành công')
  console.log("Upload thành công:", res);
   await fetchListImage();
 } catch (error) {
  console.error("Lỗi upload:", error);
 } finally {
  hideLoading();
 }
};

const isSelected = (index) => {
 return selectedIndexes.value.includes(index);
};

const imageCss = (imageIndex) => {
 let classes = ["selectable-box"];

 if (isSelected(imageIndex)) {
  // Add "active" if selected

  classes.push("active");
 }

 return classes;
};

const clearImageSelection = () => {
 selectedIndexes.value = [];
};

const deleteImageSelection = () => {
 // Delete here with Ajax etc.
 let newImageUrls = [];

 for (let i = 0; i < listImage.value.length; i++) {
  if (!isSelected(i)) {
   newImageUrls.push(listImage.value[i]);
  }
 }

 listImage.value = newImageUrls;
 selectedIndexes.value = [];
 alert("Deletion completed。");
};

const onImageSelect = (imageIndex) => {
 if (isSelected(imageIndex)) {
  // Leave only the selected image “other than”
  selectedIndexes.value = selectedIndexes.value.filter((selectedIndex) => selectedIndex !== imageIndex);
 } else {
  selectedIndexes.value.push(imageIndex);
 }
};

const openMediaModal = (variantIndex) => {
 selectedIndexes.value = [];
 currentVariantIndex.value = variantIndex;
 mediaModalRef.value?.open();
};

function openThumbnailSelector() {
 isGlobalThumbnail.value = true;
 selectedIndexes.value = [];
 mediaModalRef.value?.open();
}

const confirmImageGlobalThumbnail = () => {
 const selectedImage = selectedIndexes.value.map((i) => listImage.value[i]);
 if (selectedImage.length > 1) {
  toast.warning("Thumbnail chỉ được chọn 1 ảnh");
  return;
 }

 productModel.value.thumbnail = selectedImage[0].url;
 selectedIndexes.value = [];
 mediaModalRef.value?.close();
 isGlobalThumbnail.value = false;
};
const confirmImageSelection = () => {
 console.log(currentVariantIndex.value);
 if (currentVariantIndex.value == null) return;

 const selectedImage = selectedIndexes.value.map((i) => listImage.value[i]);
 const variant = variants.value[currentVariantIndex.value];

 variant.images = selectedImage.map((item, index) => ({
  altText: item.fileName,
  specDescription: item.fileName,
  isThumbnail: index === 0,
  sortOrder: index + 1,
  imageUrl: item.url,
 }));

 selectedIndexes.value = [];
 currentVariantIndex.value = null;
 mediaModalRef.value?.close();

 console.log(variant);
};
async function fetchListImage() {
 try {
  const response = await ImageService.fetchListImage();
  listImage.value = response.result;
 } catch (error) {
  toast.error("Lỗi khi tải danh sách hình ảnh");
  console.log(error);
 }
}
async function fetchListCategory() {
 try {
  const response = await CategoryService.fetchListCategory();
  categories.value = response.result
   .filter((item) => item.children.length === 0)
   .map((item) => ({ value: item.id, label: item.name }));
  console.log(categories.value);
 } catch (error) {
  toast.error("Lỗi khi tải danh sách danh mục");
  console.log(error);
 }
}
async function fetchListAttribute() {
 try {
  showLoading();
  const response = await AttributeService.fetchListAttribute();
  allAttributes.value = response.result;
 } catch (error) {
  handleError(error);
  toast.error("Lỗi lấy dữ liệu");
  console.log(error);
 } finally {
  hideLoading();
 }
}

const filterCategory = (input, option) => {
 return option.value.toLowerCase().indexOf(input.toLowerCase()) >= 0;
};
const filterBrand = (input, option) => {
 return option.value.toLowerCase().indexOf(input.toLowerCase()) >= 0;
};

function getFormAdd() {
 return {
  ...productModel.value,
  productVariants: variants.value,
  category: selectedCategory.value,
  brand: selectedBrand.value,
 };
}
function resetForm() {
 productModel.value = {
  name: "",
  slug: "",
  description: "",
  brand: "",
  thumbnail: "",
  content: "",
  isHome: true,
  isActive: true,
  category: null,
  productVariants: [],
 };
 selectedCategory.value = undefined;
 selectedBrand.value = undefined;
 selectedAttributes.value = [];
 Object.keys(selectedValues).forEach((key) => delete selectedValues[key]);
 variants.value = [];
}
function validateForm() {
 let errorMessage = "";
 if (!productModel.value.name) {
  errorMessage = "Vui lòng nhập tên sản phẩm";
 } else if (!productModel.value.slug) {
  errorMessage = "Vui lòng nhập đường dẫn tĩnh";
 } else if (!productModel.value.description) {
  errorMessage = "Vui lòng nhập mô tả ngắn về sản phẩm";
 } else if (!productModel.value.content) {
  errorMessage = "Vui lòng nhập thông số kỹ thuật";
 } else if (!selectedCategory.value) {
  errorMessage = "Vui lòng chọn danh mục";
 } else if (!selectedBrand.value) {
  errorMessage = "Vui lòng chọn thương hiệu";
 } else if (!variants.value.length) {
  errorMessage = "Vui lòng tạo biến thể sản phẩm";
  toast.error(errorMessage);
  return false;
 }
 if (errorMessage) {
  toast.error(errorMessage);
  return false;
 }
 return true;
}
async function submitFormAdd() {
 try {
  showLoading();
  if (!validateForm()) return;
  console.log(getFormAdd());
  await ProductService.createProduct(getFormAdd());
  toast.success("Thêm sản phẩm thành công!");
  resetForm();
 } catch (error) {
  handleError(error);
 } finally {
  hideLoading();
 }
}

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
// ✅ Thuộc tính giả lập (có id để gửi đi backend)
const allAttributes = ref([]);

const selectedAttributes = ref([]); // ['RAM', 'ROM']
const selectedValues = reactive({}); // { RAM: ['2GB', '4GB'], ROM: ['128GB'] }
const variants = ref([]);

// Lấy danh sách value từ tên thuộc tính
const getAttributeValues = (attrName) => {
 const attr = allAttributes.value.find((a) => a.name === attrName);
 return attr ? attr.values : [];
};

const handleAttributeChange = () => {
 // Reset các giá trị không còn tồn tại
 Object.keys(selectedValues).forEach((key) => {
  if (!selectedAttributes.value.includes(key)) delete selectedValues[key];
 });
};

// Cartesian product helper
const cartesianProduct = (arrays) => arrays.reduce((a, b) => a.flatMap((d) => b.map((e) => [...d, e])), [[]]);

const generateVariants = () => {
 const valueGroups = selectedAttributes.value.map((attrName) => {
  const values = selectedValues[attrName] || [];
  return values.map((val) => {
   const attrObj = allAttributes.value.find((a) => a.name === attrName);
   const valObj = attrObj.values.find((v) => v.value === val);
   return { attr: attrName, value: valObj.value, id: valObj.id };
  });
 });

 if (valueGroups.some((group) => group.length === 0)) {
  toast.info("Vui lòng chọn đầy đủ giá trị cho từng thuộc tính");
  return;
 }

 const combinations = cartesianProduct(valueGroups);

 variants.value = combinations.map((comb) => {
  const title = comb.map((c) => `${c.value}`).join(" - ");
  return {
   title,
   price: 0,
   variantName: "",
   quantity: 0,
   sold: 0,
   isActive: true,
   attributeValueIds: comb.map((c) => c.id),
   images: [],
  };
 });
};

const removeVariant = (variant) => {
 variants.value = variants.value.filter((v) => v !== variant);
 console.log(variants.value);
};

watch(
 () => productModel.value.name,
 (newValue) => {
  productModel.value.slug = generateSlug(newValue);
 }
);
onMounted(async () => {
 await fetchListCategory();
 await fetchListAttribute();
 await fetchListImage();
});
</script>
<style scoped>
.selectable-box {
 float: left;
 width: 150px;
 height: 150px;
 margin: 5px;
 background: #e8f0fd;
 position: relative;
}

.active {
 /* width: 120px;
 height: 120px; */
 padding: 5px;
}

.selectable-box img {
 width: 100%;
 height: 100%;
 object-fit: cover;
}

.selectable-box svg {
 position: absolute;
 left: 10px;
 top: 10px;
 font-size: 25px;
 color: #2b6fe7;
 background-color: #fff;
 border-radius: 50%;
}
</style>
