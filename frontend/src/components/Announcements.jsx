import React,{useState, useEffect} from 'react'
import { fetchAnnouncements } from '../api'
import './announcements.css'
const Announcements = () => {
  const [announcements, setAnnouncements] = useState([]);

    useEffect(() => {
        fetchAnnouncements().then(setAnnouncements);
    }, []);

  return (
    <div className='mt-6'>
        <h1 className='text-3xl font-bold underline underline-offset-8'> Announcements</h1>
          <ul className="superlist">
              {announcements.length === 0 ? <p>No announcements available.</p> :
                  announcements.map(ann => (
                    <li key={ann.announcementID} className="list">
                      <strong className="title">{ann.title} ({ann.date})</strong>  {ann.content}
                    </li>
                ))
              }
          </ul>
    </div>
  )
}

export default Announcements