import { createRouter, createWebHistory } from "vue-router";

import { useAuthenticationStore } from "@/stores/authentication";

import LandingView from "@/views/LandingView.vue";
import AppView from "@/views/AppView.vue";
import ExploreView from "@/views/app/ExploreView.vue";

const router = createRouter({
    history: createWebHistory(import.meta.env.BASE_URL),
    routes: [
        {
            path: "/",
            name: "home",
            component: LandingView,
            meta: {
                redirectIfAuthenticated: true,
            },
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
            path: "/app",
            name: "app",
            component: AppView, // This is commonly visited, so should be loaded eagerly
            children: [
                {
                    name: "redirect-to-explore",
                    path: "",
                    redirect: "explore",
                },
                {
                    name: "explore",
                    path: "/explore",
                    component: ExploreView, // This is also commonly visited
                },
                {
                    name: "create",
                    path: "/create",
                    component: () => import("@/views/app/CreateView.vue"),
                    meta: {
                        requiresAuth: true,
                    },
                },
                {
                    name: "quizzes",
                    path: "/quizzes",
                    component: () => import("@/views/app/QuizzesView.vue"),
                    meta: {
                        requiresAuth: true,
                    },
                },
                {
                    name: "quiz",
                    path: "/quizzes/:id",
                    component: () => import("@/views/app/QuizView.vue"),
                    meta: {
                        requiresAuth: true,
                    },
                },
                {
                    name: "quizEdit",
                    path: "/quizzes/:id/edit",
                    component: () => import("@/views/app/EditQuizView.vue"),
                    meta: {
                        requiresAuth: true,
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
            ],
        },
    ],
});

router.beforeEach(async (to, from) => {
    const authenticationStore = useAuthenticationStore();

    if (!authenticationStore.authenticated) {
        await authenticationStore.refresh().catch((err) => {
            console.error("No cookie / user session:", err);
        });
    }

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
