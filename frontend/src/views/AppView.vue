<script setup lang="ts">
import { RouterView, RouterLink } from "vue-router";
import { useWindowSize } from "@vueuse/core";

import SearchIcon from "@/assets/icons/navbar/SearchIcon.vue";
import AddIcon from "@/assets/icons/navbar/AddIcon.vue";
import FolderIcon from "@/assets/icons/navbar/FolderIcon.vue";
import ProfileIcon from "@/assets/icons/navbar/ProfileIcon.vue";

const { width } = useWindowSize();
</script>

<template>
    <div class="app-view-container">
        <div class="app-route">
            <RouterView />
        </div>
        <nav class="app-navbar">
            <RouterLink
                to="explore"
                class="icon-navigation"
                :class="{
                    'active-route': $route.name === 'explore',
                }"
                ><SearchIcon />Find a Quiz</RouterLink
            >
            <RouterLink
                to="create"
                class="icon-navigation"
                :class="{
                    'active-route': $route.name === 'create',
                }"
                ><AddIcon />Create Quiz</RouterLink
            >
            <RouterLink
                to="quizzes"
                class="icon-navigation"
                :class="{
                    'active-route': $route.name === 'quizzes',
                }"
                ><FolderIcon />My Quizzes</RouterLink
            >
            <RouterLink
                to="profile"
                class="icon-navigation"
                :class="{
                    'active-route': $route.name === 'profile',
                }"
                ><ProfileIcon />My Profile</RouterLink
            >
        </nav>
    </div>
</template>

<style scoped>
.app-view-container {
    display: flex;
    flex-direction: column;
    width: 100%;
    height: 100%;
}

.app-route {
    display: block;
    position: relative;
    width: 100%;
    flex: 1;
}

.app-navbar {
    display: flex;
    flex-direction: row;
    justify-content: space-around;
    align-items: center;
    background-color: var(--gray-100);
    padding: 0.5rem;

    box-shadow: 0 -3px 5px rgba(0, 0, 0, 0.5);
}

.icon-navigation {
    display: flex;
    flex-direction: column;
    align-items: center;
    justify-content: center;
    gap: 0.5rem;
}

.icon-navigation svg {
    width: 2.25rem;
    height: 2.25rem;
    padding: 0.25rem;
}

.active-route {
    color: var(--primary-600) !important;
}

a {
    text-decoration: none;
    color: var(--color-text);
}
</style>
