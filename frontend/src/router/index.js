import { createRouter, createWebHistory } from "vue-router";
import admin from "./admin";
import user from "./user";
import auth from "./auth";
import { jwtDecode } from "jwt-decode";
import { toast } from "vue3-toastify";
const routes = [...admin, ...user, ...auth,
{ path: '/forbidden', name: 'Forbidden', component: () => import('@/views/Page403.vue') },
{
  path: '/:pathMatch(.*)*',
  name: 'NotFound',
  component: () => import('@/views/PageNotFound.vue'),
},
];
const router = createRouter({
  history: createWebHistory(),
  routes,
  scrollBehavior(to, from, savedPosition) {
    // Nếu có vị trí lưu, quay lại vị trí đó (hữu ích khi bấm nút "quay lại")
    if (savedPosition) {
      return savedPosition;
    } else {
      // Mặc định scroll về đầu trang
      return { top: 0 };
    }
  },
});
router.beforeEach((to, from) => {
  const token = localStorage.getItem("token");
  const isAuthenticated = !!token;

  const isAdminRoute = to.path.startsWith("/admin");

  if (isAdminRoute && !isAuthenticated && to.name !== "login-admin") {
    return { name: "login-admin" };
  }

  if (to.meta.requiresAuth && !isAuthenticated) {
    toast.warn("Vui lòng đăng nhập để tiếp tục");
   return new Promise((resolve) => {
    setTimeout(() => {
      resolve(
        from.fullPath !== to.fullPath
          ? from.fullPath
          : { name: "user-login" }
      );
    }, 1500); // delay ngắn để toast kịp render
  });
  }

  let roles = [];
  if (token) {
    try {
      const decoded = jwtDecode(token);
      roles = decoded.scope?.split(" ") || [];
    } catch (e) {
      console.error("Token invalid");
      return { name: "login-admin" };
    }
  }

  const allowedRoles = to.meta.allowedRoles || [];
  if (allowedRoles.length > 0 && !allowedRoles.some((role) => roles.includes(role))) {
    toast.error("Bạn không có quyền truy cập trang này."); // 👈 thông báo toast
    return { name: "Forbidden" }; // hoặc return { name: "NotAuthorized" };
  }

  document.title = to.meta.title || "Website của bạn";
});
export default router;
