import React, { useState, useEffect } from "react";
import { useParams, Link } from "react-router-dom";
import axios from "axios";
import Sidebar from "../components/Sidebar";
import Button from "../components/Button";

const ViewRequest = () => {
    const { requestID } = useParams();
    const [request, setRequest] = useState(null);
    const [loading, setLoading] = useState(true);
    const [error, setError] = useState(null);

    // Mapping API keys to user-friendly labels
    const labelMap = {
        eventName: "Event Name",
        role: "Role",
        eventCategory: "Event Category",
        eventDate: "Event Date",
        achievement: "Achievement",
        achievementDetails: "Achievement Details",
        otherDetails: "Other Details",
        placementType: "Placement Type",
        companyName: "Company Name",
        startDate: "Start Date",
        endDate: "End Date",
        status: "Status",
        remark: "Faculty Remark",
    };

    useEffect(() => {
        axios.get(`http://localhost:8080/api/students/request/${requestID}`)
            .then(response => {
                console.log("🔹 Request Details:", response.data);
                setRequest(response.data);
                setLoading(false);
            })
            .catch(error => {
                console.error("❌ Error fetching request details:", error);
                setError("Error fetching request details.");
                setLoading(false);
            });
    }, [requestID]);

    if (loading) return <p>Loading...</p>;
    if (error) return <p className="text-red-500">{error}</p>;

    return (
        <div className="grid grid-cols-6 mt-10">
            <div className="fixed pt-16">
                <Sidebar />
            </div>
            <div className="col-start-2 col-span-5 p-5">
                <h2 className="text-3xl font-bold text-gray-700 my-8">Request Details</h2>
                
                <div className="bg-white px-6 py-4 border rounded-lg shadow-md">
                    <table className="w-full border-collapse border border-gray-300">
                        <tbody>
                            {Object.entries(labelMap).map(([key, label]) => (
                                request[key] ? (
                                    <tr key={key} className="border-b">
                                        <td className="px-4 py-2 font-semibold bg-gray-100">{label}</td>
                                        <td className="px-4 py-2">{request[key]}</td>
                                    </tr>
                                ) : null
                            ))}
                        </tbody>
                    </table>
                    
                    {/* Faculty Remark */}
                    <p className={`mt-4 font-bold text-lg ${
                        request.status === "REJECTED" ? "text-red-600" :
                        request.status === "APPROVED" ? "text-green-600" :
                        "text-blue-600"
                        }`}>
                        Faculty Remark: {request.remark || "No Remark"}
                    </p>


                    {/* Resubmission Option if Rejected */}
                    {request.status === "REJECTED" && (
                        <div className="mt-4">
                            <Link to={`/student/request/${requestID}/update`}>
                                <Button buttonText="Update & Resubmit" />
                            </Link>
                        </div>
                    )}
                </div>
            </div>
        </div>
    );
};

export default ViewRequest;