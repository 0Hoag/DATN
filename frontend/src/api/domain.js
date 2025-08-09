import Register from "@/views/user/Register.vue";

const DOMAIN = "http://localhost:8080/datn/";

export const API = {
  // Auth
  LOGIN: DOMAIN + "auth/token",
  LOGOUT: DOMAIN + "auth/logout",

  // Account

  LIST_ACCOUNT: DOMAIN + "users/getAll",
  LIST_ACCOUNT_FOR_ORDER: DOMAIN + "users",
  CREATE_ACCOUNT: DOMAIN + "users/create",
  UPDATE_ACCOUNT: DOMAIN + "users",
  UPDATE_PROFILE: DOMAIN + "users/profile",
  DELETE_ACCOUNT: DOMAIN + "users",
  BLOCK_ACCOUNT: DOMAIN + "users",
  RESTORE_ACCOUNT: DOMAIN + 'users/restore',
  DETAIL_ACCOUNT: DOMAIN + "users",
  SEARCH_USER: DOMAIN + "users/search",
  MY_INFO: DOMAIN + "users/my-info",
  CHANGE_PASSWORD: DOMAIN + "users/password",
  REGISTER: DOMAIN + "users/registration",
  // Category
  GET_CATEGORY: DOMAIN + "category/Get",
  LIST_CATEGORY: DOMAIN + "category/List",
  CREATE_CATEGORY: DOMAIN + "category/",
UPDATE_CATEGORY: DOMAIN + "category",
  DELETE_CATEGORY: DOMAIN + "category",
  SEARCH_CATEGORY: DOMAIN + "category/search",
  DETAIL_CATEGORY_BY_SLUG: DOMAIN + "category/detail",
  DETAIL_CATEGORY_BY_ID: DOMAIN + "category",
  // Attribute
  LIST_ATTRIBUTE: DOMAIN + "variantattribute/List",
  CREATE_ATTRIBUTE: DOMAIN + "variantattribute/",
  UPDATE_ATTRIBUTE: DOMAIN + "variantattribute",
  DELETE_ATTRIBUTE: DOMAIN + "variantattribute",
  DELETE_ATTRIBUTE_VALUES: DOMAIN + "variantattributevalue",
  // Review
  LIST_REVIEW: DOMAIN + "reviews/admin/all",
  CREATE_REVIEW: DOMAIN + "reviews",
  PRODUCT_REVIEW: DOMAIN + "reviews/product",
  UPDATE_REVIEW: DOMAIN + "reviews",
  DELETE_REVIEW: DOMAIN + "reviews",
  DETAIL_PRODUCT_REVIEW: DOMAIN + "reviews/product",
  SEARCH_REVIEW_BY_PRODUCT_NAME: DOMAIN + "reviews/admin/search",

  // Voucher
  LIST_VOUCHER: DOMAIN + "voucher",
  CREATE_VOUCHER: DOMAIN + "voucher",
  UPDATE_VOUCHER: DOMAIN + "voucher",
  DELETE_VOUCHER: DOMAIN + "voucher",
  SEARCH_VOUCHER: DOMAIN + "voucher/search",
  CAN_USE_VOUCHER: DOMAIN + 'user-vouchers/can-use',
  
  // Order
  LIST_ORDER: DOMAIN + "order",
  CREATE_ORDER: DOMAIN + "order",
  CREATE_ORDER_FORM_USER: DOMAIN + "order/cart",
  UPDATE_ORDER: DOMAIN + "order",
  DELETE_ORDER: DOMAIN + "order",
  UPDATE_STATUS: DOMAIN + "order/status",
  DETAIL_ORDER: DOMAIN + "order",
  SEARCH_ORDER: DOMAIN + "order/search",
  CANCEL_ORDER_BY_CLIENT: DOMAIN + "order/cancel",
  CANCEL_ORDER_BY_ADMIN: DOMAIN + "order/admin/cancel",
  LIST_ORDER_BY_USER: DOMAIN + 'order/user',


  // order return
  LIST_ORDER_RETURN: DOMAIN + "order-return",
  CREATE_ORDER_RETURN: DOMAIN + "order-return",
  UPDATE_ORDER_RETURN: DOMAIN + "order-return",
  SEARCH_ORDER_RETURN: DOMAIN + "order-return/search",

  // Product
  LIST_PRODUCT: DOMAIN + "product/Get",
  LIST_PRODUCT_SALE: DOMAIN + 'product/sale',
  LIST_PRODUCT_BY_SLUG_CATEGORY: DOMAIN + 'product/category',
  CREATE_PRODUCT: DOMAIN + "product/",
  UPDATE_PRODUCT: DOMAIN + "product",
  DELETE_PRODUCT: DOMAIN + "product",
  DETAIL_PRODUCT: DOMAIN + "product",
  DETAIL_PRODUCT_BY_SLUG: DOMAIN + "product/detail",
  SEARCH_PRODUCT: DOMAIN + "product/search",
  FILTER: DOMAIN + 'product/filter',
  // Product variant
  LIST_PRODUCT_VARIANT: DOMAIN + "product_variant/Get",
  CREATE_PRODUCT_VARIANT: DOMAIN + "product_variant",
  UPDATE_PRODUCT_VARIANT: DOMAIN + "product_variant",
  DELETE_PRODUCT_VARIANT: DOMAIN + "product_variant",

  DELETE_VARIANT_IMAGE: DOMAIN + "product_image",
  SEARCH_PRODUCT_VARIANT: DOMAIN + "product_variant/search",

  // Image
  UPLOAD_IMAGE: DOMAIN + "upload_image/",
  DELETE_IMAGES: DOMAIN + "upload_image/",
  LIST_IMAGE: DOMAIN + "upload_image/list",
  // Setting

  // Role
  LIST_ROLE: DOMAIN + "roles",
  CREATE_ROLE: DOMAIN + "roles/list",
  UPDATE_ROLE: DOMAIN + "roles/list",
  DELETE_ROLE: DOMAIN + "roles/list",

  // Adress
  LIST_ADDRESS_BY_USER: DOMAIN + "address",
  CREATE_ADDRESS_BY_USER: DOMAIN + "address",
  DELETE_ADDRESS_BY_USER: DOMAIN + "address",
  //forgot password
  VERIFY_EMAIL: DOMAIN + "forgotPassword/verifyMail",
  VERIFY_OTP: DOMAIN + "forgotPassword/verifyOtp",
  RESET_PASSWORD: DOMAIN + "forgotPassword/changePassword",

  // dashboard
  TOTAL_USER: DOMAIN + "dashboard/total-users",
  TOTAL_ORDER: DOMAIN + "dashboard/total-orders",
  TOTAL_REVENUE: DOMAIN + "dashboard/total-revenue",
  TOTAL_PRODUCT_SOLD: DOMAIN + "dashboard/total-products-sold",
  TOP_PRODUCT: DOMAIN + "dashboard/top-products",
  CHART_REVENUE: DOMAIN + "dashboard/monthly-revenue",
  CHART_ORDER: DOMAIN + "dashboard/monthly-order",
  CHART_PRODUCT: DOMAIN + "dashboard/monthly-product-sold",

  // cart
  GET_SESSTION_CART: DOMAIN + 'cart',
  ADD_CART: DOMAIN + 'cart/add',
  UPDATE_CART: DOMAIN + 'cart/update',
  DELETE_ITEM: DOMAIN + 'cart',

  // payment
  GET_PAYMENT_METHOD: DOMAIN + 'payment',
  VNPAY_RETURN: DOMAIN + 'payment/vnpay-return'
};
