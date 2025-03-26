import React, { useState, useEffect } from "react";
import axios from "axios";
import Input1 from "../components/Input1";
import Button from "../components/Button";
import Sidebar from "../components/Sidebar";
import { fetchEventNames, fetchEventCategories } from "../api2";
import "react-datepicker/dist/react-datepicker.css";
import CustomDatePicker from "./CustomDatePicker";

const UpdateCulturalDetails = () => {
    const [eventNames, setEventNames] = useState([]);
    const [eventCategories, setEventCategories] = useState([]);
    const [loading, setLoading] = useState(false);
    const [file, setFile] = useState(null);
    const [uploadProgress, setUploadProgress] = useState(0);
    const [fileError, setFileError] = useState("");
    const [isSubmitting, setIsSubmitting] = useState(false);

    const [formData, setFormData] = useState({
        studentID: "",
        eventID: "",
        eventCategoryID: "",
        eventDate: "",
        role: "",
        achievement: "",
        achievementDetails: "",
        otherDetails: "",
        customEventName: "",
        customEventCategory: "",
    });

    // Fetch Student ID from Backend API
    useEffect(() => {
        axios.get("http://localhost:8080/api/students/me", { withCredentials: true })
            .then(response => {
                if (response.data.studentID) {
                    setFormData(prevState => ({ ...prevState, studentID: response.data.studentID }));
                }
            })
            .catch(error => console.error("Failed to fetch student ID:", error));
    }, []);

    // Fetch Event Names & Categories
    const fetchData = () => {
        fetchEventNames()
            .then(setEventNames)
            .catch(error => console.error("Failed to fetch event names:", error));

        fetchEventCategories()
            .then(setEventCategories)
            .catch(error => console.error("Failed to fetch event categories:", error));
    };

    useEffect(() => {
        fetchData();
    }, []);

    const handleChange = (e) => {
        const { name, value } = e.target;
        setFormData((prevState) => ({
            ...prevState,
            [name]: value
        }));
    };

    const handleDateChange = (date) => {
        setFormData(prevState => ({ ...prevState, eventDate: date }));
    };

    // File change handler
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

    // Add New Event to Database
    const handleAddNewEvent = async (e) => {
        e.preventDefault();
        if (!formData.customEventName.trim()) {
            alert("❌ Please enter a new event name before adding.");
            return;
        }
        try {
            setLoading(true);
            const response = await axios.post("http://localhost:8080/api/events/cultural/add", {
                name: formData.customEventName
            });

            alert("✅ New event added successfully!");
            setFormData(prev => ({ ...prev, eventID: response.data.eventID, customEventName: "" }));
            fetchData();
        } catch (error) {
            alert("❌ Failed to add new event.");
            console.error("Error adding event:", error);
        } finally {
            setLoading(false);
        }
    };

    // Add New Category to Database
    const handleAddNewCategory = async (e) => {
        e.preventDefault();
        if (!formData.customEventCategory.trim()) {
            alert("❌ Please enter a new category before adding.");
            return;
        }
        try {
            setLoading(true);
            const response = await axios.post(
                "http://localhost:8080/api/eventCategories/add", 
                { eventCategoryName: formData.customEventCategory },
                { headers: { "Content-Type": "application/json" } }
            );

            alert("✅ New category added successfully!");
            setFormData(prev => ({ ...prev, eventCategoryID: response.data.eventCategoryID, customEventCategory: "" }));
            fetchData();
        } catch (error) {
            alert("❌ Failed to add new category.");
            console.error("Error adding category:", error);
        } finally {
            setLoading(false);
        }
    };

    // Form submission with file upload
    const handleSubmit = async (e) => {
        e.preventDefault();
        
        if (!formData.studentID) {
            alert("Student ID is missing");
            return;
        }

        setIsSubmitting(true);
        setUploadProgress(0);

        try {
            const formDataToSend = new FormData();
            
            // Format date to yyyy-MM-dd
            const formattedDate = formData.eventDate 
                ? new Date(formData.eventDate).toISOString().split('T')[0]
                : '';
            
            // Append all form fields
            formDataToSend.append('studentID', formData.studentID);
            formDataToSend.append('eventID', formData.eventID);
            formDataToSend.append('eventCategoryID', formData.eventCategoryID);
            formDataToSend.append('eventDate', formattedDate);
            formDataToSend.append('role', formData.role);
            formDataToSend.append('achievement', formData.achievement);
            formDataToSend.append('achievementDetails', formData.achievementDetails);
            formDataToSend.append('otherDetails', formData.otherDetails);
            
            if (file) {
                formDataToSend.append('file', file);
            }

            await axios.post(
                "http://localhost:8080/api/cultural-details/add",
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

            alert("✅ Cultural details submitted successfully!");
            // Reset form
            setFormData({
                studentID: formData.studentID,
                eventID: "",
                eventCategoryID: "",
                eventDate: "",
                role: "",
                achievement: "",
                achievementDetails: "",
                otherDetails: "",
                customEventName: "",
                customEventCategory: "",
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
                <h1 className="text-3xl font-bold text-gray-700 mb-8">Cultural Events Details</h1>
                <form onSubmit={handleSubmit} className="mt-5 space-y-4">
                    {/* Event Name Selection */}
                    <div>
                        <label className="block text-lg font-bold text-gray-700">Event Name *</label>
                        <select 
                            name="eventID" 
                            value={formData.eventID} 
                            onChange={handleChange} 
                            required 
                            className="w-1/4 px-4 py-2 mt-2 mb-4 border border-gray-300 rounded-lg shadow-sm focus:outline-none focus:ring-2 focus:ring-blue-500"
                        >
                            <option value="">Select Event Name</option>
                            {eventNames.map(event => (
                                <option key={event.eventID} value={event.eventID}>
                                    {event.name}
                                </option>
                            ))}
                        </select>

                            <div className="flex gap-4">
                            <Input1
                            labelText="Or enter new event name"
                            name="customEventName"
                            value={formData.customEventName}
                            onChange={handleChange}
                            disabled={!!formData.eventID}
                        />
                    <div className="flex justify-center items-center mt-[1rem]">
                    <Button
                            buttonText="Add New Event"
                            onClick={handleAddNewEvent}
                            type="button"
                            disabled={loading || !formData.customEventName.trim()}
                        />
                    </div>
                       
                            </div>
 
                    </div>

                    {/* Event Category Selection */}
                    <div>
                        <label className="block text-lg font-bold text-gray-700 mt-4">Event Category *</label>
                        <select 
                            name="eventCategoryID" 
                            value={formData.eventCategoryID} 
                            onChange={handleChange} 
                            required 
                            className="w-1/4 px-4 py-2 mt-2 mb-4 border border-gray-300 rounded-lg shadow-sm focus:outline-none focus:ring-2 focus:ring-blue-500"
                        >
                            <option value="">Select Category</option>
                            {eventCategories.map(category => (
                                <option key={category.eventCategoryID} value={category.eventCategoryID}>
                                    {category.eventCategoryName}
                                </option>
                            ))}
                        </select>


                            <div className="flex gap-4 ">
                            <Input1
                            labelText="Or enter new category"
                            name="customEventCategory"
                            value={formData.customEventCategory}
                            onChange={handleChange}
                            disabled={!!formData.eventCategoryID}
                        />

                            <div className="flex justify-center items-center mt-[1rem]">
                            <Button
                            buttonText="Add New Category"
                            onClick={handleAddNewCategory}
                            type="button"
                            disabled={loading || !formData.customEventCategory.trim()}
                        />


                            </div>
                     
                            </div>
             
                    </div>




                    <div className="flex gap-40">
                    <div className="w-[30rem]">
                          {/* Other Form Fields */}
                    <CustomDatePicker 
                        labelText="Event Date *" 
                        selectedDate={formData.eventDate} 
                        onChange={handleDateChange} 
                        required 
                        className="mt-6"
                    />
                    
                        </div>


                        <div>

                        <Input1 
                        labelText="Role *" 
                        name="role" 
                        value={formData.role} 
                        onChange={handleChange} 
                        required
                    />

                        </div>
                        </div>

                
                  
                 
                    
                    <label className="block text-lg font-bold text-gray-700 mt-10">Achievement:</label>
                    <select 
                        name="achievement" 
                        value={formData.achievement} 
                        onChange={handleChange} 
                        className="w-1/4 px-4 py-2 mt-2 mb-4 border border-gray-300 rounded-lg shadow-sm focus:outline-none focus:ring-2 focus:ring-blue-500"
                    >
                        <option value="">Select Achievement</option>
                        <option value="1st">1st Place</option>
                        <option value="2nd">2nd Place</option>
                        <option value="3rd">3rd Place</option>
                        <option value="Consolation">Consolation</option>
                    </select>
                    


                    <div className="flex gap-[19.5rem] pt-6">
                    <Input1 
                        labelText="Achievement Details" 
                        name="achievementDetails" 
                        value={formData.achievementDetails} 
                        onChange={handleChange} 
                    />
                    
                    <Input1 
                        labelText="Other Details" 
                        name="otherDetails" 
                        value={formData.otherDetails} 
                        onChange={handleChange} 
                    />
                        </div>
         

                    {/* File Upload Section */}
                    <div className="mt-6">
                        <label className="block text-lg font-bold text-gray-700 mb-2">
                            Upload Certificate * (pdf, doc - max 5MB)
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
};

export default UpdateCulturalDetails;