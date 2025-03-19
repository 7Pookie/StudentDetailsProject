import React from 'react'
import Input from '../components/Input'
import Button from '../components/Button'
import Sidebar1 from "../components/Sidebar1"
import UpdateDetailCard from '../components/UpdateDetailCard'
import { Link } from 'react-router-dom'

const Approvals = () => {
    return (
        <div className="grid grid-cols-6 mt-20">
          <div>
            <Sidebar1 />
          </div>
          <div className="col-span-5 p-5">
            <div>
              <h1 className='text-2xl font-bold'>Approvals</h1>
              <div className=" mt-5">
                <Link to="/faculty/approvals/pending-approvals" style={{ textDecoration: "none" }}>
                <UpdateDetailCard updateCardText={"Pending Approvals"} updateCardImage={"https://t3.ftcdn.net/jpg/06/15/01/96/240_F_615019661_OMfklHQ2szeHoTPzwESacn4XWMbRFLOU.jpg"} />
                </Link>
                <Link to="/faculty/approvals/rejected-approvals" style={{ textDecoration: "none" }}>
                <UpdateDetailCard updateCardText={"Rejected Approvals"} updateCardImage={"https://media.istockphoto.com/id/1155175683/vector/rejected-red-grunge-round-vintage-rubber-stamp.jpg?s=612x612&w=0&k=20&c=f2568gaqEOSfsc5-QfflHwEzkWDx1rPKXLShB6pO-PQ="} />
                </Link>
                <Link to="/faculty/approvals/previous-approvals" style={{ textDecoration: "none" }}>
                <UpdateDetailCard updateCardText={"Previous Approvals"} updateCardImage={"https://plus.unsplash.com/premium_photo-1725075086634-0037c3040c54?w=600&auto=format&fit=crop&q=60&ixlib=rb-4.0.3&ixid=M3wxMjA3fDB8MHxzZWFyY2h8MXx8c2FuZCUyMGNsb2NrfGVufDB8fDB8fHww"} />
                </Link>
              </div>
            </div>
          </div>
        </div>
      )
}

export default Approvals