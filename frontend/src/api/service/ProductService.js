import { API } from "../domain";
import { axiosCall } from "../plugin/axios";
export const ProductService = {
    fetchListProduct: (params) => {
        return axiosCall.get(API.LIST_PRODUCT, params);
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
    searchProduct: (params) => {
        return axiosCall.get(API.SEARCH_PRODUCT, params);
    },
    fetchListProductVariant: (params) => {
        return axiosCall.get(API.LIST_PRODUCT_VARIANT, params);
    },
};
