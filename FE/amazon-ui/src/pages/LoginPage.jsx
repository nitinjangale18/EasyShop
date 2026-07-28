import { useState } from "react";
import { useNavigate } from "react-router-dom";
import { loginUser } from "../services/authService";
import "./LoginPage.css";

function LoginPage() {
  const navigate = useNavigate();

  const [formData, setFormData] = useState({
    email: "",
    password: "",
  });

  const [showPassword, setShowPassword] = useState(false);
  const [errorMessage, setErrorMessage] = useState("");
  const [loading, setLoading] = useState(false);

  const handleChange = (e) => {
    const { name, value } = e.target;

    setFormData((previousData) => ({
      ...previousData,
      [name]: value,
    }));

    setErrorMessage("");
  };

  const handleSubmit = async (e) => {
    e.preventDefault();

    setLoading(true);
    setErrorMessage("");

    try {
      const response = await loginUser(formData);

      console.log("Login response:", response);

          localStorage.setItem("token", response.token);


      /*
        Change response.token according to your backend response.
        For example, your backend may return response.jwtToken.
      */
      if (response.token) {
        localStorage.setItem("token", response.token);
      }

      alert(response.message || "Login successful");

      navigate("/profile");
    } catch (error) {
      console.error("Login error:", error);

      const message =
        error.response?.data?.message ||
        "Invalid email or password. Please try again.";

      setErrorMessage(message);
    } finally {
      setLoading(false);
    }
  };

  return (
    <main className="login-page">
      <section className="login-card">
        <div className="login-brand">
          <div className="brand-logo">A</div>

          <div>
            <h1>Welcome back</h1>
            <p>Sign in to continue shopping</p>
          </div>
        </div>

        {errorMessage && (
          <div className="login-error" role="alert">
            {errorMessage}
          </div>
        )}

        <form className="login-form" onSubmit={handleSubmit}>
          <div className="form-group">
            <label htmlFor="email">Email address</label>

            <input
              id="email"
              type="email"
              name="email"
              placeholder="name@example.com"
              value={formData.email}
              onChange={handleChange}
              autoComplete="email"
              required
            />
          </div>

          <div className="form-group">
            <div className="password-label-row">
              <label htmlFor="password">Password</label>

              <button
                type="button"
                className="forgot-password"
                onClick={() => navigate("/forgot-password")}
              >
                Forgot password?
              </button>
            </div>

            <div className="password-input-wrapper">
              <input
                id="password"
                type={showPassword ? "text" : "password"}
                name="password"
                placeholder="Enter your password"
                value={formData.password}
                onChange={handleChange}
                autoComplete="current-password"
                required
              />

              <button
                type="button"
                className="password-toggle"
                onClick={() => setShowPassword((previous) => !previous)}
              >
                {showPassword ? "Hide" : "Show"}
              </button>
            </div>
          </div>

          <button
            type="submit"
            className="login-button"
            disabled={loading}
          >
            {loading ? "Signing in..." : "Sign in"}
          </button>
        </form>

        <div className="login-divider">
          <span>New to Amazon Clone?</span>
        </div>

        <button
          type="button"
          className="create-account-button"
          onClick={() => navigate("/register")}
        >
          Create your account
        </button>

        <p className="login-terms">
          By continuing, you agree to our{" "}
          <button type="button">Terms of Service</button> and{" "}
          <button type="button">Privacy Policy</button>.
        </p>
      </section>
    </main>
  );
}

export default LoginPage;