<template>
    <div class="quiz-hero">
        <div class="header">
            <h1>{{ props.quiz.title }}</h1>
            <div class="action-bar">
                <ButtonComponent
                    filled
                    large
                    v-if="props.playable"
                    @click="
                        $router.push({
                            name: 'quiz-player',
                            params: { id: quiz.id },
                        })
                    "
                    >Play</ButtonComponent
                >
            </div>
        </div>
        <div class="description">
            <h3>Description</h3>
            <p>{{ props.quiz.description }}</p>
            <p style="font-style: italic">
                Made by: {{ props.quiz.creator.name }}
            </p>
            <p
                style="font-style: italic"
                v-if="props.quiz?.categories?.length > 0"
            >
                Categories:
                {{ props.quiz.categories.map((c) => c.name).join(", ") }}
            </p>
        </div>
        <div
            class="edit-button"
            v-if="props.editable"
            @click="emit('edit', props.quiz)"
        >
            <ButtonComponent>Edit</ButtonComponent>
        </div>
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
.quiz-hero {
    position: relative;
    padding: 1rem;
    border: 2px solid var(--color-border);
    background-color: var(--primary-200);
}

.header {
    display: flex;
    align-items: center;
    justify-content: space-between;
    margin-bottom: 1rem;
}

.header h1 {
    margin: 0;
    font-size: 2rem;
}

.description {
    border-top: 1px solid var(--primary-200);
    padding-top: 1rem;
}

.description h3 {
    margin-top: 0;
    font-size: 1.5rem;
}

.description p {
    margin-top: 0.5rem;
    line-height: 1.5;
}

.edit-button {
    position: absolute;
    bottom: 1rem;
    right: 1rem;
}
</style>
