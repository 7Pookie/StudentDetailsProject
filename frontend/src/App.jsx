import React from 'react';
import { useState } from 'react'
import { BrowserRouter as Router, Route, Routes } from "react-router-dom";
import Home from './pages/home/Home.jsx'
import './App.css'
import Navbar from './components/navbar/Navbar.jsx';
import Profile from './pages/Profile.jsx';
import ViewDetails from './pages/ViewDetails.jsx';
import UpdateDetails from './pages/UpdateDetails.jsx';
import UpdateTechDetails from './pages/UpdateTechDetails.jsx';
import UpdateSportDetails from './pages/UpdateSportDetails.jsx';
import UpdateCulturalDetails from './pages/UpdateCulturalDetails.jsx';
import UpdateProfessionalSocietyDetails from './pages/UpdateProfessionalSocietyDetails.jsx';
import UpdatePublicationsDetails from './pages/UpdatePublicationsDetails.jsx';
import UpdatePlacementDetails from './pages/UpdatePlacementDetails.jsx';
import AddAnnouncements from "./pages/AddAnnouncements.jsx"
import Approvals from './pages/Approvals.jsx';
import PendingApprovals from './pages/PendingApprovals.jsx';
import PreviousApprovals from './pages/PreviousApprovals.jsx';
import RejectedApprovals from './pages/RejectedApprovals.jsx';
import Landing from './pages/landing/Landing.jsx';
import AdminLogin from './pages/login/admin/AdminLogin.jsx';
import StudentLogin from './pages/login/student/StudentLogin.jsx';
import FacultyLogin from './pages/login/faculty/FacultyLogin.jsx';
import AdminDashboard from './components/AdminDashboard.jsx';
function App() {
  return (
    <Router>
      <Navbar />
      <Routes>
        <Route path="/" element={<Landing />}/> 
        <Route path='/Profile' element={<Profile />} />
        <Route path='/student/update-details' element={<UpdateDetails />} />
        <Route path='/student/view-details' element={<ViewDetails />} />
        <Route path='/Approvals' element={<Approvals />} />
        <Route path='/student/update-details/update-technical-details' element={<UpdateTechDetails />} />
        <Route path='/student/update-details/update-sport-details' element={<UpdateSportDetails />} />
        <Route path='/student/update-details/update-cultural-details' element={<UpdateCulturalDetails />} />
        <Route path='/student/update-details/update-professional-society-details' element={<UpdateProfessionalSocietyDetails />} />
        <Route path='/student/update-details/update-publications-details' element={<UpdatePublicationsDetails />} />
        <Route path='/student/update-details/update-placement-details' element={<UpdatePlacementDetails />} />
        <Route path='/admin/add-announcements' element={<AddAnnouncements />} />
        <Route path='/faculty/pending-approvals' element={<PendingApprovals />} />
        <Route path='/faculty/rejected-approvals' element={<RejectedApprovals />} />
        <Route path='/faculty/previous-approvals' element={<PreviousApprovals />} />
        <Route path='/admin/login' element={<AdminLogin />} />
        <Route path='/student/dashboard' element={<Home />} />
        <Route path='/student/login' element={<StudentLogin />} />
        <Route path='/faculty/login' element={<FacultyLogin/>}/>
        <Route path='/admin/dashboard' element={<AdminDashboard />} />
        
        <Route />
      </Routes>
    </Router>
  )
}

export default App;
