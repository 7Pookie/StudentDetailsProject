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

    useEffect(() => {
        axios.get(`http://localhost:8080/api/students/request/${requestID}`)
            .then(response => {
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
                <div className="bg-white px-6 py-4 border rounded-lg">
                    {Object.entries(request).map(([key, value]) => (
                        <p key={key} className="my-4"><strong>{key}:</strong> {value}</p>
                    ))}
                    <p className="mt-4 font-bold text-green-500">Faculty Remark: {request.remark || "No Remark"}</p>
                    {request.status === "REJECTED" && (
                        <Link to={`/student/request/${requestID}/update`}>
                            <Button buttonText="Update & Resubmit" />
                        </Link>
                    )}
                </div>
            </div>
        </div>
    );
};

export default ViewRequest;
