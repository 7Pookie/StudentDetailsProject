import React from 'react'
import Button from './Button';
import {BsArrowLeftSquareFill} from "react-icons/bs"
import { useNavigate } from 'react-router-dom';

const Sidebar = () => {
    const navigate = useNavigate();
    const handleBack = () => {
      navigate(-1); // Navigates to the previous page
    }
    return (
      <div className='p-6'>
          <div onClick={handleBack} className='cursor-pointer'>
            <BsArrowLeftSquareFill className='text-3xl' />
          </div>
          <div className='flex flex-col gap-1 mt-6'>
              <Button buttonText='Pending Approvals' />
              <Button buttonText='Rejected Approvals'/>
              <Button buttonText='Approval History'/>
          </div>
      </div>
    )
}

export default Sidebar