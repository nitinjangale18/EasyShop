import { useEffect, useState } from "react";
import { getAllProducts } from "../services/productService";
import "./HomePage.css";
import { Link } from "react-router-dom";


function HomePage() {
  const [products, setProducts] = useState([]);
  const [loading, setLoading] = useState(true);
  const [error, setError] = useState("");

  useEffect(() => {
    const fetchProducts = async () => {
      try {
        const data = await getAllProducts();
        setProducts(data);
      } catch (error) {
        console.error(error);
        setError("Unable to load products");
      } finally {
        setLoading(false);
      }
    };

    fetchProducts();
  }, []);

  if (loading) {
    return <div className="status-message">Loading products...</div>;
  }

  if (error) {
    return <div className="status-message error">{error}</div>;
  }

  return (
  <main className="home-page">
    <div className="home-header">
      <h1>Products</h1>
      <p>Explore our latest products</p>
    </div>

    <div className="product-grid">
      {products.map((product) => (
        <div className="product-card" key={product.id}>

          <Link
            to={`/products/${product.id}`}
            className="product-link"
          >
            <div className="product-image-container">
              <img
                src={product.imageUrl}
                alt={product.name}
                className="product-image"
              />
            </div>

            <div className="product-info">
              <h3 className="product-name">
                {product.name}
              </h3>

              <p className="product-description">
                {product.description}
              </p>

              <div className="rating">
                <span>★★★★</span>
                <span className="empty-star">★</span>
                <small> 128</small>
              </div>

              <div className="product-price">
                <span className="currency">₹</span>
                {product.price}
              </div>

              <p
                className={
                  product.stock > 0
                    ? "stock in-stock"
                    : "stock out-stock"
                }
              >
                {product.stock > 0
                  ? "In Stock"
                  : "Currently unavailable"}
              </p>
            </div>
          </Link>

          <button
            className="cart-button"
            disabled={product.stock <= 0}
          >
            Add to Cart
          </button>

        </div>
      ))}
    </div>
  </main>
);
}

export default HomePage;