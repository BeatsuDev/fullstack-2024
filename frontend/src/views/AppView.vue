<script setup lang="ts">
import { RouterView } from "vue-router";

import SearchIcon from "@/assets/icons/navbar/SearchIcon.vue";
import AddIcon from "@/assets/icons/navbar/AddIcon.vue";
import FolderIcon from "@/assets/icons/navbar/FolderIcon.vue";
import ProfileIcon from "@/assets/icons/navbar/ProfileIcon.vue";
</script>

<template>
    <div class="app-app-container">
        <RouterView />
    </div>
    <nav class="app-navbar">
        <a
            @click="() => $router.push({ name: 'explore' })"
            class="icon-navigation"
            :class="{
                'active-route': $route.name === 'explore',
            }"
        >
            <SearchIcon />
            Find a Quiz</a
        >
        <a
            @click="() => $router.push({ name: 'create' })"
            class="icon-navigation"
            :class="{
                'active-route': $route.name === 'create',
            }"
        >
            <AddIcon />
            Create Quiz</a
        >
        <a
            @click="() => $router.push({ name: 'quizzes' })"
            class="icon-navigation"
            :class="{
                'active-route': $route.name === 'quizzes',
            }"
        >
            <FolderIcon />
            My Quizzes</a
        >
        <a
            @click="() => $router.push({ name: 'profile' })"
            class="icon-navigation"
            :class="{
                'active-route': $route.name === 'profile',
            }"
        >
            <ProfileIcon />
            My Profile</a
        >
    </nav>
</template>

<style scoped>
.icon-navigation {
    display: flex;
    flex-direction: column;
    align-items: center;
    justify-content: center;
    gap: 0.5rem;
    color: black;
}

.icon-navigation svg {
    width: 2rem;
    height: 2rem;
    padding: 0.25rem;
    color: black;
}

@media (min-width: 900px) {
    .app-navbar {
        display: flex;
        flex-direction: column;
        justify-content: space-around;
        align-items: center;
        padding: 0.5rem;
        box-shadow: 0 -3px 5px rgba(0, 0, 0, 0.5);
        position: fixed;
        top: 0;
        height: calc(100svh - 4rem);
        width: 5rem;
        margin-top: 4rem;
        background-color: var(--color-background-mute);
    }

    .app-app-container {
        padding-left: 6rem;
    }
}

@media (max-width: 900px) {
    .app-app-container {
        padding-bottom: 5rem;
    }

    .icon-navigation {
        font-size: 0.9rem;
        color: white;
    }

    .icon-navigation svg {
        width: 1.5rem;
        height: 1.5rem;
        color: white;
    }

    .app-navbar {
        display: flex;
        flex-direction: row;
        justify-content: space-between;
        align-items: center;
        background-color: var(--primary-800);
        padding: 0.5rem;
        box-shadow: 0 -3px 5px rgba(0, 0, 0, 0.5);
        position: fixed;
        bottom: 0;
        width: calc(100% - 1rem);
    }

    .active-route {
        color: var(--primary-300) !important;
    }
}

a {
    text-decoration: none;
    color: var(--color-text);
    cursor: pointer;
}
</style>
