import { API } from "../domain";
import { axiosCall } from "../plugin/axios";
export const ForgotPasswordService = {
  verifyEmail: (email) => {
    return axiosCall.post(`${API.VERIFY_EMAIL}/${email}`);
  },
  verifyOtp: (otp, email) => {
    return axiosCall.post(`${API.VERIFY_OTP}/${otp}/${email}`);
  },
  resetPassword: (email, params) => {
    return axiosCall.post(`${API.RESET_PASSWORD}/${email}`, params);
  },


};
