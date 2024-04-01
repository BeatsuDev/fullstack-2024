<template>
    <div class="quiz-hero">
        <div class="header">
            <h1>{{ props.quiz.title }}</h1>
            <div class="action-bar">
                <ButtonComponent
                    style="margin-right: 1em"
                    filled
                    large
                    v-if="props.playable"
                    @click="createMultiplayerGame"
                >
                    Multiplayer
                </ButtonComponent>
                <ButtonComponent
                    filled
                    large
                    v-if="props.playable"
                    @click="
                        $router.push({
                            name: 'quiz-player',
                            params: { id: quiz.id },
                        })
                    "
                >
                    Play
                </ButtonComponent>
            </div>
        </div>
        <div class="description">
            <h3>Description</h3>
            <p>{{ props.quiz.description }}</p>
            <p style="font-style: italic">
                Made by: {{ props.quiz.creator.name }}
            </p>
            <p
                style="font-style: italic"
                v-if="props.quiz?.categories?.length > 0"
            >
                Categories:
                {{ props.quiz.categories.map((c) => c.name).join(", ") }}
            </p>
        </div>
        <div
            class="edit-button"
            v-if="props.editable"
            @click="emit('edit', props.quiz)"
        >
            <ButtonComponent>Edit</ButtonComponent>
        </div>
    </div>
</template>

<script lang="ts" setup>
import ButtonComponent from "./ButtonComponent.vue";
import type { Quiz } from "@/api";
import { useExecutablePromise } from "@/composables/promise";
import { CompetitionApi } from "@/api";
import { useNotificationStore } from "@/stores/notification";

import router from "@/router";

const props = defineProps<{
    quiz: Quiz;
    editable?: boolean;
    playable?: boolean;
}>();

const emit = defineEmits<{
    (event: "edit", quiz: Quiz): void;
}>();

// TODO: Orker ikke emitte også implementere dette i filen over... Er allerede mye kode der, og dette funker
// Multiplayer
const multiplayerApi = new CompetitionApi();
const { execute: executeMultiplayerGameCreation, data: gameCreationResponse } =
    useExecutablePromise(
        async (...args: Parameters<typeof multiplayerApi.createCompetition>) =>
            multiplayerApi.createCompetition(...args)
    );

async function createMultiplayerGame() {
    try {
        await executeMultiplayerGameCreation(props.quiz.id);
    } catch (e) {
        useNotificationStore().addNotification({
            message: "Could not create a lobby. Please try again later. " + e,
            type: "error",
        });
        return;
    }

    useNotificationStore().addNotification({
        message: "Lobby created successfully!",
        type: "success",
    });

    const lobbyData = gameCreationResponse.value!.data;
    router.push({
        name: "quiz-lobby",
        params: { lobbyCode: lobbyData.joinCode },
    });
}
</script>

<style scoped>
.quiz-hero {
    position: relative;
    padding: 1rem;
    border: 2px solid var(--color-border);
    background-color: var(--primary-200);
}

.header {
    display: flex;
    align-items: center;
    justify-content: space-between;
    margin-bottom: 1rem;
}

.header h1 {
    margin: 0;
    font-size: 2rem;
}

.description {
    border-top: 1px solid var(--primary-200);
    padding-top: 1rem;
}

.description h3 {
    margin-top: 0;
    font-size: 1.5rem;
}

.description p {
    margin-top: 0.5rem;
    line-height: 1.5;
}

.edit-button {
    position: absolute;
    bottom: 1rem;
    right: 1rem;
}
</style>
