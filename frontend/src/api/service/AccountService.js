import { API } from "../domain";
import { axiosCall } from "../plugin/axios";

export const AccountService = {
  fetchListAccount: (params) => {
    return axiosCall.get(API.LIST_ACCOUNT, params);
  },
  fetchListAccountForOrder: (params) => {
    return axiosCall.get(API.LIST_ACCOUNT_FOR_ORDER, params);
  },
  createAccount: (params) => {
    return axiosCall.post(API.CREATE_ACCOUNT, params);
  },
  updateAccount: (id, params) => {
    return axiosCall.put(`${API.UPDATE_ACCOUNT}/${id}`, params);
  },
  updateProfile: (id, params) => {
    return axiosCall.put(`${API.UPDATE_PROFILE}/${id}`, params);
  },
  deleteAccount: (id) => {
    return axiosCall.delete(`${API.DELETE_ACCOUNT}/${id}`);
  },
  blockUser: (id, data) => {
    return axiosCall.delete(`${API.BLOCK_ACCOUNT}/${id}`,{data});
  },
  restoreUser: (id) => {
    return axiosCall.post(`${API.RESTORE_ACCOUNT}/${id}`);
  },
  login: (params) => {
    return axiosCall.post(API.LOGIN, params);
  },
  logout: (params) => {
    return axiosCall.post(API.LOGOUT, params);
  },
  // searchUser: (params) => {
  //   return axiosCall.get(API.LIST_ACCOUNT, params);
  // },
  getUserInfo: () => {
    return axiosCall.get(`${API.MY_INFO}`);
  },
  searchUser: (params) => {
    return axiosCall.get(`${API.SEARCH_USER}`, params);
  },
  changePassword: (params) => {
    return axiosCall.put(API.CHANGE_PASSWORD, params);
  },
  registerAccount: (params) => {
    return axiosCall.post(API.REGISTER, params);
  },
};
