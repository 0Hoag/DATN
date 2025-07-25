import Login from "@/views/auth/Login.vue";
import ForgotPassword from "@/views/auth/ForgotPassword.vue";

const auth = [
  {
    path: "/admin/login",
    name: "login-admin",
    component: Login,
    meta: { title: "Đăng nhập" },
  },
  {
    path: "/forgot-password",
    name: "ForgotPassword",
    component: ForgotPassword,
    meta: { title: "Quên mật khẩu" },
  },
];
export default auth;
