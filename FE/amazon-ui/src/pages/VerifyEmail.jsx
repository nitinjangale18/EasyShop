import { useEffect, useRef, useState } from "react";
import { useSearchParams, Link } from "react-router-dom";
import { verifyEmail } from "../services/authService";
import "./EmailVerification.css";


const VerifyEmail = () => {
  const [searchParams] = useSearchParams();
  const [message, setMessage] = useState("Verifying your email...");
  const [success, setSuccess] = useState(false);

  const verificationStarted = useRef(false);

  useEffect(() => {
    if (verificationStarted.current) {
      return;
    }

    verificationStarted.current = true;

    const token = searchParams.get("token");

    if (!token) {
      setMessage("Verification token is missing.");
      return;
    }

    const verify = async () => {
      try {
        const response = await verifyEmail(token);

        setMessage(response.message || "Email verified successfully.");
        setSuccess(true);
      } catch (error) {
        setMessage(
          error.response?.data?.message ||
            "Email verification failed or the link has expired."
        );
      }
    };

    verify();
  }, [searchParams]);

  return (



  <div className="verification-container">
      <div className="verification-card">
        <div className={`icon ${success ? "success" : "error"}`}>
          {success ? "✓" : "✕"}
        </div>

        <h2>Email Verification</h2>

        <p>{message}</p>

        {success && (
          <Link to="/login" className="login-btn">
            Go to Login
          </Link>
        )}
      </div>
    </div>


  );
};

export default VerifyEmail;