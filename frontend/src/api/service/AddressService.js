import { API } from "../domain";
import { axiosCall } from "../plugin/axios";

export const AddressService = {
  fetchAddressByUser: (id) => {
    return axiosCall.get(`${API.LIST_ADDRESS_BY_USER}/${id}` );
  },
  fetchAddressByIdAddress: (id) => {
    return axiosCall.get(`${API.GET_ADDRESS_BY_ID_ADDRESS}/${id}` );
  },
  createAddressByUser: (id,params) => {
    return axiosCall.post(`${API.CREATE_ADDRESS_BY_USER}/${id}`, params);
  },
   deleteAddressByUser: (id) => {
    return axiosCall.delete(API.DELETE_ADDRESS_BY_USER, {
      params: { id }
    });
  },
};
