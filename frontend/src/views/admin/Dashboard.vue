<script setup>
import { DashboardService } from "@/api/service/DashboardService";
import BarChart from "@/components/BarChart.vue";
import LineChart from "@/components/LineChart.vue";
import { onMounted, ref, computed } from "vue";

const totalUsers = ref(0);
const totalOrders = ref(0);
const totalRevenue = ref(0);
const totalProductsSold = ref(0);
const topProducts = ref([]);

const revenueData = ref([]);
const productSoleData = ref([]);
const orderSoldData = ref([]);


const revenueChartData = computed(() => ({
  labels: ["Tháng 1", "Tháng 2", "Tháng 3", "Tháng 4", "Tháng 5", "Tháng 6", "Tháng 7", "Tháng 8", "Tháng 9", "Tháng 10", "Tháng 11", "Tháng 12"],
  datasets: [
    {
      label: "Doanh thu (VNĐ)",
      data: revenueData.value,
      backgroundColor: "#4e73df",
    },
  ],
}));

const productSoldData = computed(() => ({
  labels: ["Tháng 1", "Tháng 2", "Tháng 3", "Tháng 4", "Tháng 5", "Tháng 6", "Tháng 7", "Tháng 8", "Tháng 9", "Tháng 10", "Tháng 11", "Tháng 12"],
  datasets: [
    {
      label: "Sản phẩm đã bán",
      data: productSoleData.value,
      fill: true,
      borderColor: "#10b981", // xanh lá
      backgroundColor: "rgba(16, 185, 129, 0.2)", // nền mờ
      tension: 0.4,
      pointBackgroundColor: "#10b981",
      pointRadius: 4,
    },
  ],
}));

const ordersChartData = computed(() => ({
  labels: ["Tháng 1", "Tháng 2", "Tháng 3", "Tháng 4", "Tháng 5", "Tháng 6", "Tháng 7", "Tháng 8", "Tháng 9", "Tháng 10", "Tháng 11", "Tháng 12"],
  datasets: [
    {
      label: "Số lượng đơn hàng",
      data: orderSoldData.value,
      backgroundColor: "#f6c23e",
    },
  ],
}));

const formattedTotalRevenue = computed(() => {
  const value = Number(totalRevenue.value);
  if (isNaN(value) || value == null) {
    return "0";
  }
  return new Intl.NumberFormat('vi-VN').format(value);
});


const revenueChartOptions = {responsive: true,};
const productSoldOptions = {responsive: true,};
const ordersChartOptions = {responsive: true,};

// tổng doanh thu theo năm
async function getChartRevenue() {
  try {
    const response = await DashboardService.getChartRevenue();
    const rawData = response.result;
    revenueData.value = normalizeMonthlyData(rawData, item => item.value);
    console.log("Revenue Chart Data:", revenueData);
  } catch (error) {
    console.error("Error fetching revenue chart data:", error);
  }
}

// số lượng sản phẩm bán được theo năm (12 tháng))
async function getChartProductSold() {
  try {
    const response = await DashboardService.getChartProduct();
    const rawData = response.result;
    productSoleData.value = normalizeMonthlyData(rawData, item => item.value);
    console.log("Product Sold Chart Data:", productSoleData);
  } catch (error) {
    console.error("Error fetching product sold chart data:", error);
  }
}

// số lượng đơn hàng bán được theo năm  (12 tháng)
async function getChartOrders() {
  try {
    const response = await DashboardService.getChartOrder();
    const rawData = response.result;
    orderSoldData.value = normalizeMonthlyData(rawData, item => item.value);
    console.log("Orders Chart Data:", orderSoldData);
  } catch (error) {
    console.error("Error fetching orders chart data:", error);
  }
}

function normalizeMonthlyData(rawData, getValue){
  return Array.from({ length: 12 }, (_, i) => {
     const month = i + 1;
    const entry = rawData.find(item => item.month === month);
    return entry ? getValue(entry) : 0;
  });
}

async function getTotalUsers() {
  try {
    const response = await DashboardService.getTotalUsers();
    totalUsers.value = response.result;
    console.log("Total Users:", totalUsers.value);
  } catch (error) {
    console.error("Error fetching total users:", error);
  }
}
async function getTotalOrders() {
  try {
    const response = await DashboardService.getTotalOrders();
  totalOrders.value = response.result;
    console.log("Total Orders:", totalOrders.value);
  } catch (error) {
    console.error("Error fetching total orders:", error);
  }
}
async function getTotalRevenue() {
  try {
    const response = await DashboardService.getTotalRevenue();
    totalRevenue.value = response.result;
    console.log("Total Revenue:", totalRevenue.value);
  } catch (error) {   
    console.error("Error fetching total revenue:", error);
  }
}

async function getTotalProductsSold() {
  try {
    const response = await DashboardService.getTotalProductsSold();
    totalProductsSold.value = response.result;
    console.log("Total Products Sold:", totalProductsSold.value);
  } catch (error) {
    console.error("Error fetching total products sold:", error);
  }
}

async function getTop10Products() {
  try {
    const response = await DashboardService.getTopProduct({
      year: 2025,
    });
    topProducts.value = response.result;
    console.log("Top Products:", topProducts.value);
  } catch (error) {
    console.error("Error fetching top products:", error);
  }
}




onMounted(() => {
  getTotalUsers();
  getTotalOrders();
  getTotalRevenue();
  getTotalProductsSold();
  getTop10Products();
  getChartRevenue();
  getChartProductSold();
  getChartOrders();
});
</script>

<template>
  <h1>Dashboard</h1>

  <!-- Các thẻ thống kê -->
  <div class="d-flex gap-4 flex-wrap justify-content-center">
    <!-- Card 1 - Sản phẩm bán ra -->
    <div class="card shadow border-0" style="width: 250px">
      <div class="card-body">
        <div class="d-flex align-items-center">
          <div class="rounded-circle d-flex justify-content-center align-items-center me-3" style="background-color: #fff0f0; width: 60px; height: 60px">
            <font-awesome-icon icon="box" style="color: red; font-size: 24px" />
          </div>
          <div>
            <div class="fw-bold fs-5">{{totalProductsSold}}</div>
            <div class="text-muted small">Lượng sản phẩm bán ra</div>
          </div>
        </div>
      </div>
    </div>

    <!-- Card 2 - Doanh thu -->
    <div class="card shadow border-0" style="width: 250px">
      <div class="card-body">
        <div class="d-flex align-items-center">
          <div class="rounded-circle d-flex justify-content-center align-items-center me-3" style="background-color: #f0f8f0; width: 60px; height: 60px">
            <font-awesome-icon icon="dollar-sign" style="color: green; font-size: 24px" />
          </div>
          <div>
            <div class="fw-bold fs-5">{{ formattedTotalRevenue }}</div>
            <div class="text-muted small">Doanh thu</div>
          </div>
        </div>
      </div>
    </div>

    <!-- Card 3 - Số đơn hàng -->
    <div class="card shadow border-0" style="width: 250px">
      <div class="card-body">
        <div class="d-flex align-items-center">
          <div class="rounded-circle d-flex justify-content-center align-items-center me-3" style="background-color: #e6f0ff; width: 60px; height: 60px">
            <font-awesome-icon icon="cart-shopping" style="color: #007bff; font-size: 24px" />
          </div>
          <div>
            <div class="fw-bold fs-5">{{totalOrders}}</div>
            <div class="text-muted small">Số đơn hàng</div>
          </div>
        </div>
    </div>
    </div>

    <!-- Card 4 - Khách hàng -->
    <div class="card shadow border-0" style="width: 250px">
      <div class="card-body">
        <div class="d-flex align-items-center">
          <div class="rounded-circle d-flex justify-content-center align-items-center me-3" style="background-color: #fff0f0; width: 60px; height: 60px">
            <font-awesome-icon icon="users" style="color: #e83e8c; font-size: 24px" />
          </div>
          <div>
            <div class="fw-bold fs-5">{{totalUsers}}</div>
            <div class="text-muted small">Khách hàng</div>
          </div>
        </div>
      </div>
    </div>
  </div>

  <div class="row my-4">
    <div class="col-8">
      <div class="card shadow border-0">
        <div class="card-body">
          <strong>Thống kê doanh thu</strong>
          <BarChart :chart-data="revenueChartData" :chart-options="revenueChartOptions" />
        </div>
      </div>
    </div>
    <div class="col-4">
      <div class="card shadow border-0">
        <div class="card-body">
          <strong>Top 10 sản phẩm bán chạy</strong>
          <div class="card border-1 my-2" v-for="(product, index) in topProducts" :key="index">
            <div class="card-body row">
             <div class="col-3">
              <img :src="product.thumbnail" alt="" class="object-fit-contain w-100" >
              </div>
             <div class="col-9"> 
             <h5 class="card-title">{{ product.productName }} <span class="badge text-bg-secondary">{{ product.quantitySold }}</span></h5>
             </div>
            </div>
          </div>
        </div>
      </div>
    </div>
  </div>
  <div class="row my-4">
    <div class="col-6">
      <div class="card shadow border-0">
        <div class="card-body">
          <strong>Thống kê số lượng sản phẩm bán ra</strong>
          <LineChart :chart-data="productSoldData" :chart-options="productSoldOptions" />
        </div>
      </div>
    </div>
    <div class="col-6">
      <div class="card shadow border-0">
        <div class="card-body">
          <strong>Thống kê số lượng đơn hàng</strong>
          <BarChart :chart-data="ordersChartData" :chart-options="ordersChartOptions" />
        </div>
      </div>
    </div>
  </div>
</template>
