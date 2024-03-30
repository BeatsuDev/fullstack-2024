<template>
    <div class="app-container">
        <div v-if="errorMessage">
            <p>{{ errorMessage }}</p>
        </div>
        <div v-if="!isOwnerOrCollaborator">
            <p>
                You are not allowed to edit this quiz. Only the owner and
                collaborators can edit the quiz.
            </p>
        </div>
        <div v-else-if="loadingDebounced">loading...</div>
        <div v-else-if="data">
            <QuizHero :quiz="data.data" />
            <h3>Edit quiz</h3>
            <QuizForm :value="data.data" @submit="updateQuiz" />
        </div>
        <div>
            <h3>Collaborators</h3>
            <div
                v-for="collaborator in collaborators?.data"
                :key="collaborator.id"
            >
                <UserCard :value="collaborator" editable @delete="reveal" />
            </div>
            <ButtonComponent @click="collaboratorModal = true">
                Add collaborator</ButtonComponent
            >
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
    </div>
</template>
<script lang="ts" setup>
import { useRoute, useRouter } from "vue-router";
import type { Collaborator } from "@/api";
import { CollaboratorApi, QuizApi } from "@/api";
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
import { useNotificationStore } from "@/stores/notification";
import { useConfirmDialog } from "@vueuse/core";

const route = useRoute();

const quizId = route.params.id.toString();

const quizApi = new QuizApi();

const { data, loading, error } = usePromise(quizApi.quizIdGet(quizId));

const { isOwnerOrCollaborator } = useQuizPermissions(
    computed(() => data.value?.data)
);

const loadingDebounced = useDebounceLoading(loading);

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
    return "";
});

const router = useRouter();

function updateQuiz(value: Quiz) {
    try {
        // quizApi.updateQuiz(quizId, value);
        notificationStore.addNotification({
            message: "Quiz updated successfully.",
            type: "success",
        });
        router.push("/quizzes/" + quizId);
    } catch (e) {
        notificationStore.addNotification({
            message: "An unexpected error occurred.",
            type: "error",
        });
    }
    throw "Hello, please fix swagger codegen.";
}

const collaboratorApi = new CollaboratorApi();

const { data: collaborators } = usePromise(
    collaboratorApi.getCollaborators(quizId)
);

const collaborator = reactive({
    email: "",
}) as Collaborator;

const formRules = {
    email: { required, email },
};

const collaboratorModal = ref(false);

const form = ref<HTMLFormElement | null>(null);

const v$ = useVuelidate(formRules, collaborator);

const notificationStore = useNotificationStore();

async function addCollaborator() {
    try {
        const data = await collaboratorApi.addCollaborator(
            quizId,
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
        collaboratorApi.removeCollaborator(quizId, collaborator.id);
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
</script>
