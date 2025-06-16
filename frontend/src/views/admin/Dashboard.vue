<script setup>
import BarChart from "@/components/BarChart.vue";
import LineChart from "@/components/LineChart.vue";

const revenueChartData = {
  labels: ["Tháng 1", "Tháng 2", "Tháng 3", "Tháng 4", "Tháng 5", "Tháng 6", "Tháng 7", "Tháng 8", "Tháng 9", "Tháng 10", "Tháng 11", "Tháng 12"],
  datasets: [
    {
      label: "Doanh thu (VNĐ)",
      data: [120, 150, 170, 200, 250, 220, 300, 280, 260, 310, 290, 330],
      backgroundColor: "#4e73df",
    },
  ],
};
const revenueChartOptions = {
  reponsive: true,
};
const ordersChartData = {
  labels: ["Tháng 1", "Tháng 2", "Tháng 3", "Tháng 4", "Tháng 5", "Tháng 6", "Tháng 7", "Tháng 8", "Tháng 9", "Tháng 10", "Tháng 11", "Tháng 12"],
  datasets: [
    {
      label: "Số lượng đơn hàng",
      data: [120, 150, 170, 200, 250, 220, 300, 280, 260, 310, 290, 330],
      backgroundColor: "#f6c23e",
    },
  ],
};

const ordersChartOptions = {
  reponsive: true,
};

const productSoldData = {
  labels: ["Tháng 1", "Tháng 2", "Tháng 3", "Tháng 4", "Tháng 5", "Tháng 6", "Tháng 7", "Tháng 8", "Tháng 9", "Tháng 10", "Tháng 11", "Tháng 12"],
  datasets: [
    {
      label: "Sản phẩm đã bán",
      data: [20, 35, 40, 30, 60, 80, 55, 75, 90, 95, 70, 100],
      fill: true,
      borderColor: "#10b981", // xanh lá
      backgroundColor: "rgba(16, 185, 129, 0.2)", // nền mờ
      tension: 0.4,
      pointBackgroundColor: "#10b981",
      pointRadius: 4,
    },
  ],
};

const productSoldOptions = {
  reponsive: true,
};
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
            <div class="fw-bold fs-5">147 890</div>
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
            <div class="fw-bold fs-5">147 789</div>
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
            <div class="fw-bold fs-5">4313</div>
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
            <div class="fw-bold fs-5">890</div>
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
