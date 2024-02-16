import { ref, type Ref } from "vue";

export type Composable<T> = {
    data: Ref<T | null>;
    loading: Ref<boolean>;
    finished: Ref<boolean>;
    error: Ref<unknown>;
    execute: (...args: any[]) => Promise<T>;
};

export function useComposable<T>(
    callable: (...args: any[]) => Promise<T>,
    immediate: boolean = false,
    ...args: any[]
): Composable<T> {
    const data = ref<T | null>(null) as Ref<T | null>;
    const loading = ref<boolean>(false);
    const finished = ref<boolean>(false);
    const error = ref<unknown>(null);

    const execute = (...overrideArgs: any[]) => {
        const finalArgs = { ...args, ...overrideArgs };
        loading.value = true;

        const promise = callable(...finalArgs);
        promise
            .then((result) => (data.value = result))
            .catch((err) => (error.value = err))
            .finally(() => {
                loading.value = false;
                finished.value = true;
            });

        return promise;
    };

    if (immediate) {
        execute();
    }

    return { data, loading, finished, error, execute };
}
