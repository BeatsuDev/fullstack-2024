<script lang="ts" setup>
import { ref } from "vue";
import ButtonComponent from "@/components/ButtonComponent.vue";

import router from "@/router";

const lobbyCode = ref("" as number | string);

async function joinLobby() {
    const sLobbyCode = lobbyCode.value.toString().padStart(6, "0");
    router.push({
        name: "quiz-lobby",
        params: { lobbyCode: sLobbyCode },
    });
}
</script>

<template>
    <div class="lobby-chooser-container">
        <h3>Join lobby</h3>
        <form class="join-wrapper" @submit.prevent="joinLobby">
            <input
                v-model="lobbyCode"
                placeholder="123456"
                type="number"
                min="0"
                max="999999"
            />
            <ButtonComponent large>Join Lobby</ButtonComponent>
        </form>
    </div>
</template>

<style scoped>
.lobby-chooser-container {
    display: flex;
    justify-content: center;
    align-items: center;
    flex-direction: column;
    margin-top: 4rem;
}

input {
    font-size: 2em;
    max-width: 150px;
    text-align: right;
    margin-right: 0.2rem;
}

input::-webkit-outer-spin-button,
input::-webkit-inner-spin-button {
    -webkit-appearance: none;
    margin: 0;
}

input[type="number"] {
    -moz-appearance: textfield;
}

.join-wrapper {
    display: flex;
    justify-content: center;
    align-items: center;
}
</style>
