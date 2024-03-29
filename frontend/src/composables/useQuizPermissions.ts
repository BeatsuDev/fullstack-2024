import type { Quiz } from "@/api";
import { useAuthenticationStore } from "@/stores/authentication";
import { computed, type Ref } from "vue";

export default function useQuizPermissions(quiz: Ref<Quiz | null>) {
    const auth = useAuthenticationStore();

    const isOwnerOrCollaborator = computed(() => {
        return auth.loggedInUser?.id === quiz.value?.creator.id;
    });

    return {
        isOwnerOrCollaborator,
    };
}
