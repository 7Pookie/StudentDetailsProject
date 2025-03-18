import React, {useState, useEffect} from 'react';
import Button from './Button'
import { Link } from 'react-router-dom'

const FacultyDashboard = () => {
  return (
    <div className='p-10'>
    <div className='mb-2'>
        <h1 className='text-3xl font-semibold m-1'>Joe Cherry Rose</h1>     
        <h1 className='font-semibold m-1'>12345</h1> 
    </div>   
    <div className='mt-10'>
        <h1 className='font-semibold'>joe_b29430@nitc.ac.in</h1>   
    </div>    
    <div className=' flex justify-between mt-10 gap-10'>
    <Link to="/Approvals" style={{ textDecoration: "none" }}>
      <Button buttonText='Approvals'/>
      </Link>
    </div>   
</div>
  )
}

export default FacultyDashboard