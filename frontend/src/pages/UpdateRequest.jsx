import React, { useState, useEffect } from "react";
import { useParams, useNavigate } from "react-router-dom";
import axios from "axios";
import Sidebar from "../components/Sidebar";
import Input1 from "../components/Input1";
import Button from "../components/Button";

const UpdateRequest = () => {
    const { requestID } = useParams();
    const [formData, setFormData] = useState({});
    const navigate = useNavigate();

    useEffect(() => {
        axios.get(`http://localhost:8080/api/students/request/${requestID}`)
            .then(response => setFormData(response.data))
            .catch(error => console.error("❌ Error fetching request:", error));
    }, [requestID]);

    const handleChange = (e) => {
        setFormData({ ...formData, [e.target.name]: e.target.value });
    };

    const handleSubmit = () => {
        axios.put(`http://localhost:8080/api/students/request/${requestID}/resubmit`, formData)
            .then(() => {
                alert("✅ Request resubmitted successfully!");
                navigate("/student/requests");
            })
            .catch(error => alert("❌ Failed to resubmit request:", error));
    };

    return (
        <div className="grid grid-cols-6 mt-10">
            <Sidebar />
            <div className="col-span-5 p-5">
                <h2 className="text-2xl font-bold mb-4">Update Request</h2>
                <form className="space-y-4">
                    {Object.entries(formData).map(([key, value]) => (
                        <Input1 key={key} labelText={key} name={key} value={value} onChange={handleChange} />
                    ))}
                    <Button buttonText="Submit" onClick={handleSubmit} />
                </form>
            </div>
        </div>
    );
};

export default UpdateRequest;
