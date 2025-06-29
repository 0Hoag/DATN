<template>
  <a-layout-sider
    collapsible
    :collapsed="collapsed"
    @collapse="collapsed = $event"
    

  >
    <div class="logo" ></div>
    <a-menu
      theme="dark"
      mode="inline"
      :selected-keys="[selectedKey]"
      @click="handleMenuClick"
    >
      <a-menu-item key="dashboard">
        <template #icon>
          <font-awesome-icon icon="gauge-high" />
        </template>
        <span>Dashboard</span>
      </a-menu-item>
      <a-menu-item key="categories">
        <template #icon>
          <font-awesome-icon icon="tags" />
        </template>
        <span>Danh mục</span>
      </a-menu-item>

      <a-sub-menu>
        <template #icon>
          <font-awesome-icon icon="box" />
        </template>
        <template #title> Sản phẩm </template>
        <a-menu-item key="products">Tất cả sản phẩm</a-menu-item>
        <a-menu-item key="product-create">Thêm sản phẩm</a-menu-item>
        <a-menu-item key="variant-attribute">Các thuộc tính</a-menu-item>
      </a-sub-menu>

      <a-menu-item key="users">
        <template #icon>
          <font-awesome-icon icon="users" />
        </template>
        <span>Người dùng</span>
      </a-menu-item>

      <a-sub-menu>
        <template #icon>
          <font-awesome-icon icon="cart-shopping" />
        </template>
        <template #title>
          <span>Đơn hàng</span>
        </template>
        <a-menu-item key="orders"> Tất cả đơn hàng </a-menu-item>
        <a-menu-item key="order-create"> Thêm đơn hàng </a-menu-item>
        <a-menu-item key="order-return"> Trả hàng </a-menu-item>
      </a-sub-menu>

      <a-menu-item key="reviews">
        <template #icon>
          <font-awesome-icon icon="comment" />
        </template>
        <span>Đánh giá</span>
      </a-menu-item>
      <a-menu-item key="vouchers">
        <template #icon>
          <font-awesome-icon icon="ticket" />
        </template>
        <span>Voucher</span>
      </a-menu-item>
      <a-menu-item key="setting">
        <template #icon>
          <font-awesome-icon icon="gear" />
        </template>
        <span>Cài đặt</span>
      </a-menu-item>
    </a-menu>
  </a-layout-sider>
</template>

<script>
export default {
  name: "AppSidebar",
  data() {
    return {
      collapsed: false,
    };
  },
  computed: {
    selectedKey() {
      return this.$route.name;
    },
  },
  methods: {
    handleMenuClick({ key }) {
      if (key !== this.$route.path) {
        this.$router.push({ name: key });
      }
    },
  },
};
</script>
