import { BrowserRouter, Routes, Route } from "react-router-dom";

import HomePage from "../pages/HomePage";
import LoginPage from "../pages/LoginPage";
import RegisterPage from "../pages/RegisterPage";
import Profile from "../pages/Profile";
import VerifyEmail from "../pages/VerifyEmail";
import Navbar from "../components/Navbar";
import ProductDetails from "../pages/ProductDetails";
import CartPage from "../pages/CartPage";
import OrdersPage from "../pages/OrdersPage";
import { useAuth } from "../context/AuthContext";
import OrderDetails from "../pages/OrderDetails";
import AddAddressPage from "../pages/AddAddressPage";
import CheckoutPage from "../pages/CheckoutPage";
function AppRoutes() {
  const { token } = useAuth();
  return (
    <BrowserRouter>
       {token && <Navbar />}

      <Routes>
        <Route path="/" element={<HomePage />} />
        <Route path="/login" element={<LoginPage />} />
        <Route path="/register" element={<RegisterPage />} />
        <Route path="/profile" element={<Profile />} />
        <Route path="/verify-email" element={<VerifyEmail />} />
        <Route path="/products/:id" element={<ProductDetails />} />
        <Route path="/cart" element={<CartPage />} />
        <Route
  path="/addresses/new"
  element={<AddAddressPage />}
/>
        <Route path="/orders" element={<OrdersPage />} />
        <Route path="/orders/:id" element={<OrderDetails />} />
        <Route path="/checkout" element={<CheckoutPage />} />
        <Route path="*" element={<h1>404 Not Found</h1>} />
      </Routes>

    </BrowserRouter>
  );
}

export default AppRoutes;