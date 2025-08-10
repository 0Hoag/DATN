import { handleError } from "@/api/functions/common";
import { AccountService } from "@/api/service/AccountService";
import { useAuth } from "@/composable/useAuth";
import { defineStore } from "pinia";
import { ref } from "vue";

export const useUserStore = defineStore("user", () => {
    const token = ref(localStorage.getItem("token") || "");
    const isLoggedIn = ref(!!token.value);
    const userInfo = ref(localStorage.getItem('userInfo') || null);
    const scope = ref(JSON.parse(localStorage.getItem('scope')) || []);
    const { decodeToken } = useAuth();
    // set token 
    const setToken = (newToken) => {
        token.value = newToken;
        localStorage.setItem("token", newToken);
        isLoggedIn.value = true;
    }


    // xóa token + reset state
    const clearAuth = () => {
        token.value = ''
        userInfo.value = null
        isLoggedIn.value = false
        scope.value = []
        localStorage.removeItem('token')
        localStorage.removeItem('userInfo')
        localStorage.removeItem('scope')
    }
    // get user info
    const getUserInfo = async () => {
        try {
            const res = await AccountService.getUserInfo();
            userInfo.value = {
                id: res.result.id,
                fullName: res.result.fullName,
                email: res.result.email,
                phone: res.result.phone,
            };
            decodeToken(token.value);
            localStorage.setItem('userInfo', JSON.stringify(userInfo.value));
        } catch (error) {
            console.error('Lấy thông tin người dùng thất bại:', error);
            handleError(error);
        }
    }

    const loadUser = async () => {
        const user = localStorage.getItem('userInfo');

        if (user) {
            userInfo.value = JSON.parse(user);
        } else {
            await getUserInfo();
        }
    }

    function initAuth() {
        if (token.value) {
            getUserInfo();
            // decodeToken(token.value);
        } // chỉ gọi nếu có token
    }
    return {
        userInfo,
        isLoggedIn,
        token,
        scope,
        setToken,
        clearAuth,
        getUserInfo,
        initAuth,
        decodeToken,
        loadUser,
    }
}
);