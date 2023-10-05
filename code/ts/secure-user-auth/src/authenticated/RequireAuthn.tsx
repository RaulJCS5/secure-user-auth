import * as React from 'react'
import { Navigate, useLocation } from 'react-router-dom'
import { useIsAuthenticated } from '../context/AuthnContainer'

export function RequireAuthn({ children }: { children: React.ReactNode }): React.ReactElement {
    const isAuthenticated = useIsAuthenticated()
    const location = useLocation()
    if (isAuthenticated) {
        return <>{children}</>
    } else {
        return <Navigate to="/login" state={{ source: location.pathname }} replace={true} />
    }
}