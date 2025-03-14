import React from 'react'
import Sidebar from '../components/Sidebar'
import UpdateDetailCard from "../components/UpdateDetailCard"
import { Link } from 'react-router-dom'

const UpdateDetails = () => {
    return (
        <div className="grid grid-cols-6 mt-20">
              <div className='fixed mt-[2rem]'>
                <Sidebar />
              </div>
              <div className="col-start-2 col-span-5 p-5">
                <div>
                  <h1 className='text-2xl font-bold'>Update Participation Details</h1>
                  <div className=" mt-10">
                    <Link to="/student/update-details/update-technical-details" style={{ textDecoration: "none" }}>
                    <UpdateDetailCard updateCardText={"Technical Events"} updateCardImage={"https://images.unsplash.com/photo-1485827404703-89b55fcc595e?q=80&w=2070&auto=format&fit=crop&ixlib=rb-4.0.3&ixid=M3wxMjA3fDB8MHxwaG90by1wYWdlfHx8fGVufDB8fHx8fA%3D%3D"} />
                    </Link>
                    <Link to="/UpdateCulturalDetails" style={{ textDecoration: "none" }}>
                    <UpdateDetailCard updateCardText={"Cultural Events"} updateCardImage={"https://plus.unsplash.com/premium_photo-1689838026693-02c808d3c794?q=80&w=1974&auto=format&fit=crop&ixlib=rb-4.0.3&ixid=M3wxMjA3fDB8MHxwaG90by1wYWdlfHx8fGVufDB8fHx8fA%3D%3D"} />
                    </Link>
                    <Link to="/UpdateSportDetails" style={{ textDecoration: "none" }}>
                    <UpdateDetailCard updateCardText={"Sports Events"} updateCardImage={"https://images.unsplash.com/photo-1628779238951-be2c9f2a59f4?q=80&w=1974&auto=format&fit=crop&ixlib=rb-4.0.3&ixid=M3wxMjA3fDB8MHxwaG90by1wYWdlfHx8fGVufDB8fHx8fA%3D%3D"} />
                    </Link>
                    <Link to="/UpdateProfessionalSocietyDetails" style={{ textDecoration: "none" }}>
                    <UpdateDetailCard updateCardText={"Professional Societies"} updateCardImage={"https://plus.unsplash.com/premium_photo-1661756423422-4486e27eb6dd?q=80&w=2070&auto=format&fit=crop&ixlib=rb-4.0.3&ixid=M3wxMjA3fDB8MHxwaG90by1wYWdlfHx8fGVufDB8fHx8fA%3D%3D"} />
                    </Link>
                    <Link to="/UpdatePublicationsDetails" style={{ textDecoration: "none" }}>
                    <UpdateDetailCard updateCardText={"Publications"} updateCardImage={"https://images.unsplash.com/photo-1504711331083-9c895941bf81?q=80&w=2070&auto=format&fit=crop&ixlib=rb-4.0.3&ixid=M3wxMjA3fDB8MHxwaG90by1wYWdlfHx8fGVufDB8fHx8fA%3D%3D"} />
                    </Link>
                    <Link to="/UpdatePlacementDetails" style={{ textDecoration: "none" }}>
                    <UpdateDetailCard updateCardText={"Placements"} updateCardImage={"https://images.unsplash.com/photo-1504711331083-9c895941bf81?q=80&w=2070&auto=format&fit=crop&ixlib=rb-4.0.3&ixid=M3wxMjA3fDB8MHxwaG90by1wYWdlfHx8fGVufDB8fHx8fA%3D%3D"} />
                    </Link>
                    <UpdateDetailCard updateCardText={"Clubs"} updateCardImage={"https://images.unsplash.com/photo-1556721546-9918b5b57fa2?q=80&w=2070&auto=format&fit=crop&ixlib=rb-4.0.3&ixid=M3wxMjA3fDB8MHxwaG90by1wYWdlfHx8fGVufDB8fHx8fA%3D%3D"} />
                  </div>
                </div>
              </div>
            </div>
          )
}

export default UpdateDetails