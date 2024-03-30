<template>
    <div class="header">
        <h1 style="margin-top: 0">{{ props.quiz.title }}</h1>
        <div class="action-bar">
            <ButtonComponent filled large v-if="props.playable">Play</ButtonComponent>
        </div>
        <h3>Description</h3>
        <p>{{ props.quiz.description }}</p>
        <ButtonComponent v-if="props.editable" @click="emit('edit', props.quiz)">Edit</ButtonComponent>
    </div>
</template>
<script lang="ts" setup>
import ButtonComponent from "./ButtonComponent.vue";

import type { Quiz } from "@/api";

const props = defineProps<{
    quiz: Quiz;
    editable?: boolean;
    playable?: boolean;
}>();

const emit = defineEmits<{
    (event: "edit", quiz: Quiz): void;
}>();
</script>
<style scoped>

.header {
    padding: 1rem;
    border-bottom: 1px solid var(--primary-200);
    border-radius: 1rem;
    background-color: var(--primary-200);
}

.action-bar {
    justify-content: end;
    width: 100%;
    display: flex;
}
</style>
