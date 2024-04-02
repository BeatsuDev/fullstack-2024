<template>
    <main>
        <div class="app-container">
            <AlertComponent v-if="errorMessage" type="danger">
                {{ errorMessage }}
            </AlertComponent>
            <div v-else-if="loadingDebounced">
                <AlertComponent type="warning"> Loading...</AlertComponent>
            </div>
            <div v-else-if="quizReadOnly">
                <AlertComponent v-if="revisionId" type="warning">
                    <div
                        style="
                            display: flex;
                            justify-content: space-between;
                            align-items: center;
                        "
                    >
                        You are viewing a revision of this quiz. You can't edit
                        it.
                        <div>
                            <ButtonComponent @click="cancelRevert">
                                Go back to the latest version
                            </ButtonComponent>
                            <ButtonComponent
                                style="margin-left: 1rem"
                                @click="revert"
                            >
                                Revert to this version
                            </ButtonComponent>
                        </div>
                    </div>
                </AlertComponent>

                <QuizHero
                    :editable="isOwnerOrCollaborator && !revisionId"
                    :quiz="quizReadOnly"
                    playable
                    @edit="editQuiz"
                />
                <AlertComponent v-if="attempts > 0" type="info">
                    <div
                        style="
                            display: flex;
                            justify-content: space-between;
                            align-items: center;
                        "
                    >
                        You have tried this quiz before.
                        <ButtonComponent
                            filled
                            @click="
                                router.push('/quizzes/' + quizId + '/results')
                            "
                        >
                            View previous results
                        </ButtonComponent>
                    </div>
                </AlertComponent>
                <div v-if="isOwnerOrCollaborator">
                    <h3>Questions</h3>
                    <QuestionCard
                        v-for="question in quizReadOnly.questions"
                        :key="question.id"
                        :editable="isOwnerOrCollaborator && !revisionId"
                        :value="question"
                        @delete="reveal"
                        @edit="editQuestion"
                    />
                    <div class="centered">
                        <ButtonComponent
                            filled
                            large
                            @click="questionModal = true"
                            >Add question
                        </ButtonComponent>
                    </div>
                </div>
            </div>
            <AlertComponent v-if="feedbackIsLoading" type="warning"
                >Loading feedbacks...
            </AlertComponent>
            <div v-else-if="feedbacks">
                <h3>Feedbacks</h3>
                <FeedbackCard
                    v-for="feedback in feedbacks"
                    :key="feedback.id"
                    :feedback="feedback"
                ></FeedbackCard>
                <div class="card">
                    <FeedbackForm v-if="!revisionId" @submit="submitFeedback" />
                </div>
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
</template>
<script lang="ts" setup>
import {
    type Feedback,
    FeedbackApi,
    type FeedbackCreate,
    type Question,
    QuestionApi,
    type QuestionCreate,
} from "@/api";
import { usePromise } from "@/composables/promise";
import ButtonComponent from "@/components/ButtonComponent.vue";
import useQuizPermissions from "@/composables/useQuizPermissions";
import { computed, ref } from "vue";
import { useRoute, useRouter } from "vue-router";
import QuestionCard from "@/components/QuestionCard.vue";
import GenericModal from "@/components/GenericModal.vue";
import QuestionForm from "@/components/QuestionForm.vue";
import FeedbackCard from "@/components/FeedbackCard.vue";
import AlertComponent from "@/components/AlertComponent.vue";
import FeedbackForm from "@/components/FeedbackForm.vue";
import QuizHero from "@/components/QuizHero.vue";
import { useNotificationStore } from "@/stores/notification";
import { useConfirmDialog } from "@vueuse/core";
import useDebounceLoading from "@/composables/useDebounceLoading";
import useQuizApi from "@/composables/useQuizApi";
import useQuizAttempt from "@/composables/useQuizAttempt";

const route = useRoute();

/**
 * Quiz
 */

const quizId = computed(() => route.params.id.toString());

const revisionId = computed(() => route.query.revision?.toString());

const router = useRouter();

const { data, loading, revert, quizReadOnly, errorMessage } = useQuizApi(
    quizId,
    revisionId
);

const loadingDebounced = useDebounceLoading(loading);

const { isOwnerOrCollaborator } = useQuizPermissions(quizReadOnly);

function cancelRevert() {
    router.push("/quizzes/" + quizId.value);
}

function editQuiz() {
    router.push("/quizzes/" + quizId.value + "/edit");
}

/**
 * Results
 */

const { attempts } = useQuizAttempt(quizId);

/**
 * Questions
 */

const questionModal = ref(false);

function blankQuestion() {
    return {
        quizId: quizId.value,
        question: "",
        options: [""],
        answer: "",
    } as QuestionCreate;
}

const questionApi = new QuestionApi();

async function submitQuestion(value: QuestionCreate | Question) {
    questionModal.value = false;
    if ("id" in value) {
        await updateQuestion(value);
    } else {
        await createQuestion(value);
    }
    question.value = blankQuestion();
}

async function createQuestion(value: QuestionCreate) {
    try {
        const question = await questionApi.createQuestion(value);
        // @ts-ignore
        data.value.data.questions.push(question.data);
        notificationStore.addNotification({
            message: "Question created successfully.",
            type: "success",
        });
    } catch (e) {
        notificationStore.addNotification({
            message: "An unexpected error occurred. Please try again later.",
            type: "error",
        });
    }
}

async function updateQuestion(value: Question) {
    try {
        await questionApi.updateQuestion(
            { ...value, quizId: quizId.value } as QuestionCreate,
            value.id
        );
        const index = data.value.data.questions.findIndex(
            (q) => q.id == value.id
        );
        if (index != -1) {
            data.value.data.questions[index] = value as Question;
        }
        notificationStore.addNotification({
            message: "Question updated successfully.",
            type: "success",
        });
    } catch (e) {
        notificationStore.addNotification({
            message: "An unexpected error occurred. Please try again later.",
            type: "error",
        });
    }
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
        data.value.data.questions = data.value.data.questions.filter(
            (q) => q.id != question.id
        );
        notificationStore.addNotification({
            message: "Question deleted successfully.",
            type: "success",
        });
    } catch (e) {
        notificationStore.addNotification({
            message: "An unexpected error occurred. Please try again later.",
            type: "error",
        });
    }
}

/*
 * Feedback
 */

const feedbackApi = new FeedbackApi();

const { data: feedbackData, loading: feedbacksLoading } = usePromise(
    feedbackApi.getFeedback(quizId.value)
);

const feedbackIsLoading = useDebounceLoading(feedbacksLoading);

const feedbacks = computed(() => feedbackData.value?.data as Feedback[]);

async function submitFeedback(value: FeedbackCreate) {
    try {
        const newFeedback = await feedbackApi.giveFeedback(quizId.value, value);
        feedbackData.value?.data.push(newFeedback.data);
    } catch (e) {
        notificationStore.addNotification({
            message: "An unexpected error occurred. Please try again later.",
            type: "error",
        });
    }
}
</script>
<style scoped>
.centered {
    display: flex;
    justify-content: center;
}
</style>
