const DOMAIN = "http://localhost:8080/datn/";

export const API = {
  // Auth
  LOGIN: DOMAIN + "auth/token",
  LOGOUT: DOMAIN + "auth/logout",

  // Account
  LIST_ACCOUNT: DOMAIN + "users",
  CREATE_ACCOUNT: DOMAIN + "users/create",
  UPDATE_ACCOUNT: DOMAIN + "users",
  DELETE_ACCOUNT: DOMAIN + "users",
  DETAIL_ACCOUNT: DOMAIN + "users",
  SEARCH_USER: DOMAIN + "users/search",
  // Category
  LIST_CATEGORY: DOMAIN + "category/List",
  CREATE_CATEGORY: DOMAIN + "category/",
  UPDATE_CATEGORY: DOMAIN + "category",
  DELETE_CATEGORY: DOMAIN + "category",

  // Attribute
  LIST_ATTRIBUTE: DOMAIN + "variantattribute/List",
  CREATE_ATTRIBUTE: DOMAIN + "variantattribute/",
  UPDATE_ATTRIBUTE: DOMAIN + "variantattribute",
  DELETE_ATTRIBUTE: DOMAIN + "variantattribute",
  DELETE_ATTRIBUTE_VALUES: DOMAIN + "variantattributevalue",
  // Review
  LIST_REVIEW: DOMAIN + "review",
  CREATE_REVIEW: DOMAIN + "review",
  PRODUCT_REVIEW: DOMAIN + "review/product",
  UPDATE_REVIEW: DOMAIN + "review",
  DELETE_REVIEW: DOMAIN + "review",
  // Voucher
  LIST_VOUCHER: DOMAIN + "voucher",
  CREATE_VOUCHER: DOMAIN + "voucher",
  UPDATE_VOUCHER: DOMAIN + "voucher",
  DELETE_VOUCHER: DOMAIN + "voucher",
  SEARCH_VOUCHER: DOMAIN + "voucher/search",
  // Order
  LIST_ORDER: DOMAIN + "order",
  CREATE_ORDER: DOMAIN + "order",
  UPDATE_ORDER: DOMAIN + "order",
  DELETE_ORDER: DOMAIN + "order",
  UPDATE_STATUS: DOMAIN + "order/status",
  DETAIL_ORDER: DOMAIN + "order",
  SEARCH_ORDER: DOMAIN + "order/search",

  // order return
  LIST_ORDER_RETURN: DOMAIN + "order-return",
  CREATE_ORDER_RETURN: DOMAIN + "order-return",
  UPDATE_ORDER_RETURN: DOMAIN + "order-return",
  SEARCH_ORDER_RETURN: DOMAIN + "order-return/search",

  // Product
  LIST_PRODUCT: DOMAIN + "product/Get",
  CREATE_PRODUCT: DOMAIN + "product/",
  UPDATE_PRODUCT: DOMAIN + "product",
  DELETE_PRODUCT: DOMAIN + "product",
  DETAIL_PRODUCT: DOMAIN + "product",
  SEARCH_PRODUCT: DOMAIN + "product/search",

  // Product variant
  LIST_PRODUCT_VARIANT: DOMAIN + "product_variant/Get",
  CREATE_PRODUCT_VARIANT: DOMAIN + "product_variant",
  UPDATE_PRODUCT_VARIANT: DOMAIN + "product_variant",
  DELETE_PRODUCT_VARIANT: DOMAIN + "product_variant",

  DELETE_VARIANT_IMAGE: DOMAIN + "product_image",

  // Image
  UPLOAD_IMAGE: DOMAIN + "upload-image/",
  DELETE_IMAGES: DOMAIN + "upload-image/",
  LIST_IMAGE: DOMAIN + "upload_image/list",
  // Setting

  // Role
  LIST_ROLE: DOMAIN + "roles",
  CREATE_ROLE: DOMAIN + "roles/list",
  UPDATE_ROLE: DOMAIN + "roles/list",
  DELETE_ROLE: DOMAIN + "roles/list",

  // Adress
  LIST_ADDRESS_BY_USER: DOMAIN +  "address",
  CREATE_ADDRESS_BY_USER: DOMAIN +  "address",
};
