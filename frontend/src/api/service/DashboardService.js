import { API } from "../domain";
import { axiosCall } from "../plugin/axios";

export const DashboardService = {
  getTotalUsers: (params) => {
    return axiosCall.get(API.TOTAL_USER, params);
  },
  getTotalOrders: (params) => {
    return axiosCall.get(API.TOTAL_ORDER, params);
  },
  getTotalRevenue: (params) => {
    return axiosCall.get(API.TOTAL_REVENUE, params);
  },
  getTotalProductsSold: (params) => {
    return axiosCall.get(API.TOTAL_PRODUCT_SOLD, params);
  },
  getTopProduct: (params) => {
    return axiosCall.get(API.TOP_PRODUCT, params);
  },
  getChartRevenue: (params) => {
    return axiosCall.get(API.CHART_REVENUE, params);
  },
  getChartOrder: (params) => {
    return axiosCall.get(API.CHART_ORDER, params);
  },
  getChartProduct: (params) => {
    return axiosCall.get(API.CHART_PRODUCT, params);
  },

};
