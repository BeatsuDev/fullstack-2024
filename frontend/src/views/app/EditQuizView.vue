<template>
    <main class="app-container">
        <AlertComponent v-if="errorMessage" type="danger">
            {{ errorMessage }}
        </AlertComponent>
        <div v-if="!isOwnerOrCollaborator">
            <p>
                You are not allowed to edit this quiz. Only the owner and
                collaborators can edit the quiz.
            </p>
        </div>
        <AlertComponent v-else-if="loadingDebounced" type="info"
            >loading...</AlertComponent
        >
        <div v-else-if="data">
            <QuizHero :quiz="quizReadOnly" />
            <div class="card" style="margin: 1rem auto; width: max-content">
                <h3>Edit quiz</h3>
                <QuizForm :value="data?.data" @submit="updateQuiz" />
            </div>
        </div>
        <div v-if="revisions">
            <h3>Revisions</h3>
            <RevisionInfiniteScroll
                :value="revisions.data"
                @view="viewRevision"
            />
        </div>
        <h3>Collaborators</h3>
        <div v-for="collaborator in collaborators?.data" :key="collaborator.id">
            <UserCard :value="collaborator" editable @delete="reveal" />
        </div>
        <div style="display: flex; justify-content: center">
            <ButtonComponent
                filled
                large
                @click="collaboratorModal = true"
                style="margin-top: 10px"
            >
                Add collaborator
            </ButtonComponent>
        </div>
        <GenericModal v-model="collaboratorModal" title="Add collaborator">
            <form @submit.prevent>
                <ValidatedInput
                    id="email"
                    v-model="collaborator.email"
                    :validator="v$.email"
                    placeholder="Enter the email of the collaborator"
                ></ValidatedInput>
                <ButtonComponent filled @click="addCollaborator">
                    Add collaborator
                </ButtonComponent>
            </form>
        </GenericModal>
        <GenericModal v-model="isRevealed" title="Delete collaborator">
            <p>
                Are you sure you want to remove
                {{ deletingCollaborator?.name }} as collaborator?
            </p>
            <ButtonComponent filled @click="confirm">Yes</ButtonComponent>
            <ButtonComponent @click="cancel">No</ButtonComponent>
        </GenericModal>

        <h3>Export quiz to template</h3>

        <ButtonComponent filled @click="exportQuiz">
            Export quiz to template
        </ButtonComponent>
    </main>
</template>
<script lang="ts" setup>
import { useRoute, useRouter } from "vue-router";
import type { Collaborator, QuizCreate, Revision } from "@/api";
import { CollaboratorApi, QuizApi, RevisionApi } from "@/api";
import { usePromise } from "@/composables/promise";
import useDebounceLoading from "@/composables/useDebounceLoading";
import QuizForm from "@/components/QuizForm.vue";
import QuizHero from "@/components/QuizHero.vue";

import { computed, reactive, ref } from "vue";
import useQuizPermissions from "@/composables/useQuizPermissions";
import GenericModal from "@/components/GenericModal.vue";
import ValidatedInput from "@/components/ValidatedInput.vue";
import { email, required } from "@vuelidate/validators";
import useVuelidate from "@vuelidate/core";
import UserCard from "@/components/UserCard.vue";
import ButtonComponent from "@/components/ButtonComponent.vue";
import RevisionInfiniteScroll from "@/components/RevisionInfiniteScroll.vue";
import { useNotificationStore } from "@/stores/notification";
import { useConfirmDialog } from "@vueuse/core";
import useQuizApi from "@/composables/useQuizApi";
import AlertComponent from "@/components/AlertComponent.vue";

const route = useRoute();

const quizId = computed(() => route.params.id.toString());

const { data, loading, errorMessage, quizReadOnly } = useQuizApi(quizId);

const { isOwnerOrCollaborator } = useQuizPermissions(
    computed(() => data.value?.data)
);

const loadingDebounced = useDebounceLoading(loading);

const router = useRouter();

const quizApi = new QuizApi();

async function updateQuiz(value: QuizCreate) {
    try {
        await quizApi.updateQuiz(quizId.value, value);
        notificationStore.addNotification({
            message: "Quiz updated successfully.",
            type: "success",
        });
        router.push("/quizzes/" + quizId.value);
    } catch (e) {
        notificationStore.addNotification({
            message: "An unexpected error occurred.",
            type: "error",
        });
    }
}

const collaboratorApi = new CollaboratorApi();

const { data: collaborators } = usePromise(
    collaboratorApi.getCollaborators(quizId.value)
);

const collaborator = reactive({
    email: "",
}) as Collaborator;

const formRules = {
    email: { required, email },
};

const collaboratorModal = ref(false);

const v$ = useVuelidate(formRules, collaborator);

const notificationStore = useNotificationStore();

async function addCollaborator() {
    try {
        const data = await collaboratorApi.addCollaborator(
            quizId.value,
            collaborator
        );
        collaborators.value?.data.push(data.data as Collaborator);
        notificationStore.addNotification({
            message: "Collaborator added successfully.",
            type: "success",
        });
    } catch (e) {
        if (e.response.status === 404) {
            notificationStore.addNotification({
                message: "User specified does not exist.",
                type: "error",
            });
        } else {
            notificationStore.addNotification({
                message: "An unexpected error occurred.",
                type: "error",
            });
        }
    } finally {
        collaboratorModal.value = false;
    }
}

const deletingCollaborator = ref<Collaborator | null>(null);

const { confirm, cancel, reveal, isRevealed, onReveal, onConfirm } =
    useConfirmDialog();

onReveal((value: Collaborator) => {
    console.log(value);
    deletingCollaborator.value = value;
});

onConfirm(() => {
    deleteCollaborator(deletingCollaborator.value as Collaborator);
});

function deleteCollaborator(collaborator: Collaborator) {
    try {
        collaboratorApi.removeCollaborator(quizId.value, collaborator.id);
        notificationStore.addNotification({
            message: "Collaborator removed successfully.",
            type: "success",
        });
        if (collaborators.value) {
            collaborators.value.data = collaborators.value.data.filter(
                (c) => c.id !== collaborator.id
            );
        }
    } catch (e) {
        notificationStore.addNotification({
            message: "An unexpected error occurred.",
            type: "error",
        });
    }
}

const revisionApi = new RevisionApi();

const { data: revisions } = usePromise(revisionApi.getRevisions(quizId.value));

function viewRevision(revision: Revision) {
    router.push(`/quizzes/${quizId.value}?revision=${revision.revisionId}`);
}

function exportQuiz() {
    const element = document.createElement("a");
    const file = new Blob([JSON.stringify(data.value?.data)], {
        type: "application/json",
    });
    element.href = URL.createObjectURL(file);
    element.download = "quiz.json";
    document.body.appendChild(element); // Required for this to work in FireFox
    element.click();
    URL.revokeObjectURL(element.href);
    document.body.removeChild(element);
}
</script>
