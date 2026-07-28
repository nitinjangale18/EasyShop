import api from "../api/axiosInstance";


console.log("Axios Base URL:", api.defaults.baseURL);


export const registerUser = async (userData) => {
 console.log("Registering user with data:", userData);
  const response = await api.post("/api/auth/register", userData);
  console.log("Response from server:", response.data);
  return response.data;
};

export const loginUser = async (loginData) => {
  console.log("Logging in with:", loginData);

  const response = await api.post("/api/auth/login", loginData);

  return response.data;
};