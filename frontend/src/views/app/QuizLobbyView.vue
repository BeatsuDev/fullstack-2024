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

const multiplayerStore = useMultiplayerStore();
const authenticationStore = useAuthenticationStore();
const lobbyCode = computed(() => router.currentRoute.value.params.lobbyCode);
const currentUser = authenticationStore.loggedInUser as User;

const lobbyUsers = [
    { ...currentUser, avatar: new AvatarGenerator().generateRandomAvatar() },
    {
        name: "John Doe",
        id: "1",
        avatar: new AvatarGenerator().generateRandomAvatar(),
    },
    {
        name: "Jane Doe",
        id: "2",
        avatar: new AvatarGenerator().generateRandomAvatar(),
    },
    {
        name: "Alice",
        id: "3",
        avatar: new AvatarGenerator().generateRandomAvatar(),
    },
] as {
    name: string;
    id: string;
    avatar: string;
}[];

// Set name
// TODO: and notify the server about the new user

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

    // Setup websocket
});

// Backend call stuffz
const multiplayerApi = new CompetitionApi();

const { execute: executeStartGame } = useExecutablePromise(
    (...args: Parameters<typeof multiplayerApi.startCompetition>) =>
        multiplayerApi.startCompetition(...args)
);

async function startGame() {
    const lobbyCode = multiplayerStore.lobbyCode;
    if (!lobbyCode) {
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

@media (min-width: 768px) {
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
