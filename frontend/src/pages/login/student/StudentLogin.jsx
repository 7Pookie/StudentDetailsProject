import React from 'react'
import './studentlogin.css';
import Google from './google.png';
import Student from './studentlogo.jpeg';
const StudentLogin = () => {
    const handleLogin = () => {
        window.location.href = "http://localhost:8080/oauth2/authorization/google";
    };
  return (
    <div className="login-container">
    <div className="login-card">
        {/* ✅ Logo Placeholder */}
        <img src={Student} alt="Student Login" className="login-icon" />

        <h2 className="login-head">Welcome, Student</h2>
        <p className="login-subtext">Sign in with Google to continue</p>

        {/* ✅ Google Login Button */}
        <button onClick={handleLogin} className="google-button">
            <img src={Google} alt="Google" className="google-icon" />
            Login with Google
        </button>
    </div>
</div>
  )
}

export default StudentLogin;
