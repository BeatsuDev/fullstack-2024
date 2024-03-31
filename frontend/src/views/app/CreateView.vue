<template>
    <div class="centered-container">
        <h3>Create quiz</h3>
        <QuizForm :value="quiz" @submit="createQuiz" :loading="loading" />
        <div style="margin-top: 10px;">
            <a @click="inspirationModal = true" style="cursor: pointer;">Need some inspiration? Or import quiz.</a>
        </div>
        <GenericModal title="Templates" v-model="inspirationModal">
            <div style="display: flex; flex-direction: column;">

            <p>
                Choose a template to get started with your quiz
            </p>
            <SelectComponent v-model="selectedTemplate">
                <option v-for="template in templates" :key="template.title" :value="template">
                    {{ template.title }}
                </option>
            </SelectComponent>
                <p>or upload template</p>
                <input type="file" @change="uploadJson" style="margin-bottom: 1rem"
                       accept=".json"
                />
            <ButtonComponent @click="createQuiz()">Use template</ButtonComponent>
                <div class="error-message" style="margin-top: 1rem" v-if="error">
                    {{ error }}
                </div>
            </div>
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
import SelectComponent from "@/components/SelectComponent.vue";

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

const selectedTemplate = ref<QuizTemplate | null>();

const error = ref<string | null>(null);
function uploadJson(file) {

const reader = new FileReader();
    reader.onload = async (e) => {
        try {
            const json = JSON.parse(e.target.result as string);
            selectedTemplate.value = json;
        } catch (e) {
            error.value = "Invalid json file";
        }
    };
    reader.readAsText(file.target.files[0]);

}

const questionsApi = new QuestionApi();

function createQuiz(quiz?: QuizCreate) {
    let questions =  []

    if (!quiz ) {
        if (!selectedTemplate.value) {
            error.value = "Please select a template or upload a json file";
            return;
        }
        quiz = selectedTemplate.value
        console.log(quiz)
        questions = selectedTemplate.value.questions
        delete selectedTemplate.value.questions

    }
    // @ts-ignore
    if (quiz.id) {
        // @ts-ignore
        delete quiz.id;
    }
    quizApi
        .createQuiz(quiz)
        .then((data) => {
            if (questions.length > 0) {
                questions.forEach(async (question) => {
                    await questionsApi.createQuestion({
                        ...question,
                        quizId: data.data.id,
                    });
                });

            }
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
