import type { Ref } from "vue";
import { QuizApi, RevisionApi } from "@/api";
import type { Quiz } from "@/api/models/quiz";
import { useExecutablePromise } from "@/composables/promise";
import { useRouter } from "vue-router";
import { computed, ref, watch } from "vue";
import { useNotificationStore } from "@/stores/notification";

export default function useQuizApi(
    quizId: Ref<string>,
    revisionId: Ref<string | null> = ref(null)
) {
    const router = useRouter();

    const quizApi = new QuizApi();
    const revisionApi = new RevisionApi();

    const { data, loading, error, execute } = useExecutablePromise(fetchQuiz);

    watch(
        [quizId, revisionId],
        () => {
            execute();
        },
        { immediate: true }
    );

    const errorMessage = computed(() => {
        if (!error.value) {
            return "";
        }
        console.log("Handled error:", error.value);
        if (error.value) {
            if (
                error.value.response.status == "404" ||
                error.value.response.status == "422"
            ) {
                return "Quiz not found.";
            }
            return "An unexpected error occurred. Please try again later.";
        }
        return "";
    });

    async function fetchQuiz() {
        if (revisionId.value) {
            return await revisionApi.getRevision(
                quizId.value,
                revisionId.value
            );
        } else {
            return await quizApi.getQuiz(quizId.value);
        }
    }

    const notificationStore = useNotificationStore();

    async function revert() {
        if (revisionId.value) {
            await revisionApi.revertToRevision(quizId.value, revisionId.value);
            router.push("/quizzes/" + quizId.value);
            notificationStore.addNotification({
                message: "Reverted to revision successfully.",
                type: "success",
            });
        }
    }

    const quizReadOnly = computed(() => data.value?.data as Quiz);
    return {
        data,
        quizReadOnly,
        loading,
        errorMessage,
        revert,
    };
}
