import type { Ref } from "vue";
import { ref, watch } from "vue";

export const delayTime = 300;

export default function useDebounceLoading(
    value: Ref<boolean>,
    delay = delayTime
) {
    const isLoadingDebounced = ref(value.value);

    let id = null as number | null;

    watch(value, setDebouncedRef);

    function setDebouncedRef() {
        if (id != null) {
            clearTimeout(id);
            id = null;
        }
        id = setTimeout(() => {
            isLoadingDebounced.value = value.value;
        }, delay);
    }

    return isLoadingDebounced;
}
