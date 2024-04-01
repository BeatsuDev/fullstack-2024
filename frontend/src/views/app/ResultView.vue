<template>
    <main class="app-container">
        <div v-if="errorMessage">
            {{ errorMessage }}
        </div>
        <div v-else>
            <h3>Results</h3>
            <p>
                You have made {{ attempts }} attempts on this quiz. Keep it up!
            </p>
            <div class="row" v-if="data">
                <div class="big">
                    <Bar :data="chartDataBasedOnAttempts" />
                </div>
                <div class="small">
                    <div v-if="bestAttempt">
                        <h3>History</h3>
                        <p>
                            You had your highest score on {{ new Date(bestAttempt.attemptedAt).toLocaleString() }},
                            with a score of {{ bestAttemptScore }}.
                        </p>
                        <p v-if="numOfUncompletedAttempts">
                            You also have {{ numOfUncompletedAttempts }} uncompleted attempts.
                        </p>
                    </div>
                </div>
            </div>
            <div class="row">
                <div class="small">
                    <h3>Incorrect answers</h3>
                    <p>
                        This chart shows the number of times you have answered
                        incorrectly on each question.
                    </p>
                </div>
                <div class="big">
                    <Bar :data="chartDataBasedOnIncorrectAnswerOnQuestion" />
                </div>

            </div>
        </div>
    </main>

</template>
<script lang="ts" setup>

import { useRoute } from "vue-router";
import { computed } from "vue";
import useQuizAttempt from "@/composables/useQuizAttempt";
import { Bar } from "vue-chartjs";
import { BarElement, CategoryScale, Chart as ChartJS, Legend, LinearScale, Title, Tooltip } from "chart.js";

ChartJS.register(Title, Tooltip, Legend, BarElement, CategoryScale, LinearScale);

const route = useRoute();

const quizId = computed(() => route.params.id.toString());

const {
    data,
    attempts,
    chartDataBasedOnAttempts,
    bestAttempt,
    bestAttemptScore,
    numOfUncompletedAttempts,
    chartDataBasedOnIncorrectAnswerOnQuestion,
    errorMessage,
} = useQuizAttempt(quizId);
</script>
<style scoped>
.row {
    display: flex;
    margin-bottom: 1rem;
    background-color: var(--color-background-mute);
}

.small {
    width: 30%;
    padding: 1rem;
    background-color: var(--primary-50);
}

.big {
    width: 70%;
    padding: 1rem;
}

@media (max-width: 768px) {
    .row {
        flex-direction: column;
    }

    .small {
        width: 100%;
    }

    .big {
        width: 100%;
    }
}
</style>