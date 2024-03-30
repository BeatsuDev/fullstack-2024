<script setup lang="ts">
import { ref } from "vue";

import type { Question } from "@/api";
import { getQuestionType, QuestionTypes } from "@/composables/useQuestionType";
import MultipleChoiceQuestionPlayer from "./MultipleChoiceQuestionPlayer.vue";
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
            <h2>{{ question.question }}</h2>
            <div class="answers-container">
                <MultipleChoiceQuestionPlayer
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
