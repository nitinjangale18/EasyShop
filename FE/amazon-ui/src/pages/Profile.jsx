import { useEffect, useState } from "react";
import { getCurrentUserProfile } from "../services/userService";
import "./Profile.css";

const Profile = () => {
  const [user, setUser] = useState(null);
  const [error, setError] = useState("");

  useEffect(() => {
    const fetchProfile = async () => {
      try {
        const data = await getCurrentUserProfile();
        setUser(data);
      } catch (err) {
        setError("Unable to load profile");
      }
    };

    fetchProfile();
  }, []);

  if (error) {
    return (
      <div className="profile-page">
        <div className="profile-error">
          <h2>Something went wrong</h2>
          <p>{error}</p>
        </div>
      </div>
    );
  }

  if (!user) {
    return (
      <div className="profile-page">
        <div className="profile-loading">Loading profile...</div>
      </div>
    );
  }

  const initials =
    `${user.firstName?.charAt(0) || ""}${user.lastName?.charAt(0) || ""}`
      .toUpperCase();

  return (
    <div className="profile-page">
      <div className="profile-container">

        {/* Header */}
        <div className="profile-header">
          <div className="profile-avatar">
            {initials}
          </div>

          <div>
            <h1>
              {user.firstName} {user.lastName}
            </h1>
            <p>Manage your account information</p>
          </div>
        </div>

        {/* Profile Information */}
        <div className="profile-card">
          <div className="card-header">
            <h2>Personal Information</h2>
            <p>Your account details</p>
          </div>

          <div className="profile-info">

            <div className="info-row">
              <div className="info-label">Full Name</div>
              <div className="info-value">
                {user.firstName} {user.lastName}
              </div>
            </div>

            <div className="info-row">
              <div className="info-label">Email Address</div>
              <div className="info-value">
                {user.email}
              </div>
            </div>

            <div className="info-row">
              <div className="info-label">Account Role</div>
              <div className="info-value">
                <span className="role-badge">
                  {user.role}
                </span>
              </div>
            </div>

          </div>
        </div>

        {/* Account Section */}
        <div className="profile-card">
          <div className="card-header">
            <h2>Account</h2>
            <p>Manage your account settings</p>
          </div>

          <div className="account-option">
            <div>
              <h3>Account Information</h3>
              <p>View and manage your personal information.</p>
            </div>

            <span className="arrow">›</span>
          </div>

          <div className="account-option">
            <div>
              <h3>Security</h3>
              <p>Manage your password and account security.</p>
            </div>

            <span className="arrow">›</span>
          </div>
        </div>

      </div>
    </div>
  );
};

export default Profile;