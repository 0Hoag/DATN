import { API } from "../domain";
import { axiosCall } from "../plugin/axios";
export const CartService = {
  getSession: (params) => {
    return axiosCall.get(API.GET_SESSTION_CART, params);
  },
  addCart: (params) => {
    return axiosCall.post(API.ADD_CART, params);
  },
  updateCart: (params) => {
    return axiosCall.put(API.UPDATE_CART, params);
  },

  deletItem: (id) => {
    return axiosCall.delete(`${API.DELETE_ITEM}/${id}`);
  },

};
