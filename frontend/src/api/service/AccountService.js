import { API } from "../domain";
import { axiosCall } from "../plugin/axios";

export const AccountService = {
  fetchListAccount: (params) => {
    return axiosCall.get(API.LIST_ACCOUNT, params);
  },
  createAccount: (params) => {
    return axiosCall.post(API.CREATE_ACCOUNT, params);
  },
  updateAccount: (id, params) => {
    return axiosCall.put(`${API.UPDATE_ACCOUNT}/${id}`, params);
  },
  deleteAccount: (id) => {
    return axiosCall.delete(`${API.DELETE_ACCOUNT}/${id}`);
  },
  login: (params) => {
    return axiosCall.post(API.LOGIN, params);
  },
  logout: (params) => {
    return axiosCall.post(API.LOGOUT, params);
  },
};
