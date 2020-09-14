import React, { useState } from 'react';
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

const schedulerData = [
    { startDate: '2020-09-13T09:45', endDate: '2020-09-13T11:00', title: 'מבוא למדמ"ח', isIn: 0 },
    { startDate: '2020-09-20T09:45', endDate: '2020-09-20T11:00', title: 'מבוא למדמ"ח', isIn: 1 },
    { startDate: '2020-09-13T12:00', endDate: '2020-09-13T13:30', title: 'קומפילציה', isIn: 1 },
];

const allocations = [
    { text: 'סחתיין! שובצת לשיעור', id: 1, color: teal },
    { text: 'תמיד יש פעם הבאה', id: 0, color: grey },
];

function HomePage() {
    const [currentDate, setDate] = useState(Date.now);
    const [appointments, setData] = useState(schedulerData);
    const resources = [{
        fieldName: 'isIn',
        title: 'כניסה לשיעור',
        instances: allocations,
    }]

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