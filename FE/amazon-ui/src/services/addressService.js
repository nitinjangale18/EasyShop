import api from "../api/axiosInstance";

export const getMyAddresses = async () => {
  const response = await api.get("/api/users/addresses");
  return response.data;
};

export const createAddress = async (addressData) => {
  const response = await api.post(
    "/api/users/addresses",
    addressData
  );

  return response.data;
};