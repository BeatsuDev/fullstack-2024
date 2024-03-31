<script setup lang="ts">
import { computed, watch } from "vue";

import type { Question } from "@/api";
import { getQuestionType, QuestionTypes } from "@/composables/useQuestionType";
import MultipleChoiceOptions from "./MultipleChoiceOptions.vue";
import TextAnswer from "./TextAnswer.vue";
import BooleanAnswer from "./BooleanAnswer.vue";

const props = defineProps<{
    question: Question;
}>();

const emit = defineEmits<{
    answerSelected: [option: string];
}>();

const questionType = computed(() => getQuestionType(props.question));

watch(questionType, (newVal) => {
    console.log("Question type changed to", newVal);
});
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
                    v-if="questionType === QuestionTypes.MULTIPLE"
                    :question="question"
                    @answerSelected="(option) => emit('answerSelected', option)"
                />
                <TextAnswer
                    v-else-if="questionType == QuestionTypes.TEXT"
                    @answerSelected="(option) => emit('answerSelected', option)"
                />
                <BooleanAnswer
                    v-else-if="questionType == QuestionTypes.BOOLEAN"
                    @answerSelected="(option) => emit('answerSelected', option)"
                />
                <div v-else>
                    <p>Question type not supported</p>
                </div>
            </div>
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
