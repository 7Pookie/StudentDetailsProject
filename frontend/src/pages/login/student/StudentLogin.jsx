import React from 'react'
import './studentlogin.css';
const StudentLogin = () => {
    const handleLogin = () => {
        window.location.href = "http://localhost:8080/oauth2/authorization/google";
    };
  return (
    <div>
        <div className="login">
       <h2 className="head">Student Login</h2>
       <button onClick={handleLogin} className="button">Login with Google</button>
       </div>
    </div>
  )
}

export default StudentLogin;
