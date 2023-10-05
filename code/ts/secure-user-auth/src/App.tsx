import React from 'react';

import {
  createBrowserRouter, Outlet, RouterProvider,
} from 'react-router-dom'
import AuthnContainer from './context/AuthnContainer';
import Home from './components/Home';
import { Me } from './components/Me';
import { RequireAuthn } from './authenticated/RequireAuthn';
import { Login } from './components/Login';
import { Logout } from './components/Logout';
import { Register } from './components/Register';
import { About } from './components/About';
import { Contact } from './components/Contact';

const browserRouter = createBrowserRouter([
  {
    'path': '/',
    'element': <AuthnContainer> <Outlet /> </AuthnContainer>,
    'children': [
      {
        'path': '/',
        'element': <Home />,
        'children': [
          {
            'path': '/',
            'element': <h1>Home</h1>,
          },
          {
            'path': '/me',
            'element': <RequireAuthn><Me /></RequireAuthn>,
          },
          {
            'path': '/messages',
            'element': <RequireAuthn><h1>Messages</h1></RequireAuthn>,
          },
          {
            'path': '/about',
            'element': <About></About>
          },
          {
            'path': '/contact',
            'element': <Contact/>,
          },
          {
            'path': '/login',
            'element': <Login></Login>,
          },
          {
            'path': '/register',
            'element': <Register></Register>,
          },
          {
            'path': '/logout',
            'element': <RequireAuthn><Logout></Logout></RequireAuthn>,
          },
        ],
      },
    ],
  }
])

export function App() {
  return (
    <RouterProvider router={browserRouter}></RouterProvider>
  )
}
