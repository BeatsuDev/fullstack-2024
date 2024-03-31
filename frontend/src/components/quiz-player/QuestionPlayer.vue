<script setup lang="ts">
import { ref } from "vue";

import type { Question } from "@/api";
import { getQuestionType, QuestionTypes } from "@/composables/useQuestionType";
import MultipleChoiceOptions from "./MultipleChoiceOptions.vue";
import ButtonComponent from "../ButtonComponent.vue";

const props = defineProps<{
    question: Question;
}>();

const emit = defineEmits<{
    answerSelected: [option: string];
}>();

const questionType = getQuestionType(props.question);

// Free text input
const freeTextInput = ref("");
</script>

<template>
    <div class="question-player-container">
        <div class="question-container">
            <h2>Question: {{ question.question }}</h2>
            <div class="media-container">
                <img
                    v-if="question.mediaUrl"
                    :src="question.mediaUrl"
                    alt="Question media"
                />
                <div class="placeholder-media"></div>
            </div>
            <div class="answers-container">
                <MultipleChoiceOptions
                    v-if="questionType === 'multiple'"
                    :question="question"
                    @answerSelected="(option) => emit('answerSelected', option)"
                />
                <div v-else>
                    <p>Question type not supported</p>
                </div>
            </div>
        </div>
        <div v-if="questionType == QuestionTypes.TEXT" class="action-bar">
            <input ref="freeTextInput" type="text" />
            <ButtonComponent
                @click="emit('answerSelected', freeTextInput)"
                filled
            >
                Submit Answer
            </ButtonComponent>
        </div>
    </div>
</template>

<style scoped>
.question-player-container {
    width: 100%;
    flex: 1;
}

.question-container {
    height: 100%;
}

.media-container {
    margin: auto;
    width: 100%;
    height: 60%;
}

.media-container > * {
    width: 100%;
    height: 100%;
}

.media-container > img {
    object-fit: contain;
}

.media-container > .placeholder-media {
    background-color: red;
}
</style>
