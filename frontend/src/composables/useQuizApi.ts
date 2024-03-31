import type { Ref } from "vue";
import {  QuizApi, RevisionApi } from "@/api";
import type { Quiz} from "@/api/models/quiz";
import { useExecutablePromise } from "@/composables/promise";
import { useRouter } from "vue-router";
import { computed, ref, watch, watchEffect } from "vue";
import { useNotificationStore } from "@/stores/notification";

export default function useQuizApi(quizId: Ref<string>, revisionId: Ref<string | null> = ref(null)) {

    const router = useRouter();

    const quizApi = new QuizApi();
    const revisionApi = new RevisionApi();

    const { data, loading, error, execute } = useExecutablePromise(fetchQuiz);

    watchEffect(() => console.log(data?.value?.data?.questions))

    watch(
        [quizId, revisionId],
        () => {
            execute();
        },
        { immediate: true }
    );

    const errorMessage = computed(() => {
        console.log(error.value);
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

    async function fetchQuiz() {
        if (revisionId.value) {
            return await revisionApi.getRevision(quizId.value, revisionId.value);
        } else {
            return await quizApi.quizIdGet(quizId.value);
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