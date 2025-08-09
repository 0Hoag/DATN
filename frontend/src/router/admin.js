import Layout from "@/layouts/admin/Layout.vue";
import Categoires from "@/views/admin/category/Categoires.vue";
import Dashboard from "@/views/admin/Dashboard.vue";
import Orders from "@/views/admin/order/Orders.vue";
import Products from "@/views/admin/product/Products.vue";
import Reviews from "@/views/admin/review/Reviews.vue";
import Setting from "@/views/admin/Setting.vue";
import Users from "@/views/admin/account/Users.vue";
import Vouchers from "@/views/admin/voucher/Vouchers.vue";
import CreateOrder from "@/views/admin/order/CreateOrder.vue";
import EditOrder from "@/views/admin/order/EditOrder.vue";
import CreateProduct from "../views/admin/product/CreateProduct.vue";
import EditProduct from "@/views/admin/product/EditProduct.vue";
import VariantAttribute from "@/views/admin/product/VariantAttribute.vue";
import ReturnOrder from "@/views/admin/order/ReturnOrder.vue";
import { all } from "axios";
import MyProfile from "@/views/admin/profile/MyProfile.vue";

const admin = [
  {
    path: "/admin",
    component: Layout,
    children: [
      {
        path: "dashboard",
        name: "dashboard",
        component: Dashboard,
        meta: { title: "Dashboard" },
      },
      {
        path: "categories",
        name: "categories",
        component: Categoires,
        meta: { title: "Quản lý danh mục" ,
           allowedRoles: ["ROLE_ADMIN","ROLE_MANAGER" ],
        },
      },
      {
        path: "products",
        name: "products",
        component: Products,
        meta: { title: "Quản lý sản phẩm" },
      },
      {
        path: "products/create-product",
        name: "product-create",
        component: CreateProduct,
        meta: { title: "Tạo sản phẩm",
           allowedRoles: ["MANAGE_PRODUCTS" ],
         },
      },
      {
        path: "products/edit-product/:id",
        name: "product-edit",
        component: EditProduct,
        meta: { title: "Chỉnh sửa sản phẩm" ,
           allowedRoles: ["MANAGE_PRODUCTS" ],
        },
      },
      {
        path: "products/variant-attribute",
        name: "variant-attribute",
        component: VariantAttribute,
        meta: { title: "Thuộc tính biến thể" ,
            allowedRoles: ["MANAGE_PRODUCTS" ],
        },
      },

      {
        path: "users",
        name: "users",
        component: Users,
        meta: { title: "Quản lý người dùng" ,
            allowedRoles: ["MANAGE_USERS"],
        },
      },
      {
        path: "orders",
        name: "orders",
        component: Orders,
        meta: { title: "Quản lý đơn hàng" },
      },
      {
        path: "orders/create-order",
        name: "order-create",
        component: CreateOrder,
        meta: {
          title: "Tạo đơn hàng",
        },
      },
      {
        path: "orders/edit-order/:id",
        name: "order-edit",
        component: EditOrder,
        meta: {
          title: "Chỉnh sửa đơn hàng",
        },
      },
      {
        path: "orders/return-order",
        name: "order-return",
        component: ReturnOrder,
        meta: { title: "Danh sách trả hàng" },
      },
      {
        path: "setting",
        name: "setting",
        component: Setting,
        meta: { title: "Setting" },
      },
      {
        path: "reviews",
        name: "reviews",
        component: Reviews,
        meta: {
          title: "Quản lý đánh giá sản phẩm",
          allowedRoles: ["ROLE_ADMIN", "ROLE_MANAGER"],
        },
      },
      {
        path: "vouchers",
        name: "vouchers",
        component: Vouchers,
        meta: { title: "Quản lý voucher" },
      },
      {
        path: "profile",
        name: "profile",
        component: MyProfile,
        meta: { title: "Hồ sơ của người dùng" },
      },
    ],
    meta: {
      allowedRoles: ["ROLE_ADMIN", "ROLE_MANAGER", "ROLE_SHIFT_STAFF"],
    },
  },
];

export default admin;
