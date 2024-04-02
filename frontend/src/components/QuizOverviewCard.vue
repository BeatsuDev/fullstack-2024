<script setup lang="ts">
import type { Category, QuizOverview } from "@/api";
import router from "@/router";
import { computed } from "vue";

const props = defineProps<{
    quiz: QuizOverview;
}>();

function onQuizCardClick(quiz: QuizOverview) {
    router.push({ name: "quiz", params: { id: quiz.id } });
}

function getCategoryStyle(category: Category) {
    return {
        border: `2px solid ${category.color}`,
    };
}

const cardStyles = computed(() => {
    if (!props.quiz) return {};
    if (!props.quiz.categories || props.quiz.categories.length === 0) {
        return { backgroundColor: stringToColour(props.quiz.title) };
    }
    return {
        backgroundColor: props.quiz.categories[0].color,
    };
});

//take from https://stackoverflow.com/questions/3426404/create-a-hexadecimal-colour-based-on-a-string-with-javascript
const stringToColour = (str: string) => {
    let hash = 0;
    str.split("").forEach(char => {
        hash = char.charCodeAt(0) + ((hash << 5) - hash);
    });
    let colour = "#";
    for (let i = 0; i < 3; i++) {
        const value = (hash >> (i * 8)) & 0xff;
        colour += value.toString(16).padStart(2, "0");
    }
    return colour;
};

</script>

<template>
    <a class="quiz-overview-card" @click="onQuizCardClick(quiz)">
        <div
            class="quiz-card-banner"
            :style="cardStyles"
        ></div>
        <div class="quiz-card-content">
            <div class="quiz-card-categories">
                <div
                    v-for="(category, i) in quiz.categories"
                    :key="i"
                    :style="getCategoryStyle(category)"
                >
                    {{ category.name }}
                </div>
            </div>
            <h4 style="margin-top: 1em">{{ quiz.title }}</h4>
            <p style="margin-top: 0; font-size: 0.8em">
                Created by {{ quiz.creator.name }}
            </p>
            <p>{{ quiz.description }}</p>
        </div>
    </a>
</template>

<style scoped>
.quiz-overview-card {
    border: 2px solid var(--color-border);
    transition-duration: 150ms;
    background-color: var(--color-background);
}

.quiz-overview-card:hover {
    box-shadow: 2px 2px 4px rgb(0 0 0 / 50%);
    transform: translate(-2px, -2px);
    cursor: pointer;
}

.quiz-overview-card .quiz-card-banner {
    height: 5em;
}

.quiz-overview-card .quiz-card-content {
    padding: 1em;
}

.quiz-overview-card .quiz-card-categories {
    display: flex;
    flex-wrap: wrap;
    gap: 0.5em;
    margin-bottom: 0.5em;
}

.quiz-overview-card .quiz-card-categories div {
    padding: 0.25em 0.5em;
    border-radius: 1em;
    font-size: 0.8em;
    text-transform: uppercase;
}
</style>
