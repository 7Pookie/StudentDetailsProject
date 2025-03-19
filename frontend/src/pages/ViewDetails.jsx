import React, { useState, useEffect } from "react";
import { Link } from "react-router-dom";
import axios from "axios";
import Sidebar from "../components/Sidebar";

const ViewDetails = () => {
    const [requests, setRequests] = useState({});
    const [loading, setLoading] = useState(true);
    const [error, setError] = useState(null);
    const [studentID, setStudentID] = useState(null);

    useEffect(() => {
        axios.get("http://localhost:8080/api/students/me", { withCredentials: true })
            .then(response => {
              console.log("🔹 Student ID Response:", response.data);
                if (response.data.studentID) {
                    setStudentID(response.data.studentID);
                } else {
                    console.error("❌ Student ID not found!");
                }
            })
            .catch(error => console.error("❌ Failed to fetch student ID:", error));
    }, []);

    useEffect(() => {
        if (!studentID) return;

        axios.get(`http://localhost:8080/api/students/requests/${studentID}`)
            .then(response => {
              console.log("🔹 Requests Response:", response.data);
              if (Array.isArray(response.data)) {
                setRequests({ "General": response.data });
            } else {
                setRequests(response.data);
            }
                setLoading(false);
            })
            .catch(error => {
                console.error("❌ Error fetching requests:", error);
                setError("Error fetching requests. Please try again later.");
                setLoading(false);
            });
    }, [studentID]);

    if (loading) return <p>Loading...</p>;
    if (error) return <p className="text-red-500">{error}</p>;

    return (
        <div className="grid grid-cols-6 mt-10">
            <div className="fixed pt-16">
            <Sidebar />
            </div>
            <div className="col-start-2 col-span-5 p-5">
                <h2 className="text-2xl font-bold mb-4 pt-14">All Requests</h2>

                {Object.keys(requests).map((category) => (
                    requests[category].length > 0 && (
                        <div key={category} className="mb-6">
                            <table className="min-w-full bg-white border border-gray-300">
                                <thead>
                                    <tr className="bg-red-300">
                                        <th className="border px-4 py-2">Event Name</th>
                                        <th className="border px-4 py-2">Role</th>
                                        <th className="border px-4 py-2">Status</th>
                                        <th className="border px-4 py-2">Action</th>
                                    </tr>
                                </thead>
                                <tbody>
                                    {requests[category].map((req, index) => (
                                        <tr key={index} className="border">
                                            <td className="border px-4 py-2 text-center">{req.eventName}</td>
                                            <td className="border px-4 py-2 text-center">{req.role}</td>
                                            <td className={`border px-4 py-2 text-center font-bold ${req.status === "REJECTED"?"text-red-600": req.status === "APPROVED"?"text-green-600": "text-blue-600"}`}>
                                                {req.status}
                                            </td>
                                            <td className="border px-4 py-2 text-center">
                                                <Link to={`/student/request/${req.requestID}`} className="text-blue-500 underline">
                                                    View Details
                                                </Link>
                                            </td>
                                        </tr>
                                    ))}
                                </tbody>
                            </table>
                        </div>
                    )
                ))}
            </div>
        </div>
    );
};

export default ViewDetails;
