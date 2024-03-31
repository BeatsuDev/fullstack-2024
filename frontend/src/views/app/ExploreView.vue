<script setup lang="ts">
import { ref, reactive } from "vue";
import FilterIcon from "@/assets/icons/FilterIcon.vue";
import ButtonComponent from "@/components/ButtonComponent.vue";
import FilterOptions from "@/components/FilterOptions.vue";

import type { QuizOverview } from "@/api";
import { QuizApi } from "@/api";
import { useExecutablePromise } from "@/composables/promise";
import router from "@/router";

//
const filterOptions = reactive({
    selectedCategories: [],
    minDifficulty: 1,
    maxDifficulty: 10,
});

// Search
const quizApi = new QuizApi();
const searchQuery = ref("");
const {
    execute: executeSearch,
    error,
    loading: quizFetchLoading,
} = useExecutablePromise(async (...args) => await quizApi.quizGet(...args));

function searchQuizzes() {
    filtersWindowOpen.value = false;
    executeSearch(
        50,
        searchQuery.value,
        filterOptions.minDifficulty,
        filterOptions.maxDifficulty,
        filterOptions.selectedCategories
    ).then((response) => {
        foundQuizzes.value = [...response.data];
    });
}

// Quizzes
const foundQuizzes = ref<QuizOverview[]>([]);

executeSearch(
    50,
    searchQuery.value,
    filterOptions.minDifficulty,
    filterOptions.maxDifficulty,
    filterOptions.selectedCategories
).then((response) => {
    foundQuizzes.value = [...response.data];
});
// Filter window
const filtersWindowOpen = ref(false);

function toggleFiltersWindow() {
    filtersWindowOpen.value = !filtersWindowOpen.value;
}

function onQuizCardClick(quiz: QuizOverview) {
    router.push({ name: "quiz", params: { id: quiz.id } });
}
</script>

<template>
    <div class="explore-view-container">
        <div class="search-container">
            <ButtonComponent
                rounded-lg
                class="toggle-filters"
                @click="toggleFiltersWindow"
                ><FilterIcon
            /></ButtonComponent>
            <input
                type="text"
                v-model="searchQuery"
                placeholder="Search for quizzes..."
                @keydown.enter="searchQuizzes"
            />
            <ButtonComponent @click="searchQuizzes" rounded large filled>
                Search
            </ButtonComponent>

            <!-- This one is not affected by the transition animation and hides all overflow -->
            <div class="outer-filters-container">
                <Transition>
                    <FilterOptions
                        v-if="filtersWindowOpen"
                        v-model="filterOptions"
                    />
                </Transition>
            </div>
        </div>
        <main class="found-quizzes-container">
            <div v-if="quizFetchLoading">Loading...</div>
            <div v-else-if="error">{{ error }}</div>
            <div v-else-if="foundQuizzes.length === 0">No quizzes found.</div>
            <div class="found-quizzes-grid" v-else>
                <div
                    v-for="(quiz, i) in foundQuizzes"
                    :key="i"
                    class="quiz-overview-card"
                    @click="onQuizCardClick(quiz)"
                >
                    <div
                        class="quiz-card-banner"
                        :style="`background-color: hsl(${Math.random() * 360}deg ${60 + Math.random() * 40}% 50%);`"
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
                        <h4 style="margin-top: 0">{{ quiz.title }}</h4>
                        <p>{{ quiz.description }}</p>
                    </div>
                </div>
            </div>
        </main>
    </div>
</template>

<style scoped>
.explore-view-container {
    display: flex;
    flex-direction: column;
    width: 100%;
    height: 100%;
}

/* Fucking hacky as fuck. I wish I didn't have to do this. Change if can! */
/* 100vh - (nav height + nav y-padding) - (search container height + search container y-padding) - (bottom nav height + bottom nav y-padding) - (main y-padding) */
main {
    height: calc(100vh - 66px - 57.6562px - 86px - 32px);
}

.search-container {
    position: relative;
    display: flex;
    justify-content: space-around;

    background-color: var(--primary-200);
    padding: 0.5em;
}

.search-container input {
    flex-grow: 1;
    margin: 0 1em;
    background-color: var(--primary-100);
    border: none;
}

.outer-filters-container {
    overflow: hidden;

    position: absolute;
    top: 100%;
    left: 0;
    width: 100%;
}

.v-enter-active,
.v-leave-active {
    transition: transform 150ms cubic-bezier(0.85, 0, 0.15, 1);
}

.v-enter-from,
.v-leave-to {
    transform: translateY(-100%);
}

.found-quizzes-container {
    padding: 1em;
    max-height: 100%;
}

.found-quizzes-grid {
    display: grid;
    grid-template-columns: repeat(auto-fill, minmax(300px, 1fr));
    gap: 1em;
}

.quiz-overview-card {
    border: 1px solid black;
    border-radius: 1em;
    transition-duration: 150ms;
}

.quiz-overview-card:hover {
    box-shadow: 2px 2px 4px rgb(0 0 0 / 50%);
    transform: translate(-2px, -2px);
    cursor: pointer;
}

.quiz-overview-card .quiz-card-banner {
    border-top-left-radius: 1em;
    border-top-right-radius: 1em;
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
