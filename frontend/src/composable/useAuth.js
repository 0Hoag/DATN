import { handleError } from "@/api/functions/common";
import { AccountService } from "@/api/service/AccountService";
import { useUserStore } from "@/store/userStore";
import { jwtDecode } from "jwt-decode";
import { onBeforeMount, ref } from "vue";

export function useAuth() {

    const store = useUserStore();

    // decode token
    const decodeToken = (rawToken = store.token) => {
        try {
            const decoded = jwtDecode(rawToken);
            store.scope = decoded.scope.split(" ");
            localStorage.setItem('scope', JSON.stringify(store.scope));
        } catch (error) {
            console.error('Decode token thất bại:', error)
            store.clearAuth();
        }
    }



    // login
    const login = async (credentials) => {
        try {
            const res = await AccountService.login(credentials);
            store.setToken(res.result.token);
            decodeToken(res.result.token);

            await store.getUserInfo();
            return true;
        } catch (error) {
            console.error('Đăng nhập thất bại:', error);
            handleError(error);
            return false;
        }
    }

    //logout 
    const logout = async () => {
        try {
            await AccountService.logout({ token: store.token })
            store.clearAuth()
        } catch (e) {
            console.warn('Logout lỗi:', e)
            handleError(e);
        }
    }


    const hasScope = (requiredScopes) => {
        if (!store.scope) return false;
        if (Array.isArray(requiredScopes)) {
            return requiredScopes.some(rs => store.scope.includes(rs));
        } else {
            return store.scope.includes(requiredScopes);
        }
    };


    return {
        login,
        logout,
        decodeToken,
        hasScope,
    }

}