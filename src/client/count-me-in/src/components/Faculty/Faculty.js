import React, { useState, useCallback } from 'react'
import TextField from '@material-ui/core/TextField';
import classes from './Faculty.module.css';
import Button from '@material-ui/core/Button';
import { Alert, AlertTitle } from '@material-ui/lab';
import { useForm } from 'react-hook-form';
import HomePage from '../HomePage/HomePage'
import { useHistory } from 'react-router-dom';
import { Checkbox, FormControlLabel } from '@material-ui/core';
import RadioGroup from '@material-ui/core/RadioGroup';
import Radio from '@material-ui/core/Radio';
import FormControl from '@material-ui/core/FormControl';
import Axios from 'axios';

const dates = [
    { Date: '2020-09-13T09:45', id: "1" },
    { Date: '2020-09-15T10:45', id: "2" },
    { Date: '2020-09-13T12:00', id: "3" },
];

const students = [
    { name: "toya", studentId: "1" },
    { name: "cooper", studentId: "2" },
    { name: "loosi", studentId: "3" },
]

function Faculty({ onLogin }) {
    const history = useHistory();
    const [slots, setSlots] = useState([]);
    const [assignees, setAssignees] = useState([]);
    const [showAlert, setAlert] = useState(false);
    const [courseName, setCourseName] = useState('');
    const [groupId, setGroupId] = useState('');
    const [searchOn, setSearchOn] = useState(false);
    const [radioOn, setRadioOn] = useState(false);
    const [value, setValue] = useState();

    let dateLabels;
    if (slots.length > 0)
        dateLabels = slots.map((slot) => <FormControlLabel value={slot.slotDateID.toString()} control={<Radio />} label={slot.date} />);

    const validate = () => {
        if (courseName == "" || groupId == "") {
            setAlert(true)
            setTimeout(() => {
                setAlert(false)
            }, 3000);
        }
        else {
            setSearchOn(true);
            Axios.post(`http://localhost:8080/count-me-in/getSlots`, {
                session_id: localStorage.getItem("sessionId"),
                courseID: courseName,
                groupID: groupId,
            }, {
                headers: {
                    'Content-Type': 'text/plain;charset=UTF-8',
                }
            }).then(({ data }) => {
                const newSlots = data.map((slot)=> {
                    slot.date = (new Date(slot.date)).toLocaleDateString ();
                    return slot;
                })

                setSlots(newSlots)
            })
        }
    }
    const handleChange = (event) => {
        setValue(event.target.value);
        Axios.post(`http://localhost:8080/count-me-in/getRegisteredStudents`, {
                session_id: localStorage.getItem("sessionId"),
                slotID: event.target.value,
            }, {
                headers: {
                    'Content-Type': 'text/plain;charset=UTF-8',
                }
            }).then(({ data }) => {
                const newAssignees = data.map((assignee)=> {
                    // slot.date = (new Date(slot.date)).toLocaleDateString ();
                    return assignee;
                })

                setAssignees(data)
            })
        setRadioOn(true);
    };

    const studentsList = assignees.map((elem) => <label> {elem.name} </label>)

    return (
        <div className={classes.container}>
            <div className={classes.formLayout}>
                <h2 className={classes.title}>מצא שיעור</h2>
                <form className={classes.loginBox}>
                    <TextField className={classes.loginInput} value={courseName} onChange={(event) => setCourseName(event.target.value)} id="courseName" label="שם קורס" variant="filled" color="secondary" />
                    <TextField className={classes.loginInput} value={groupId} onChange={(event) => setGroupId(event.target.value)} id="groupId" label="מספר קבוצה" variant="filled" color="secondary" />
                    <Button onClick={validate} className={classes.loginBtn} variant="outlined" size="medium" color={'#000000'}>
                        מצא
                    </Button>
                </form>
                {showAlert &&
                    <Alert severity="error">
                        <AlertTitle>שגיאה</AlertTitle>
                        חייב להכניס שם קורס ומספר קבוצה
                    </Alert>
                }
            </div>
            {searchOn &&
                <div className={classes.formLayout}>
                    <h2 className={classes.secTitle}>בחר תאריך</h2>
                    <form className={classes.searchBox}>
                        <RadioGroup value={value} onChange={handleChange}>
                            {dateLabels}
                        </RadioGroup>
                    </form>
                </div>
            }
            {radioOn &&
                <div className={classes.formLayout}>
                    <h2 className={classes.secTitle}>רשימת סטודנטים</h2>
                    {studentsList}
                </div>
            }
        </div>
    )
}

export default Faculty;