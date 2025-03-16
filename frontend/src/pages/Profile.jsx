import React, { useState, useEffect } from "react";
import axios from "axios";
import Input from "../components/Input";
import Button from "../components/Button";
import Sidebar from "../components/Sidebar";

const Profile = () => {
    const [studentId, setStudentId] = useState(null);  // ✅ Store Student ID
    const [profileData, setProfileData] = useState(null);
    const [loading, setLoading] = useState(true);
    const [error, setError] = useState(null);

    // ✅ Fetch Student ID from Backend API
    useEffect(() => {
        axios.get("http://localhost:8080/api/students/me", { withCredentials: true })
            .then(response => {
                if (response.data.studentID) {
                    setStudentId(response.data.studentID);
                }
            })
            .catch(error => {
                console.error("❌ Failed to fetch student ID:", error);
                setError("Failed to fetch student ID.");
                setLoading(false);
            });
    }, []);

    // ✅ Fetch Student Profile Data Once Student ID is Available
    useEffect(() => {
        if (!studentId) return;

        axios.get(`http://localhost:8080/api/students/profile/${studentId}`)
            .then(response => {
                setProfileData(response.data);
                setLoading(false);
            })
            .catch(error => {
                console.error("❌ Error fetching profile:", error);
                setError("Failed to load profile.");
                setLoading(false);
            });
    }, [studentId]);

    if (loading) return <p>Loading...</p>;
    if (error) return <p className="text-red-500">{error}</p>;

    return (
        <div className="grid grid-cols-6 mt-20">
            <div>
                <Sidebar />
            </div>
            <div className="col-span-5 p-5">
                <h1 className="text-2xl font-bold">Profile</h1>
                <div className="mt-5">
                    <Input labelText="Name" placeholderText={profileData?.name || "N/A"} />
                    <div className='flex space-between'>
                        <Input labelText="Registration Number" placeholderText={profileData?.roll_no || "N/A"} />
                        <Input labelText="Date Of Birth" placeholderText={profileData?.dob || "N/A"} />
                    </div>
                    <div className='flex space-between'>
                        <Input labelText="Programme" placeholderText={profileData?.program || "N/A"} />
                        <Input labelText="Branch" placeholderText={profileData?.branch || "N/A"} />
                    </div>
                    <div className='flex space-between'>
                        <Input labelText="Email" placeholderText={profileData?.email || "N/A"} />
                        <Input labelText="APAAR Id" placeholderText={profileData?.apaarid || "N/A"} />
                    </div>
                    <Input labelText="Faculty Advisor" placeholderText={profileData?.facultyName || "Not Assigned"} />
                </div>
            </div>
        </div>
    );
};

export default Profile;
