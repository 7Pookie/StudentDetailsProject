import axios from "axios";

const API_BASE_URL = "http://localhost:8080/api/technical-details";  // Adjust your backend URL

// Fetch all event names
export const fetchEventNames = async () => {
    const response = await axios.get(`${API_BASE_URL}/event-names`);
    return response.data;
};

// Fetch all event categories
export const fetchEventCategories = async () => {
    const response = await axios.get(`${API_BASE_URL}/event-categories`);
    return response.data;
};

// Submit technical details
export const addTechnicalDetail = async (technicalDetail) => {
    const response = await axios.post(`${API_BASE_URL}/add`, technicalDetail);
    return response.data;
};
