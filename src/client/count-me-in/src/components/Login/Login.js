import React, { useState } from 'react'
import TextField from '@material-ui/core/TextField';
import classes from './Login.module.css';
import Button from '@material-ui/core/Button';
import { Alert, AlertTitle } from '@material-ui/lab';
import { useForm } from 'react-hook-form';
import HomePage from '../HomePage/HomePage'
import { useHistory } from 'react-router-dom';


function Login({onLogin}) {
    const history = useHistory();
    const [showAlert, setAlert] = useState(false);
    const [email, setEmail] = useState('');
    const [password, setPassword] = useState('');
    const validate = () => {
        if (email == "" || password == "") {
            setAlert(true)
            setTimeout(() => {
                setAlert(false)
            }, 3000);
        } else {
            onLogin({email: email})
            history.replace("home");
        }
    }

    return (
        <div className={classes.container}>
            <div className={classes.formLayout}>
                <h2 className={classes.title}>התחברות</h2>
                <form className={classes.loginBox}>
                    <TextField className={classes.loginInput} value={email} onChange={(event) => setEmail(event.target.value)} id="email" label="מייל" variant="filled" color="secondary" />
                    <TextField className={classes.loginInput} value={password} onChange={(event) => setPassword(event.target.value)} id="password" label="סיסמא" type="password" variant="filled" color="secondary" />
                    <Button className={classes.loginBtn} variant="outlined" size="medium" color={'#000000'} onClick={validate}>
                        התחבר
                </Button>
                </form>
            </div>
            {showAlert && 
                <Alert severity="error">
                    <AlertTitle>שגיאה</AlertTitle>
                    חייב להכניס מייל וסיסמא
                </Alert>
                }
        </div>
    )
}

export default Login;