import React, { useState, useEffect } from "react";
import { useParams } from "react-router-dom";
import axios from "axios";
import Input1 from "../components/Input1";
import Button from "../components/Button";
import Sidebar1 from "../components/Sidebar1";

const ApprovePage = () => {
    const { requestID } = useParams(); 
    const [formData, setFormData] = useState({});
    const [loading, setLoading] = useState(true);
    const [error, setError] = useState(null);
    const [remark, setRemark] = useState("");

    useEffect(() => {
        axios.get(`http://localhost:8080/api/faculty/request/${requestID}`)
            .then(response => {
                setFormData(response.data);
                setLoading(false);
            })
            .catch(error => {
                console.error("❌ Error fetching request details:", error);
                setError("Error fetching request details.");
                setLoading(false);
            });
    }, [requestID]);

    const handleApproval = (status) => {
        axios.put(`http://localhost:8080/api/faculty/request/${requestID}`, { status, remark })
            .then(() => {
                alert(`✅ Request ${status} successfully!`);
                window.history.back(); // Redirect to previous page
            })
            .catch(error => alert("❌ Failed to update status:", error));
    };

    if (loading) return <p>Loading...</p>;
    if (error) return <p className="text-red-500">{error}</p>;

    return (
        <div className="grid grid-cols-6 mt-10">
            <div className="fixed pt-16">
            <Sidebar1 />
            </div>
            <div className="col-start-2 col-span-5 p-5">
                <h2 className="text-2xl font-bold mt-6 mb-10">Approval Form</h2>
                <form className="space-y-4">
                    {Object.entries(formData).map(([key, value]) => (
                        <Input1 key={key} labelText={key} value={value} readOnly />
                    ))}

                    <Input1 labelText="Faculty Remark" value={remark} onChange={(e) => setRemark(e.target.value)} />

                    {/* Approve & Reject Buttons */}
                    <div className="flex space-x-4 mt-4">
                        <Button buttonText="Approve" onClick={() => handleApproval("APPROVED")} />
                        <Button buttonText="Reject" onClick={() => handleApproval("REJECTED")} />
                    </div>
                </form>
            </div>
        </div>
    );
};

export default ApprovePage;
