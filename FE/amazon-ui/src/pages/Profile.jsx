import { useEffect, useState } from "react";
import { getCurrentUserProfile } from "../services/userService";

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
    return <p>{error}</p>;
  }

  if (!user) {
    return <p>Loading profile...</p>;
  }

  return (
    <div>
      <h1>User Profile</h1>

      <p>
        <strong>Name:</strong> {user.firstName} {user.lastName}
      </p>

      <p>
        <strong>Email:</strong> {user.email}
      </p>

      <p>
        <strong>Role:</strong> {user.role}
      </p>
    </div>
  );
};

export default Profile;