import React from 'react'
import './../student/studentlogin.css';
const AdminLogin = () => {
  const handleLogin = () => {
      window.location.href = "http://localhost:8080/oauth2/authorization/google";
  };
return (
  <div>
      <div className="login">
     <h2 className="head">Admin Login</h2>
     <button onClick={handleLogin} className="button">Login with Google</button>
     </div>
  </div>
)
}

export default AdminLogin;
