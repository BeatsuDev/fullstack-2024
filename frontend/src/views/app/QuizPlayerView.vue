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
</script>

<template>
    <main>
        <div v-if="loading">Loading...</div>
        <div v-else-if="error">{{ error }}</div>
        <div v-else-if="response">
            <h1>{{ response.data.title }}</h1>
            <QuestionPlayer :question="currentQuestion" />
        </div>
    </main>
</template>

<style scoped>
main {
    height: calc(100vh - 66px);
}
</style>
