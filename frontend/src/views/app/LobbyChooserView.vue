<script lang="ts" setup>
import { ref } from "vue";
import ButtonComponent from "@/components/ButtonComponent.vue";

import router from "@/router";

const lobbyCode = ref("");

async function joinLobby() {
    router.push({
        name: "quiz-lobby",
        params: { lobbyCode: lobbyCode.value },
    });
}
</script>

<template>
    <div class="lobby-chooser-container">
        <h3>Join lobby</h3>
        <div class="join-wrapper">
            <input v-model="lobbyCode" placeholder="123456" type="text" />
            <ButtonComponent large @click="joinLobby"> Join Lobby</ButtonComponent>
        </div>
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

.join-wrapper {
    display: flex;
    justify-content: center;
    align-items: center;
}
</style>
