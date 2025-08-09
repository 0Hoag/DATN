import { API } from "../domain";
import { axiosCall } from "../plugin/axios";
export const AttributeService = {
  fetchListAttribute: (params) => {
    return axiosCall.get(API.LIST_ATTRIBUTE, params);
  },
  createAttribute: (params) => {
    return axiosCall.post(API.CREATE_ATTRIBUTE, params);
  },
  updateAttribute: (id, params) => {
    return axiosCall.put(`${API.UPDATE_ATTRIBUTE}/${id}`, params);
  },

  deleteAttribute: (id) => {
    return axiosCall.delete(`${API.DELETE_ATTRIBUTE}/${id}`);
  },
  deleteAttributeValues: (id) => {
    return axiosCall.delete(`${API.DELETE_ATTRIBUTE_VALUES}/${id}`);
  },
};
