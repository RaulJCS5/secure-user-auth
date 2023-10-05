import { useNavigate, useLocation } from "react-router-dom"
import { useSetIsAuthenticated, useSetAuthToken, useAuthToken } from "../context/AuthnContainer"
import { useState } from "react"
import config from "../config"
import { removeCookie } from 'typescript-cookie'

export function Logout() {
    const [isSubmitting, setIsSubmitting] = useState(false)
    const [error, setError] = useState<string | undefined>(undefined)
    const setIsAuthenticated = useSetIsAuthenticated()
    const setAuthToken = useSetAuthToken()
    const authToken = useAuthToken()
    const navigate = useNavigate()
    const location = useLocation()
    function handleLogout(event: React.FormEvent<HTMLFormElement>) {
        event.preventDefault()
        setIsSubmitting(true)
        const authTokenBearer = `Bearer ${authToken}`
        const options = {
            method: 'POST',
            headers: {
                'Content-Type': 'application/json',
                Authorization: authTokenBearer
            },
        }
        fetch(`${config.apiUrl}/signout`, options)
            .then(response => {
                if (!response.ok) {
                    throw new Error(`HTTP error! status: ${response.status}`)
                }
            })
            .then(data => {
                // Remove the authentication cookie
                removeCookie('authToken')
                setAuthToken(undefined);
                setIsAuthenticated(false);
                setIsSubmitting(false);
                // Navigate to the login page after successful logout
                navigate('/login', { state: { source: location }, replace: true })
            })
            .catch(error => {
                setError(error.message)
                setIsSubmitting(false)
            })
    }
    return (
        <div>
            <h1>Logout</h1>
            <div>
                <form onSubmit={handleLogout}>
                    <button type="submit" disabled={isSubmitting}>
                        Logout
                    </button>
                    {error && <p>{error}</p>}
                </form>
            </div>
        </div>
    )
}