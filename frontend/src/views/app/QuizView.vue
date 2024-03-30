<template>
    <main>
        <div class="container">
            <div v-if="errorMessage">
                <p>{{ errorMessage }}</p>
            </div>
            <div v-else-if="loadingDebounced">
                <div v-if="loading">Loading...</div>
            </div>
            <div v-else-if="quiz">
                <div class="header">
                    <div>
                        <h1 style="margin-top: 0">{{ quiz.title }}</h1>
                        <div class="action-bar">
                            <ButtonComponent filled large>Play</ButtonComponent>
                        </div>
                        <h3>Description</h3>
                        <p>{{ quiz.description }}</p>
                    </div>
                    <h3>Description</h3>
                    <p>{{ quiz.description }}</p>
                    <ButtonComponent @click="quizModal = true">Edit</ButtonComponent>
                </div>
                <div v-if="isOwnerOrCollaborator">
                    <h3>Questions</h3>
                    <QuestionCard
                        v-for="question in quiz.questions"
                        :key="question.id"
                        :value="question"
                        editable
                        @edit="editQuestion"
                        @delete="reveal"
                    />
                </div>
                <ButtonComponent
                    arge
                    filled
                    class="centered"
                    @click="questionModal = true"
                >Add question
                </ButtonComponent>
            </div>
        </div>
    </main>
    <GenericModal v-model="isRevealed" title="Delete question">
        <p>Are you sure you want to delete this question?</p>
        <ButtonComponent filled @click="confirm">Yes</ButtonComponent>
        <ButtonComponent @click="cancel">No</ButtonComponent>
    </GenericModal>
    <GenericModal v-model="questionModal" title="Add question">
        <QuestionForm :value="question" @submit="submitQuestion" />
    </GenericModal>
    <GenericModal v-model="quizModal" title="Edit quiz">
        <QuizForm :value="quiz" @submit="updateQuiz" />
    </GenericModal>
</template>
<script lang="ts" setup>
import { type Question, QuestionApi, type QuestionCreate, type Quiz, QuizApi } from "@/api";
import { useExecutablePromise } from "@/composables/promise";
import ButtonComponent from "@/components/ButtonComponent.vue";
import useQuizPermissions from "@/composables/useQuizPermissions";
import { computed, ref } from "vue";
import { useRoute } from "vue-router";
import QuestionCard from "@/components/QuestionCard.vue";
import GenericModal from "@/components/GenericModal.vue";
import QuestionForm from "@/components/QuestionForm.vue";
import { useNotificationStore } from "@/stores/notification";
import { useConfirmDialog } from "@vueuse/core";
import QuizForm from "@/components/QuizForm.vue";
import useDebounceLoading from "@/composables/useDebounceLoading";

const route = useRoute();

const quizId = route.params.id.toString();

const quizApi = new QuizApi();

const { data, loading, error, execute } = useExecutablePromise(() => quizApi.quizIdGet(quizId));

const loadingDebounced = useDebounceLoading(loading);

execute();

const quizModal = ref(false);

function updateQuiz(value: Quiz) {
    //quizApi. updateQuiz(quizId, value);
    console.log(value);
    quizModal.value = false;
    execute();
}

const errorMessage = computed(() => {
    if (!error.value) {
        return "";
    }
    if (error.value.status == "404") {
        return "Quiz not found.";
    }
    if (error.value) {
        return "An unexpected error occurred. Please try again later.";
    }
});

const quiz = computed<Quiz>(() => data.value?.data as Quiz);

const { isOwnerOrCollaborator } = useQuizPermissions(quiz);

const questionModal = ref(false);

function blankQuestion() {
    return {
        quizId: quizId,
        question: "",
        options: [""],
        answer: "",
    } as QuestionCreate;
}

const questionApi = new QuestionApi();

async function submitQuestion(value: QuestionCreate) {
    questionModal.value = false;
    if ("id" in value) {
        await updateQuestion(value, value.id);
    } else {
        await createQuestion(value);
    }
    question.value = blankQuestion();
}

async function createQuestion(value: QuestionCreate) {
    try {
        await questionApi.createQuestion(value);
        execute();
    } catch (e) {
        notificationStore.addNotification({
            message: "An unexpected error occurred. Please try again later.",
            type: "error",
        });
    }
}

async function updateQuestion(value: QuestionCreate, id: string) {
    await questionApi.updateQuestion(value, id);
    execute();
}

function editQuestion(value: Question) {
    question.value = value;
    questionModal.value = true;
}

const notificationStore = useNotificationStore();

const { isRevealed, onConfirm, confirm, cancel, onReveal, reveal } =
    useConfirmDialog();

onReveal((value: Question) => {
    question.value = value;
});

onConfirm(() => {
    deleteQuestion(question.value);
});

const question = ref<QuestionCreate | Question>(blankQuestion());

async function deleteQuestion(question: Question) {
    try {
        await questionApi.deleteQuestion(question.id);
        execute();
    } catch (e) {
        notificationStore.addNotification({
            message: "An unexpected error occurred. Please try again later.",
            type: "error",
        });
    }
}
</script>
<style scoped>
/* Fucking hacky as fuck. I wish I didn't have to do this. Change if can! */
/* 100vh - (nav height + nav y-padding) - (bottom nav height + bottom nav y-padding)*/
main {
    height: calc(100vh - 66px - 86px);
}

.container {
    width: 80%;
    margin: 0 auto;
    padding: 1rem;
}

.header {
    padding: 1rem;
    border-bottom: 1px solid var(--primary-200);
    border-radius: 1rem;
    background-color: var(--primary-200);
}

.action-bar {
    justify-content: end;
    width: 100%;
    display: flex;
}

@media (max-width: 768px) {
    .container {
        width: 100%;
    }
}
</style>
