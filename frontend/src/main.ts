import "./assets/main.css";

import { createApp } from "vue";
import { createPinia } from "pinia";
import { createI18n } from "vue-i18n";
import globalAxios from "axios";

import App from "./App.vue";
import router from "./router";
import translations from "./internationalization";

const app = createApp(App);

const i18n = createI18n({
    locale: "no",
    fallbackLocale: "en",
    messages: translations,
});

globalAxios.defaults.baseURL = "http://localhost:8080/".replace(/\/+$/, "");
app.use(createPinia());
app.use(router);
app.use(i18n);

app.mount("#app");
