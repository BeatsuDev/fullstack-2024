<script setup lang="ts">
import { ref, computed } from "vue";
import { QuizApi } from "@/api";
import { usePromise } from "@/composables/promise";
import router from "@/router";

import QuestionPlayer from "@/components/quiz-player/QuestionPlayer.vue";

const quizApi = new QuizApi();
const {
    promise,
    data: response,
    loading,
    error,
} = usePromise(
    quizApi.quizIdGet(
        (router.currentRoute.value.params.id as unknown as string) ?? ""
    )
);

promise.then((response) => {
    console.log(response.data);
});

const questionNumber = ref(0);
const currentQuestion = computed(
    () => response.value?.data.questions[questionNumber.value] ?? null
);
const currentQuiz = computed(() => response.value?.data ?? null);

function nextQuestion() {
    if (currentQuiz.value == null) return;
    if (questionNumber.value >= (response.value?.data.questions.length ?? 0)) {
        console.log("Quiz finished!");
        return;
    }
    questionNumber.value++;
}
</script>

<template>
    <main>
        <div class="player-container" v-if="loading">Loading...</div>
        <div class="player-container" v-else-if="error">{{ error }}</div>
        <div class="player-container" v-else-if="response">
            <h1>{{ response.data.title }}</h1>
            <QuestionPlayer
                v-if="currentQuestion"
                :question="currentQuestion"
                @answerSelected="nextQuestion"
            />
            <div v-else>No question selected...</div>
        </div>
    </main>
</template>

<style scoped>
.player-container {
    height: calc(100vh - 66px - 4em);
    padding: 2em;

    display: flex;
    flex-direction: column;
}

.player-container > h1 {
    margin-top: 1em;
    width: 100%;
    text-align: center;
}
</style>
