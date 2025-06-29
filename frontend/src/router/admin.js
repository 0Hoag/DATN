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
 
const admin = [
 { path: "/", redirect: { name: "dashboard" } },
 {
  path: "/admin",
  component: Layout,
  children: [
   {
    path: "dashboard",
    name: "dashboard",
    component: Dashboard,

   },
   {
    path: "categories",
    name: "categories",
    component: Categoires,
   },
   {
    path: "products",
    name: "products",
    component: Products,
   },
   {
    path: "products/create-product",
    name: "product-create",
    component: CreateProduct,
   },
   {
    path: "products/edit-product/:id",
    name: "product-edit",
    component: EditProduct,
   },
   {
    path: "products/variant-attribute",
    name: "variant-attribute",
    component: VariantAttribute,
   },

   {
    path: "users",
    name: "users",
    component: Users,
   },
   {
    path: "orders",
    name: "orders",
    component: Orders,
   },
   {
    path: "orders/create-order",
    name: "order-create",
    component: CreateOrder,
   },
   {
    path: "orders/edit-order/:id",
    name: "order-edit",
    component: EditOrder,
   },
   {
    path: "orders/return-order",
    name: "order-return",
    component: ReturnOrder,
   },
   {
    path: "setting",
    name: "setting",
    component: Setting,
   },
   {
    path: "reviews",
    name: "reviews",
    component: Reviews,
   },
   {
    path: "vouchers",
    name: "vouchers",
    component: Vouchers,
   },
  ],
  meta: {
    allowedRoles: ["ROLE_ADMIN", "ROLE_MANAGER"]
  }
 },
];


export default admin;
