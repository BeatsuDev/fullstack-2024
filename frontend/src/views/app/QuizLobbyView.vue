<script lang="ts" setup>
import type { User } from "@/api";
import { CompetitionApi } from "@/api";
import { useAuthenticationStore } from "@/stores/authentication";
import router from "@/router";
import { useNotificationStore } from "@/stores/notification";
import { computed, onMounted, ref } from "vue";
import { useExecutablePromise } from "@/composables/promise";
import { useMultiplayerStore } from "@/stores/multiplayer";
import ButtonComponent from "@/components/ButtonComponent.vue";

import { Client } from "@stomp/stompjs";
import LobbyResult from "@/components/LobbyResult.vue";
import AnonymousUserModal from "@/components/AnonymousUserModal.vue";

const multiplayerStore = useMultiplayerStore();
const authenticationStore = useAuthenticationStore();
const lobbyCode = computed(() => router.currentRoute.value.params.lobbyCode);
const currentUser = ref(authenticationStore.loggedInUser);
const noUser = computed(() => !currentUser.value);

// Setup websocket
const stompClient = new Client({
    brokerURL: "ws://localhost:8080/competition-ws",
    onConnect: () => {
        stompClient.subscribe("/competition", (message: any) => {
            const data = multiplayerStore.processMessage(message);
            if (!data) return;
            switch (data.type) {
                case "JOIN":
                case "FINISH":
                    break;
                case "PROCEED": {
                    const attemptId = multiplayerStore.players
                        .filter(
                            (player) => player.user.id === currentUser.value?.id
                        )
                        .map((player) => player.attempt.id)[0];
                    const quizId =
                        multiplayerStore.lobby?.competitors[0].attempt.quiz?.id;
                    console.log("PROCEED");
                    console.log(multiplayerStore.lobby);
                    const questionId = data.questionId;

                    router.push({
                        name: "quiz-player",
                        params: { id: quizId },
                        query: {
                            attemptId,
                            questionId,
                            lobbyCode: multiplayerStore.lobbyCode,
                        },
                    });
                    break;
                }
                default:
                    return data satisfies never;
            }
        });
    },
});

onMounted(async () => {
    if (noUser.value) {
        return;
    }

    joinLobby();
});

async function joinLobby() {
    let lobbyCodeParam = router.currentRoute.value.params.lobbyCode;
    if (Array.isArray(lobbyCodeParam)) lobbyCodeParam = lobbyCodeParam[0];
    const lobbyCode = parseInt(lobbyCodeParam);
    await multiplayerStore.joinCompetition(lobbyCode);

    useNotificationStore().addNotification({
        message: `${currentUser.value?.name} joined lobby ${multiplayerStore.lobbyCode}!`,
        type: "info",
    });
    stompClient.activate();
}

function setUser(user: User) {
    currentUser.value = user;
    console.log(noUser.value);
    joinLobby();
}

const multiplayerApi = new CompetitionApi();
const { execute: executeStartGame } = useExecutablePromise(
    async (...args: Parameters<typeof multiplayerApi.startCompetition>) =>
        multiplayerApi.startCompetition(...args)
);

async function startGame() {
    const lobbyCode = multiplayerStore.lobbyCode;
    if (!lobbyCode) {
        useNotificationStore().addNotification({
            message: "Could not start the game. The lobby code was missing.",
            type: "error",
        });
        router.push({ name: "lobby-chooser" });
        return;
    }

    try {
        await executeStartGame(lobbyCode);
    } catch (e) {
        useNotificationStore().addNotification({
            message: "Could not start the game. Please try again later. " + e,
            type: "error",
        });
        return;
    }

    useNotificationStore().addNotification({
        message: "Game started!",
        type: "success",
    });
}
</script>

<template>
    <div class="lobby-container">
        <div class="inner-lobby-container">
            <div class="info">
                <h1>Quiz Lobby: {{ lobbyCode }}</h1>
                <ButtonComponent class="start-button" @click="startGame">
                    Start Game
                </ButtonComponent>
            </div>
            <LobbyResult />
            <AnonymousUserModal v-model="noUser" @updateUser="setUser" />
        </div>
    </div>
</template>

<style scoped>
.lobby-container {
    height: calc(100vh - 66px - 4em);
    display: flex;
    flex-direction: column;
}

.inner-lobby-container {
    flex: 1;
    display: flex;
    flex-direction: column;
    overflow: hidden;
    justify-content: space-between;
}

@media (max-width: 900px) {
    .inner-lobby-container {
        justify-content: unset;
    }
}

.start-button {
    font-size: 2em;
    width: 100%;
    margin-left: auto;
}

.info {
    margin: 1em;
}

.user-list {
    border: 5px solid var(--primary-500);
    gap: 1em;
    margin: 1rem;
    overflow-y: auto;
    box-shadow: 10px 10px 15px rgba(0, 0, 0, 0.5);
}

.user {
    display: flex;
    align-items: center;
    padding: 0.5rem;
    font-size: 1.5em;
    margin-bottom: 0.75rem;
}

.user > img {
    margin-right: 1rem;
}

@media (min-width: 900px) {
    .lobby-container {
        padding: 2em;
    }

    .inner-lobby-container {
        flex-direction: row;
    }

    .user-list {
        width: 50%;
        float: right;
    }

    .start-button {
        align-self: center;
    }
}
</style>
