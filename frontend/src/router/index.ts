import { createRouter, createWebHistory } from "vue-router";

import LandingView from "@/views/LandingView.vue";

const router = createRouter({
    history: createWebHistory(import.meta.env.BASE_URL),
    routes: [
        {
            path: "/",
            name: "home",
            component: LandingView,
        },
        {
            path: "/login",
            name: "login",
            component: () => import("@/views/LoginView.vue"),
        },
    ],
});

export default router;
