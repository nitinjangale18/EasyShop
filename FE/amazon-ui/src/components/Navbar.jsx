import { Link, useNavigate } from "react-router-dom";
import { useAuth } from "../context/AuthContext";
import "./Navbar.css";

function Navbar() {
  const navigate = useNavigate();

  const { user, logout } = useAuth();

  const handleLogout = () => {
    logout();
    navigate("/login", { replace: true });
  };

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
          Hello,{" "}
          {user
            ? `${user.firstName} ${user.lastName}`
            : "Guest"}
        </span>

        <Link to="/">Home</Link>


        <Link to="/profile">Profile</Link>

        <Link to="/orders">My Orders</Link>

        <Link to="/cart">Cart 🛒</Link>

        <button onClick={handleLogout}>
          Logout
        </button>

      </div>

    </nav>
  );
}

export default Navbar;