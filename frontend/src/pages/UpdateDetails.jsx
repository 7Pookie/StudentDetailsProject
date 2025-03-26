import React from 'react'
import Sidebar from '../components/Sidebar'
import UpdateDetailCard from "../components/UpdateDetailCard"
import { Link } from 'react-router-dom'

const UpdateDetails = () => {
    return (
        <div className="grid grid-cols-6 mt-20">
              <div className='fixed mt-[2rem]'>
                <Sidebar button1route={"/student/update-details"} button2route={"/student/view-details"} button3route={"/student/profile/:studentId"} />
              </div>
              <div className="col-start-2 col-span-5 p-5">
                <div>
                  <h1 className='text-2xl font-bold'>Update Participation Details</h1>
                  <div className=" mt-10">
                    <Link to="/student/update-details/update-technical-details" style={{ textDecoration: "none" }}>
                    <UpdateDetailCard updateCardText={"Technical Events"} updateCardImage={"https://cdn-icons-png.flaticon.com/128/4257/4257483.png"} />
                    </Link>
                    <Link to="/student/update-details/update-cultural-details" style={{ textDecoration: "none" }}>
                    <UpdateDetailCard updateCardText={"Cultural Events"} updateCardImage={"https://cdn-icons-png.flaticon.com/128/1778/1778557.png"} />
                    </Link>
                    <Link to="/student/update-details/update-sport-details" style={{ textDecoration: "none" }}>
                    <UpdateDetailCard updateCardText={"Sports Events"} updateCardImage={"https://cdn-icons-png.flaticon.com/128/4163/4163679.png"} />
                    </Link>
                    <Link to="/student/update-details/update-professional-society-details" style={{ textDecoration: "none" }}>
                    <UpdateDetailCard updateCardText={"Professional Societies"} updateCardImage={"https://cdn-icons-png.flaticon.com/128/2562/2562464.png"} />
                    </Link>
                    <Link to="/student/update-details/update-publications-details" style={{ textDecoration: "none" }}>
                    <UpdateDetailCard updateCardText={"Publications"} updateCardImage={"https://cdn-icons-png.flaticon.com/128/2680/2680927.png"} />
                    </Link>
                    <Link to="/student/update-details/update-placement-details" style={{ textDecoration: "none" }}>
                    <UpdateDetailCard updateCardText={"Placements"} updateCardImage={"https://cdn-icons-png.flaticon.com/128/15188/15188745.png"} />
                    </Link>
                    <Link to="/student/update-details/update-club-details" style={{ textDecoration: "none" }}>
                    <UpdateDetailCard updateCardText={"Clubs"} updateCardImage={"https://cdn-icons-png.flaticon.com/128/9495/9495009.png"} />
                    </Link>
                  </div>
                </div>
              </div>
            </div>
          )
}

export default UpdateDetails