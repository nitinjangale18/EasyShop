import { Link, useNavigate } from "react-router-dom";
import "./Navbar.css";

function Navbar() {
  const navigate = useNavigate();

  const logout = () => {
    localStorage.removeItem("token");
    localStorage.removeItem("user");
    navigate("/login", { replace: true });
  };

  const user = JSON.parse(localStorage.getItem("user"));

  return (
    <nav className="navbar">

      <div className="navbar-logo">
        <Link to="/">Amazon Clone</Link>
      </div>

      <div className="navbar-search">
        <input
          type="text"
          placeholder="Search Amazon"
        />
        <button>🔍</button>
      </div>

      <div className="navbar-links">
        <span>
          Hello, {user?.name || "Guest"}
        </span>

        <Link to="/">Home</Link>

        <Link to="/products">Products</Link>

        <Link to="/profile">Profile</Link>

        <Link to="/cart">Cart 🛒</Link>

        <button onClick={logout}>
          Logout
        </button>
      </div>

    </nav>
  );
}

export default Navbar;