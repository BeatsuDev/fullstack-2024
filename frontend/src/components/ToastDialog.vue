<script setup lang="ts">
import type { ToastOptions } from "@/notifications/toast";
import { onMounted } from "vue";
import { ref } from "vue";

const props = defineProps<ToastOptions>();

const visible = ref(false);
const hideTimeout = ref<ReturnType<typeof setTimeout> | null>(null);

function close() {
    visible.value = false;
    if (hideTimeout.value) {
        clearTimeout(hideTimeout.value);
    }
}

onMounted(() => {
    visible.value = true;
    if (props.duration) {
        hideTimeout.value = setTimeout(close, props.duration);
    }
});
</script>

<template>
    <Transition>
        <div
            v-if="visible"
            id="toast-dialog"
            :style="{ borderColor: `var(--${type}-500)` }"
        >
            <div id="toast-dialog-content" :class="type">
                <h2>{{ title }}</h2>
                <p>{{ message }}</p>
                <button id="close-toast" @click="visible = false">
                    [close]
                </button>
            </div>
        </div>
    </Transition>
</template>

<style scoped>
.v-enter-active,
.v-leave-active {
    transition:
        opacity 150ms ease-in,
        height 150ms ease-in 150ms;
}
.v-enter-from {
    opacity: 0;
    transform: translateY(2em);
}

.v-leave-to {
    height: 0;
}

.v-enter-to,
.v-leave-from {
    transform: translateY(0);
}

#toast-dialog {
    position: relative;
    top: 1em;
    width: 100%;
    height: 100%;
    display: flex;
    justify-content: center;
    align-items: center;
    border-radius: 0.5em;
    border: 2px solid;
}

#close-toast {
    position: absolute;
    top: 0.5em;
    right: 0.5em;
    background-color: var(--gray-100);
    border: none;
    border-radius: 0.5em;
    padding: 0.25em 0.5em;
    cursor: pointer;
    text-decoration: underline;
}
</style>
@/notifications/toast
