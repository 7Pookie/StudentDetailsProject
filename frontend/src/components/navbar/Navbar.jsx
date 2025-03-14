import {Link} from 'react-router-dom';
import './navbar.css';
const Navbar = () => {
    return (
        <div className="navbar border border-b-2 bg-black ">
        <div className="logo">
            <Link to="/student/dashboard" style={{ textDecoration: "none" }}>
                <span className="logo-entry">EduTech</span>
            </Link>
        </div>
        <div className="links">
            <Link to="/student/dashboard" style={{ textDecoration: "none" }}>
                <div className="flex justify-between items-center gap-1.5">
                <div className="h-[5px] w-[5px] rounded-full bg-green-600 shadow-md shadow-green-700 transition-all duration-200 hover:bg-green-900"></div>
                    <span className="entries hover:">Home</span>
                </div>
            </Link>
            <Link to="/Profile" style={{ textDecoration: "none" }}>
            <div className="flex justify-between items-center gap-1.5">
            <div className="h-[5px] w-[5px] rounded-full bg-green-600 shadow-md shadow-green-700 transition-all duration-200 hover:bg-green-900"></div>
                    <span className="entries">Profile</span>
                </div>
            </Link>
            <Link to="/Help" style={{ textDecoration: "none" }}>
            <div className="flex justify-between items-center gap-1.5">
            <div className="h-[5px] w-[5px] rounded-full bg-green-600 shadow-md shadow-green-700 transition-all duration-200 hover:bg-green-900"></div>
                <span className="entries">Help</span>
                </div>
            </Link>
        </div>
    </div>
    );
};

export default Navbar;