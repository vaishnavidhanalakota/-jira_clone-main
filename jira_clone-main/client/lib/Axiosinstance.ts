import axios from "axios";

const axiosInstance = axios.create({
  baseURL: process.env.NEXT_PUBLIC_API_BASE_URL || "http://localhost:8080",
  headers: { "Content-Type": "application/json" },
  timeout: 15000,
});

export default axiosInstance;
