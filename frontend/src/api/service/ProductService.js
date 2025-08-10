import { API } from "../domain";
import { axiosCall } from "../plugin/axios";
export const ProductService = {
    fetchListProduct: (params) => {
        return axiosCall.get(API.LIST_PRODUCT, params);
    },
    fetchListProductSale: (params) => {
        return axiosCall.get(API.LIST_PRODUCT_SALE, params);
    },
    fetchListProductBySlugCategory: (slugCategory,params) => {
        return axiosCall.get(`${API.LIST_PRODUCT_BY_SLUG_CATEGORY}/${slugCategory}`, params);
    },
    createProduct: (params) => {
        return axiosCall.post(API.CREATE_PRODUCT, params);
    },
    updateProduct: (id, params) => {
        return axiosCall.put(`${API.UPDATE_PRODUCT}/${id}`, params);
    },
    deleteProduct: (id) => {
        return axiosCall.delete(`${API.DELETE_PRODUCT}/${id}`);
    },
    detailProduct: (id) => {
        return axiosCall.get(`${API.DETAIL_PRODUCT}/${id}`);
    },
    detailProductBySlug: (id) => {
        return axiosCall.get(`${API.DETAIL_PRODUCT_BY_SLUG}/${id}`);
    },
    searchProduct: (params) => {
        return axiosCall.get(API.SEARCH_PRODUCT, params);
    },
    searchProductForUser: (params) => {
        return axiosCall.get(API.SEARCH_PRODUCT_FOR_USER, params);
    },
    fetchListProductVariant: (params) => {
        return axiosCall.get(API.LIST_PRODUCT_VARIANT, params);
    },
    createProductVariant: (params) => {
        return axiosCall.post(API.CREATE_PRODUCT_VARIANT, params);
    },
    updateProductVariant: (id, params) => {
        return axiosCall.put(`${API.UPDATE_PRODUCT_VARIANT}/${id}`, params);
    },
    deleteProductVariant: (id) => {
        return axiosCall.delete(`${API.DELETE_PRODUCT_VARIANT}/${id}`);
    },
    deleteVariantImage: (params) => {
        return axiosCall.delete(`${API.DELETE_VARIANT_IMAGE}`,params);
    },
    searchProductVariant: (params) => {
        return axiosCall.get(API.SEARCH_PRODUCT_VARIANT, params);
    },
    filter: (params) => {
        return axiosCall.get(API.FILTER, params);
    },

};
