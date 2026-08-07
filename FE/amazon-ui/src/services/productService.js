import api from "../api/axiosInstance";

const PRODUCT_BASE_URL = "http://localhost:8082/api/products";

export const getAllProducts = async () => {
  const response = await api.get(PRODUCT_BASE_URL);
  return response.data;
};

export const getProductById = async (id) => {
  const response = await api.get(`${PRODUCT_BASE_URL}/${id}`);
  return response.data;
};

export const addProduct = async (productData) => {
  const response = await api.post(PRODUCT_BASE_URL, productData);
  return response.data;
};

export const updateProduct = async (id, productData) => {
  const response = await api.put(
    `${PRODUCT_BASE_URL}/${id}`,
    productData
  );

  return response.data;
};

export const deleteProduct = async (id) => {
  await api.delete(`${PRODUCT_BASE_URL}/${id}`);
};