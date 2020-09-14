import React, { useState } from 'react';
import { createMuiTheme, ThemeProvider } from "@material-ui/core/styles";
import classes from './App.module.css';
import Login from './components/Login/Login'
import AppBar from "@material-ui/core/AppBar";
import { Toolbar } from "@material-ui/core";
import {
  BrowserRouter as Router,
  Switch,
  Route,
  Redirect,
  Link
} from "react-router-dom";
import HomePage from './components/HomePage/HomePage';
import Bidding from './components/Bidding/Bidding';

const theme = createMuiTheme({
  typography: {
    fontFamily: "Segoe UI",
  },
  palette: {
    primary: {
      light: "#6abba7",
      main: "#ff9800",
      dark: "#307766",
      contrastText: "#fff",
    },
    secondary: {
      light: "#ffffff",
      main: "#a8abab",
      dark: "#b2b2b2",
      contrastText: "#000",
    },
  },
  direction: "rtl", // Both here and <body dir="rtl">
});

function App() {
  const [loggedUser, setUser] = useState({});

  return (
    <ThemeProvider theme={theme}>

      <Router>
        <AppBar position="static">
          <Toolbar className={classes.navbar}>
            <div dir={'ltr'} className={classes.titleContainer}>
              <label className={classes.mainTitle}><strong>Count Me In!</strong></label>
            </div>
            <Link to="/Home" className={classes.link}>
              <h3>Schedule</h3>
            </Link>
            <Link to="/bidding" className={classes.link} >
              <h3>Bidding</h3>
            </Link>
            <Link onClick={() => { setUser({}) }}to="/" className={classes.logout}>
              <h3>Logout</h3>
            </Link>
          </Toolbar>
        </AppBar >
        <Switch>
          <PrivateRoute loggedUser={loggedUser} path="/home">
            <HomePage />
          </PrivateRoute>
          {/* TODO delete */}
          {/* <Route path="/">
            <HomePage />
          </Route> */}
          <PrivateRoute loggedUser={loggedUser} path="/bidding">
            <Bidding />
          </PrivateRoute>
          <Route path="/login">
            <Login onLogin={(user) => { setUser(user) }} />
          </Route>
          <Route path="/">
            <Redirect
              to={{
                pathname: "/login",
              }}
            />
            <Login />
          </Route>
        </Switch>
      </Router>
    </ThemeProvider>
  );
}

function PrivateRoute({ children, loggedUser }) {
  return (
    <Route
      render={({ location }) =>
        Object.keys(loggedUser).length != 0 ? (
          children
        ) : (
            <Redirect
              to={{
                pathname: "/login",
                state: { from: location }
              }}
            />
          )
      }
    />
  );
}

export default App;
