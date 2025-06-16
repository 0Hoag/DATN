import { API } from "../domain";
import { axiosCall } from "../plugin/axios";
export const OrderService = {
  fetchListOrder: (params) => {
    return axiosCall.get(API.LIST_ORDER, params);
  },
  createOrder: (params) => {
    return axiosCall.post(API.CREATE_ORDER, params);
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
};
