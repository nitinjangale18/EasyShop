import { useEffect, useState } from "react";
import {
  getCart,
  updateCartItem,
  removeCartItem,
  clearCart,
} from "../services/cartService";

import "./CartPage.css";

function CartPage() {
  const [cart, setCart] = useState(null);
  const [loading, setLoading] = useState(true);

  const loadCart = async () => {
    try {
      const data = await getCart();
      setCart(data);
    } catch (error) {
      console.error("Error loading cart:", error);
    } finally {
      setLoading(false);
    }
  };

  useEffect(() => {
    loadCart();
  }, []);

  const changeQuantity = async (productId, newQuantity) => {
    if (newQuantity < 1) {
      return;
    }

    try {
      await updateCartItem(productId, newQuantity);
      await loadCart();
    } catch (error) {
      console.error("Error updating cart:", error);

      alert(
        error.response?.data?.message ||
          "Unable to update quantity"
      );
    }
  };

  const handleRemove = async (productId) => {
    try {
      await removeCartItem(productId);
      await loadCart();
    } catch (error) {
      console.error("Error removing product:", error);

      alert(
        error.response?.data?.message ||
          "Unable to remove product"
      );
    }
  };

  const handleClearCart = async () => {
    const confirmed = window.confirm(
      "Are you sure you want to clear your cart?"
    );

    if (!confirmed) {
      return;
    }

    try {
      await clearCart();
      await loadCart();
    } catch (error) {
      console.error("Error clearing cart:", error);

      alert(
        error.response?.data?.message ||
          "Unable to clear cart"
      );
    }
  };

  if (loading) {
    return (
      <div className="cart-message">
        <h2>Loading your cart...</h2>
      </div>
    );
  }

  if (!cart || cart.items.length === 0) {
    return (
      <div className="cart-message">
        <h2>Your cart is empty 🛒</h2>
        <p>Add some products to get started.</p>
      </div>
    );
  }

  return (
    <div className="cart-page">

      <div className="cart-header-row">
        <h1 className="cart-title">
          Shopping Cart
        </h1>

        <button
          className="clear-cart-button"
          onClick={handleClearCart}
        >
          Clear Cart
        </button>
      </div>

      <div className="cart-layout">

        <div className="cart-items">

          {cart.items.map((item) => (

            <div
              className="cart-card"
              key={item.id}
            >

              <div className="cart-image-container">
                <img
                  src={item.imageUrl}
                  alt={item.name}
                  className="cart-image"
                />
              </div>

              <div className="cart-details">

                <h2 className="cart-product-name">
                  {item.name}
                </h2>

                <p className="cart-stock">
                  In Stock
                </p>

                <p className="cart-price">
                  ₹{item.price?.toLocaleString()}
                </p>

                <div className="quantity-section">

                  <span className="quantity-label">
                    Quantity
                  </span>

                  <div className="quantity-control">

                    <button
                      onClick={() =>
                        changeQuantity(
                          item.productId,
                          item.quantity - 1
                        )
                      }
                      disabled={item.quantity <= 1}
                    >
                      −
                    </button>

                    <span>
                      {item.quantity}
                    </span>

                    <button
                      onClick={() =>
                        changeQuantity(
                          item.productId,
                          item.quantity + 1
                        )
                      }
                    >
                      +
                    </button>

                  </div>

                </div>

                <button
                  className="remove-button"
                  onClick={() =>
                    handleRemove(item.productId)
                  }
                >
                  Remove
                </button>

              </div>

              <div className="cart-subtotal">

                <span>Subtotal</span>

                <strong>
                  ₹{item.subtotal?.toLocaleString()}
                </strong>

              </div>

            </div>

          ))}

        </div>

        <div className="cart-summary">

          <h2>Order Summary</h2>

          <div className="summary-row">
            <span>Items</span>

            <span>
              {cart.items.reduce(
                (total, item) =>
                  total + item.quantity,
                0
              )}
            </span>
          </div>

          <div className="summary-row">
            <span>Delivery</span>

            <span className="free-delivery">
              FREE
            </span>
          </div>

          <hr />

          <div className="summary-total">

            <span>Total</span>

            <span>
              ₹{cart.total?.toLocaleString()}
            </span>

          </div>

          <button className="checkout-button">
            Proceed to Checkout
          </button>

        </div>

      </div>

    </div>
  );
}

export default CartPage;