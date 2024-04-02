<script lang="ts" setup>
import type { QuizOverview } from "@/api";
import QuizOverviewCard from "@/components/QuizOverviewCard.vue";

defineProps<{
    quizzes: QuizOverview[];
}>();
</script>

<template>
    <div class="found-quizzes-grid">
        <QuizOverviewCard v-for="(quiz, i) in quizzes" :key="i" :quiz="quiz" />
    </div>
</template>

<style scoped>
.found-quizzes-grid {
    display: grid;
    grid-template-columns: repeat(auto-fill, minmax(300px, 1fr));
    gap: 1em;
}
</style>
