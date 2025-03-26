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
import ApprovePage from './pages/ApprovePage.jsx';
import ViewRequest from './pages/ViewRequest.jsx';
import UpdateRequest from './pages/UpdateRequest.jsx';
import UpadateClubDetails from './pages/UpdateClubDetails.jsx'
import HomeAdmin from './pages/HomeAdmin/HomeAdmin.jsx';
import HomeFaculty from './pages/HomeFaculty/HomeFaculty.jsx';


function App() {
  const [currentPage, setCurrentPage] = useState(window.location.pathname);
  return (
    <Router>
      { !(currentPage == "/student/login" || currentPage == "/admin/login" || currentPage == "/faculty/login" || currentPage == "/"  ) && <Navbar />}
      <Routes>
        <Route path="/" element={<Landing />}/> 
        <Route path='/student/profile/:studentId' element={<Profile />} />
        <Route path='/student/view-details' element={<ViewDetails />} />
        <Route path='/student/update-details' element={<UpdateDetails />} />
        <Route path='/faculty/approvals' element={<Approvals />} />
        <Route path='/student/update-details/update-technical-details' element={<UpdateTechDetails />} />
        <Route path='/student/update-details/update-sport-details' element={<UpdateSportDetails />} />
        <Route path='/student/update-details/update-cultural-details' element={<UpdateCulturalDetails />} />
        <Route path='/student/update-details/update-professional-society-details' element={<UpdateProfessionalSocietyDetails />} />
        <Route path='/student/update-details/update-publications-details' element={<UpdatePublicationsDetails />} />
        <Route path='/student/update-details/update-placement-details' element={<UpdatePlacementDetails />} />
        <Route path='/student/update-details/update-club-details' element={<UpadateClubDetails />} />
        <Route path='/admin/add-announcements' element={<AddAnnouncements />} />
        <Route path='/faculty/approvals/pending-approvals' element={<PendingApprovals />} />
        <Route path='/faculty/approvals/rejected-approvals' element={<RejectedApprovals />} />
        <Route path='/faculty/approvals/previous-approvals' element={<PreviousApprovals />} />
        <Route path='/admin/login' element={<AdminLogin />} />
        <Route path='/student/dashboard' element={<Home />} />
        <Route path='/admin/dashboard' element={<HomeAdmin />} />
        <Route path='/student/login' element={<StudentLogin />} />
        <Route path='/faculty/login' element={<FacultyLogin/>}/>
        <Route path='/admin/dashboard' element={<AdminDashboard />} />
        <Route path='/faculty/dashboard' element={<HomeFaculty />} />
        <Route path='/faculty/approve/:requestID' element={<ApprovePage />} />
        <Route path='/student/request/:requestID' element={<ViewRequest />} />
        <Route path='/student/request/:requestID/update' element={<UpdateRequest />} />
        <Route path='/student/request/:requestID' element={<ViewRequest />} />
        <Route path='/student/request/:requestID/update' element={<UpdateRequest />} />
        <Route />
      </Routes>
    </Router>
  )
}

export default App;
