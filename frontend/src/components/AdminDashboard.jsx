import React from 'react'
import Button from "./Button"
import { Link } from 'react-router-dom'

const AdminDashboard = () => {
  return (
    <div className='p-10'>
        <div className='mb-2'>
            <h1 className='text-3xl font-semibold m-1'>Kranti Sambhav</h1>     
            <h1 className='font-semibold m-1'>12345</h1> 
        </div>   
        <div className='mt-10'>
            <h1 className='font-semibold'>kranti_b220970cs@gmail.com</h1>   
        </div>    
        <div className=' flex justify-between mt-10 gap-10'>
        <Link to="/admin/add-announcements" style={{ textDecoration: "none" }}>
          <Button buttonText='Add Announcements'/>
          </Link>
        </div>     
    </div>
  )
}

export default AdminDashboard