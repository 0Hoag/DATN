import { API } from "../domain";
import { axiosCall } from "../plugin/axios";

export const LogService = {
  fetchActivityLogs: (params) => {
    return axiosCall.get(API.LIST_ACTIVITY_LOGS, params);
  },
  fetchTransactionLogs: (params) => {
    return axiosCall.get(API.LIST_TRANSACTION_LOGS, params);
  },

};
