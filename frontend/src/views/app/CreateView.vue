<template>
    <form @submit.prevent ref="form">
        <h3>Lag quiz<</h3>
        <label for="title">Title</label>
        <ValidatedInput id="title" :validator="v$.title" v-model="quiz.title" />
        <label for="Description"></label>
        <ValidatedInput
            id="description"
            :validator="v$.description"
            v-model="quiz.description"
        />
        <input type="range" min="1" max="10" v-model="quiz.difficulty" />
        <ButtonComponent @click="createQuiz" :loading="loading"
            >Create</ButtonComponent
        >
    </form>
</template>
<script setup lang="ts">
import ValidatedInput from "@/components/ValidatedInput.vue";
import { reactive, type Ref, ref } from "vue";
import { type QuizCreate } from "@/api/models/quiz-create";
import { useExecutablePromise } from "@/composables/promise";
import ButtonComponent from "@/components/ButtonComponent.vue";
import { QuizApi } from "@/api";
import { required } from "@vuelidate/validators";
import { useVuelidate } from "@vuelidate/core";
import { useNotificationStore } from "@/stores/notification";
import { useRouter } from "vue-router";

const quiz = reactive({
    title: "",
    description: "",
    difficulty: 1,
});

const formRules = {
    title: { required },
    description: { required },
    difficulty: { required },
};

const v$ = useVuelidate(formRules, quiz);

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

const form = ref<HTMLFormElement | null>(null);
async function createQuiz() {
    if (!form.value) return;
    if (!(await v$.value.$validate())) {
        return;
    }

    await execute(quiz);
}
</script>
