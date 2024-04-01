<script setup lang="ts">
import { ref, reactive } from "vue";
import FilterIcon from "@/assets/icons/FilterIcon.vue";
import ButtonComponent from "@/components/ButtonComponent.vue";
import FilterOptions from "@/components/FilterOptions.vue";
import QuizGrid from "@/components/QuizGrid.vue";

import type { QuizOverview, Category } from "@/api";
import { QuizApi } from "@/api";
import { useExecutablePromise } from "@/composables/promise";
import AlertComponent from "@/components/AlertComponent.vue";

//
const filterOptions = reactive({
    selectedCategories: [] as Category[],
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
} = useExecutablePromise(
    async (...args: Parameters<typeof quizApi.getQuizzes>) =>
        await quizApi.getQuizzes(...args)
);

function searchQuizzes() {
    filtersWindowOpen.value = false;
    executeSearch(
        50,
        0,
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
    0,
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
            <AlertComponent v-else-if="error" type="error">{{
                error
            }}</AlertComponent>
            <AlertComponent v-else-if="foundQuizzes.length === 0" type="warning"
                >No quizzes found.</AlertComponent
            >
            <QuizGrid v-else :quizzes="foundQuizzes" />
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

.found-quizzes-container {
    padding: 1em;
    max-height: 100%;
}

.v-enter-active,
.v-leave-active {
    transition: transform 150ms cubic-bezier(0.85, 0, 0.15, 1);
}

.v-enter-from,
.v-leave-to {
    transform: translateY(-100%);
}
</style>
