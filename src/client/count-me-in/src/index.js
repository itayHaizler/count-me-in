import React from 'react';
import ReactDOM from 'react-dom';
import './index.css';
import App from './App';
import { StylesProvider, jssPreset } from "@material-ui/core/styles";
import { create } from "jss";
import * as serviceWorker from './serviceWorker';
import rtl from "jss-rtl";
import axios from 'axios'

const jss = create({ plugins: [...jssPreset().plugins, rtl()] });
axios.get("http://localhost:8080/count-me-in/openSession").then(({data}) => {
// fetch("http://localhost:8080/count-me-in/openSession").then((response) => {
  // return response.text();
// }).then((data) => {
  console.log(data);
  localStorage.setItem('sessionId', data)
})

ReactDOM.render(
  <React.StrictMode>
    <StylesProvider jss={jss}>
      <App />
    </StylesProvider>
  </React.StrictMode>,
  document.getElementById('root')
);

// If you want your app to work offline and load faster, you can change
// unregister() to register() below. Note this comes with some pitfalls.
// Learn more about service workers: https://bit.ly/CRA-PWA
serviceWorker.unregister();
