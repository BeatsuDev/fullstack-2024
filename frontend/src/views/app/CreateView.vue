<template>
    <h3>Create quiz</h3>
    <QuizForm :value="quiz" @submit="execute(quiz)" :loading="loading" />
</template>
<script setup lang="ts">
import { reactive } from "vue";
import { type QuizCreate } from "@/api/models/quiz-create";
import { useExecutablePromise } from "@/composables/promise";
import { QuizApi } from "@/api";
import { useNotificationStore } from "@/stores/notification";
import { useRouter } from "vue-router";
import QuizForm from "@/components/QuizForm.vue";

const quiz = reactive({
    title: "",
    description: "",
    difficulty: 1,
});


const notificationStore = useNotificationStore();
const router = useRouter();

const quizApi = new QuizApi();
const { execute, loading } = useExecutablePromise(async (quiz: QuizCreate) => {
    quizApi
        .createQuiz(quiz)
        .then((data) => {
            notificationStore.addNotification({
                message: "Quiz created",
                type: "success",
            });
            router.push("/quizzes/" + data.data.id);
        })
        .catch(() => {
            notificationStore.addNotification({
                message: "Could not create quiz",
                type: "error",
            });
        });
});

</script>
