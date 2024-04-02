<script setup lang="ts">
import { ref } from "vue";
import { type AxiosError, isAxiosError } from "axios";
import { useNotificationStore } from "@/stores/notification";
import ButtonComponent from "@/components/ButtonComponent.vue";

import router from "@/router";
import { CompetitionApi } from "@/api";
import { useMultiplayerStore } from "@/stores/multiplayer";

const lobbyCode = ref("");

async function joinLobby() {
    const multiplayerApi = new CompetitionApi();

    let response;
    try {
        response = await multiplayerApi.joinCompetition(
            parseInt(lobbyCode.value)
        );
    } catch (e: any) {
        if (!(e instanceof Error) && !isAxiosError(e)) {
            return useNotificationStore().addNotification({
                message:
                    "Could not join lobby. Unknown error. Please try again later.",
                type: "error",
            });
        }

        const error = e as Error | AxiosError;

        if (error instanceof Error && !isAxiosError(error)) {
            return useNotificationStore().addNotification({
                message:
                    "Could not join lobby. Please try again later. " + error,
                type: "error",
            });
        }

        const axiosError = error as AxiosError;

        if (axiosError.status) {
            switch (axiosError.status) {
                case 404:
                    return useNotificationStore().addNotification({
                        message: "Lobby not found. Please try again.",
                        type: "error",
                    });
            }
        }

        return useNotificationStore().addNotification({
            message:
                "Could not join lobby. Unknown error. Please try again later.",
            type: "error",
        });
    }

    useMultiplayerStore().multiplayerData = response.data;
    router.push({
        name: "quiz-lobby",
        params: { lobbyCode: lobbyCode.value },
    });
}
</script>

<template>
    <div class="lobby-chooser-container">
        <h3>Join lobby</h3>
        <input v-model="lobbyCode" type="text" placeholder="123456" />
        <ButtonComponent large @click="joinLobby"> Join Lobby </ButtonComponent>
    </div>
</template>

<style scoped>
.lobby-chooser-container {
    display: flex;
    justify-content: center;
    align-items: center;
    height: 100%;
    flex-direction: column;
}

input {
    font-size: 2em;
    max-width: 150px;
    text-align: right;
    margin-right: 0.2rem;
}
</style>
