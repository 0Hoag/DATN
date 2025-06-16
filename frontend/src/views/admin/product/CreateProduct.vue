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
                <input v-model="productModel.description" name="description" class="form-control" placeholder="Mô tả ngắn về sản phẩm" required />
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
              <a-select v-model:value="selectedBrand" class="w-100" show-search placeholder="Chọn thương hiệu" :options="barnds" :filter-option="filterBrand"></a-select>
            </div>
            <div class="mb-3">
              <label for="thumbnail" class="form-label fw-bold">Thumnail</label>
              <br />
              <img src="..." class="img-thumbnail rounded" alt="..." />
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
              <a-select v-model:value="selectedValues[attr]" mode="multiple" style="width: 100%" :placeholder="`Chọn giá trị cho ${attr}`">
                <a-select-option v-for="val in getAttributeValues(attr)" :key="val.value" :value="val.value">
                  {{ val.value }}
                </a-select-option>
              </a-select>
            </div>

            <!-- Tạo biến thể -->
            <a-button type="primary" class="my-3" @click="generateVariants">Tạo biến thể</a-button>

            <!-- Danh sách biến thể -->
            <a-collapse accordion>
              <a-collapse-panel v-for="(variant, index) in variants" :key="index" :header="variant.title">
                <div class="row g-3">
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
</template>

<script setup>
import { handleError, hideLoading, showLoading } from "@/api/functions/common";
import { computed, watch, ref, reactive, onMounted } from "vue";
import { ProductService } from "@/api/service/ProductService";
import { toast } from "vue3-toastify";
import { CategoryService } from "@/api/service/CategoryService";
import { AttributeService } from "@/api/service/AttributeService";

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

async function fetchListCategory() {
  try {
    const response = await CategoryService.fetchListCategory();
    categories.value = response.result.filter((item) => item.children.length === 0).map((item) => ({ value: item.id, label: item.name }));
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
  return { ...productModel.value, productVariants: variants.value, category: selectedCategory.value, brand: selectedBrand.value };
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
    };
  });
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
});
</script>
