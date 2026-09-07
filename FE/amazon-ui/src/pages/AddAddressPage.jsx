import { useState } from "react";
import { useNavigate } from "react-router-dom";
import { createAddress } from "../services/addressService";
import "./AddAddressPage.css";

function AddAddressPage() {

  const navigate = useNavigate();

  const [address, setAddress] = useState({
    fullName: "",
    phoneNumber: "",
    addressLine: "",
    city: "",
    state: "",
    pincode: "",
    addressType: "HOME",
  });

  const [loading, setLoading] = useState(false);
  const [error, setError] = useState("");

  const handleChange = (e) => {

    const { name, value } = e.target;

    setAddress((previous) => ({
      ...previous,
      [name]: value,
    }));

    setError("");
  };

  const handleSubmit = async (e) => {

    e.preventDefault();

    try {

      setLoading(true);
      setError("");

      const createdAddress = await createAddress(address);

      console.log("Created address:", createdAddress);

      alert("Address saved successfully.");

      // Go back to checkout
      navigate("/checkout");

    } catch (error) {

      console.error("Add address error:", error);

      setError(
        error.response?.data?.message ||
        "Unable to save address. Please try again."
      );

    } finally {

      setLoading(false);

    }
  };

  return (
    <main className="add-address-page">

      <div className="add-address-container">

        {/* Header */}

        <div className="add-address-header">

          <button
            className="back-button"
            onClick={() => navigate("/checkout")}
          >
            ← Back to Checkout
          </button>

          <h1>Add a new address</h1>

          <p>
            Enter your delivery address details
          </p>

        </div>

        {/* Form */}

        <div className="add-address-card">

          {error && (
            <div className="address-error">
              {error}
            </div>
          )}

          <form onSubmit={handleSubmit}>

            {/* Name + Phone */}

            <div className="address-form-row">

              <div className="form-group">

                <label>
                  Full Name
                </label>

                <input
                  type="text"
                  name="fullName"
                  value={address.fullName}
                  onChange={handleChange}
                  placeholder="Enter full name"
                  required
                />

              </div>

              <div className="form-group">

                <label>
                  Phone Number
                </label>

                <input
                  type="tel"
                  name="phoneNumber"
                  value={address.phoneNumber}
                  onChange={handleChange}
                  placeholder="Enter phone number"
                  required
                />

              </div>

            </div>

            {/* Address */}

            <div className="form-group">

              <label>
                Address
              </label>

              <input
                type="text"
                name="addressLine"
                value={address.addressLine}
                onChange={handleChange}
                placeholder="House No, Street, Area"
                required
              />

            </div>

            {/* City + State */}

            <div className="address-form-row">

              <div className="form-group">

                <label>
                  City
                </label>

                <input
                  type="text"
                  name="city"
                  value={address.city}
                  onChange={handleChange}
                  placeholder="Enter city"
                  required
                />

              </div>

              <div className="form-group">

                <label>
                  State
                </label>

                <input
                  type="text"
                  name="state"
                  value={address.state}
                  onChange={handleChange}
                  placeholder="Enter state"
                  required
                />

              </div>

            </div>

            {/* Pincode + Type */}

            <div className="address-form-row">

              <div className="form-group">

                <label>
                  Pincode
                </label>

                <input
                  type="text"
                  name="pincode"
                  value={address.pincode}
                  onChange={handleChange}
                  placeholder="Enter pincode"
                  required
                />

              </div>

              <div className="form-group">

                <label>
                  Address Type
                </label>

                <select
                  name="addressType"
                  value={address.addressType}
                  onChange={handleChange}
                >

                  <option value="HOME">
                    Home
                  </option>

                  <option value="WORK">
                    Work
                  </option>

                  <option value="OTHER">
                    Other
                  </option>

                </select>

              </div>

            </div>

            {/* Buttons */}

            <div className="address-form-actions">

              <button
                type="button"
                className="cancel-address-button"
                onClick={() => navigate("/checkout")}
              >
                Cancel
              </button>

              <button
                type="submit"
                className="save-address-button"
                disabled={loading}
              >
                {loading
                  ? "Saving..."
                  : "Save Address"}
              </button>

            </div>

          </form>

        </div>

      </div>

    </main>
  );
}

export default AddAddressPage;