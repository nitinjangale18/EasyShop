import api from "../api/axiosInstance";

export const createOrder = async (addressId) => {
  const response = await api.post("/api/orders", {
    addressId: addressId,
  });

  return response.data;
};

export const createBuyNowOrder = async (
  productId,
  addressId
) => {
  const response = await api.post("/api/orders/buy-now", {
    productId: productId,
    addressId: addressId,
  });

  return response.data;
};

export const getMyOrders = async () => {
  const response = await api.get("/api/orders");
  return response.data;
};

export const getOrderById = async (id) => {
  const response = await api.get(`/api/orders/${id}`);
  return response.data;
};

export const cancelOrder = async (orderId) => {
  const response = await api.put(`/api/orders/${orderId}/cancel`);
  return response.data;
};

