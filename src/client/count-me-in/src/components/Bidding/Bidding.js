import React, { useState, useEffect, useCallback } from 'react';
import classes from './Bidding.module.css';
import Paper from '@material-ui/core/Paper';
import { Alert, AlertTitle } from '@material-ui/lab';
import {
    Scheduler,
    WeekView,
    Appointments,
    Toolbar,
} from '@devexpress/dx-react-scheduler-material-ui';
import { TextField, IconButton } from '@material-ui/core';
import SaveIcon from '@material-ui/icons/Save';
import Axios from 'axios';

// const schedulerData = [
//     { id: 1, startDate: '2020-09-13T09:45', endDate: '2020-09-13T11:00', title: 'מבוא למדמ"ח', bidingPercentage: 30 },
//     { id: 2, startDate: '2020-09-15T10:45', endDate: '2020-09-15T12:00', title: 'מבוא למדמ"ח', bidingPercentage: 40 },
//     { id: 3, startDate: '2020-09-13T12:00', endDate: '2020-09-13T13:30', title: 'קומפילציה', bidingPercentage: 30 },
// ];

function Bidding() {
    const [appointments, setAppointments] = useState([]);
    const [showAlert, setAlert] = useState(false);
    const headers = {
        'Content-Type': 'text/plain;charset=UTF-8',
    }
    const loadAppointments = useCallback(() => {
        Axios.post(`http://localhost:8080/count-me-in/getScheduleBiding`, {
            session_id: localStorage.getItem("sessionId")
        }, {
            headers: headers
        }).then(({ data }) => {
            console.log(data);
            const newAppointments = data.map((appointment) => {
                const now = new Date();
                const diffDay = appointment.day - now.getDay();
                now.setHours(now.getHours() + 24 * diffDay);
                now.setHours(appointment.hour);
                const end = new Date(now);
                end.setHours(end.getHours() + appointment.duration);
                const newApp = {
                    id: appointment.slotID,
                    startDate: now.toISOString(),
                    endDate: end.toISOString(),
                    title: appointment.courseID,
                    bidingPercentage: appointment.bidingPercentage
                }

                return newApp;
            })

            setAppointments(newAppointments);
        });
    }, []);

    useEffect(() => {
        loadAppointments()
    }, [loadAppointments])

    const TimeTableCell = ({ onDoubleClick, ...restProps }) => {
        return <WeekView.TimeTableCell onDoubleClick={undefined} {...restProps} />;
    };

    const BiddingSlot = ({ style, ...restProps }) => {
        const startDate = new Date(restProps.data.startDate)
        const endDate = new Date(restProps.data.endDate)
        const [percents, setPercents] = useState(restProps.data.bidingPercentage);

        return (
            <Appointments.AppointmentContent dir={'rtl'} {...restProps}>
                <div className={restProps.container}>
                    <div>
                        <strong>{restProps.data.title}</strong>
                    </div>
                    <div>
                        {startDate.getHours() + ':' + (startDate.getMinutes() < 10 ? '0' + startDate.getMinutes() : startDate.getMinutes())
                            + ' - ' + endDate.getHours() + ':' + (endDate.getMinutes() < 10 ? '0' + endDate.getMinutes() : endDate.getMinutes())}</div>
                    <TextField
                        type="number"
                        size="small"
                        className={classes.percent}
                        value={percents}
                        onChange={(ev) => {
                            setPercents(parseInt(ev.target.value))
                        }
                        } />
                    <IconButton onClick={() => {
                        let sum = 0;
                        appointments.forEach((appointment) => {
                            if (appointment.id != restProps.data.id)
                                sum += appointment.bidingPercentage;
                            else
                                sum += percents;
                        })
                        if (sum > 100) {
                            setAlert(true)
                            setTimeout(() => {
                                setAlert(false)
                            }, 3000);
                        } else {
                            const data = appointments.map((appointment) => {
                                if (appointment.id === restProps.data.id)
                                    appointment.bidingPercentage = percents;
                                return appointment;
                            })
                            setAppointments(data);
                        }
                    }}
                        aria-label="save" className={classes.margin} size="small">
                        <SaveIcon fontSize="inherit" />
                    </IconButton>
                </div>
            </Appointments.AppointmentContent >
        );
    };

    return (
        <div >
            <Paper dir={'ltr'}>
                {showAlert &&
                    <div className={classes.alert}>
                        <Alert className={classes.innerMessage} severity="warning">
                            <AlertTitle>שים לב</AlertTitle>
                        סך האחוזים חייב להיות עד 100.
                                </Alert>
                    </div>
                }
                <Scheduler
                    data={appointments}
                    height={650}
                >
                    <WeekView
                        startDayHour={9}
                        endDayHour={21}
                        excludedDays={[7, 6]}
                        timeTableCellComponent={TimeTableCell}
                        cellDuration={30}
                    />

                    <Appointments
                        appointmentContentComponent={BiddingSlot}
                    />
                </Scheduler>
            </Paper>)
        </div>
    )
}

export default Bidding;