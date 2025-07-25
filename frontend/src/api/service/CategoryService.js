import { API } from "../domain";
import { axiosCall } from "../plugin/axios";
export const CategoryService = {
  fetchListCategory: (params) => {
    return axiosCall.get(API.GET_CATEGORY, params);
  },
  fetchListCategoryForUser: (params) => {
    return axiosCall.get(API.LIST_CATEGORY, params);
  },
  createCategory: (params) => {
    return axiosCall.post(API.CREATE_CATEGORY, params);
  },
  updateCategory: (id, params) => {
    return axiosCall.put(`${API.UPDATE_CATEGORY}/${id}`, params);
  },

  deleteCategory: (id) => {
    return axiosCall.delete(`${API.DELETE_CATEGORY}/${id}`);
  },
   searchCategory: (params) => {
    return axiosCall.get(API.SEARCH_CATEGORY, params);
  },
};
