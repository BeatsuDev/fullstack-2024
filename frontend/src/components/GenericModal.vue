<template>
    <dialog ref="dialog" @abort="close" @close="close">
        <div v-if="!props.unclosable" class="close-button" @click="close"></div>
        <h3>{{ props.title }}</h3>
        <slot></slot>
    </dialog>
</template>

<script lang="ts" setup>
import { onMounted, ref, watch } from "vue";

const props = defineProps<{
    title: string;
    unclosable?: boolean;
}>();

const active = defineModel({
    required: true,
});

const dialog = ref<HTMLDialogElement | null>();

watch(active, (value) => {
    if (value) {
        dialog.value!.showModal();
    } else {
        dialog.value!.close();
    }
});
onMounted(() => {
    if (active.value) {
        dialog.value!.showModal();
    } else {
        dialog.value!.close();
    }
});

window.addEventListener("keydown", (e) => {
    if (e.key === "Escape") {
        active.value = false;
    }
});

function close() {
    active.value = false;
}
</script>

<style scoped>
dialog {
    padding: 1rem;
    border-radius: 5px;
    border: 0;
}

.close-button {
    position: absolute;
    top: 0;
    right: 0;
    padding: 0.5rem;
    background: none;
    border: none;
    cursor: pointer;
}

.close-button::before {
    content: "✕";
}

dialog::backdrop {
    background: rgba(0, 0, 0, 0.4);
}

dialog[open],
dialog::backdrop {
    animation: show 500ms ease;
}

@keyframes show {
    0% {
        opacity: 0;
    }
}
</style>
