import Login from "@/views/auth/Login.vue";
import ForgotPassword from "@/views/auth/ForgotPassword.vue";

const auth = [
  {
    path: "/login",
    name: "Login",
    component: Login
  },
  {
    path: "/forgot-password",
    name: "ForgotPassword",
    component: ForgotPassword
  },
];
export default auth;
