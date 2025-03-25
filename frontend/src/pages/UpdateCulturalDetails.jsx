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
                <h1 className="text-3xl font-bold text-gray-700 mb-8 underline">Cultural Events Details</h1>
                <form onSubmit={handleSubmit} className="mt-5 space-y-4">
                    {/* ✅ Event Name Selection */}
                    <div className="">
                      
                        <label className="block text-lg font-bold text-gray-700">Event Name *</label>
                        <select name="eventID" value={formData.eventID} onChange={handleChange} required className="w-1/4 px-4 py-2 mt-2 mb-4 border border-gray-300 rounded-lg shadow-sm focus:outline-none focus:ring-2 focus:ring-blue-500">
                            <option value="">Select Event Name</option>
                            {eventNames.map(event => (
                                <option key={event.eventID} value={event.eventID}>
                                    {event.name}
                                </option>
                            ))}
                        </select>

                        <div className="flex gap-5 ">
                        <Input1
                            labelText="Or enter new event name"
                            name="customEventName"
                            value={formData.customEventName}
                            onChange={handleChange}
                            disabled={!!formData.eventID} 
                        />
                        <div className="mt-[2rem]">
                        <Button
                            buttonText="Add New Event"
                            onClick={handleAddNewEvent}
                            type="button" 
                            disabled={loading}
                        />
                        </div>
                        </div>
                    </div>

                    <div className="">
                        <label className="block text-lg font-bold text-gray-700 mt-10">Event Category *</label>
                        <select name="eventCategoryID" value={formData.eventCategoryID} onChange={handleChange} required className="w-1/4 px-4 py-2 mt-2 mb-4 border border-gray-300 rounded-lg shadow-sm focus:outline-none focus:ring-2 focus:ring-blue-500">
                            <option value="">Select Category</option>
                            {eventCategories.map(category => (
                                <option key={category.eventCategoryID} value={category.eventCategoryID}>
                                    {category.eventCategoryName}
                                </option>
                            ))}
                        </select>

                        <div className="flex gap-5">
                        <Input1
                            labelText="Or enter new category"
                            name="customEventCategory"
                            value={formData.customEventCategory}
                            onChange={handleChange}
                            disabled={!!formData.eventCategoryID} // ✅ Disable if dropdown is selected
                        />
                        
                        <div className="mt-[2rem]">
                        <Button
                            buttonText="Add New Category"
                            onClick={handleAddNewCategory}
                            type="button" // Explicitly set type to "button"
                            disabled={loading}
                        />
                        </div>
                        {/* ✅ Add New Category Button */}
                        </div>
                        {/* ✅ New Category Input */}
                    </div>
                    

                    <div className="w-1/2">
                    {/* ✅ Other Form Fields */}
                    <CustomDatePicker labelText="Event Date *" selectedDate={formData.eventDate} onChange={handleDateChange} required className=" w-full"/>
                    </div>
                      
                    <div className="flex gap-64 ">
                    <Input1 labelText="Role *" name="role" value={formData.role} onChange={handleChange} required/>
                        
                        <div className="flex  gap-6 px-20"> 
                        <label className="block text-lg font-bold text-gray-700 mt-8">Achievement:</label>
<select name="achievement" value={formData.achievement} onChange={handleChange} className="w-full px-4 py-2  mt-6 mb-8 border border-gray-300 rounded-lg shadow-sm focus:outline-none focus:ring-2 focus:ring-blue-500">
    <option value="">Select Achievement</option>
    <option value="1st">1st Place</option>
    <option value="2nd">2nd Place</option>
    <option value="3rd">3rd Place</option>
    <option value="Consolation">Consolation</option>
</select>
                        </div>


                    </div>

                    <div className="flex gap-[21rem]">
                    <Input1 labelText="Achievement Details" name="achievementDetails" value={formData.achievementDetails} onChange={handleChange} />
                    <Input1 labelText="Other Details" name="otherDetails" value={formData.otherDetails} onChange={handleChange} />
                    </div>

                    <div className="flex flex-col gap-8">
                    <div className="">
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
