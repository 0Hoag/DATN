import { API } from "../domain";
import { axiosCall } from "../plugin/axios";
export const ReviewService = {
  fetchListReview: (params) => {
    return axiosCall.get(API.LIST_REVIEW, params);
  },

  fetchListReviewByProduct: (id, params) => {
    return axiosCall.get(`${API.DETAIL_PRODUCT_REVIEW}/${id}`, params);
  },

  createReview: (params) => {
    return axiosCall.post(API.CREATE_REVIEW, params);
  },

  updateReview: (id, params) => {
    return axiosCall.put(`${API.UPDATE_REVIEW}/${id}`, params);
  },

  deleteReview: (id) => {
    return axiosCall.delete(`${API.DELETE_REVIEW}/${id}`);
  },
  
  searchReviewByProductName: (params) => {
    return axiosCall.get(API.SEARCH_REVIEW_BY_PRODUCT_NAME, params);
  },

};
