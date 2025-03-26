import React, { useState, useEffect } from 'react';
import Input1 from '../components/Input1';
import Button from '../components/Button';
import Sidebar from '../components/Sidebar';
import axios from 'axios';
import CustomDatePicker from "./CustomDatePicker";

const UpdatePlacementDetails = () => {
  const [formData, setFormData] = useState({
    studentID: "",
    placementType: "",
    companyID: "",
    customCompanyName: "",
    startDate: "",
    endDate: "",
    role: "",
    file: null,
    fileUrl: "",  
    status: "PENDING",
    remark: "",
  });

  const [companies, setCompanies] = useState([]);
  const [loading, setLoading] = useState(false);

  useEffect(() => {
    // Fetch student ID
    axios.get("http://localhost:8080/api/students/me", { withCredentials: true })
        .then(response => {
            if (response.data.studentID) {
                setFormData(prevState => ({ ...prevState, studentID: response.data.studentID }));
            }
        })
        .catch(error => console.error("Failed to fetch student ID:", error));
    
    // Fetch Companies
    axios.get("http://localhost:8080/api/companies/all")
      .then(response => setCompanies(response.data))
      .catch(error => console.error("Error fetching companies:", error));
  }, []);

  const handleAddNewCompany = async (e) => {
    e.preventDefault();
    if (!formData.customCompanyName.trim()) {
        alert("❌ Please enter a new company name before adding.");
        return;
    }
    setLoading(true);
    try {
        const response = await axios.post("http://localhost:8080/api/companies/add", {
            companyName: formData.customCompanyName
        });
        alert("✅ New company added successfully!");
        setFormData(prevState => ({
          ...prevState, 
          companyID: response.data.companyID, 
          customCompanyName: "" 
        }));
        setCompanies(prev => [...prev, response.data]); // ✅ Update company list dynamically
    } catch (error) {
        alert("❌ Failed to add new company.");
        console.error("Error adding company:", error);
    } finally {
        setLoading(false);
    }
  };

  const handleChange = (e) => {
    setFormData({ ...formData, [e.target.name]: e.target.value });
  };

  // ✅ FIX: Convert date to YYYY-MM-DD format before saving in state
  const handleDateChange = (fieldName, date) => {
    if (date) {
      const formattedDate = date.toISOString().split("T")[0]; // Converts to YYYY-MM-DD
      setFormData(prevState => ({ ...prevState, [fieldName]: formattedDate }));
    }
  };

  const handleFileChange = (e) => {
    setFormData({ ...formData, file: e.target.files[0] });
  };

  const handleSubmit = async (e) => {
    e.preventDefault();
    console.log("🛠 Form Data Before Submission:", formData);
    setLoading(true);

    try {
        const formDataToSend = new FormData();
        formDataToSend.append("studentID", formData.studentID);
        formDataToSend.append("placementType", formData.placementType);
        formDataToSend.append("companyID", Number(formData.companyID));
        formDataToSend.append("role", formData.role);
        formDataToSend.append("startDate", formData.startDate);
        formDataToSend.append("endDate", formData.endDate);
        formDataToSend.append("status", formData.status);
        formDataToSend.append("remark", formData.remark);

        if (formData.file) {
            formDataToSend.append("file", formData.file);
        }

        const response = await axios.post("http://localhost:8080/api/placements/add", formDataToSend, {
            headers: { "Content-Type": "multipart/form-data" },
        });

        alert("✅ Placement details added successfully!");
        
        if (response.data.fileUrl) {
            setFormData(prevState => ({ ...prevState, fileUrl: response.data.fileUrl }));
        }

    } catch (error) {
        console.error("❌ Error submitting:", error);
        alert("❌ Failed to add placement details.");
    }

    setLoading(false);
  };

  return (
    <div className="grid grid-cols-6 mt-20">
      <div className="pt-16"><Sidebar /></div>
      <div className="col-span-5 bg-white py-10 px-20 rounded-lg shadow-lg">
        <h1 className="text-3xl font-bold text-gray-700 mb-8">Club Details</h1>
        <form onSubmit={handleSubmit} className="space-y-4">
          
          <label className="block text-lg font-bold text-gray-700">Club Name</label>
          <select name="placementType" value={formData.placementType} onChange={handleChange} required>
            <option value="">Select Type</option>
            <option value="Internship">Internship</option>
            <option value="Full-time">Full-time</option>
          </select>

          {/* Company Selection */}
          <div>
            <label className="block text-lg font-bold text-gray-700">Category : </label>
            <select name="companyID" value={formData.companyID} onChange={handleChange}>
              <option value="">Select Club </option>
              {companies.map(company => (
                <option key={company.companyID} value={company.companyID}>{company.companyName}</option>
              ))}
            </select>

            {/* Add New Company */}
            <Input1
              labelText="Or enter new club name"
              name="customCompanyName"
              value={formData.customCompanyName}
              onChange={handleChange}
              disabled={!!formData.companyID} 
            />
          
            <Button
              buttonText="Add New Club"
              onClick={handleAddNewCompany}
              type="button" 
              disabled={loading}
            />
          </div>

          <Input1 labelText="Role *" name="role" value={formData.role} onChange={handleChange} required />

          {/* ✅ FIX: Pass correct props to CustomDatePicker */}
          <CustomDatePicker 
            labelText="Start Date *" 
            selectedDate={formData.startDate ? new Date(formData.startDate) : null} 
            onChange={(date) => handleDateChange("startDate", date)} 
            required 
          />

          <CustomDatePicker 
            labelText="End Date *" 
            selectedDate={formData.endDate ? new Date(formData.endDate) : null} 
            onChange={(date) => handleDateChange("endDate", date)} 
            required 
          />

          {/* File Upload */}
          <label className="block text-lg font-bold text-gray-700">Upload Proof of Joining:</label>
          <input
            type="file"
            accept=".pdf,.doc,.docx,.png,.jpg"
            onChange={handleFileChange}
          />

          {/* Show Uploaded File if Available */}
          {formData.fileUrl && (
            <a href={formData.fileUrl} target="_blank" rel="noopener noreferrer" className="text-blue-600 underline">
              📄 View Uploaded Document
            </a>
          )}

          <Button buttonText="Submit" type="submit" disabled={loading} />
        </form>
      </div>
    </div>
  );
}

export default UpdatePlacementDetails;