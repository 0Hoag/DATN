import { API } from "../domain";
import { axiosCall } from "../plugin/axios";

export const AddressService = {
  fetchAddressByUser: (id) => {
    return axiosCall.get(`${API.LIST_ADDRESS_BY_USER}/${id}` );
  },
  createAddressByUser: (params) => {
    return axiosCall.post(API.CREATE_ADDRESS_BY_USER, params);
  },
};
