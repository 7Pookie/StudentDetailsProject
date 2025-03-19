import {Link} from 'react-router-dom';
import './Landing.css';
import React, { useState } from "react";
import axios from "axios";
import LandingImage from "./landing.jpeg";

const Landing = () => {
    const [role, setRole] = useState("");

    const handleSubmit = async () => {
        if (!role) {
            alert("Please select a role.");
            return;
        }
        try {
            const response = await axios.post(
                "http://localhost:8080/auth/select-role",
                { role },
                { headers: { "Content-Type": "application/json" }, withCredentials: true }
            );

            console.log("Backend Response:", response.data);

            if (response.data.redirectUrl) {
                window.location.href = response.data.redirectUrl; // ✅ Redirect to React login
            } else {
                alert("Invalid role selection.");
            }
        } catch (error) {
            console.error("Error:", error.response ? error.response.data : error.message);
            alert("Failed to communicate with the server.");
        }
    };


    return(
        <div className="landing-container">
            
            <img src={LandingImage} alt="Edutech" className="landing-img" />

            <div className="landing-content">
                <h1 className="edu">Edutech</h1>
                <div className="card1">
                    <p className="select-role">Select Role</p>
                    <div className="btn-group">
                        <select value={role} onChange={(e) => setRole(e.target.value)} className="dropdown">
                            <option value="">Choose Role</option>
                            <option value="student">Student</option>
                            <option value="faculty">Faculty</option>
                            <option value="admin">Admin</option>
                        </select>
                        <button onClick={handleSubmit} className="btn1">Continue</button>
                    </div>
                </div>
            </div>
        </div>
    );
};

export default Landing;