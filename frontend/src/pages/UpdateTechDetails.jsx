import React, { useState, useEffect } from "react";
import axios from "axios";
import Input1 from "../components/Input1";
import Button from "../components/Button";
import Sidebar from "../components/Sidebar";
import { fetchEventNames, fetchEventCategories, addTechnicalDetail } from "../api1";
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
            await addTechnicalDetail(formData);
            alert("✅ Technical details added successfully!");
        } catch (error) {
            console.error("❌ Failed to submit details:", error.response?.data || error.message);
            alert("❌ Submission failed. Please try again.");
        }
    };

    return (
        <div className="grid grid-cols-6 mt-20">
            <div>
                <Sidebar />
            </div>
            <div className="col-span-5 p-5">
                <h1 className="text-2xl font-bold">Technical Events Details</h1>
                <form onSubmit={handleSubmit} className="mt-5 space-y-4">
                    {/* ✅ Event Name Selection */}
                    <div>
                        <label className="font-semibold">Event Name *</label>
                        <select name="eventID" value={formData.eventID} onChange={handleChange} required>
                            <option value="">Select Event Name</option>
                            {eventNames.map(event => (
                                <option key={event.eventID} value={event.eventID}>
                                    {event.name}
                                </option>
                            ))}
                        </select>

                        {/* ✅ New Event Name Input */}
                        <Input1
                            labelText="Or enter new event name"
                            name="customEventName"
                            value={formData.customEventName}
                            onChange={handleChange}
                            disabled={!!formData.eventID} // ✅ Disable if dropdown is selected
                        />

                        {/* ✅ Add New Event Button */}
                        <Button
                            buttonText="Add New Event"
                            onClick={handleAddNewEvent}
                            type="button" // Explicitly set type to "button"
                            disabled={loading}
                        />
                    </div>

                    {/* ✅ Event Category Selection */}
                    <div>
                        <label className="font-semibold">Event Category *</label>
                        <select name="eventCategoryID" value={formData.eventCategoryID} onChange={handleChange} required>
                            <option value="">Select Category</option>
                            {eventCategories.map(category => (
                                <option key={category.eventCategoryID} value={category.eventCategoryID}>
                                    {category.eventCategoryName}
                                </option>
                            ))}
                        </select>

                        {/* ✅ New Category Input */}
                        <Input1
                            labelText="Or enter new category"
                            name="customEventCategory"
                            value={formData.customEventCategory}
                            onChange={handleChange}
                            disabled={!!formData.eventCategoryID} // ✅ Disable if dropdown is selected
                        />

                        {/* ✅ Add New Category Button */}
                        <Button
                            buttonText="Add New Category"
                            onClick={handleAddNewCategory}
                            type="button" // Explicitly set type to "button"
                            disabled={loading}
                        />
                    </div>

                    {/* ✅ Other Form Fields */}
                    <CustomDatePicker labelText="Event Date *" selectedDate={formData.eventDate} onChange={handleDateChange} required/>
                    <Input1 labelText="Role *" name="role" value={formData.role} onChange={handleChange} required/>
                    <label>Achievement:</label>
                    <select name="achievement" value={formData.achievement} onChange={handleChange} >
                        <option value="">Select Achievement</option>
                        <option value="1st">1st Place</option>
                        <option value="2nd">2nd Place</option>
                        <option value="3rd">3rd Place</option>
                        <option value="Consolation">Consolation</option>
                    </select>
                    <Input1 labelText="Achievement Details" name="achievementDetails" value={formData.achievementDetails} onChange={handleChange} />
                    <Input1 labelText="Other Details" name="otherDetails" value={formData.otherDetails} onChange={handleChange} />

                    {/* ✅ Submit Button */}
                    <Button buttonText="Submit" type="submit" />
                </form>
            </div>
        </div>
    );
};

export default UpdateTechDetails;
