import React, { useState, useEffect } from 'react';
import Sidebar1 from '../components/Sidebar1';
import axios from 'axios';
import { Link } from 'react-router-dom';
const PreviousApprovals = () => {
    const [requests, setRequests] = useState([]);
    const [loading, setLoading] = useState(true);
    const [error, setError] = useState(null);
    const [facultyID, setFacultyID] = useState(null);

    // Fetch Faculty ID
    useEffect(() => {
        axios.get("http://localhost:8080/api/faculty/me", { withCredentials: true })
            .then(response => {
                if (response.data.facultyID) {
                    console.log("✅ Faculty ID:", response.data.facultyID);
                    setFacultyID(response.data.facultyID);
                } else {
                    console.error("❌ Faculty ID not found in response!");
                }
            })
            .catch(error => console.error("❌ Failed to fetch faculty ID:", error));
    }, []);

    // Fetch Approved Requests
    useEffect(() => {
        if (!facultyID) return;

        axios.get(`http://localhost:8080/api/faculty/approved-requests/${facultyID}`)
            .then(response => {
                setRequests(response.data);
                setLoading(false);
            })
            .catch(error => {
                console.error("❌ Error fetching approved requests:", error);
                setError("Error fetching approved requests. Please try again later.");
                setLoading(false);
            });
    }, [facultyID]);

    if (loading) return <p>Loading...</p>;
    if (error) return <p className="text-red-500">{error}</p>;

    return(
        <div className="grid grid-cols-6 mt-10">
                        <div>
            <Sidebar1 button1Route={"/faculty/approvals/pending-approvals"} button2Route={"/faculty/approvals/rejected-approvals"} button3Route={"/faculty/approvals/previous-approvals"}/>
          </div>
              <div className="col-start-2 col-span-5 p-5">
          <h2 className="text-2xl font-bold mb-4 pt-14">Approval History</h2>
          <table className="min-w-full bg-white border border-gray-300">
              <thead>
                  <tr className="bg-blue-300">
                      <th className="border px-4 py-2">Student Name</th>
                      <th className="border px-4 py-2">Roll No</th>
                      <th className="border px-4 py-2">Event Type</th>
                      <th className="border px-4 py-2">Status</th>
                  </tr>
              </thead>
              <tbody>
      {requests.map((req, index) => (
        <tr key={index} className="border">
          <td className="border px-4 py-2 text-center">
            <Link to={`/faculty/approve/${req.requestID}`} className="text-blue-500 underline">
              {req.studentName}
            </Link>
          </td>
          <td className="border px-4 py-2 text-center">{req.studentRollNo}</td>
          <td className="border px-4 py-2 text-center">{req.tableName}</td>
          <td className="border px-4 py-2 text-center">{req.status}</td>
        </tr>
      ))}
    </tbody>
          </table>
              </div>
      </div>
              );
};

export default PreviousApprovals;
