<template>
    <dialog ref="dialog" @close="close" @abort="close">
        <h3>{{props.title}}</h3>
        <slot></slot>
    </dialog>
</template>
<script lang="ts" setup>
import { ref, watch } from "vue";

const props = defineProps<{
    title: string;
}>();

const active = defineModel({
    required: true
});

const dialog = ref<HTMLDialogElement | null>();

watch(active, (value) => {
    if (!dialog.value) {
        return;
    }
    if (value) {
        dialog.value?.showModal();
    } else {
        dialog.value?.close();
    }
});

function close() {
    active.value = false;
}

</script>
<style scoped>
dialog {
    padding: 50px;
    border-radius: 5px;
    border: 0;
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
