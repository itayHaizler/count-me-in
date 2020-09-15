import React, { useState } from 'react';
import { createMuiTheme, ThemeProvider } from "@material-ui/core/styles";
import classes from './App.module.css';
import Login from './components/Login/Login'
import Faculty from './components/Faculty/Faculty'
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
import TextField from '@material-ui/core/TextField';
import Button from '@material-ui/core/Button';
import Popover from '@material-ui/core/Popover';
import Typography from '@material-ui/core/Typography';

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
  const [tab, setTab] = useState();
  const [currP, setCurrP] = useState();
  const [anchorEl, setAnchorEl] = React.useState(null);
  const isLoggedIn = Object.keys(loggedUser).length != 0;

  const handleClose = () => {
    setAnchorEl(null);
  };

  const open = Boolean(anchorEl);
  const id = open ? 'simple-popover' : undefined;

  const handleClick = (event) => {
    setAnchorEl(event.currentTarget);
  };

  return (
    <ThemeProvider theme={theme}>

      <Router>
        <AppBar position="static">
          <Toolbar className={classes.navbar}>
            <div dir={'ltr'} className={classes.titleContainer}>
              <label className={classes.mainTitle}><strong>Count Me In!</strong></label>
            </div>
            {!isLoggedIn &&
              <div>
                <Button className={classes.Info} aria-describedby={id} variant="contained" color="primary" onClick={handleClick}>
                  info
                </Button>
                <Popover
                  id={id}
                  open={open}
                  anchorEl={anchorEl}
                  onClose={handleClose}
                  anchorOrigin={{
                    vertical: 'bottom',
                    horizontal: 'center',
                  }}
                  transformOrigin={{
                    vertical: 'top',
                    horizontal: 'center',
                  }}
                >
                <Typography className={classes.typography}>
                  שלום, ברוכים הבאים לcount me in!
באתר זה תוכלו להרשם להרצאות המתקיימות הסמסטר בצורה היברידית והמערכת תחשב ותבחר מי יורשה להכנס לשיעור הפרונטלי בצורה הוגנת. הינך מקבל סך נקודות שבועיות אשר מצטברות משבוע לשבוע. עלייך לחלק את הנקודות שברשותך בין הקורסים הנמצאים במערכת השעות שלך. החלוקה תעשה בעזרת חלוקת אחוזים. ברשותך 100% אותם תוכל לחלק בין הקורסים השונים. שים לב כי החלוקה נתונה לשיקול דעתך, אינך חייב לחלק את כל האחוזים ואינך חייב לחלק אחוזים לכל קורס. לאחר מכן המערכת תשקלל את בחירות כל התלמידים ותבחר מי ייכנס לכל שיעור. את חלוקת האחוזים תוכל לשנות באופן דינאמי בלשונית biding.
את מערכת השעות והשיבוצים לשבועיים הקרובים תוכל לראות בלשונית schedule כאשר השיעורים אליהם הצלחת להיכנס יסומנו בירוק, ואלה שלא יסומנו באפור. שים לב שכל חלוקת אחוזים רלוונטית לשבוע השלישי.</Typography>
                </Popover>
              </div>
            }
            {isLoggedIn && !loggedUser.isSegel &&
              <div className={classes.tabs}>
                <Link to="/Home" onClick={() => setTab(1)} className={!isLoggedIn || tab != 1 ? classes.link : classes.clickedLink}>
                  <h3>מערכת שעות</h3>
                </Link>
                <Link to="/bidding" onClick={() => setTab(2)} className={!isLoggedIn || tab != 2 ? classes.link : classes.clickedLink} >
                  <h3>מכרז</h3>
                </Link>
              </div>
            }
            {isLoggedIn && !loggedUser.isSegel &&
              <label className={classes.percents}><strong>אחוזים: {currP}%</strong></label>
            }
            {isLoggedIn && !loggedUser.isSegel &&
              <label className={classes.points}><strong>נקודות: 1500</strong></label>
            }
            {isLoggedIn &&
              <Link onClick={() => { setUser({}) }} to="/" className={classes.logout}>
                <h3>התנתקות</h3>
              </Link>
            }
          </Toolbar>
        </AppBar >
        <Switch>
          <PrivateRoute loggedUser={loggedUser} path="/home">
            <HomePage />
          </PrivateRoute>
          <PrivateRoute loggedUser={loggedUser} path="/bidding">
            <Bidding updatePercents={(p) => { setCurrP(p) }} />
          </PrivateRoute>
          <Route path="/login">
            <Login onLogin={(user) => {
              setTab(1)
              setUser(user)
            }} />
          </Route>
          <PrivateRoute loggedUser={loggedUser} path="/faculty">
            <Faculty />
          </PrivateRoute>
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
    </ThemeProvider >
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
