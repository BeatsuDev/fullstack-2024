<template>
    <div class="card">
        <div class="header">
            <h3 class="question">{{ props.value.question }} </h3> ({{ readableQuestionType }})

        </div>
        <div v-if="questionType == QuestionTypes.MULTIPLE">
            <div class="option-header">Options</div>
            <ul class="options" >
                <li v-for="option in props.value.options" :key="option" class="option">
                    {{ option }}
                </li>
            </ul>
        </div>
        <div class="option-header">
            Correct Answer
        </div>
        <div class="answer">
            {{ props.value.answer }}
            </div>
        <div class="action-bar">
            <ButtonComponent v-if="props.editable" @click="emit('delete', value)" class="delete-button">Delete</ButtonComponent>
            <ButtonComponent v-if="props.editable" @click="emit('edit', value)" class="edit-button" filled>Edit</ButtonComponent>
        </div>
    </div>
</template>

<script lang="ts" setup>
import type { Question } from "@/api";
import { defineProps, defineEmits, computed } from "vue";
import ButtonComponent from "./ButtonComponent.vue";
import useQuestionType, { QuestionTypes } from "@/composables/useQuestionType";

const props = defineProps<{
    value: Question;
    editable?: boolean;
}>();

const emit = defineEmits<{
    (event: "edit", value: Question): void;
    (event: "delete", value: Question): void;
}>();

const { questionType, readableQuestionType} = useQuestionType(computed(() => props.value));
</script>

<style scoped>
.card {
    border: 2px solid var(--color-border);
    padding: 1.5em;
    margin: 1em 0;
    background-color: var(--color-background-mute);
}

.option-header {
    font-weight: bold;
    margin-bottom: 0.5em;

}

.question {
    margin-bottom: 1em;
    margin-top: 0;
}

.options {
    list-style-type: none;
    padding: 0;
}

.answer {
    list-style-type: none;
    padding: 1rem;
    background-color: var(--success-100);
}

.option {
    margin-bottom: 0.5em;
    padding: 1rem;
    background-color: var(--primary-100);
}

.action-bar {
    display: flex;
    justify-content: flex-end;
    margin-top: 1em;
}

.delete-button,
.edit-button {
    margin-left: 0.5em;
}

.header {
    display: flex;
    justify-content: space-between;
}
</style>
