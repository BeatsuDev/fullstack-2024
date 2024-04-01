import "./assets/main.css";

import { createApp } from "vue";
import { createPinia } from "pinia";
import globalAxios from "axios";

import App from "./App.vue";
import router from "./router";

const app = createApp(App);

globalAxios.defaults.baseURL = "http://localhost:8080/".replace(/\/+$/, "");
app.use(createPinia());
app.use(router);

app.mount("#app");
