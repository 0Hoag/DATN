import { API } from "../domain";
import { axiosCall } from "../plugin/axios";

export const ActivityLogService = {
  fetchListLogs: (params) => {
    return axiosCall.get(API.LIST_ACTIVITY_LOGS, params);
  },

};
