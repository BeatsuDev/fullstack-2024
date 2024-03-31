import type { Ref } from "vue";
import { computed, watch } from "vue";
import { AttemptApi } from "@/api";
import { useExecutablePromise } from "@/composables/promise";

export default function useQuizAttempt(revisionId: Ref<string>) {
    const attemptApi = new AttemptApi();
    const { data, loading, execute } = useExecutablePromise(() => attemptApi.getAttempts(revisionId.value));

    watch(revisionId, () => {
        execute();

    }, {
        immediate: true,
    });

    const attempts = computed(() => data.value?.data?.length || 0);

    return {
        data,
        loading,
        attempts,
    };

}