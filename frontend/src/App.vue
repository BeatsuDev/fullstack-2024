<script setup lang="ts">
import { storeToRefs } from "pinia";
import { RouterView } from "vue-router";
import NotificationsContainer from "@/components/NotificationsContainer.vue";
import { useAuthenticationStore } from "./stores/authentication";
import { useNotificationStore } from "./stores/notification";

import router from "./router";

const authenticationStore = useAuthenticationStore();
const notificationStore = useNotificationStore();
const { authenticated } = storeToRefs(useAuthenticationStore());

function logout(): void {
    authenticationStore
        .deauthenticate()
        .then(() => router.push({ name: "home" }))
        .catch((error: any) => {
            notificationStore.addNotification({
                message:
                    "Failed to log out. Please try again. " +
                    (error instanceof Error)
                        ? error.message
                        : "",
                type: "error",
            });
        });
}
</script>

<template>
    <header>
        <nav id="navigation-bar">
            <a @click="router.push({ name: 'home' })" id="company-name">
                Kazoot
            </a>
            <div id="routes">
                <a
                    v-if="!authenticated"
                    id="login-router-link"
                    @click="router.push({ name: 'login' })"
                >
                    log in
                </a>
                <a v-else @click="logout()">log out</a>
            </div>
        </nav>
    </header>
    <div id="app-container">
        <RouterView v-slot="{ Component: viewComponent, route }">
            <Transition
                :name="
                    (route.meta.transitionName as string | undefined) || 'fade'
                "
                mode="out-in"
            >
                <Component :is="viewComponent" />
            </Transition>
        </RouterView>
        <NotificationsContainer />
    </div>
</template>

<style scoped>
#app-container {
    padding-top: 4rem;
    height: 100%;
    width: 100%;
    overflow-y: auto;
}

#navigation-bar {
    z-index: 1;
    position: absolute;
    display: flex;
    align-items: center;
    padding: 1rem 1.5rem;
    box-sizing: border-box;
    width: 100%;
    height: 4rem;
    color: white;
    background-color: var(--primary-800);
    font-size: 1.25em;
}

#company-name {
    font-size: 1.5em;
    text-decoration: none;
    color: white;
    cursor: pointer;
    user-select: none;
}

#routes > a {
    position: relative;
    color: var(--gray-200);
    text-decoration: none;
    transition: color 50ms;
    cursor: pointer;
    user-select: none;
}

#routes > a:hover {
    color: white;
}

#routes > a::before {
    content: "";
    display: block;
    position: absolute;
    width: 0;
    height: 2px;
    bottom: 0;
    border-radius: 2px;
    background-color: white;
    transition: width 100ms;
}

#routes > a:hover::before {
    width: 100%;
}

#routes {
    flex: 1;
    display: flex;
    justify-content: end;
    gap: 1.5rem;
}
</style>
