import { API } from "../domain";
import { axiosCall } from "../plugin/axios";
export const VoucherService = {
    fetchListVoucher: (params) => {
        return axiosCall.get(API.LIST_VOUCHER, params);
    },
    createVoucher: (params) => {
        return axiosCall.post(API.CREATE_VOUCHER, params);
    },
    updateVoucher: (id, params) => {
        return axiosCall.put(`${API.UPDATE_VOUCHER}/${id}`, params);
    },
    deleteVoucher: (id) => {
        return axiosCall.delete(`${API.DELETE_VOUCHER}/${id}`);
    },
    searchVoucher: (params) => {
        return axiosCall.get(API.SEARCH_VOUCHER, params);
    },
};
