import { API } from "../domain";
import { axiosCall } from "../plugin/axios";
export const OrderService = {
  fetchListOrder: (params) => {
    return axiosCall.get(API.LIST_ORDER, params);
  },
  createOrder: (params) => {
    return axiosCall.post(API.CREATE_ORDER, params);
  },
  createOrderFromUser: (params) => {
    return axiosCall.post(API.CREATE_ORDER_FORM_USER, params);
  },
  updateOrder: (id, params) => {
    return axiosCall.put(`${API.UPDATE_ORDER}/${id}`, params);
  },
  deleteOrder: (id) => {
    return axiosCall.delete(`${API.DELETE_ORDER}/${id}`);
  },
  updateStatus: (id, params) => {
    return axiosCall.put(`${API.UPDATE_STATUS}/${id}`, params);
  },
  detailOrder: (id) => {
    return axiosCall.get(`${API.DETAIL_ORDER}/${id}`);
  },
  searchOrder: (params) => {
    return axiosCall.get(API.SEARCH_ORDER, params);
  },
  cancelOrder: (id, params) => {
    return axiosCall.post(`${API.CANCEL_ORDER_BY_CLIENT}/${id}`,params);
  },
  cancelOrderByAdmin: (id,params) => {
    return axiosCall.post(`${API.CANCEL_ORDER_BY_ADMIN}/${id}`,params);
  },
  fetchOrdersByUser: (id, params) => {
    return axiosCall.get(`${API.LIST_ORDER_BY_USER}/${id}`,params);
  },
  // order return
  fetchListOrderReturn: (params) => {
    return axiosCall.get(API.LIST_ORDER_RETURN, params);
  },
  createOrderReturn: (params) => {
    return axiosCall.post(API.CREATE_ORDER_RETURN, params);
  },
  updateOrderReturn: (id, params) => {
    return axiosCall.put(`${API.UPDATE_ORDER_RETURN}/${id}`, params);
  },
  searchOrderReturn: (params) => {
    return axiosCall.get(API.SEARCH_ORDER, params);
  },
};
