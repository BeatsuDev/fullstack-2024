<script lang="ts" setup>
import { RouterView } from "vue-router";
import { useWindowSize } from "@vueuse/core";

import SearchIcon from "@/assets/icons/navbar/SearchIcon.vue";
import AddIcon from "@/assets/icons/navbar/AddIcon.vue";
import FolderIcon from "@/assets/icons/navbar/FolderIcon.vue";
import ProfileIcon from "@/assets/icons/navbar/ProfileIcon.vue";

const { width } = useWindowSize();
console.log(width.value);
</script>

<template>
    <div
        :style="{
            overflow: $route.name === 'quizzes' ? 'hidden' : 'auto',
            'padding-bottom':
                width < 900
                    ? '5rem'
                    : $route.name === 'quizzes'
                      ? '0'
                      : '0.25rem',
            height: $route.name === 'quizzes' ? 'calc(100svh - 4rem)' : '',
        }"
        class="app-app-container"
    >
        <RouterView />
    </div>
    <nav class="app-navbar">
        <a
            :class="{
                'active-route': $route.name === 'explore',
            }"
            class="icon-navigation"
            @click="() => $router.push({ name: 'explore' })"
        >
            <SearchIcon />
            Find a Quiz</a
        >
        <a
            :class="{
                'active-route': $route.name === 'create',
            }"
            class="icon-navigation"
            @click="() => $router.push({ name: 'create' })"
        >
            <AddIcon />
            Create Quiz</a
        >
        <a
            :class="{
                'active-route': $route.name === 'quizzes',
            }"
            class="icon-navigation"
            @click="() => $router.push({ name: 'lobby-chooser' })"
        >
            <FolderIcon />
            Join lobby</a
        >
        <a
            :class="{
                'active-route': $route.name === 'profile',
            }"
            class="icon-navigation"
            @click="() => $router.push({ name: 'profile' })"
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
        margin-bottom: 4rem;
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

.app-app-container {
    min-height: 100%;
}

a {
    text-decoration: none;
    color: var(--color-text);
    cursor: pointer;
}
</style>
