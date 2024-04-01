<script setup lang="ts">
import type { User } from "@/api";
import { AvatarGenerator } from "random-avatar-generator";
import { useAuthenticationStore } from "@/stores/authentication";
import router from "@/router";
import { useNotificationStore } from "@/stores/notification";
import { onMounted, computed } from "vue";
import { CompetitionApi } from "@/api";
import { useExecutablePromise } from "@/composables/promise";
import { useMultiplayerStore } from "@/stores/multiplayer";
import ButtonComponent from "@/components/ButtonComponent.vue";

import { Client } from "@stomp/stompjs";

const multiplayerStore = useMultiplayerStore();
const authenticationStore = useAuthenticationStore();
const lobbyCode = computed(() => router.currentRoute.value.params.lobbyCode);
const currentUser = authenticationStore.loggedInUser as User;

const lobbyUsers = computed(() => {
    return multiplayerStore.players.map((player) => {
        return {
            id: player.user.id,
            name: player.user.name,
            avatar: new AvatarGenerator().generateRandomAvatar(player.user.id),
        };
    });
});

// Setup websocket
const stompClient = new Client({
    brokerURL: "ws://localhost:8080/competition-ws",
    onConnect: () => {
        stompClient.subscribe("/competition", (message: any) => {
            const data = multiplayerStore.processMessage(message);

            switch (data) {
                case "JOIN":
                    updateLobby();
                    break;
                case "PROCEED": {
                    stompClient.deactivate();

                    const attemptId =
                        multiplayerStore.multiplayerData?.competition.competitors
                            .filter(
                                (competitor) =>
                                    competitor.user.id === currentUser.id
                            )
                            .map((competitor) => competitor.attempt.id)[0];
                    const quizId =
                        multiplayerStore.multiplayerData?.competition
                            .competitors[0].attempt.quiz?.id;

                    router.push({
                        name: "quiz-player",
                        params: { id: quizId },
                        query: { attemptId },
                    });
                    break;
                }
                case null:
                    break;
            }
        });
    },
});

onMounted(async () => {
    if (!currentUser) {
        router.push({ name: "login" });
        return;
    }

    useNotificationStore().addNotification({
        message: `${currentUser.name} joined lobby ${lobbyCode.value}!`,
        type: "info",
    });

    const lobbyCodeParams = router.currentRoute.value.params.lobbyCode;
    multiplayerStore.lobbyCode = Array.isArray(lobbyCodeParams)
        ? Number(lobbyCodeParams[0])
        : Number(lobbyCodeParams);

    const response = await multiplayerApi.joinCompetition(
        multiplayerStore.lobbyCode
    );
    multiplayerStore.multiplayerData = response.data;
    stompClient.activate();
});

// Backend call stuffz
const multiplayerApi = new CompetitionApi();
const { execute: executeStartGame } = useExecutablePromise(
    async (...args: Parameters<typeof multiplayerApi.startCompetition>) =>
        multiplayerApi.startCompetition(...args)
);

const { execute: executeUpdateLobby } = useExecutablePromise(
    async (...args: Parameters<typeof multiplayerApi.getCompetition>) => {
        const response = await multiplayerApi.getCompetition(...args);
        multiplayerStore.multiplayerData = {
            competitionId: multiplayerStore.multiplayerId,
            competition: response.data,
        };
    }
);

function updateLobby() {
    if (!multiplayerStore.lobbyCode) {
        useNotificationStore().addNotification({
            message: "Could not update the lobby. The lobby code was missing.",
            type: "error",
        });
        router.push({ name: "lobby-chooser" });
    } else {
        executeUpdateLobby(multiplayerStore.lobbyCode);
    }
}

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
            <div class="user-list">
                <div
                    v-for="user in lobbyUsers"
                    :key="user.id"
                    class="user"
                    :style="{
                        backgroundColor:
                            user.id === currentUser.id
                                ? 'lightblue'
                                : 'var(--primary-200)',
                    }"
                >
                    <img height="100" :src="user.avatar" alt="Avatar" />
                    <p>{{ user.name }}</p>
                </div>
                <div class="user">More friends will show up here!</div>
            </div>
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
