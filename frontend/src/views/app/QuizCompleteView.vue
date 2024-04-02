<script setup lang="ts">
import ButtonComponent from "@/components/ButtonComponent.vue";
import { useMultiplayerStore } from "@/stores/multiplayer";
import router from "@/router";

const quizId = router.currentRoute.value.params.id as string;

// Fetch multiplayer data and reset it
const multiplayerStore = useMultiplayerStore();
const gameData = multiplayerStore.multiplayerData;
multiplayerStore.$reset();
</script>

<template>
    <div class="quiz-complete-container">
        Wow! You completed the quiz! Great job!

        <ButtonComponent
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
            @click="
                router.push({
                    name: 'quiz-attempts',
                    params: { id: quizId },
                })
            "
        >
            View your past attempts on this quiz
        </ButtonComponent>
    </div>
</template>

<style scoped>
.quiz-complete-container {
    display: flex;
    flex-direction: column;
    justify-content: center;
    align-items: center;
    height: 100%;
}
</style>
