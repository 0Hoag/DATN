import { handleError, hideLoading, showLoading } from "@/api/functions/common";
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
      // handleError(error);
    }
  };

  const addToCart = async (variantId, quantity = 1) => {
    try {
      const existingItem = cartItem.value.find(item => item.productVariant.id === variantId);
      console.log('exisitngItem ', existingItem);
      if (existingItem) {
        const currentQty = existingItem.quantity;
        console.log('currentQty',currentQty);
        const maxQty = existingItem.productVariant.quantity;

        if (currentQty + quantity > maxQty) {
          toast.warn("Không thể thêm quá số lượng sản phẩm còn lại.");
          return;
        }
      }

      const res = await CartService.addCart({ variantId, quantity });
      toast.success('Thêm vào giỏ hàng thành công')
      syncCartData(res.result);
    } catch (error) {
      handleError(error);
    }
  };

  const updateCart = async (variantId, quantity = 1) => {
    try {
      // showLoading();
      const res = await CartService.updateCart({ variantId, quantity });
      // toast.success('Cập nhật số lượng thành công')
      syncCartData(res.result);
    } catch (error) {
      handleError(error);
    } finally {
      // hideLoading();
    }
  };

  const deleteItem = async (variantId) => {
    try {
      const res = await CartService.deletItem(variantId);
      toast.success('Xóa sản phẩm thành công')
      syncCartData(res.result);
    } catch (error) {
      console.log(error);
      handleError(error);
    }
  };

  const clearCart = () => {
    cartItem.value = [];
    userId.value = null;
    cartId.value = null;
    sessionId.value = null;
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
    clearCart,

  };
});
