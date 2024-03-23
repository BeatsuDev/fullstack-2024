import { createRouter, createWebHistory } from "vue-router";

import { useAuthenticationStore } from "@/stores/authentication";
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
            meta: {
                redirectIfAuthenticated: true,
            },
        },
        {
            path: "/register",
            name: "register",
            component: () => import("@/views/RegisterView.vue"),
            meta: {
                redirectIfAuthenticated: true,
            },
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

// Navigation guards
const authenticationStore = useAuthenticationStore();

router.beforeEach((to, from) => {
    // Set meta transition name for page transitions
    const toDepth = to.path.split("/").filter((i) => i).length;
    const fromDepth = from.path.split("/").filter((i) => i).length;

    if (!(toDepth === fromDepth)) {
        to.meta.transitionName =
            toDepth < fromDepth ? "slide-left" : "slide-right";
    }

    // Redirect to login if not authenticated and attempting to go to authenticated route
    if (to.meta.requiresAuth && !authenticationStore.authenticated) {
        return "/login";
    }

    // Rediirect to app if authenticated and attempting to go to non-authenticated route
    if (to.meta.redirectIfAuthenticated && authenticationStore.authenticated) {
        return "/app";
    }
});

export default router;
