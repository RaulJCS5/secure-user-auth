import {
    Link, Outlet,
} from 'react-router-dom'
import { useChronometer } from '../context/AuthnContainer';

function Home() {
    const chronometer = useChronometer()
    return (
        <div>
            <header>
                <Link to="/">Project</Link>
                <div>
                    <Link to="/me">Me</Link>
                    <Link to="/messages">Messages</Link>
                    <Link to="/about">About</Link>
                    <Link to="/contact">Contact</Link>
                    <Link to="/login">Login</Link>
                    <Link to="/register">Register</Link>
                    <Link to="/logout">Logout</Link>
                </div>
            </header>
            <span>Chronometer: {chronometer.days}:{chronometer.hours}:{chronometer.minutes}:{chronometer.seconds}</span>
            <Outlet />
            <footer>
                <span>Made with ❤ by </span>
                <a
                    href="https://github.com/RaulJCS5"
                    target="_blank"
                    rel="noopener noreferrer"
                    style={{ textDecoration: 'none', }}
                >
                    RaulJCS5
                </a>
                <span> © 2023 </span>
                <a
                    href="https://reactjs.org"
                    target="_blank"
                    rel="noopener noreferrer"
                >
                    Learn React
                </a>
            </footer>
        </div>
    )
}

export default Home;