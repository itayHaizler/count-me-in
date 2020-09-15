import React, { useState, useCallback, useEffect } from 'react';
import classes from './HomePage.module.css';
import Paper from '@material-ui/core/Paper';
import { ViewState, EditingState, IntegratedEditing } from '@devexpress/dx-react-scheduler';
import {
    Scheduler,
    WeekView,
    Appointments,
    AppointmentTooltip,
    Toolbar,
    DateNavigator,
    TodayButton,
    Resources,
} from '@devexpress/dx-react-scheduler-material-ui';
import { teal, grey } from '@material-ui/core/colors';
import Axios from 'axios';

// const schedulerData = [
//     { startDate: '2020-09-13T09:45', endDate: '2020-09-13T11:00', title: 'מבוא למדמ"ח', isApproved: false },
//     { startDate: '2020-09-20T09:45', endDate: '2020-09-20T11:00', title: 'מבוא למדמ"ח', isApproved: true },
//     { startDate: '2020-09-13T12:00', endDate: '2020-09-13T13:30', title: 'קומפילציה', isApproved: true },
// ];

const allocations = [
    { text: 'סחתיין! שובצת לשיעור', id: true, color: teal },
    { text: 'תמיד יש פעם הבאה', id: false, color: grey },
];

function HomePage() {
    const [currentDate, setDate] = useState(Date.now);
    const [appointments, setAppointments] = useState([]);

    const loadSchedule = useCallback(() => {
        Axios.post(`http://localhost:8080/count-me-in/getSchedule`, {
            session_id: localStorage.getItem("sessionId")
        }, {
            headers: headers
        }).then(({ data }) => {
            const newAppointments = data.map((appointment) => {
                const startDate = new Date(appointment.date);
                const endDate = new Date(appointment.date);
                endDate.setHours(endDate.getHours() + appointment.duration);

                return {
                    title: appointment.courseID,
                    startDate,
                    endDate,
                    isApproved: appointment.isApproved,
                }
            })

            setAppointments(newAppointments);
        })

    }, []);

    useEffect(() => {
        loadSchedule()
    }, [loadSchedule])

    const resources = [{
        fieldName: 'isApproved',
        title: 'כניסה לשיעור',
        instances: allocations,
    }]

    const headers = {
        'Content-Type': 'text/plain;charset=UTF-8',
    }



    const Appointment = ({ children }) => {
        return <div dir={'rtl'}>
            {children}
        </div>

    }
    const TimeTableCell = ({ onDoubleClick, ...restProps }) => {
        return <WeekView.TimeTableCell onDoubleClick={undefined} {...restProps} />;
    };

    return (
        <div >
            <Paper dir={'ltr'}>
                <Scheduler
                    data={appointments}
                    height={650}
                >
                    <ViewState
                        currentDate={currentDate}
                        onCurrentDateChange={(date) => setDate(date)}

                    />
                    <WeekView
                        onDoubleClick={undefined}
                        startDayHour={9}
                        endDayHour={21}
                        timeTableCellComponent={TimeTableCell}
                        cellDuration={60}
                    />
                    <Toolbar />
                    <DateNavigator />
                    <TodayButton />
                    <Appointments dir={'rtl'} />
                    <Resources
                        data={resources}
                    />
                    <AppointmentTooltip
                        showCloseButton
                    />
                </Scheduler>
            </Paper>
        </div>
    )
}

export default HomePage;