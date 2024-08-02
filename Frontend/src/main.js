import { createApp } from 'vue'
import App from './App.vue'
import router from './router'
import store from './store'
import axios from "axios";

axios.defaults.baseURL = "http://localhost:8080/";
const token = localStorage.getItem("token");
if (token) {
  axios.defaults.headers.common["Authorization"] = `Bearer ${token}`;
}

//import bootstrap
// import 'bootstrap/dist/css/bootstrap.css'
import '/css/main.min.css'
import 'bootstrap/dist/js/bootstrap.bundle'
import 'bootstrap-icons/font/bootstrap-icons.css';


createApp(App).use(store).use(router).mount('#app')
