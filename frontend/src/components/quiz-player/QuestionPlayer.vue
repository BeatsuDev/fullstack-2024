<script setup lang="ts">
import { computed, watch } from "vue";

import type { Question } from "@/api";
import { getQuestionType, QuestionTypes } from "@/composables/useQuestionType";
import MultipleChoiceOptions from "./MultipleChoiceOptions.vue";
import TextAnswer from "./TextAnswer.vue";
import BooleanAnswer from "./BooleanAnswer.vue";
import { BASE_PATH } from "@/api/base";
import { useWindowSize } from "@vueuse/core";

const props = defineProps<{
    question: Question;
    countdown: number | undefined;
}>();

const emit = defineEmits<{
    answerSelected: [option: string];
}>();

const questionType = computed(() => getQuestionType(props.question));

watch(questionType, (newVal) => {
    console.log("Question type changed to", newVal);
});

const {width} = useWindowSize();
</script>

<template>
    <div class="question-player-container">
        <div class="question-container">
            <h2
                v-if="countdown !== undefined"
                :class="{
                    countdown: true,
                    countdownUrgent: countdown <= 10,
                    countdownVital: countdown <= 5,
                }"
            >
                {{ countdown.toString().padStart(2, "0") }}
            </h2>
            <h2>Question: {{ question.question }}</h2>
            <div class="media-container" v-if="width > 600">
                <img
                    v-if="question.image"
                    :src="BASE_PATH + '/' + question.image.path"
                    alt="Question media"
                />
                <div class="placeholder-media" v-else></div>
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
    height: 70svh;
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

.countdown {
    position: absolute;
    top: 4rem;
    right: 3.5rem;
    font-size: 5rem;
    border-radius: 10%;
    padding: 1rem 1.5rem;
    background-color: var(--primary-700);
    color: var(--white-300);

    text-align: right;
    text-shadow: 2px 2px 4px rgba(0, 0, 0, 0.5);
}

.countdownUrgent {
    color: var(--warning-200);
}

.countdownVital {
    background-color: var(--error-500) !important;
}
</style>
