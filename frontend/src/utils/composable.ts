import { ref, type Ref } from "vue";

export type Composable<T> = {
    data: Ref<T | null>;
    loading: Ref<boolean>;
    error: Ref<unknown>;
};

export function useComposable<T>(promise: Promise<T>): Composable<T> {
    const data = ref<T | null>(null) as Ref<T | null>;
    const loading = ref<boolean>(true);
    const error = ref<unknown>(null);

    promise
        .then((result) => (data.value = result))
        .catch((err) => (error.value = err))
        .finally(() => (loading.value = false));

    return { data, loading, error };
}
