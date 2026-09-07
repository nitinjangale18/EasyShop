import { useEffect, useState } from "react";
import { useNavigate, useParams } from "react-router-dom";
import { cancelOrder, getOrderById } from "../services/orderService";
import "./OrderDetails.css";

function OrderDetails() {
  const { id } = useParams();
  const navigate = useNavigate();

  const [order, setOrder] = useState(null);
  const [loading, setLoading] = useState(true);
  const [cancelling, setCancelling] = useState(false);
  const [error, setError] = useState("");

  useEffect(() => {
    const loadOrder = async () => {
      try {
        const data = await getOrderById(id);

        console.log("Order details:", data);

        setOrder(data);
      } catch (error) {
        console.error("Error loading order:", error);
        setError("Unable to load order details.");
      } finally {
        setLoading(false);
      }
    };

    loadOrder();
  }, [id]);

  const handleCancelOrder = async () => {
    if (!window.confirm("Are you sure you want to cancel this order?")) {
      return;
    }

    try {
      setCancelling(true);

      const updatedOrder = await cancelOrder(order.id);

      setOrder(updatedOrder);

      alert("Order cancelled successfully.");
    } catch (error) {
      console.error("Cancel order error:", error);

      alert(
        error.response?.data?.message ||
        "Unable to cancel order."
      );
    } finally {
      setCancelling(false);
    }
  };

  if (loading) {
    return (
      <main className="order-details-page">
        <div className="order-details-loading">
          <h2>Loading order...</h2>
        </div>
      </main>
    );
  }

  if (error) {
    return (
      <main className="order-details-page">
        <div className="order-details-error">
          {error}
        </div>
      </main>
    );
  }

  if (!order) {
    return (
      <main className="order-details-page">
        <h2>Order not found</h2>
      </main>
    );
  }

  return (
    <main className="order-details-page">

      <div className="order-details-container">

        {/* Back */}
        <button
          className="back-orders-button"
          onClick={() => navigate("/orders")}
        >
          ← Back to My Orders
        </button>

        {/* Header */}
        <div className="order-details-header">

          <div>
            <p className="order-label">
              ORDER DETAILS
            </p>

            <h1>
              Order #{order.id}
            </h1>

            <p className="order-date">
              Placed on{" "}
              {new Date(order.createdAt).toLocaleDateString(
                "en-IN",
                {
                  day: "numeric",
                  month: "numeric",
                  year: "numeric",
                }
              )}
            </p>
          </div>

          <span
            className={`order-status ${order.status.toLowerCase()}`}
          >
            {order.status}
          </span>

        </div>

        {/* Order information */}
        <section className="order-info-card">

          <div>
            <span>Order ID</span>
            <strong>#{order.id}</strong>
          </div>

          <div>
            <span>Order Date</span>
            <strong>
              {new Date(order.createdAt).toLocaleDateString(
                "en-IN"
              )}
            </strong>
          </div>

          <div>
            <span>Order Status</span>
            <strong>{order.status}</strong>
          </div>

          <div>
            <span>Total Amount</span>
            <strong>
              ₹
              {Number(order.totalAmount).toLocaleString(
                "en-IN"
              )}
            </strong>
          </div>

        </section>

        {/* Delivery Address */}
        <section className="delivery-address-card">

          <div className="section-title">
            <h2>Delivery Address</h2>
          </div>

          <div className="delivery-address-content">

            <h3>
              {order.deliveryFullName}
            </h3>

            <p>
              📞 {order.deliveryPhoneNumber}
            </p>

            <p>
              {order.deliveryAddressLine}
            </p>

            <p>
              {order.deliveryCity},{" "}
              {order.deliveryState} -{" "}
              {order.deliveryPincode}
            </p>

          </div>

        </section>

        {/* Items */}
        <section className="order-items-card">

          <div className="section-title">

            <h2>Order Items</h2>

            <span>
              {order.items?.length || 0} items
            </span>

          </div>

          <div className="order-items-list">

            {order.items?.map((item) => (

              <div
                className="order-item"
                key={item.id}
              >

                <div className="order-item-image">
                  🛍️
                </div>

                <div className="order-item-info">

                  <h3>
                    {item.productName}
                  </h3>

                  <p>
                    Quantity: {item.quantity}
                  </p>

                  <p>
                    Price: ₹
                    {Number(item.price).toLocaleString(
                      "en-IN"
                    )}
                  </p>

                </div>

                <strong>
                  ₹
                  {(
                    Number(item.price) *
                    Number(item.quantity)
                  ).toLocaleString("en-IN")}
                </strong>

              </div>

            ))}

          </div>

        </section>

        {/* Total */}
        <section className="order-total-card">

          <span>Total Amount</span>

          <strong>
            ₹
            {Number(order.totalAmount).toLocaleString(
              "en-IN"
            )}
          </strong>

        </section>

        {/* Cancel */}
        {order.status !== "CANCELLED" &&
          order.status !== "COMPLETED" && (

            <button
              className="cancel-order-button"
              onClick={handleCancelOrder}
              disabled={cancelling}
            >
              {cancelling
                ? "Cancelling..."
                : "Cancel Order"}
            </button>

          )}

      </div>

    </main>
  );
}

export default OrderDetails;