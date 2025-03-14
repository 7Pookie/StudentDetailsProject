import React,{useState} from 'react'
import Input from '../components/Input'
import Button from '../components/Button'
import Sidebar from "../components/Sidebar"
import './addannouncement.css';
import { addAnnouncement } from '../api'

const AddAnnouncements = ({token}) => {
    const [title, setTitle] = useState("");
    const [content, setContent] = useState("");
    const [message, setMessage] = useState("");

    const handleSubmit = async (e) => {
        e.preventDefault();
        try {
            await addAnnouncement(title, content, token);
            setMessage("Announcement added successfully!");
            setTitle("");
            setContent("");
        } catch (error) {
          setMessage("Failed to add announcement.");
      }
  };
    return (
        <div className="grid grid-cols-6 mt-20">
        <div>
          <Sidebar />
        </div>
        <div className="col-span-5 p-5">
          <div>
            <h1 className='text-2xl font-bold'>Announcements</h1>
            <div className="formdata">
            {message && <p>{message}</p>}
            <form className="form" onSubmit={handleSubmit}>
                <input
                    className="input"
                    type="text"
                    placeholder="Title"
                    value={title}
                    onChange={(e) => setTitle(e.target.value)}
                    required
                />
                <textarea
                    className="input1"
                    placeholder="Content"
                    value={content}
                    onChange={(e) => setContent(e.target.value)}
                    required
                />
                <button className="button" type="submit">Submit</button>
            </form>
            </div>
          </div>
        </div>
      </div>
      )
}

export default AddAnnouncements;