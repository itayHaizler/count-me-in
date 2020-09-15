import React, { useState } from 'react'
import TextField from '@material-ui/core/TextField';
import classes from './Login.module.css';
import Button from '@material-ui/core/Button';
import { Alert, AlertTitle } from '@material-ui/lab';
import { useForm } from 'react-hook-form';
import HomePage from '../HomePage/HomePage'
import { useHistory } from 'react-router-dom';
import { Checkbox, FormControlLabel } from '@material-ui/core';
import axios from 'axios'

function Login({ onLogin }) {
    const history = useHistory();
    const [showAlert, setAlert] = useState(false);
    const [id, setId] = useState('');
    const [isSegel, setIsSegel] = useState(false);
    const [email, setEmail] = useState('');
    const [password, setPassword] = useState('');
    const validate = () => {
        if (email == "" || password == "" || id == "") {
            setAlert(true)
            setTimeout(() => {
                setAlert(false)
            }, 3000);
        } else {
            const headers = {
                'Content-Type': 'text/plain;charset=UTF-8',
            }

            axios.post("http://localhost:8080/count-me-in/loginStudent", {
                email: email,
                password: password,
                studentID: id,
                session_id: localStorage.getItem("sessionId")
            }, {
                headers: headers
            }).then((data) => {
                onLogin({ email: email })
                history.replace("home");

            }).catch(() => console.log("OOPS"))

        }
    }

    return (
        <div className={classes.container}>
            <div className={classes.formLayout}>
                <h2 className={classes.title}>התחברות</h2>
                <form className={classes.loginBox}>
                    <TextField className={classes.loginInput} value={id} onChange={(event) => setId(event.target.value)} id="id" label="תעודת זהות" variant="filled" color="secondary" />
                    <TextField className={classes.loginInput} value={email} onChange={(event) => setEmail(event.target.value)} id="email" label="מייל" variant="filled" color="secondary" />
                    <TextField className={classes.loginInput} value={password} onChange={(event) => setPassword(event.target.value)} id="password" label="סיסמא" type="password" variant="filled" color="secondary" />
                    <FormControlLabel
                        control={
                            <Checkbox
                                value={isSegel}
                                onChange={(event) => setIsSegel(event.target.checked)}
                                color="default"
                                inputProps={{ 'aria-label': 'checkbox with default color' }}
                            />
                        }
                        label="התחבר כאיש סגל"

                    />
                    <Button className={classes.loginBtn} variant="outlined" size="medium" color={'default'} onClick={validate}>
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