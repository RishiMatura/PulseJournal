import { useState } from "react";
import { useNavigate } from "react-router-dom";

function Signup() {
    const [userName, setUserName] = useState("");
    const [password, setPassword] = useState("");
    const navigate = useNavigate();

    const handleSignup = async () => {
        const trimmedUsername = userName.trim();
        const trimmedPassword = password.trim();

        if (!trimmedUsername || !trimmedPassword) {
            alert("Username and password cannot be empty.");
            return;
        }

        try {
            const response = await fetch("http://localhost:8080/public/create-user", {
                method: "POST",
                headers: { "Content-Type": "application/json" },
                body: JSON.stringify({ userName: trimmedUsername, password: trimmedPassword }),
            });

            if (response.ok) {
                alert("Signup successful! Please log in.");
                navigate("/login");
            } else {
                const errorMsg = await response.text();
                alert("Signup failed. Reason: " + errorMsg);
            }
        } catch (error) {
            console.error("Signup error:", error);
            alert("Something went wrong. Please try again later.");
        }
    };

    return (
        <div className="container mt-5" style={{ maxWidth: "500px" }}>
            <div className="card shadow p-4">
                <h2 className="text-center mb-4">Signup</h2>
                <div className="mb-3">
                    <label className="form-label">Username</label>
                    <input
                        type="text"
                        className="form-control"
                        placeholder="Enter username"
                        value={userName}
                        onChange={(e) => setUserName(e.target.value)}
                    />
                </div>
                <div className="mb-3">
                    <label className="form-label">Password</label>
                    <input
                        type="password"
                        className="form-control"
                        placeholder="Enter password"
                        value={password}
                        onChange={(e) => setPassword(e.target.value)}
                    />
                </div>
                <button
                    className="btn btn-primary w-100"
                    onClick={handleSignup}
                    disabled={!userName.trim() || !password.trim()}
                >
                    Sign Up
                </button>
            </div>
        </div>
    );
}

export default Signup;
