import api from "../api/axiosInstance";

export const getCurrentUserProfile = async () => {
    const response = await api.get("/api/users/profile");
    return response.data;
};

