import { createRouter, createWebHistory } from "vue-router";

import LandingView from "@/views/LandingView.vue";
import AppView from "@/views/AppView.vue";

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
        {
            path: "/register",
            name: "register",
            component: () => import("@/views/RegisterView.vue"),
        },
        {
            path: "/profile",
            name: "profile",
            component: () => import("@/views/ProfilePageView.vue"),
            meta: {
                requiresAuth: true,
            },
        },
        {
            path: "/app",
            name: "app",
            component: AppView,
        },
    ],
});

router.beforeEach((to, from) => {
    const toDepth = to.path.split("/").filter((i) => i).length;
    const fromDepth = from.path.split("/").filter((i) => i).length;

    if (toDepth === fromDepth) return;
    const goingUp = toDepth < fromDepth;
    to.meta.transitionName = goingUp ? "slide-left" : "slide-right";
});

export default router;
