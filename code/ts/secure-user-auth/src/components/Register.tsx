import React, { useState } from 'react'
import { Navigate, useLocation, useNavigate } from 'react-router-dom'
import { useIsAuthenticated } from '../context/AuthnContainer'
import config from '../config'

export function Register() {
    const isAuthenticated = useIsAuthenticated()
    const [isSubmitting, setIsSubmitting] = useState(false)
    const [error, setError] = useState<string | undefined>(undefined)
    const navigate = useNavigate()
    const location = useLocation()
    const [inputs, setInputs] = useState(
        {
            username: '',
            password: ''
        }
    )
    if (isAuthenticated) {
        return <Navigate to={location.state?.source?.pathname || "/me"} replace={true} />
    }
    function handleChange(event: React.ChangeEvent<HTMLInputElement>) {
        setInputs({
            ...inputs,
            [event.target.name]: event.target.value
        })
    }
    async function handleSubmit(event: React.FormEvent<HTMLFormElement>) {
        event.preventDefault()
        setIsSubmitting(true)
        const options = {
            method: 'POST',
            headers: { 'Content-Type': 'application/json' },
            body: JSON.stringify(inputs)
        }
        fetch(`${config.apiUrl}/users`, options)
            .then(response => {
                if (!response.ok) {
                    throw new Error(`HTTP error! status: ${response.status}`)
                }
            })
            .then(data => {
                setError(undefined);
                setIsSubmitting(false);
                navigate('/login', { state: { source: location }, replace: true })
            })
            .catch(error => {
                setError(error.message);
                setIsSubmitting(false);
            })
    }
    return (
        <div>
            <form onSubmit={handleSubmit}>
                <h2>Register</h2>
                <div>
                    <label htmlFor="username">Username</label>
                    <input
                        id="username"
                        type="text"
                        name="username"
                        value={inputs.username}
                        onChange={handleChange}
                        placeholder="Enter your username"
                        required
                    />
                </div>
                <div>
                    <label htmlFor="password">Password</label>
                    <input
                        id="password"
                        type="password"
                        name="password"
                        value={inputs.password}
                        onChange={handleChange}
                        placeholder="Enter your password"
                        required
                    />
                </div>
                <div>
                    <button type="submit" disabled={isSubmitting}>
                        {isSubmitting ? 'Registering...' : 'Register'}
                    </button>
                </div>
                {error && <p>{error}</p>}
            </form>
        </div>
    );
}