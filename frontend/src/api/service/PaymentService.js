import { API } from "../domain";
import { axiosCall } from "../plugin/axios";
export const PaymentService = {
    fetchPaymentMethods: (params) => {
        return axiosCall.get(API.GET_PAYMENT_METHOD, params);
    },
}