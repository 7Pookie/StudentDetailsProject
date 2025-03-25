import React from 'react'
import Button from './Button';
import {BsArrowLeftSquareFill} from "react-icons/bs"
import { Link, useNavigate } from 'react-router-dom';

const Sidebar = ({button1Route , button2Route , button3Route}) => {
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
            <Link to={button1Route}>
            <Button buttonText='Pending Approvals' />
            </Link>

            <Link to={button2Route}>
            <Button buttonText='Rejected Approvals' />
            </Link>

            <Link to={button3Route}>
            <Button buttonText='Previous Approvals' />
            </Link>
          </div>
      </div>
    )
}

export default Sidebar