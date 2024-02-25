import { ref, type Ref } from "vue";

export type PromiseComposable<T> = {
    data: Ref<T | null>;
    loading: Ref<boolean>;
    finished: Ref<boolean>;
    error: Ref<unknown>;
    promise: Promise<T>;
};

export function usePromise<T>(promise: Promise<T>): PromiseComposable<T> {
    const data = ref<T | null>(null) as Ref<T | null>;
    const loading = ref<boolean>(true);
    const finished = ref<boolean>(false);
    const error = ref<unknown>(null);

    promise
        .then((result) => (data.value = result))
        .catch((err) => (error.value = err))
        .finally(() => {
            loading.value = false;
            finished.value = true;
        });

    return { data, loading, finished, error, promise };
}

export type ExecutablePromiseComposable<Params extends any[], T> = {
    data: Ref<T | null>;
    loading: Ref<boolean>;
    finished: Ref<boolean>;
    error: Ref<unknown>;
    execute: (...args: Params) => Promise<T>;
};

export function useExecutablePromise<Params extends any[], ReturnType>(
    callable: (...args: Params) => Promise<ReturnType>
): ExecutablePromiseComposable<Params, ReturnType> {
    const data = ref<ReturnType | null>(null) as Ref<ReturnType | null>;
    const loading = ref<boolean>(false);
    const finished = ref<boolean>(false);
    const error = ref<unknown>(null);

    const execute = (...args: Params) => {
        loading.value = true;

        const promise = callable(...args);
        promise
            .then((result) => (data.value = result))
            .catch((err) => (error.value = err))
            .finally(() => {
                loading.value = false;
                finished.value = true;
            });

        return promise;
    };

    return { data, loading, finished, error, execute };
}
