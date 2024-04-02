<script setup lang="ts">
import ButtonComponent from "@/components/ButtonComponent.vue";
import { useMultiplayerStore } from "@/stores/multiplayer";
import router from "@/router";
import LobbyResult from "@/components/LobbyResult.vue";

const quizId = router.currentRoute.value.params.id as string;

// Multiplayer
const multiplayerStore = useMultiplayerStore();
const gameData = multiplayerStore.multiplayerData;
multiplayerStore.reset();
</script>

<template>
    <div class="quiz-complete-container">
        Wow! You completed the quiz! Great job!

        <LobbyResult results />
        <div class="buttons">
            <ButtonComponent
                v-if="quizId"
                large
                @click="
                    router.push({
                        name: 'quiz-attempts',
                        params: { id: quizId },
                    })
                "
            >
                View your past attempts on this quiz
            </ButtonComponent>
            <ButtonComponent
                v-if="quizId"
                large
                filled
                @click="
                    router.push({
                        name: 'quiz',
                        params: { id: quizId },
                    })
                "
            >
                Play again
            </ButtonComponent>
            <ButtonComponent
                v-else
                large
                filled
                @click="
                    router.push({
                        name: 'explore',
                    })
                "
            >
                Find another quiz
            </ButtonComponent>
        </div>
    </div>
</template>

<style scoped>
.quiz-complete-container {
    font-size: 1.5em;
    margin-top: 4em;
    display: flex;
    flex-direction: column;
    justify-content: center;
    align-items: center;
    height: 100%;
}

.buttons {
    margin-top: 1em;
    display: flex;
    width: 100%;
    justify-content: center;
    gap: 1em;
}

.buttons > button {
    font-size: 1em;
}
</style>
