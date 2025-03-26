import React, { useState, useEffect } from 'react';
import Input1 from '../components/Input1';
import Button from '../components/Button';
import Sidebar from '../components/Sidebar';
import CustomDatePicker from './CustomDatePicker';
import axios from 'axios';

const UpdateProfessionalSocietyDetails = () => {
    const [formData, setFormData] = useState({
        studentID: "",
        societyID: "",
        customSocietyName: "",
        fieldID: "",
        customFieldName: "",
        dateJoined: "",
        role: "",
        achievementDetails: "",
    });

    const [societyNames, setSocietyNames] = useState([]);
    const [fields, setFields] = useState([]);
    const [loading, setLoading] = useState(false);
    const [file, setFile] = useState(null);
    const [uploadProgress, setUploadProgress] = useState(0);
    const [fileError, setFileError] = useState("");
    const [isSubmitting, setIsSubmitting] = useState(false);

    useEffect(() => {
        axios.get("http://localhost:8080/api/students/me", { withCredentials: true })
            .then(response => {
                if (response.data.studentID) {
                    setFormData(prevState => ({ ...prevState, studentID: response.data.studentID }));
                }
            })
            .catch(error => console.error("Failed to fetch student ID:", error));

        // Fetch societies and fields
        axios.get("http://localhost:8080/api/professional-society/societies")
            .then(response => setSocietyNames(response.data))
            .catch(error => console.error("❌ Error fetching societies:", error));

        axios.get("http://localhost:8080/api/professional-society/fields")
            .then(response => setFields(response.data))
            .catch(error => console.error("❌ Error fetching fields:", error));
    }, []);

    const handleChange = (e) => {
        setFormData({ ...formData, [e.target.name]: e.target.value });
    };

    const handleDateChange = (date) => {
        setFormData({ ...formData, dateJoined: date });
    };

    const handleFileChange = (e) => {
        const selectedFile = e.target.files[0];
        setFileError("");
        
        if (selectedFile) {
            const validTypes = ['application/pdf', 'application/msword', 
                               'application/vnd.openxmlformats-officedocument.wordprocessingml.document',
                               'image/jpeg', 'image/png'];
            const maxSize = 5 * 1024 * 1024; // 5MB
            
            if (!validTypes.includes(selectedFile.type)) {
                setFileError("Invalid file type. Please upload PDF, DOC, DOCX, JPG or PNG.");
                return;
            }
            
            if (selectedFile.size > maxSize) {
                setFileError("File size exceeds 5MB limit.");
                return;
            }
            
            setFile(selectedFile);
        }
    };

    const handleAddNewSociety = async (e) => {
        e.preventDefault();
        if (!formData.customSocietyName.trim()) {
            alert("❌ Please enter a society name before adding.");
            return;
        }
        
        try {
            setLoading(true);
            const response = await axios.post(
                "http://localhost:8080/api/professional-society/societies/add", 
                { societyName: formData.customSocietyName },
                { headers: { "Content-Type": "application/json" } }
            );

            alert("✅ New society added successfully!");
            setSocietyNames([...societyNames, response.data]);
            setFormData(prev => ({ ...prev, societyID: response.data.societyID, customSocietyName: "" }));
        } catch (error) {
            alert("❌ Failed to add new society.");
            console.error("Error adding society:", error);
        } finally {
            setLoading(false);
        }
    };

    const handleAddNewField = async (e) => {
        e.preventDefault();
        if (!formData.customFieldName.trim()) {
            alert("❌ Please enter a field name before adding.");
            return;
        }
        
        try {
            setLoading(true);
            const response = await axios.post(
                "http://localhost:8080/api/professional-society/fields/add", 
                { fieldName: formData.customFieldName },
                { headers: { "Content-Type": "application/json" } }
            );

            alert("✅ New field added successfully!");
            setFields([...fields, response.data]);
            setFormData(prev => ({ ...prev, fieldID: response.data.fieldID, customFieldName: "" }));
        } catch (error) {
            alert("❌ Failed to add new field.");
            console.error("Error adding field:", error);
        } finally {
            setLoading(false);
        }
    };

    const handleSubmit = async (e) => {
        e.preventDefault();
        
        if (!formData.studentID) {
            alert("❌ Student ID is missing.");
            return;
        }

        setIsSubmitting(true);
        setUploadProgress(0);

        try {
            const formDataToSend = new FormData();
            
            // Format date to yyyy-MM-dd
            const formattedDate = formData.dateJoined 
                ? new Date(formData.dateJoined).toISOString().split('T')[0]
                : '';
            
            // Append all form fields
            formDataToSend.append('studentID', formData.studentID);
            formDataToSend.append('societyID', formData.societyID);
            formDataToSend.append('fieldID', formData.fieldID);
            formDataToSend.append('dateJoined', formattedDate);
            formDataToSend.append('role', formData.role);
            formDataToSend.append('achievementDetails', formData.achievementDetails);
            
            if (file) {
                formDataToSend.append('file', file);
            }

            await axios.post(
                "http://localhost:8080/api/professional-society/add",
                formDataToSend,
                {
                    headers: { 'Content-Type': 'multipart/form-data' },
                    onUploadProgress: (progressEvent) => {
                        const progress = Math.round(
                            (progressEvent.loaded * 100) / progressEvent.total
                        );
                        setUploadProgress(progress);
                    },
                    withCredentials: true
                }
            );

            alert("✅ Professional society details submitted successfully!");
            // Reset form
            setFormData({
                studentID: formData.studentID,
                societyID: "",
                customSocietyName: "",
                fieldID: "",
                customFieldName: "",
                dateJoined: "",
                role: "",
                achievementDetails: "",
            });
            setFile(null);
            
        } catch (error) {
            console.error("Error:", error);
            alert(`❌ Error: ${error.response?.data?.message || error.message}`);
        } finally {
            setIsSubmitting(false);
            setUploadProgress(0);
        }
    };

    return (
        <div className="grid grid-cols-6 mt-20">
            <div className="pt-16">
                <Sidebar />
            </div>
            <div className="col-span-5 bg-white py-10 px-20 rounded-lg shadow-lg">
                <h1 className="text-3xl font-bold text-gray-700 mb-8">Professional Society Details</h1>
                <form onSubmit={handleSubmit} className="mt-5 space-y-4">
                    
                    {/* Society Name Selection */}
                    <div>
                        <label className="block text-lg font-bold text-gray-700">Society Name *</label>
                        <select 
                            name="societyID" 
                            value={formData.societyID} 
                            onChange={handleChange} 
                            required
                            className="w-1/2 px-4 py-2 mt-2 mb-4 border border-gray-300 rounded-lg shadow-sm focus:outline-none focus:ring-2 focus:ring-blue-500"
                        >
                            <option value="">Select Society Name</option>
                            {societyNames.length > 0 ? (
                                societyNames.map(society => (
                                    <option key={society.societyID} value={society.societyID}>
                                        {society.societyName}
                                    </option>
                                ))
                            ) : (
                                <option disabled>Loading Societies...</option>
                            )}
                        </select>

                        <Input1
                            labelText="Or enter new society name"
                            name="customSocietyName"
                            value={formData.customSocietyName}
                            onChange={handleChange}
                            disabled={!!formData.societyID}
                        />

                        <Button
                            buttonText="Add New Society"
                            onClick={handleAddNewSociety}
                            type="button"
                            disabled={loading || !formData.customSocietyName.trim()}
                        />
                    </div>

                    {/* Society Field Selection */}
                    <div>
                        <label className="block text-lg font-bold text-gray-700 mt-10">Society Field *</label>
                        <select 
                            name="fieldID" 
                            value={formData.fieldID} 
                            onChange={handleChange} 
                            required
                            className="w-1/2 px-4 py-2 mt-2 mb-4 border border-gray-300 rounded-lg shadow-sm focus:outline-none focus:ring-2 focus:ring-blue-500"
                        >
                            <option value="">Select Field</option>
                            {fields.length > 0 ? (
                                fields.map(field => (
                                    <option key={field.fieldID} value={field.fieldID}>
                                        {field.fieldName}
                                    </option>
                                ))
                            ) : (
                                <option disabled>Loading Fields...</option>
                            )}
                        </select>

                        <Input1
                            labelText="Or enter new field"
                            name="customFieldName"
                            value={formData.customFieldName}
                            onChange={handleChange}
                            disabled={!!formData.fieldID}
                        />

                        <Button
                            buttonText="Add New Field"
                            onClick={handleAddNewField}
                            type="button"
                            disabled={loading || !formData.customFieldName.trim()}
                        />
                    </div>

                    {/* Date Joined */}
                    <CustomDatePicker
                        labelText="Date Joined *"
                        selectedDate={formData.dateJoined}
                        onChange={handleDateChange}
                        required
                        className="mt-6"
                    />

                    {/* Role */}
                    <Input1
                        labelText="Role *"
                        name="role"
                        value={formData.role}
                        onChange={handleChange}
                        required
                    />

                    {/* Achievement Details */}
                    <Input1
                        labelText="Achievement Details"
                        name="achievementDetails"
                        value={formData.achievementDetails}
                        onChange={handleChange}
                    />

                    {/* File Upload Section */}
                    <div className="mt-6">
                        <label className="block text-lg font-bold text-gray-700 mb-2">
                            Upload Membership Certificate * (pdf, doc - max 5MB)
                        </label>
                        <input
                            type="file"
                            onChange={handleFileChange}
                            accept=".pdf,.doc,.docx,.jpg,.jpeg,.png"
                            className="block w-full text-sm text-gray-500
                                      file:mr-4 file:py-2 file:px-4
                                      file:rounded-md file:border-0
                                      file:text-sm file:font-semibold
                                      file:bg-blue-50 file:text-blue-700
                                      hover:file:bg-blue-100"
                        />
                        
                        {/* File info and progress */}
                        {file && (
                            <div className="mt-2">
                                <p className="text-sm text-gray-600">
                                    Selected: {file.name} ({Math.round(file.size / 1024)} KB)
                                </p>
                                {uploadProgress > 0 && uploadProgress < 100 && (
                                    <div className="w-full bg-gray-200 rounded-full h-2.5 mt-2">
                                        <div 
                                            className="bg-blue-600 h-2.5 rounded-full" 
                                            style={{ width: `${uploadProgress}%` }}
                                            
                                        ></div>
                                    </div>
                                )}
                            </div>
                        )}
                        
                        {/* Error message */}
                        {fileError && (
                            <p className="mt-2 text-sm text-red-600">{fileError}</p>
                        )}
                    </div>

                    {/* Submit Button */}
                    <div className="mt-8">
                        <Button 
                            buttonText={isSubmitting ? "Submitting..." : "Submit"}
                            type="submit"
                            disabled={isSubmitting}
                            className={isSubmitting ? 'opacity-50 cursor-not-allowed' : ''}
                        />
                    </div>
                </form>
            </div>
        </div>
    );
}

export default UpdateProfessionalSocietyDetails;