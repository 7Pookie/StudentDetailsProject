import {Link} from 'react-router-dom';
import './home.css';
import Button from '../../components/Button';
import Announcements from '../../components/Announcements';
import AdminDashboard from '../../components/AdminDashboard';

const HomeAdmin = () => {
    return (
        <div>
            <div className="head underline underline-offset-8 mr-20 flex justify-center">Admin Dashboard</div>
            <div className="page">
                <div className="card">
                    <div className="info">
                        <div className="text">
                            <AdminDashboard />
                            <Announcements />
                        </div>
                    </div>
                </div>
            </div>
        </div>
    );
};

export default HomeAdmin;