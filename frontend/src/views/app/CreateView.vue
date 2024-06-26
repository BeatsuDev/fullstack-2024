<template>
    <div class="centered-container">
        <div class="card" style="margin-top: 2rem">
            <h3>Create quiz</h3>
            <QuizForm :value="quiz" @submit="createQuiz" />
            <div style="margin-top: 10px">
                <a style="cursor: pointer" @click="inspirationModal = true"
                    >Need some inspiration? Or import quiz.</a
                >
            </div>
        </div>
        <GenericModal v-model="inspirationModal" title="Templates">
            <div style="display: flex; flex-direction: column">
                <p>Choose a template to get started with your quiz</p>
                <SelectComponent v-model="selectedTemplate">
                    <option
                        v-for="template in templates"
                        :key="template.title"
                        :value="template"
                    >
                        {{ template.title }}
                    </option>
                </SelectComponent>
                <p>or upload template</p>
                <input
                    accept=".json"
                    style="margin-bottom: 1rem"
                    type="file"
                    @change="uploadJson"
                />
                <ButtonComponent @click="createQuiz()"
                    >Use template
                </ButtonComponent>
                <div
                    v-if="error"
                    class="error-message"
                    style="margin-top: 1rem"
                >
                    {{ error }}
                </div>
            </div>
        </GenericModal>
    </div>
</template>
<script lang="ts" setup>
import { reactive, ref } from "vue";
import { type QuizCreate } from "@/api/models/quiz-create";
import { QuestionApi, type QuestionCreate, QuizApi } from "@/api";
import { useNotificationStore } from "@/stores/notification";
import { useRouter } from "vue-router";
import QuizForm from "@/components/QuizForm.vue";
import ButtonComponent from "@/components/ButtonComponent.vue";
import GenericModal from "@/components/GenericModal.vue";
import { templates } from "@/composables/templates";
import SelectComponent from "@/components/SelectComponent.vue";

const quiz = reactive({
    title: "",
    description: "",
    difficulty: 1,
    categories: [],
} as QuizCreate);

const notificationStore = useNotificationStore();
const router = useRouter();

const quizApi = new QuizApi();

interface QuizTemplate extends QuizCreate {
    questions: QuestionCreate[];
}

const inspirationModal = ref(false);

const selectedTemplate = ref<QuizTemplate | null>();

const error = ref<string | null>(null);

function uploadJson(file: any) {
    const reader = new FileReader();
    reader.onload = async (e) => {
        try {
            const json = JSON.parse(e.target?.result as string);
            selectedTemplate.value = json;
        } catch (e) {
            error.value = "Invalid json file";
        }
    };
    reader.readAsText(file.target.files[0]);
}

const questionsApi = new QuestionApi();

async function createQuiz(quiz?: QuizCreate) {
    let questions: QuestionCreate[] = [];

    if (!quiz) {
        if (!selectedTemplate.value) {
            error.value = "Please select a template or upload a json file";
            return;
        }
        quiz = selectedTemplate.value;
        questions = selectedTemplate.value.questions;
    }

    quiz = {
        title: quiz.title,
        description: quiz.description,
        difficulty: quiz.difficulty,
        categories: quiz.categories.map((c) => (c.id ? c.id : c)),
    };

    await quizApi
        .createQuiz(quiz)
        .then(success)
        .catch((error) => {
            notificationStore.addNotification({
                message: "Could not create quiz. " + error.message,
                type: "error",
            });
        });

    async function success(
        response: Awaited<ReturnType<typeof quizApi.createQuiz>>
    ) {
        for (const question of questions) {
            await questionsApi.createQuestion({
                ...question,
                quizId: response.data.id,
            });
        }
        notificationStore.addNotification({
            message: "Quiz created",
            type: "success",
        });
        router.push("/quizzes/" + response.data.id);
    }
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
