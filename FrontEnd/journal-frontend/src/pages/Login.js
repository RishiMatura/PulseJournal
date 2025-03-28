import React, { useState } from 'react';
import axios from 'axios';
import { useNavigate } from 'react-router-dom';

const Login = () => {
    const [username, setUsername] = useState('');
    const [password, setPassword] = useState('');
    const [errorMessage, setErrorMessage] = useState('');
    const [loading, setLoading] = useState(false);
    const navigate = useNavigate();

    const handleLogin = async (e) => {
        e.preventDefault();
        setLoading(true);
        setErrorMessage('');

        try {
            await axios.get('http://localhost:8080/journal/public/auth-check', {
                headers: {
                    Authorization: 'Basic ' + btoa(`${username}:${password}`)
                }
            });

            localStorage.setItem('username', username);
            localStorage.setItem('password', password);
            navigate('/dashboard');
        } catch (error) {
            console.error('Login error:', error);
            setErrorMessage('Invalid username or password');
        } finally {
            setLoading(false);
        }
    };

    return (
        <div
            className="d-flex justify-content-center align-items-center min-vh-100"
            style={{
                background: "linear-gradient(to right, #e0eafc, #cfdef3)",
                fontFamily: "'Segoe UI', sans-serif"
            }}
        >
            <div
                className="card shadow-lg border-0 rounded-4 p-4 d-flex flex-column justify-content-center"
                style={{
                    width: "100%",
                    maxWidth: "460px",
                    minHeight: "580px"
                }}
            >
                <h2 className="text-center text-primary fw-bold mb-3">Welcome Back 👋</h2>
                <p className="text-center text-muted mb-4">Login to your Journal</p>

                {errorMessage && (
                    <div className="alert alert-danger text-center py-2 mb-3">
                        {errorMessage}
                    </div>
                )}

                <form onSubmit={handleLogin} className="flex-grow-1">
                    <div className="mb-4">
                        <label className="form-label fw-semibold">Username</label>
                        <input
                            type="text"
                            className="form-control form-control-sm shadow-sm rounded-3"
                            style={{ fontSize: "0.875rem" }}
                            placeholder="Enter your username"
                            value={username}
                            onChange={(e) => setUsername(e.target.value)}
                            required
                        />
                    </div>

                    <div className="mb-4">
                        <label className="form-label fw-semibold">Password</label>
                        <input
                            type="password"
                            className="form-control form-control-sm shadow-sm rounded-3"
                            style={{ fontSize: "0.875rem" }}
                            placeholder="Enter your password"
                            value={password}
                            onChange={(e) => setPassword(e.target.value)}
                            required
                        />
                    </div>

                    {/* Extra space before login button */}
                    <div className="mt-3 mb-4">
                        <button
                            type="submit"
                            className="btn btn-primary btn-lg w-100 rounded-3 shadow-sm"
                            disabled={loading}
                            style={{ transition: "0.3s ease" }}
                        >
                            {loading ? 'Logging in...' : 'Login'}
                        </button>
                    </div>
                </form>

                <div className="text-center mt-auto">
                    <small className="text-muted">Not an account?</small><br />
                    <button
                        className="btn btn-link p-0 mt-1"
                        onClick={() => navigate('/signup')}
                        style={{
                            fontSize: "0.9rem",
                            textDecoration: "underline",
                            color: "#007bff"
                        }}
                    >
                        Click here to Sign Up
                    </button>
                </div>
            </div>
        </div>
    );
};

export default Login;
