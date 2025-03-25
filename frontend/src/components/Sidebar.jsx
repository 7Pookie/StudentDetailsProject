import React from 'react'
import Button from './Button';
import {BsArrowLeftSquareFill} from "react-icons/bs"
import { Link, useNavigate } from 'react-router-dom';

const Sidebar = ({button1route , button2route , button3route}) => {
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
              <Link to={button1route}>
              <Button buttonText='Update Details' />
              </Link>

              <Link to={button2route}>
              <Button buttonText='View Details' />
              </Link>

              <Link to={button3route}>
              <Button buttonText='User Profile' />
              </Link>

          </div>
      </div>
    )
}

export default Sidebar