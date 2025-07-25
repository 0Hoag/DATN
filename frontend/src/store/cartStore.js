import { handleError } from "@/api/functions/common";
import { CartService } from "@/api/service/CartService";
import { ref } from "vue";
import { defineStore } from "pinia";
import { toast } from "vue3-toastify";

export const useCartStore = defineStore('cart', () => {
  const cartItem = ref([]);
  const userId = ref(null);
  const sessionId = ref(null);
  const cartId = ref(null);

  const syncCartData = (data) => {
    cartItem.value = data?.cartItem ?? [];
    userId.value = data?.userId ?? null;
    cartId.value = data?.cartId ?? null;
    sessionId.value = data?.sessionId ?? null;
  };

  const getCart = async () => {
     console.log("▶ Gọi hàm getCart()");
    try {
      const res = await CartService.getSession();
    console.log("Kết quả getSession:", res.result); // 👈 xem có sessionId không
      cartItem.value = res.result;
      console.log("cartItem", cartItem.value);
    } catch (error) {
       console.log(error);
      handleError(error);
    }
  };

  const addToCart = async (variantId, quantity = 1) => {
    try {
      const res = await CartService.addCart({ variantId, quantity });
      toast.success('Thêm vào giỏ hàng thành công')
      syncCartData(res.result);
    } catch (error) {
      handleError(error);
    }
  };

  const updateCart = async (variantId, quantity = 1) => {
    try {
      const res = await CartService.updateCart({ variantId, quantity });
      syncCartData(res.result);
    } catch (error) {
      handleError(error);
    }
  };

  const deleteItem = async (variantId) => {
    try {
      const res = await CartService.deletItem(variantId);
      syncCartData(res.result);
    } catch (error) {
      console.log(error);
      handleError(error);
    }
  };

  return {
    cartItem,
    userId,
    cartId,
    sessionId,
    getCart,
    addToCart,
    updateCart,
    deleteItem,
  };
});
