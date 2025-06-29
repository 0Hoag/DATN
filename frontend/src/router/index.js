import { createRouter, createWebHistory } from "vue-router";
import admin from "./admin";
import user from "./user";
import auth from "./auth";
import { jwtDecode } from "jwt-decode";
import { toast } from "vue3-toastify";
const routes = [...admin, ...user, ...auth];
const router = createRouter({
 history: createWebHistory(),
 routes,
});

router.beforeEach((to, from) => {
 const token = localStorage.getItem("token");
 const isAuthenticated = !!token;

 const isAdminRoute = to.path.startsWith("/admin");

 if (isAdminRoute && !isAuthenticated) {
  return { name: "Login" };
 }

 let roles = [];
 if (token) {
  try {
   const decoded = jwtDecode(token);
   roles = decoded.scope?.split(" ") || [];
  } catch (e) {
   console.error("Token invalid");
   return { name: "Login" };
  }
 }

  const allowedRoles = to.meta.allowedRoles || [];
  if (
    allowedRoles.length > 0 &&
    !allowedRoles.some((role) => roles.includes(role))
  ) {
    toast.error("Bạn không có quyền truy cập trang này."); // 👈 thông báo toast
    return { name: "Login" }; // hoặc return { name: "NotAuthorized" };
  }
});
export default router;
