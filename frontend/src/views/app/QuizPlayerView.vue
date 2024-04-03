<script lang="ts" setup>
import { computed, ref } from "vue";
import { type AnswerDTO, AttemptApi } from "@/api";
import { useExecutablePromise, usePromise } from "@/composables/promise";
import { useNotificationStore } from "@/stores/notification";
import { useMultiplayerStore } from "@/stores/multiplayer";
import router from "@/router";
import { Client } from "@stomp/stompjs";

import QuestionPlayer from "@/components/quiz-player/QuestionPlayer.vue";
import LobbyResult from "@/components/LobbyResult.vue";
import AlertComponent from "@/components/AlertComponent.vue";

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
const isMultiplayer = computed(() => multiplayerStore.lobby != null);

const { execute: executeSubmitAnswer, error: submitError } =
    useExecutablePromise(
        async (...args: Parameters<typeof attemptApi.submitAnswer>) => {
            return await attemptApi.submitAnswer(...args);
        }
    );

const hasAnswered = ref(false);

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
            } else {
                hasAnswered.value = true;
            }
        })
        .catch((e) => {
            if (e.response?.status == 409) {
                hasAnswered.value = true;
                notificationStore.addNotification({
                    message: "You have already answered this question.",
                    type: "warning",
                });
                return;
            }
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
            const data = multiplayerStore.processMessage(message);
            if (!data) return;

            switch (data.type) {
                case "FINISH":
                    finishQuiz();
                    break;
                case "JOIN":
                    break;
                case "PROCEED":
                    hasAnswered.value = false;
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

let countdownInterval = null as number | null;
const countdown = ref(undefined as number | undefined);

function setQuestionNumber(questionId: string) {
    showResultsView();
    console.log("id " + questionId)
    router.currentRoute.value.query.questionId = questionId;
    questionNumber.value =
        currentQuiz.value?.quiz?.questions
            .map((q) => q.id)
            .indexOf(questionId) || 0;

    if (countdownInterval) clearInterval(countdownInterval);
    countdown.value = 20;
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
            query: { lobby: lobbyCode, id: router.currentRoute.value.params.id },
        });
        return;
    }
    router.push({
        name: "quiz-complete",

        query: { id: router.currentRoute.value.params.id },
    });
}

</script>

<template>
    <AlertComponent v-if="loading" class="player-container" type="info">Loading...</AlertComponent>
    <LobbyResult v-else-if="showResults && multiplayerStore.lobbyUsers.length > 0" results />
    <AlertComponent v-else-if="error" class="player-container" type="danger">{{ error }}</AlertComponent>
    <AlertComponent type="info" v-else-if="hasAnswered">Waiting for other players...</AlertComponent>
    <div v-else-if="response" class="player-container">
        <h1 class="header">{{ response.data.quiz!.title }}</h1>
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

.header {
    margin-top: 2rem;
}


@media (max-width: 600px) {
    .player-container {
        margin: 0 1rem;
    }

    .header {
        margin-top: 5rem;
    }
}

</style>
