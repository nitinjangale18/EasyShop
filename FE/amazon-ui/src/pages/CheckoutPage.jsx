import { useEffect, useState } from "react";
import { useNavigate, useSearchParams } from "react-router-dom";

import { getMyAddresses } from "../services/addressService";
import { getProductById } from "../services/productService";

import { getCart, removeCartItem } from "../services/cartService";

import {
  createOrder,
  createBuyNowOrder,
} from "../services/orderService";

import "./CheckoutPage.css";

function CheckoutPage() {
  const navigate = useNavigate();

  const [searchParams] = useSearchParams();

  const isBuyNow = searchParams.get("buyNow") === "true";

  const fromCart = searchParams.get("fromCart") === "true";

  const buyNowProductId = searchParams.get("productId");

  const [addresses, setAddresses] = useState([]);
  const [selectedAddress, setSelectedAddress] = useState(null);
  const [cart, setCart] = useState(null);
  const [loading, setLoading] = useState(true);
  const [error, setError] = useState("");
  const [placingOrder, setPlacingOrder] = useState(false);

 const handlePlaceOrder = async () => {

  if (!selectedAddress) {
    alert("Please select a delivery address.");
    return;
  }

  try {

    setPlacingOrder(true);

    let order;

    if (isBuyNow) {

      // BUY NOW ORDER
      order = await createBuyNowOrder(
        buyNowProductId,
        selectedAddress
      );

      // If Buy Now came from Cart,
      // remove that product from the cart
      if (fromCart) {

        try {

          await removeCartItem(buyNowProductId);

          console.log(
            "Buy Now product removed from cart"
          );

        } catch (cartError) {

          console.error(
            "Order created but failed to remove product from cart:",
            cartError
          );

        }
      }

    } else {

      // NORMAL CART CHECKOUT
      order = await createOrder(selectedAddress);
    }

    console.log("Created order:", order);

    alert("Order placed successfully!");

    navigate(`/orders/${order.id}`);

  } catch (error) {

    console.error(
      "Place order error:",
      error
    );

    alert(
      error.response?.data?.message ||
      "Unable to place order. Please try again."
    );

  } finally {

    setPlacingOrder(false);

  }
};

  useEffect(() => {
  const loadCheckoutData = async () => {
    try {
      const addressData = await getMyAddresses();

      console.log("Addresses:", addressData);

      setAddresses(addressData);

      // Select default address
      const defaultAddress = addressData.find(
        (address) => address.isDefault
      );

      if (defaultAddress) {
        setSelectedAddress(defaultAddress.id);
      } else if (addressData.length > 0) {
        setSelectedAddress(addressData[0].id);
      }

      // BUY NOW
      if (isBuyNow && buyNowProductId) {

        const productData = await getProductById(
          buyNowProductId
        );

        console.log("Buy Now Product:", productData);

        const buyNowCart = {
          items: [
            {
              id: productData.id,
              productId: productData.id,
              name: productData.name,
              price: productData.price,
              imageUrl: productData.imageUrl,
              quantity: 1,
              subtotal: productData.price,
            },
          ],
          total: productData.price,
        };

        setCart(buyNowCart);

      } else {

        // NORMAL CART CHECKOUT
        const cartData = await getCart();

        console.log("Cart:", cartData);

        setCart(cartData);
      }

    } catch (error) {
      console.error(
        "Checkout loading error:",
        error
      );

      setError(
        "Unable to load checkout data."
      );

    } finally {
      setLoading(false);
    }
  };

  loadCheckoutData();

}, [isBuyNow, buyNowProductId]);

  if (loading) {
    return (
      <main className="checkout-page">
        <div className="checkout-loading">
          <div className="loading-spinner"></div>
          <p>Loading checkout...</p>
        </div>
      </main>
    );
  }

  return (
    <main className="checkout-page">

      <div className="checkout-container">

        {/* Header */}
        <div className="checkout-header">

          <div>
            <h1>Checkout</h1>
            <p>Choose your delivery address</p>
          </div>

          <div className="checkout-step">

            <span className="step-active">1</span>
            <span>Address</span>

            <span className="step-line"></span>

            <span className="step-inactive">2</span>
            <span>Payment</span>

          </div>

        </div>

        <div className="checkout-content">

          {/* LEFT SIDE */}
          <section className="address-section">

            <div className="section-header">

              <div>
                <h2>Delivery Address</h2>

                <p>
                  Select where you want your order delivered.
                </p>
              </div>

              <button
                type="button"
                className="add-address-button"
                onClick={() => navigate("/addresses/new")}
              >
                + Add new address
              </button>

            </div>

            {error && (
              <div className="checkout-error">
                {error}
              </div>
            )}

            {addresses.length === 0 ? (

              <div className="empty-address">

                <div className="empty-address-icon">
                  📍
                </div>

                <h3>No saved addresses</h3>

                <p>
                  Add a delivery address to continue with your
                  order.
                </p>

                <button
                  type="button"
                  className="add-address-button"
                  onClick={() => navigate("/addresses/new")}
                >
                  + Add new address
                </button>

              </div>

            ) : (

              <div className="address-list">

                {addresses.map((address) => (

                  <div
                    key={address.id}
                    className={`address-card ${
                      selectedAddress === address.id
                        ? "selected"
                        : ""
                    }`}
                    onClick={() =>
                      setSelectedAddress(address.id)
                    }
                  >

                    <div className="address-radio">

                      <input
                        type="radio"
                        name="deliveryAddress"
                        checked={
                          selectedAddress === address.id
                        }
                        onChange={() =>
                          setSelectedAddress(address.id)
                        }
                      />

                    </div>

                    <div className="address-details">

                      <div className="address-top">

                        <h3>
                          {address.fullName}
                        </h3>

                        {address.isDefault && (
                          <span className="default-badge">
                            Default
                          </span>
                        )}

                        <span className="address-type-badge">
                          {address.addressType}
                        </span>

                      </div>

                      <p className="address-phone">
                        📞 {address.phoneNumber}
                      </p>

                      <p className="address-line">
                        {address.addressLine}
                      </p>

                      <p className="address-location">
                        {address.city}, {address.state} -{" "}
                        {address.pincode}
                      </p>

                      <div className="address-actions">

                        <button
                          type="button"
                          onClick={(e) =>
                            e.stopPropagation()
                          }
                        >
                          Edit
                        </button>

                        <span>|</span>

                        <button
                          type="button"
                          onClick={(e) =>
                            e.stopPropagation()
                          }
                        >
                          Remove
                        </button>

                      </div>

                    </div>

                  </div>

                ))}

              </div>

            )}

          </section>

          {/* RIGHT SIDE */}
          <aside className="order-summary">

            <h2>Order Summary</h2>

            {/* CART ITEMS */}
            <div className="checkout-items">

              {cart?.items?.map((item) => (

                <div
                  className="checkout-item"
                  key={item.id}
                >

                  <img
                    src={item.imageUrl}
                    alt={item.name}
                    className="checkout-item-image"
                  />

                  <div className="checkout-item-info">

                    <h4>
                      {item.name}
                    </h4>

                    <p>
                      Qty: {item.quantity || 1}
                    </p>

                    <strong>
                      ₹
                      {Number(item.price).toLocaleString(
                        "en-IN"
                      )}
                    </strong>

                  </div>

                </div>

              ))}

            </div>

            <div className="summary-divider"></div>

            {/* ITEMS TOTAL */}
            <div className="summary-row">

              <span>
                Items ({cart?.items?.length || 0})
              </span>

              <span>
                ₹
                {Number(cart?.total || 0).toLocaleString(
                  "en-IN"
                )}
              </span>

            </div>

            {/* DELIVERY */}
            <div className="summary-row">

              <span>
                Delivery
              </span>

              <span className="free-text">
                FREE
              </span>

            </div>

            <div className="summary-divider"></div>

            {/* TOTAL */}
            <div className="summary-total">

              <span>
                Order Total
              </span>

              <strong>
                ₹
                {Number(cart?.total || 0).toLocaleString(
                  "en-IN"
                )}
              </strong>

            </div>

            {/* PLACE ORDER */}
            <button
              className="place-order-button"
              disabled={
                !cart ||
                !cart.items ||
                cart.items.length === 0 ||
                selectedAddress === null ||
                placingOrder
              }
              onClick={handlePlaceOrder}
            >
              {placingOrder
                ? "Placing order..."
                : "Place your order"}
            </button>

            <div className="secure-checkout">
              🔒 Secure checkout
            </div>

            <p className="checkout-note">
              By placing your order, you agree to our
              terms and conditions.
            </p>

          </aside>

        </div>

      </div>

    </main>
  );
}

export default CheckoutPage;