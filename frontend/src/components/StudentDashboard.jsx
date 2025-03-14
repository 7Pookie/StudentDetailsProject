import React, {useState, useEffect} from 'react'
import Button from './Button'
import { Link } from 'react-router-dom'
import axios from 'axios';

const StudentDashboard = () => {
  const [student, setStudent] = useState({ name: "", rollNo: "", email: "" });

  useEffect(() => {
    axios.get("http://localhost:8080/api/students/details", { withCredentials: true }) // ✅ Send session cookies
      .then(response => {
        setStudent(response.data);
      })
      .catch(error => {
        console.error("Error fetching student details:", error);
        alert("Session expired. Please log in again.");
        window.location.href = "/student/login";
      });
  }, []);

  return (
    <div className='p-10 ml-14'>
        <div className='mb-2'>
            <h1 className='text-3xl font-bold m-1'>{student.name}</h1>     
            <h1 className='font-semibold m-1 text-xl'>{student.rollNo}</h1> 
        </div>   
        <div className='mt-10'>
            <h1 className='font-semibold'>{student.email}</h1>   
        </div>    
        <div className=' flex mt-10 gap-20'>
        <Link to="/student/update-details" style={{ textDecoration: "none" }}>
          <Button buttonText='Update Details' />  
        </Link>
        <Link to="/student/view-details" style={{ textDecoration: "none" }}>
          <Button buttonText='View Details'/>
          </Link>
        </div>  
    </div>
  )
}

export default StudentDashboard