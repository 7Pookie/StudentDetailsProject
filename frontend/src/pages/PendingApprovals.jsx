import React, {useState, useEffect} from 'react'
import Sidebar from '../components/Sidebar'
import axios from 'axios';
import './pending-approvals.css';
import { Link } from 'react-router-dom';

const PendingApprovals = () => {
    const [requests, setRequests] = useState([]);
    const [loading, setLoading] = useState(true);
    const [error, setError] = useState(null);
    const [facultyID, setFacultyID] = useState(null);

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

  useEffect(() => {
    if (!facultyID) return; 

    axios.get(`http://localhost:8080/api/faculty/requests/${facultyID}`)
        .then(response => {
            setRequests(response.data);
            setLoading(false);
        })
        .catch(error => {
            console.error("❌ Error fetching requests:", error);
            setError("Error fetching requests. Please try again later.");
            setLoading(false);
        });
  }, [facultyID]);

    if (loading) return <p>Loading...</p>;
    if (error) return <p className="text-red-500">{error}</p>;
    
    return (
      <div className="p-5">
      <h2 className="text-2xl font-bold mb-4">Pending Requests</h2>
      <table className="min-w-full bg-white border border-gray-300">
          <thead>
              <tr className="bg-gray-200">
                  <th className="border px-4 py-2">Student Name</th>
                  <th className="border px-4 py-2">Roll No</th>
                  <th className="border px-4 py-2">Event Type</th>
                  <th className="border px-4 py-2">Status</th>
              </tr>
          </thead>
          <tbody>
  {requests.map((req, index) => (
    <tr key={index} className="border">
      <td className="border px-4 py-2">
        <Link to={`/faculty/approve/${req.requestID}`} className="text-blue-500 underline">
          {req.studentName}
        </Link>
      </td>
      <td className="border px-4 py-2">{req.studentRollNo}</td>
      <td className="border px-4 py-2">{req.tableName}</td>
      <td className="border px-4 py-2">{req.status}</td>
    </tr>
  ))}
</tbody>
      </table>
  </div>
          )
}

export default PendingApprovals