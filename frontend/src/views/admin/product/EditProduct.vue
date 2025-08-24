<template>
  <h1>Sửa sản phẩm</h1>

  <div class="row">
    <div class="col-9">
      <div class="card">
        <div class="card-body">
          <div>
            <div class="mb-3">
              <label for="name" class="form-label fw-bold">Tên sản phẩm</label>
              <input
                v-model="productModel.name"
                name="name"
                class="form-control"
                placeholder="Tên sản phẩm"
                required
              />
            </div>
            <div class="mb-3">
              <label for="slug" class="form-label fw-bold">Đường dẫn tĩnh</label>
              <input
                v-model="productModel.slug"
                name="slug"
                class="form-control"
                placeholder="Đường dẫn tĩnh"
                required
              />
            </div>
            <div class="mb-3">
              <label for="description" class="form-label fw-bold"
                >Mô tả ngắn về sản phẩm</label
              >
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
              :src="
                productModel.thumbnail ||
                'https://upload.wikimedia.org/wikipedia/commons/6/65/No-Image-Placeholder.svg'
              "
              class="img-thumbnail rounded"
              alt="..."
              @click="openThumbnailSelector"
              width="200"
              height="200"
            />
          </div>
          <div class="form-check mb-3">
            <label for="showHome" class="form-label fw-bold">Hiển thị ở trang chủ</label>
            <input
              type="checkbox"
              class="form-check-input"
              id="showHome"
              v-model="productModel.isHome"
            />
          </div>

          <button
            class="btn btn-primary"
            type="button"
            @click="submitFormEdit"
            v-if="hasScope(['ROLE_ADMIN', 'ROLE_MANAGER'])"
          >
            Lưu
          </button>
        </div>
      </div>
    </div>

    <!-- test -->
    <div class="col-9 mt-3">
      <div class="card">
        <div class="card-body">
          <!-- <div class="d-flex justify-content-between mb-3"> -->
          <h4>Biến thể sản phẩm</h4>
          <!-- <button class="btn btn-primary" @click="openAddVariantModal()">Thêm biến thể</button> -->
          <!-- </div> -->

          <!-- Danh sách biến thể -->
          <a-collapse accordion>
            <a-collapse-panel v-for="(variant, index) in variants" :key="index">
              <template #header>
                <div class="d-flex justify-content-between">
                  <strong>{{ variant.title }}</strong>
                  <a-button
                    type="primary"
                    danger
                    @click="removeVariant(variant)"
                    v-if="hasScope(['ROLE_ADMIN', 'ROLE_MANAGER'])"
                    >Xóa</a-button
                  >
                </div>
              </template>
              <div class="row g-3">
                <a href="javascript:void(0)" @click="openMediaModal(index)">Upload ảnh</a>
                <div class="col-md-12">
                  <p>Ảnh đại diện</p>

                  <div
                    v-for="(img, index) in variant.images"
                    :key="index"
                    class="position-relative d-inline-block me-2"
                  >
                    <!-- Nút X để xóa ảnh -->

                    <font-awesome-icon
                      icon="xmark"
                      class="position-absolute top-0 end-0 m-1 text-danger bg-white rounded-circle p-1"
                      style="cursor: pointer; z-index: 1"
                      @click="removeVariantImage(variant, index)"
                    />

                    <!-- Ảnh -->
                    <img
                      :src="img.imageUrl"
                      @click="showLightBox(variant.images, index)"
                      class="img-thumbnail rounded object-fit-contain"
                      style="width: 150px; height: 150px"
                      alt="Xem ảnh"
                      title="Click vào để xem hình ảnh"
                    />
                  </div>
                </div>

                <div class="col-md-12">
                  <label>Tên biến thể</label>
                  <input v-model="variant.variantName" class="form-control" required />
                </div>
                <div class="col-md-6">
                  <label>Giá</label>
                  <input
                    v-model="variant.price"
                    type="number"
                    class="form-control"
                    required
                  />
                </div>
                <div class="col-md-6">
                  <label>Giá khuyến mãi</label>
                  <input
                    v-model="variant.salePrice"
                    type="number"
                    class="form-control"
                    required
                  />
                </div>
                <div class="col-md-6">
                  <label>Tồn kho</label>
                  <input
                    v-model="variant.quantity"
                    type="number"
                    class="form-control"
                    required
                  />
                </div>
                <div class="col-md-6">
                  <button class="btn btn-primary" @click="updateVariant(variant)">
                    Cập nhật
                  </button>
                </div>
              </div>
            </a-collapse-panel>
          </a-collapse>
        </div>
      </div>
    </div>
  </div>

  <!-- Thư viện ảnh -->
  <Modal ref="mediaModalRef" :size="'xl'">
    <template #header>Thư viện ảnh</template>
    <template #body>
      <a-tabs v-model:activeKey="activeKey">
        <a-tab-pane key="1" tab="Upload ảnh">
          <div class="container mt-5 d-flex justify-content-center">
            <div
              class="upload-box text-center p-5 border border-2 border-dashed rounded"
              @dragover.prevent
              @drop.prevent="onDrop"
            >
              <div class="mb-3">
                <font-awesome-icon icon="cloud-arrow-up" size="2xl" />
              </div>
              <p class="mb-2">Chọn tệp để upload hình ảnh</p>
              <label class="btn btn-outline-secondary">
                Chọn tệp
                <input
                  type="file"
                  hidden
                  multiple
                  @change="onFilesSelected"
                  accept="image/*"
                />
              </label>
              <button class="btn btn-primary ms-2" @click="onUploadImage">
                Upload {{ selectedFiles.length > 0 ? selectedFiles.length + " tệp" : "" }}
              </button>
            </div>
          </div>
        </a-tab-pane>
        <a-tab-pane key="2" tab="Thư viện">
          <h6>Danh sách ảnh</h6>
          <div
            ref="scrollContainer"
            class="overflow-auto d-flex flex-column"
            style="height: 600px"
          >
            <div class="flex-shrink-0">
              <div
                v-for="(img, index) in listImage"
                :key="index"
                :class="imageCss(index)"
                class="border mx-3"
              >
                <font-awesome-icon icon="circle-check" v-if="isSelected(index)" />
                <img
                  :src="img?.url"
                  @click="onImageSelect(index)"
                  alt=""
                  width="150"
                  height="150"
                  :title="img.fileName"
                />
              </div>
            </div>

            <div ref="loadMoreTrigger" class="flex-shrink-0" style="height: 20px"></div>

            <p v-if="isLoading" class="mt-2">Đang tải...</p>
          </div>

          <div class="my-3">
            <button
              class="btn btn-primary"
              @click="
                isGlobalThumbnail
                  ? confirmImageGlobalThumbnail()
                  : confirmImageSelection()
              "
            >
              Chọn {{ selectedIndexes.length > 0 ? selectedIndexes.length + " ảnh" : "" }}
            </button>
            <button
              class="btn btn-secondary mx-2"
              v-if="selectedIndexes.length > 0"
              @click="clearImageSelection"
            >
              Hủy chọn tất cả
            </button>
          </div>
        </a-tab-pane>
      </a-tabs>
    </template>
  </Modal>

  <!-- Modal thêm biến thể sản phẩm -->
  <Modal ref="addVariantModalRef" :size="'xl'">
    <template #header>
      <h6>Thêm biến thể sản phẩm</h6>
    </template>
    <template #body> </template>
  </Modal>

  <!-- vue easy lightbox -->
  <VueEasyLightbox
    :visible="visible"
    :imgs="imagesLightBox"
    :index="index"
    @hide="visible = false"
  />
</template>

<script setup>
import { handleError, hideLoading, showLoading } from "@/api/functions/common";
import { computed, watch, ref, reactive, onMounted, onUnmounted } from "vue";
import { ProductService } from "@/api/service/ProductService";
import { toast } from "vue3-toastify";
import { CategoryService } from "@/api/service/CategoryService";
import { useRoute } from "vue-router";
import { AttributeService } from "@/api/service/AttributeService";
import { ImageService } from "@/api/service/ImageService";
import Modal from "@/components/Modal.vue";
import { useAuth } from "@/composable/useAuth";
import VueEasyLightbox from "vue-easy-lightbox";

//vue easy lightbox
const visible = ref(false);
const index = ref(0);
const imagesLightBox = ref([]);
function showLightBox(images, i) {
  index.value = i;
  visible.value = true;
  imagesLightBox.value = images.map((img) => img.imageUrl);
  console.log("img", imagesLightBox.value);
}

const { hasScope } = useAuth();
const addVariantModalRef = ref(null);
const modal2 = ref();

function openAddVariantModal() {
  addVariantModalRef.value?.open();
}

function openModal2() {
  modal2.value.open();
}

const route = useRoute();
const idProduct = ref(route.params.id);

const mediaModalRef = ref(null);
const isGlobalThumbnail = ref(false);
const listImage = ref([]);
const listRemoveImage = ref([]);
const activeKey = ref("2");

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

const selectedIndexes = ref([]);
const currentVariantIndex = ref(null);

const selectedFiles = ref([]);
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
    toast.success("Upload thành công");
    console.log("Upload thành công:", res);
    paginationImage.value.current = 1;
    paginationImage.value.totalPages = null;
    listImage.value = [];
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
    selectedIndexes.value = selectedIndexes.value.filter(
      (selectedIndex) => selectedIndex !== imageIndex
    );
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

  const newImages = selectedImage.map((item, index) => ({
    altText: item.fileName,
    specDescription: item.fileName,
    isThumbnail: index === 0,
    sortOrder: index + 1,
    imageUrl: item.url,
  }));

  variant.images.push(...newImages);

  selectedIndexes.value = [];
  currentVariantIndex.value = null;
  mediaModalRef.value?.close();

  console.log(variant);
};
//scroll infinity image
const paginationImage = ref({
  current: 1,
  pageSize: 30,
  total: 0,
  totalPages: null,
});
const loadMoreTrigger = ref(null);
const scrollContainer = ref(null);
const isLoading = ref(false);
let observer = null;
async function fetchListImage() {
  if (isLoading.value) return;
  if (
    paginationImage.value.totalPages &&
    paginationImage.value.current > paginationImage.value.totalPages
  ) {
    return; // hết dữ liệu
  }
  isLoading.value = true;
  try {
    const response = await ImageService.fetchListImage({
      page: paginationImage.value.current,
      size: paginationImage.value.pageSize,
    });
    listImage.value.push(...response.result.data);
    paginationImage.value.totalPages = response.result.totalPages;
    paginationImage.value.current++;
  } catch (error) {
    toast.error("Lỗi khi tải danh sách hình ảnh");
    console.log(error);
    handleError(error);
  } finally {
    isLoading.value = false;
  }
}
async function fetchListCategory() {
  try {
    const response = await CategoryService.fetchListCategoryForUser();
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

async function fetchProductById() {
  try {
    showLoading();
    const response = await ProductService.detailProduct(idProduct.value);
    console.log("product", response.result);
    productModel.value = response.result;
    variants.value = response.result.productVariants;
    selectedCategory.value = response.result.category.id;
    selectedBrand.value = response.result.brand;
    generateVariantTitles();
    console.log(productModel.value);
    console.log(variants.value);
  } catch (error) {
    handleError(error);
    toast.error("Lỗi khi tải sản phẩm");
  } finally {
    hideLoading();
  }
}
function generateVariantTitles() {
  if (!productModel.value?.productVariants) return;

  productModel.value.productVariants.forEach((variant) => {
    const title = variant.attributeValues
      .map((attr) => `${attr.attributeName.toUpperCase()} ${attr.attributeValue}`)
      .join(" - ");
    variant.title = title;
  });
}

const filterCategory = (input, option) => {
  return option.value.toLowerCase().indexOf(input.toLowerCase()) >= 0;
};
const filterBrand = (input, option) => {
  return option.value.toLowerCase().indexOf(input.toLowerCase()) >= 0;
};

function getFormEdit() {
  return {
    ...productModel.value,
    productVariants: variants.value,
    category: selectedCategory.value,
    brand: selectedBrand.value,
  };
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

async function submitFormEdit() {
  try {
    showLoading();
    if (!validateForm()) return;
    console.log(getFormEdit());
    const data = getFormEdit();
    console.log("data", data);
    await ProductService.updateProduct(data.id, data);
    toast.success("Sửa sản phẩm thành công!");
  } catch (error) {
    handleError(error);
  } finally {
    hideLoading();
  }
}

const validateUpdateVariant = () => {
  let errorMessage = "";

  const errorVariants = variants.value.filter(
    (variant) =>
      variant.images.length === 0 ||
      !variant.variantName.trim() ||
      variant.price === 0 ||
      variant.quantity === 0
  );

  if (errorVariants.length > 0) {
    errorMessage = "Có lỗi ở các biến thể sau:\n";
    errorVariants.forEach((variant, index) => {
      let errs = [];
      if (variant.images.length === 0) errs.push("Chọn ít nhất 1 hình ảnh");
      if (!variant.variantName.trim()) errs.push("Biến thể sản phẩm chưa có tên");
      if (variant.price === 0) errs.push("giá = 0");
      if (variant.quantity === 0) errs.push("số lượng = 0");

      errorMessage += `- Biến thể vị trí ${index + 1} (${
        variant.variantName ?? "Chưa có tên"
      }): ${errs.join(", ")}\n`;
    });

    toast.error(errorMessage);
    return false;
  }

  // Nếu qua hết -> hợp lệ
  return true;
};

const updateVariant = async (variant) => {
  try {
    showLoading();

    if (!validateUpdateVariant()) return;

    await ProductService.updateProductVariant(variant.id, variant);
    if (listRemoveImage.value.length > 0) await deleteVariantImage();
    toast.success("Sửa biến thể thành công!");
  } catch (error) {
    handleError(error);
  } finally {
    hideLoading();
  }
};

const removeVariantImage = (variant, imgIndex) => {
  const imgRemoved = variant.images[imgIndex];

  // Cập nhật mảng ảnh
  const listImages = variant.images.filter((_, i) => i !== imgIndex);
  variant.images = listImages.map((img, index) => ({
    ...img,
    sortOrder: index + 1,
    isThumbnail: index === 0,
  }));
  // Đưa ảnh bị xóa vào danh sách đã xoá
  if (imgRemoved.id) listRemoveImage.value.push(imgRemoved.id);
  console.log(listRemoveImage.value, "list remove");
};

const deleteVariantImage = async () => {
  try {
    showLoading();
    const data = [...listRemoveImage.value];
    await ProductService.deleteVariantImage({
      data: data, // 👈 Đây là đúng chỗ
    });
    toast.success("Xóa ảnh thành công");
  } catch (error) {
  } finally {
    hideLoading();
  }
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
const cartesianProduct = (arrays) =>
  arrays.reduce((a, b) => a.flatMap((d) => b.map((e) => [...d, e])), [[]]);

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
      salePrice: 0,
      variantName: "",
      quantity: 0,
      sold: 0,
      isActive: true,
      attributeValueIds: comb.map((c) => c.id),
    };
  });
};
//drag và drop file
const onDrop = (event) => {
  selectedFiles.value = Array.from(event.dataTransfer.files);
  const files = selectedFiles.value;
  for (let i = 0; i < files.length; i++) {
    if (files[i].type.split("/")[0] !== "image") continue;
  }
};
watch(
  () => productModel.value.name,
  (newValue) => {
    productModel.value.slug = generateSlug(newValue);
  }
);

watch(
  variants,
  (newVal, oldVal) => {
    console.log("Variants thay đổi:", newVal)
  },
  { deep: true } // 👈 cần deep để bắt thay đổi bên trong object
)
onMounted(async () => {
  await fetchListCategory();
  await fetchProductById();
  await fetchListAttribute();
  await fetchListImage();

  observer = new IntersectionObserver(
    (entries) => {
      if (entries[0].isIntersecting) {
        fetchListImage();
      }
    },
    {
      root: scrollContainer.value,
      rootMargin: "100px",
      threshold: 0.1,
    }
  );
  if (loadMoreTrigger.value) {
    observer.observe(loadMoreTrigger.value);
  }
});
onUnmounted(() => {
  if (observer) {
    observer.disconnect();
  }
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
<style scoped>
/* Z-index cao hơn cho modal 2 */
.modal.show:nth-of-type(2) {
  z-index: 1060;
}

/* Backdrop cho modal thứ hai */
.modal-backdrop.show:nth-of-type(2) {
  z-index: 1055;
}
</style>
