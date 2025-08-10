const user = [
    {
        path: "/",
        component: () => import("@/layouts/user/Layout.vue"),
        children: [
            {
                path: "",
                name: "home",
                component: () => import("@/views/user/Home2.vue"),
                  meta: { title: "Trang chủ" },
            },
            {
                path: "category/:slug",
                name: "category",
                component: () => import("@/views/user/Category.vue"),
            },
              {
                path: "/login",
                name: "user-login",
                component: () => import("@/views/user/Login.vue"),
                meta: { title: "Đăng nhập" },
            },
            {
                path: "/register",
                name: "register",
                component: () => import("@/views/user/Register.vue"),
                meta: { title: "Đăng ký tài khoản" },
            },
            {
                path: "/cart",
                name: "cart",
                component: () => import("@/views/user/Cart.vue"),
                meta: { title: "Giỏ hàng" },
            },
            {
                path: "/payment",
                name: "payment",
                component: () => import("@/views/user/Payment.vue"),
                meta: { title: "Trang thanh toán", requiresAuth: true, },
            },
            {
                path: "/account",
                name: "account",
                component: () => import("@/views/user/Account.vue"),
                meta: { title: "Hồ sơ" , requiresAuth: true, },
            },
            {
                path: '/product/:slug',
                name: 'product',
                component: () => import('@/views/user/ProductDetail.vue'),
            },
            {
                path: '/search',
                name: 'search',
                component: () => import('@/views/user/Search.vue'),
            },
        
        ],
    },
]
export default user;