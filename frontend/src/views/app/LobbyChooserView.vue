<script setup lang="ts">
import { ref } from "vue";
import ButtonComponent from "@/components/ButtonComponent.vue";

const lobbyCode = ref("");
</script>

<template>
    <div class="lobby-chooser-container">
        <input v-model="lobbyCode" type="text" placeholder="123456" />
        <ButtonComponent
            large
            @click="
                $router.push({
                    name: 'quiz-lobby',
                    params: { lobbyCode },
                })
            "
        >
            Join Lobby
        </ButtonComponent>
    </div>
</template>

<style scoped>
.lobby-chooser-container {
    display: flex;
    justify-content: center;
    align-items: center;
    height: 100%;
}

input {
    font-size: 2em;
    max-width: 150px;
    text-align: right;
    margin-right: 0.2rem;
}
</style>
