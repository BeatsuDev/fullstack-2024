<script setup lang="ts">
import ButtonComponent from "./ButtonComponent.vue";

const isVisible = defineModel<boolean>();

defineProps<{
    title?: string;
    message?: string;
    confirmText: string;
    cancelText: string;
}>();

const emit = defineEmits<{
    confirm: [];
    cancel: [];
}>();

function onConfirm() {
    isVisible.value = false;
    emit("confirm");
}

function onCancel() {
    isVisible.value = false;
    emit("cancel");
}
</script>

<template>
    <Transition>
        <div v-if="isVisible" id="dialog-overlay">
            <div id="dialog">
                <h3>{{ title }}</h3>
                <p>{{ message }}</p>
                <div id="buttons">
                    <ButtonComponent
                        id="confirm-button"
                        @click="onConfirm"
                        rounded
                        large
                        filled
                        >{{ confirmText }}</ButtonComponent
                    >
                    <ButtonComponent
                        id="cancel-button"
                        @click="onCancel"
                        rounded
                        large
                        >{{ cancelText }}</ButtonComponent
                    >
                </div>
            </div>
        </div>
    </Transition>
</template>

<style scoped>
#dialog-overlay {
    position: absolute;
    width: 100%;
    height: 100%;
    background-color: rgba(0, 0, 0, 0.5);
    display: flex;
    justify-content: center;
    align-items: center;
    z-index: 50;
}

#dialog {
    background-color: var(--primary-50);
    padding: 1.2em;
    border-radius: 5px;
    border: 2px solid var(--primary-200);
    box-shadow: 0 0 10px var(--primary-200);
    text-align: center;
    min-width: 300px;
    max-width: 400px;
    width: 50%;
}

#dialog h3 {
    margin: 0;
}

#buttons {
    display: flex;
    justify-content: center;
    gap: 1em;
    margin-top: 2em;
}

#buttons > button {
    padding: 0.5em 1em;
}

#confirm-button {
    background-color: var(--success-600);
}

#cancel-button {
    background-color: var(--error-300);
}

#cancel-button:hover {
    background-color: var(--error-600);
}

.v-enter-active,
.v-leave-active {
    transition: all 120ms ease;
}

.v-enter-from,
.v-leave-to {
    opacity: 0;
}
</style>
