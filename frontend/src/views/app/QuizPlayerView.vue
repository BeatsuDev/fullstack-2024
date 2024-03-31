<script setup lang="ts">
import { ref, computed } from "vue";
import { AttemptApi, type AnswerDTO } from "@/api";
import { usePromise, useExecutablePromise } from "@/composables/promise";
import { useNotificationStore } from "@/stores/notification";
import router from "@/router";

import QuestionPlayer from "@/components/quiz-player/QuestionPlayer.vue";

const notificationStore = useNotificationStore();
const attemptApi = new AttemptApi();
const {
    promise,
    data: response,
    loading,
    error,
} = usePromise(
    attemptApi.attemptQuiz(
        (router.currentRoute.value.params.id as unknown as string) ?? ""
    )
);

promise.then((response) => {
    console.log(response.data);
});

const questionNumber = ref(0);
const currentQuestion = computed(
    () => response.value?.data.quiz!.questions[questionNumber.value] ?? null
);
const currentQuiz = computed(() => response.value?.data ?? null);
const currentAttemptId = computed(() => response.value?.data.id ?? null);

const { execute: executeSubmitAnswer, error: submitError } =
    useExecutablePromise(
        async (...args: Parameters<typeof attemptApi.submitAnswer>) => {
            return await attemptApi.submitAnswer(...args);
        }
    );

async function submitAnswer(answer: string) {
    console.log(answer);
    if (currentQuiz.value == null) return;
    if (currentQuestion.value == null) return;
    if (currentAttemptId.value == null) return;

    // Send the answer to the server
    const submitPromise = executeSubmitAnswer(
        {
            questionId: currentQuestion.value.id,
            answer,
        } as AnswerDTO,
        router.currentRoute.value.params.id as string,
        currentAttemptId.value
    );

    submitPromise
        .then((response) => {
            const solutionData = response.data;

            notificationStore.addNotification({
                message: `Your answer was ${
                    solutionData.correct ? "correct!" : "incorrect!"
                }`,
                type: solutionData.correct ? "success" : "warning",
            });
            questionNumber.value++;

            // Check if the quiz is finished
            if (
                questionNumber.value >=
                currentQuiz.value!.quiz!.questions.length
            ) {
                finishQuiz();
                return;
            }
        })
        .catch((error) => {
            if (submitError.value != null) {
                notificationStore.addNotification({
                    message: "Failed to submit answer. " + submitError.value,
                    type: "error",
                });
                return;
            }
        });
}

function finishQuiz() {
    console.log("Quiz finished!");
}
</script>

<template>
    <main>
        <div class="player-container" v-if="loading">Loading...</div>
        <div class="player-container" v-else-if="error">{{ error }}</div>
        <div class="player-container" v-else-if="response">
            <h1>{{ response.data.quiz!.title }}</h1>
            <QuestionPlayer
                v-if="currentQuestion"
                :question="currentQuestion"
                @answerSelected="submitAnswer"
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
