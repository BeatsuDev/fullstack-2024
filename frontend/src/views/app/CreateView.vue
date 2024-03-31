<template>
    <div class="centered-container">
        <h3>Create quiz</h3>
        <QuizForm :value="quiz" @submit="createQuiz" :loading="loading" />
        <div>
            <a @click="inspirationModal = true">Need some inspiration?</a>
        </div>
        <GenericModal title="Templates" v-model="inspirationModal">
            <p>Here are some templates you can use:</p>
            <select v-model="selectedTemplate">
                <option v-for="template in templates" :key="template.title" :value="template">
                    {{ template.title }}
                </option>
            </select>
            <ButtonComponent @click="createQuiz()">Use template</ButtonComponent>
        </GenericModal>
    </div>
</template>
<script setup lang="ts"> import { reactive, ref } from "vue";
import { type QuizCreate } from "@/api/models/quiz-create";
import { useExecutablePromise } from "@/composables/promise";
import { QuestionApi, type QuestionCreate, QuizApi } from "@/api";
import { useNotificationStore } from "@/stores/notification";
import { useRouter } from "vue-router";
import QuizForm from "@/components/QuizForm.vue";
import ButtonComponent from "@/components/ButtonComponent.vue";
import GenericModal from "@/components/GenericModal.vue";
import {templates} from "@/composables/templates";

const quiz = reactive({
    title: "",
    description: "",
    difficulty: 1,
});


const notificationStore = useNotificationStore();
const router = useRouter();

const quizApi = new QuizApi();
const { execute, loading } = useExecutablePromise(async (quiz: QuizCreate) => {
});



interface QuizTemplate extends QuizCreate {
    questions: QuestionCreate[];
}

const inspirationModal = ref(false);

const selectedTemplate = ref<QuizTemplate>(templates[0]);

const questionsApi = new QuestionApi();

function createQuiz(quiz?: QuizCreate) {
    let questions =  []

    if (!quiz ) {
        quiz = selectedTemplate.value
        console.log(quiz)
        questions = selectedTemplate.value.questions
        delete selectedTemplate.value.questions
    }
    quizApi
        .createQuiz(quiz)
        .then((data) => {
            notificationStore.addNotification({
                message: "Quiz created",
                type: "success",
            });
            if (questions.length > 0) {
                questions.forEach(async (question) => {
                    await questionsApi.createQuestion({
                        ...question,
                        quizId: data.data.id,
                    });
                });

            }
            router.push("/quizzes/" + data.data.id);
        })
        .catch(() => {
            notificationStore.addNotification({
                message: "Could not create quiz",
                type: "error",
            });
        });

}


</script>
<style scoped>
.centered-container {
    display: flex;
    flex-direction: column;
    align-items: center;
    justify-content: center;
    height: 100%;
}
</style>
