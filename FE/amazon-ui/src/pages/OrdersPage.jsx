import { useEffect, useState } from "react";
import { getMyOrders } from "../services/orderService";
import "./OrdersPage.css";
import { Link } from "react-router-dom";

function OrdersPage() {
  const [orders, setOrders] = useState([]);
  const [loading, setLoading] = useState(true);
  const [error, setError] = useState("");

  useEffect(() => {
    const loadOrders = async () => {
      try {
        const data = await getMyOrders();

        console.log("My orders:", data);

        setOrders(data);
      } catch (error) {
        console.error("Error loading orders:", error);

        setError(
          error.response?.data?.message ||
            "Unable to load orders"
        );
      } finally {
        setLoading(false);
      }
    };

    loadOrders();
  }, []);

  if (loading) {
    return (
      <div className="orders-page">
        <div className="orders-loading">
          <div className="spinner"></div>
          <h2>Loading your orders...</h2>
        </div>
      </div>
    );
  }

  if (error) {
    return (
      <div className="orders-page">
        <div className="orders-error">
          <h2>Something went wrong</h2>
          <p>{error}</p>
        </div>
      </div>
    );
  }

  if (orders.length === 0) {
    return (
      <div className="orders-page">
        <div className="empty-orders">
          <div className="empty-icon">📦</div>
          <h2>You have no orders yet</h2>
          <p>Your orders will appear here after you make a purchase.</p>
        </div>
      </div>
    );
  }

  return (
    <div className="orders-page">
      <div className="orders-container">

        <div className="orders-header">
          <h1>My Orders</h1>
          <p>Track and manage your recent orders</p>
        </div>

        <div className="orders-list">
          {orders.map((order) => (
            <div className="order-card" key={order.id}>

              {/* Order Header */}
              <div className="order-header">

                <div>
                  <span className="order-label">
                    Order ID
                  </span>

                  <h2>#{order.id}</h2>
                </div>

                <div className="order-status">
                  <span
                    className={`status-badge ${order.status?.toLowerCase()}`}
                  >
                    {order.status}
                  </span>
                </div>

              </div>

              {/* Order Information */}
              <div className="order-info">

                <div className="info-item">
                  <span>Order Date</span>
                  <strong>
                    {order.createdAt
                      ? new Date(order.createdAt).toLocaleDateString()
                      : "-"}
                  </strong>
                </div>

                <div className="info-item">
                  <span>Total Amount</span>
                  <strong className="total-amount">
                    ₹{order.totalAmount}
                  </strong>
                </div>

              </div>

              {/* Items */}
              <div className="order-items">

                <h3>Items</h3>

                {order.items.map((item) => (
                  <div className="order-item" key={item.id}>

                    <div className="product-placeholder">
                      🛍️
                    </div>

                    <div className="product-details">
                      <h4>{item.productName}</h4>

                      <p>
                        Quantity: {item.quantity}
                      </p>

                      <p>
                        Price: ₹{item.price}
                      </p>
                    </div>

                  </div>
                ))}

                <Link
                to={`/orders/${order.id}`}
                className="view-order-link"
                >
                View Order Details →
                </Link>

              </div>

            </div>
          ))}
        </div>

      </div>
    </div>
  );
}

export default OrdersPage;