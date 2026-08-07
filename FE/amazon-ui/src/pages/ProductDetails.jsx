import { useEffect, useState } from "react";
import { useParams } from "react-router-dom";
import { getProductById } from "../services/productService";
import "./ProductDetails.css";

function ProductDetails() {
  const { id } = useParams();

  const [product, setProduct] = useState(null);
  const [loading, setLoading] = useState(true);

  useEffect(() => {
    const fetchProduct = async () => {
      try {
        const data = await getProductById(id);
        setProduct(data);
      } catch (error) {
        console.error(error);
      } finally {
        setLoading(false);
      }
    };

    fetchProduct();
  }, [id]);

  if (loading) {
    return <div className="product-status">Loading product...</div>;
  }

  if (!product) {
    return <div className="product-status error">Product not found</div>;
  }

  return (
    <main className="product-details-page">
      <div className="product-details-container">

        <section className="details-image-section">
          <div className="details-image-box">
            <img
              src={product.imageUrl}
              alt={product.name}
              className="details-product-image"
            />
          </div>
        </section>

        <section className="details-info-section">
          <p className="details-category">{product.category}</p>

          <h1 className="details-product-name">
            {product.name}
          </h1>

          <div className="details-rating">
            <span>★★★★</span>
            <span className="details-empty-star">★</span>
            <span className="rating-count"> 128 ratings</span>
          </div>

          <hr />

          <div className="details-price">
            <span className="details-currency">₹</span>
            <span>{product.price}</span>
          </div>

          <p className="tax-text">
            Inclusive of all taxes
          </p>

          <div className="details-description">
            <h3>About this item</h3>
            <p>{product.description}</p>
          </div>

          <div className="details-stock">
            {product.stock > 0 ? (
              <>
                <p className="in-stock-text">In Stock</p>
                <p className="stock-count">
                  {product.stock} units available
                </p>
              </>
            ) : (
              <p className="out-stock-text">
                Currently unavailable
              </p>
            )}
          </div>

          <div className="purchase-box">
            <div className="purchase-price">
              ₹{product.price}
            </div>

            <p className="delivery-text">
              FREE delivery available
            </p>

            <button
              className="add-cart-btn"
              disabled={product.stock <= 0}
            >
              Add to Cart
            </button>

            <button
              className="buy-now-btn"
              disabled={product.stock <= 0}
            >
              Buy Now
            </button>
          </div>
        </section>

      </div>
    </main>
  );
}

export default ProductDetails;