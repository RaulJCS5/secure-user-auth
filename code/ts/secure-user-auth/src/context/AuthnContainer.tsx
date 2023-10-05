import {
    useState,
    createContext,
    useContext,
    useEffect,
} from 'react';

type AuthnContextType = {
    isAuthenticated: boolean,
    setIsAuthenticated: (isAuthenticated: boolean) => void,
    authToken: string | undefined,
    setAuthToken: (user: string | undefined) => void,
    chronometer: ChronometerType,
    setChronometer: (chronometer: ChronometerType) => void,
}
type ChronometerType = {
    days: string,
    hours: string,
    minutes: string,
    seconds: string,
}

const AuthnContext = createContext<AuthnContextType>({
    isAuthenticated: false,
    setIsAuthenticated: () => { },
    authToken: undefined,
    setAuthToken: () => { },
    chronometer: { days: "", hours: "", minutes: "", seconds: "" },
    setChronometer: () => { },
});

function AuthnContainer({ ...props }) {
    const [isAuthenticated, setIsAuthenticated] = useState(false);
    const [authToken, setAuthToken] = useState<string | undefined>(undefined);
    const [counter, setCounter] = useState(0);
    const [chronometer, setChronometer] = useState<ChronometerType>({ days: "", hours: "", minutes: "", seconds: "" });
    useEffect(() => {
        const tid = setInterval(
            () => setCounter((oldState) => oldState + 1),
            1000,
        )
        return () => {
            clearInterval(tid)
        }
    }, [counter, setCounter])

    useEffect(() => {
        // Calculate the total number of seconds from the counter value
        const totalSeconds = counter;
        // Calculate days, hours, minutes, and seconds
        const days = Math.floor(totalSeconds / (3600 * 24));
        const hours = Math.floor((totalSeconds % (3600 * 24)) / 3600);
        const minutes = Math.floor((totalSeconds % 3600) / 60);
        const seconds = totalSeconds % 60;
        // Format each component as a two-digit number
        const formattedDays = String(days).padStart(2, '0');
        const formattedHours = String(hours).padStart(2, '0');
        const formattedMinutes = String(minutes).padStart(2, '0');
        const formattedSeconds = String(seconds).padStart(2, '0');
        // Update the countdown state with formatted values
        setChronometer({ days: formattedDays, hours: formattedHours, minutes: formattedMinutes, seconds: formattedSeconds });
    }, [counter]);



    return (
        <AuthnContext.Provider value={{ isAuthenticated, setIsAuthenticated, authToken: authToken, setAuthToken: setAuthToken, chronometer, setChronometer }}>
            {props.children}
        </AuthnContext.Provider>
    );
}


export function useIsAuthenticated() {
    return useContext(AuthnContext).isAuthenticated;
}

export function useSetIsAuthenticated() {
    return useContext(AuthnContext).setIsAuthenticated;
}

export function useAuthToken() {
    return useContext(AuthnContext).authToken;
}

export function useSetAuthToken() {
    return useContext(AuthnContext).setAuthToken;
}

export function useChronometer() {
    return useContext(AuthnContext).chronometer;
}

export default AuthnContainer;