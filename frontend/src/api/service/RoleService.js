import { API } from "../domain";
import { axiosCall } from "../plugin/axios";
export const RoleService = {
  fetchListRole: (params) => {
    return axiosCall.get(API.LIST_ROLE, params);
  },
  createRole: (params) => {
    return axiosCall.post(API.CREATE_ROLE, params);
  },
  updateRole: (id, params) => {
    return axiosCall.put(`${API.UPDATE_ROLE}/${id}`, params);
  },
  deleteRole: (id) => {
    return axiosCall.delete(`${API.DELETE_ROLE}/${id}`);
  },
};
