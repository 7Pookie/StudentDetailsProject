import axios from "axios";

// Base URL
const API_BASE_URL = "http://localhost:8080/api/announcements";

// Function to initiate Google OAuth2 login
export const googleLogin = () => {
    window.location.href = "http://localhost:8080/oauth2/authorization/google";
};

// Function to fetch all announcements
export const fetchAnnouncements = async () => {
    try {
        const response = await axios.get(`${API_BASE_URL}/all`, {
            withCredentials: true, // ✅ Includes cookies/session info for OAuth
            headers: {
                "Content-Type": "application/json",
            },
        });
        return response.data; // ✅ Axios automatically parses JSON
    } catch (error) {
        console.error("Error fetching announcements:", error);
        throw error;
    }
};

// Function to add a new announcement
export const addAnnouncement = async (title, content, token) => {
    console.log("🔑 Admin Token:", token);
    try {
        const response = await axios.post(
            "http://localhost:8080/api/announcements/add",
            { title, content },
            {
                withCredentials: true, // ✅ Ensures cookies are sent with requests
                headers: {
                    "Authorization": `Bearer ${token}`,
                    "Content-Type": "application/json",
                },
                body: JSON.stringify({ title, content }),
            }
        );
        return response.data;
    } catch (error) {
        console.error("Failed to add announcement:", error.response?.data || error.message);
        throw error;
    }
};
