import { useState } from "react"
import { useSetAuthToken, useSetIsAuthenticated, useIsAuthenticated } from "../context/AuthnContainer"
import { Navigate, useLocation, useNavigate } from "react-router-dom"
import config from "../config"
import { getCookie, setCookie } from 'typescript-cookie'
import { useEffect } from "react"
import { AuthTokenModel } from "./model/AuthTokenModel"


export function Login() {
    const setAuthToken = useSetAuthToken()
    const isAuthenticated = useIsAuthenticated()
    const setIsAuthenticated = useSetIsAuthenticated()
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
    useEffect(() => {
        const authToken = getCookie('authToken')
        if (authToken) {
            setAuthToken(authToken)
            setIsAuthenticated(true);
        }
    }, []);
    if (isAuthenticated) {
        return <Navigate to={location.state?.source?.pathname || "/me"} replace={true} />
    }
    function handleSubmit(event: React.FormEvent<HTMLFormElement>) {
        event.preventDefault()
        setIsSubmitting(true)
        const options = {
            method: 'POST',
            headers: { 'Content-Type': 'application/json' },
            body: JSON.stringify(inputs),
            //credentials: 'include' as RequestCredentials
        }
        fetch(`${config.apiUrl}/users/token`, options)
            .then(response => {
                if (!response.ok) {
                    throw new Error(`HTTP error! status: ${response.status}`)
                }
                /*
                // get cookie header
                console.log('cookie header')
                console.log(response.headers.get('Set-Cookie'))
                */
                return response.json()
            })
            .then(data => {
                const authTokenData = JSON.stringify(data)
                const authTokenModel: AuthTokenModel = JSON.parse(authTokenData);
                const authToken = authTokenModel.token
                setIsSubmitting(false)
                setAuthToken(authToken)
                setIsAuthenticated(true)
                setCookie('authToken', authToken, { expires: Date.now() + 1000 * 60 * 60 * 24 * 7 })
                navigate('/me', { state: { source: location }, replace: true })
            })
            .catch(error => {
                setError(error.message)
                setIsSubmitting(false)
            })
    }
    function handleChange(event: React.ChangeEvent<HTMLInputElement>) {
        const name = event.target.name
        const value = event.target.value
        setInputs({
            ...inputs,
            [name]: value
        })
        setError(undefined)
    }


    return (
        <div>
            <form onSubmit={handleSubmit}>
                <h2>Login</h2>
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
                        {isSubmitting ? 'Logging in...' : 'Login'}
                    </button>
                </div>
                {error && <p>{error}</p>}
            </form>
        </div>
    );
}