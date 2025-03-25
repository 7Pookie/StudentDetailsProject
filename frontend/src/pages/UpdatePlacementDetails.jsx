import React, { useState, useEffect } from "react";
import axios from "axios";
import Input1 from "../components/Input1";
import Button from "../components/Button";
import Sidebar from "../components/Sidebar";
import { fetchEventNames, fetchEventCategories,addCulturalDetail } from "../api2";
import "react-datepicker/dist/react-datepicker.css";
import CustomDatePicker from "./CustomDatePicker";

const UpdateTechDetails = () => {
    const [eventNames, setEventNames] = useState([]);
    const [eventCategories, setEventCategories] = useState([]);
    const [loading, setLoading] = useState(false);

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

    // ✅ Fetch Student ID from Backend API
    useEffect(() => {
        axios.get("http://localhost:8080/api/students/me", { withCredentials: true })
            .then(response => {
                if (response.data.studentID) {
                    setFormData(prevState => ({ ...prevState, studentID: response.data.studentID }));
                }
            })
            .catch(error => console.error("Failed to fetch student ID:", error));
    }, []);

    // ✅ Fetch Event Names & Categories
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
        console.log(`Updated Field: ${e.target.name}, Value: ${e.target.value}`);
    };

    const handleDateChange = (date) => {
        setFormData(prevState => ({ ...prevState, eventDate: date }));
    };

    // ✅ Add New Event to Database
    const handleAddNewEvent = async (e) => {
        e.preventDefault(); // Prevent form submission
        if (!formData.customEventName.trim()) {
            alert("❌ Please enter a new event name before adding.");
            return;
        }
        try {
            setLoading(true);
            const response = await axios.post("http://localhost:8080/api/events/add", {
                name: formData.customEventName
            });

            alert("✅ New event added successfully!");
            setFormData({ ...formData, eventID: response.data.eventID, customEventName: "" });
            fetchData(); // Refresh dropdowns
        } catch (error) {
            alert("❌ Failed to add new event.");
            console.error("Error adding event:", error);
        } finally {
            setLoading(false);
        }
    };

    // ✅ Add New Category to Database
    const handleAddNewCategory = async (e) => {
        e.preventDefault();
        if (!formData.customEventCategory.trim()) {
            alert("❌ Please enter a new category before adding.");
            return;
        }
        try {
            setLoading(true);
            const response = await axios.post("http://localhost:8080/api/eventCategories/add", {
                categoryName: formData.customEventCategory
            });

            alert("✅ New category added successfully!");
            setFormData({ ...formData, eventCategoryID: response.data.eventCategoryID, customEventCategory: "" });
            fetchData(); // Refresh dropdowns
        } catch (error) {
            alert("❌ Failed to add new category.");
            console.error("Error adding category:", error);
        } finally {
            setLoading(false);
        }
    };

    // ✅ Form Submission
    const handleSubmit = async (e) => {
        e.preventDefault();
        console.log("🛠️ Submitting data:", formData);

        if (!formData.studentID) {
            alert("❌ Student ID is missing. Please log in again.");
            return;
        }

        try {
            await addCulturalDetail(formData);
            alert("✅ Technical details added successfully!");
        } catch (error) {
            console.error("❌ Failed to submit details:", error.response?.data || error.message);
            alert("❌ Submission failed. Please try again.");
        }
    };

    return (
        <div className="grid grid-cols-6 mt-20">
            <div className="pt-16">
                <Sidebar />
            </div>
            <div className="col-span-5 bg-white py-5 px-16 rounded-lg shadow-lg">
                <h1 className="text-3xl font-bold text-gray-700 mb-8 underline">Placement / Internships Details</h1>
                <form onSubmit={handleSubmit} className="mt-5 space-y-4">
                    {/* ✅ Event Name Selection */}
                    <div className="">
                      
                        <label className="block text-lg font-bold text-gray-700">Placement Type *</label>
                        <select name="eventID" value={formData.eventID} onChange={handleChange} required className="w-1/4 px-4 py-2 mt-2 mb-4 border border-gray-300 rounded-lg shadow-sm focus:outline-none focus:ring-2 focus:ring-blue-500">
                            <option value="">Select placement type</option>
                            {eventNames.map(event => (
                                <option key={event.eventID} value={event.eventID}>
                                    {event.name}
                                </option>
                            ))}
                        </select>
                    </div>

                    <div className="">
                        <label className="block text-lg font-bold text-gray-700 mt-10">Company Name *</label>
                        <select name="eventCategoryID" value={formData.eventCategoryID} onChange={handleChange} required className="w-1/4 px-4 py-2 mt-2 mb-4 border border-gray-300 rounded-lg shadow-sm focus:outline-none focus:ring-2 focus:ring-blue-500">
                            <option value="">Select Company</option>
                            {eventCategories.map(category => (
                                <option key={category.eventCategoryID} value={category.eventCategoryID}>
                                    {category.eventCategoryName}
                                </option>
                            ))}
                        </select>
                        {/* ✅ New Category Input */}
                    </div>
                    

                    <div className="w-1/2">
                    {/* ✅ Other Form Fields */}
                    <CustomDatePicker labelText="Joining Date" selectedDate={formData.eventDate} onChange={handleDateChange} required className=" w-full"/>
                    </div>

                    <div className="w-1/2">
                    {/* ✅ Other Form Fields */}
                    <CustomDatePicker labelText="Ending Date (For internships only)*" selectedDate={formData.eventDate} onChange={handleDateChange} required className=" w-full"/>
                    </div>
                      
                    <div className="flex gap-64 ">
                    <Input1 labelText="Role *" name="role" value={formData.role} onChange={handleChange} required/>
                    </div>

                  

                    <div className="flex flex-col gap-8">
                    <div className="flex flex-col gap-2">
                    <label className="block text-md font-semibold text-gray-700 ">Offer Letter *</label>
                    <input type="file"  className=""/>
                    </div>

                    {/* ✅ Submit Button */}
                    <div className="">
                    <Button buttonText="Submit" type="submit" />
                    </div>
                    </div>

                    
                </form>
            </div>
        </div>
    );
};

export default UpdateTechDetails;
