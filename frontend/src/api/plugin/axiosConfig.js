import axios from "axios";
import { jwtDecode } from "jwt-decode";
import { toast } from "vue3-toastify";

axios.interceptors.request.use(
  (config) => {
    const token = localStorage.getItem("token");

    if (token) {
      try {
        const decoded = jwtDecode(token);
        const now = Date.now() / 1000; // thời gian hiện tại (tính bằng giây)

        if (decoded.exp && decoded.exp < now) {
          // Token đã hết hạn
          localStorage.removeItem("token");
          toast.error("Phiên đăng nhập đã hết hạn. Vui lòng đăng nhập lại.");
          if (window.location.pathname !== "/login") {
            setTimeout(() => {
              window.location.href = "/login";
            }, 1500);
          }
          return Promise.reject(new Error("Token expired"));
        }

        config.headers.Authorization = `Bearer ${token}`;
      } catch (e) {
        // Token lỗi
        localStorage.removeItem("token");
        toast.error("Token không hợp lệ. Vui lòng đăng nhập lại.");
        if (window.location.pathname !== "/login") {
          setTimeout(() => {
            window.location.href = "/login";
          }, 1500);
        }
        return Promise.reject(new Error("Invalid token"));
      }
    }

    return config;
  },
  (error) => Promise.reject(error)
);

axios.interceptors.response.use(
  (response) => {
    return response;
  },
  (error) => {
    if (error.response) {
      const status = error.response.status;
      if (status === 401) {
        localStorage.removeItem("token");
        window.location.href = "/login";
      } else if (status === 403) {
        localStorage.removeItem("token");
        toast.error("Vui lòng đăng nhập lại");
      }
    }
    return Promise.reject(error);
  }
);
export default axios;
