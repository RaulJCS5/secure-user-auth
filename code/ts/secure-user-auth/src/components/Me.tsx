import { useIsAuthenticated, useAuthToken } from "../context/AuthnContainer"

export function Me() {
    const authToken = useAuthToken()
    const isAuthenticated = useIsAuthenticated()
    return (
        <div>
            <h1>Me</h1>
            <div>
                <p>User:
                    <span> {authToken}</span>
                </p>
                <p>IsAuthenticated:
                    <span> {isAuthenticated.toString()}</span>
                </p>
            </div>
        </div>
    )
}