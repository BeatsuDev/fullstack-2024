<script setup lang="ts">
import { ref, computed } from "vue";
import { AttemptApi, type AnswerDTO, CompetitionApi } from "@/api";
import { usePromise, useExecutablePromise } from "@/composables/promise";
import { useNotificationStore } from "@/stores/notification";
import { useMultiplayerStore } from "@/stores/multiplayer";
import router from "@/router";
import { Client } from "@stomp/stompjs";

import QuestionPlayer from "@/components/quiz-player/QuestionPlayer.vue";
import LobbyResult from "@/components/LobbyResult.vue";

const multiplayerStore = useMultiplayerStore();
const notificationStore = useNotificationStore();
const attemptApi = new AttemptApi();
const {
    data: response,
    loading,
    error,
} = router.currentRoute.value.query?.attemptId
    ? usePromise(
          attemptApi
              .getQuizAttempt(
                  router.currentRoute.value.params.id as string,
                  router.currentRoute.value.query.attemptId as string
              )
              .then((r) => {
                  setQuestionNumberFromQuery();
                  return r;
              })
      )
    : usePromise(
          attemptApi.attemptQuiz(
              (router.currentRoute.value.params.id as unknown as string) ?? ""
          )
      );

if (!router.currentRoute.value.query.attemptId) {
    multiplayerStore.reset();
}

const questionNumber = ref(0);
const currentQuestion = computed(
    () => response.value?.data.quiz!.questions[questionNumber.value] ?? null
);
const currentQuiz = computed(() => response.value?.data ?? null);
const currentAttemptId = computed(() => response.value?.data.id ?? null);
const isMultiplayer = computed(() => multiplayerStore.multiplayerData != null);

const { execute: executeSubmitAnswer, error: submitError } =
    useExecutablePromise(
        async (...args: Parameters<typeof attemptApi.submitAnswer>) => {
            return await attemptApi.submitAnswer(...args);
        }
    );

const showResults = ref(false);
async function submitAnswer(answer: string) {
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
        currentAttemptId.value,
        multiplayerStore.multiplayerId
    );

    submitPromise
        .then((response) => {
            const solutionData = response.data;

            notificationStore.addNotification({
                message: `Your answer was ${
                    solutionData.correct ? "correct!" : "incorrect!"
                } ${isMultiplayer.value ? "Waiting for other players to finish..." : ""}`,
                type: solutionData.correct ? "success" : "warning",
            });

            if (!isMultiplayer.value) {
                goToNextQuestion();
            }
        })
        .catch(() => {
            if (submitError.value != null) {
                notificationStore.addNotification({
                    message: "Failed to submit answer. " + submitError.value,
                    type: "error",
                });
                return;
            }
        });
}

function showResultsView() {
    showResults.value = true;
    setTimeout(() => {
        showResults.value = false;
    }, 3000);
}

const stompClient = new Client({
    brokerURL: "ws://localhost:8080/competition-ws",
    onConnect: () => {
        stompClient.subscribe("/competition", (message: any) => {
            console.log(message);
            const data = multiplayerStore.processMessage(message);
            console.log(data);
            if (!data) return;

            switch (data.type) {
                case "FINISH":
                    finishQuiz();
                    break;
                case "JOIN":
                    break;
                case "PROCEED":
                    setQuestionNumber(data.questionId);
                    break;
                default:
                    data satisfies never;
            }
        });
    },
});

if (isMultiplayer.value) {
    stompClient.activate();
}

function goToNextQuestion() {
    questionNumber.value++;
    if (questionNumber.value >= currentQuiz.value!.quiz!.questions.length) {
        finishQuiz();
        return;
    }
}

let countdownInterval = null as NodeJS.Timeout | null;
const countdown = ref(undefined as number | undefined);

function setQuestionNumber(questionId: string) {
    showResultsView();
    router.currentRoute.value.query.questionId = questionId;
    questionNumber.value =
        currentQuiz.value?.quiz?.questions
            .map((q) => q.id)
            .indexOf(questionId) || 0;

    if (countdownInterval) clearInterval(countdownInterval);
    countdown.value = 15;
    countdownInterval = setInterval(
        () => countdown.value && --countdown.value,
        1000
    );
}

function setQuestionNumberFromQuery() {
    let questionId = router.currentRoute.value.query.questionId;
    if (Array.isArray(questionId)) questionId = questionId[0];
    if (questionId) {
        setQuestionNumber(questionId);
    }
}

function finishQuiz() {
    stompClient.deactivate();
    // Ensure a deep copy just in case
    const lobbyCode = multiplayerStore.lobbyCode
        ? parseInt(multiplayerStore.lobbyCode.toString())
        : null;

    if (lobbyCode) {
        router.push({
            name: "quiz-complete",
            query: { lobby: lobbyCode },
        });
        return;
    }
    router.push({ name: "quiz-complete" });
}
</script>

<template>
    <LobbyResult v-if="showResults" />
    <div class="player-container" v-else-if="loading">Loading...</div>
    <div class="player-container" v-else-if="error">{{ error }}</div>
    <div class="player-container" v-else-if="response">
        <h1 style="margin-top: 1rem">{{ response.data.quiz!.title }}</h1>
        <QuestionPlayer
            v-if="currentQuestion"
            :countdown="countdown"
            :question="currentQuestion"
            @answerSelected="submitAnswer"
        />
        <div v-else>No question selected...</div>
    </div>
</template>
<style scoped>
.player-container {
    margin: 0 2rem;
}
</style>
