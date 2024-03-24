<script setup lang="ts">
import { ref } from "vue";
import FilterIcon from "@/assets/icons/FilterIcon.vue";
import ButtonComponent from "@/components/ButtonComponent.vue";

import type { Category, QuizOverview } from "@/api";
import { QuizApi } from "@/api";
import { usePromise } from "@/composables/promise";

// Search
const searchQuery = ref("");

function searchQuizzes() {
    filtersWindowOpen.value = false;
    console.log("Searching for quizzes...");
    alert("Searching for quizzes...");
}

// Filter window
const filtersWindowOpen = ref(false);

function toggleFiltersWindow() {
    filtersWindowOpen.value = !filtersWindowOpen.value;
}

// Categories
const categories = ref<Category[]>([
    { name: "Biology", color: "#0f0" },
    { name: "Science", color: "#ad0" },
    { name: "Math", color: "#f00" },
    { name: "History", color: "#00f" },
    { name: "Geography", color: "#f0f" },
    { name: "Literature", color: "#0ff" },
    { name: "Art", color: "#ff0" },
    { name: "Music", color: "#f80" },
    { name: "Movies", color: "#f08" },
    { name: "Sports", color: "#8f0" },
    { name: "Technology", color: "#08f" },
    { name: "General Knowledge", color: "#80f" },
]);

const selectedCategories = ref<Category[]>([]);

function isCategorySelected(category: Category): boolean {
    return selectedCategories.value.map((c) => c.name).includes(category.name);
}

function toggleCategory(category: Category) {
    if (isCategorySelected(category)) {
        selectedCategories.value = selectedCategories.value.filter(
            (c) => c.name !== category.name
        );
    } else {
        selectedCategories.value = [...selectedCategories.value, category];
    }
}

function getCategoryStyle(category: Category) {
    if (isCategorySelected(category)) {
        return {
            border: `2px solid ${category.color}`,
            backgroundColor: category.color,
            color: "white",
            "box-shadow": "1px 1px 2px rgb(0 0 0 / 50%)",
        };
    }
    return {
        border: `2px solid ${category.color}`,
    };
}

// Difficulty range
const minDifficulty = ref(1);
const maxDifficulty = ref(10);

// Quizzes
const quizApi = new QuizApi();
const foundQuizzes = ref<QuizOverview[]>([]);

const { promise, loading: quizFetchLoading } = usePromise(
    quizApi.quizGet(
        50,
        searchQuery.value,
        minDifficulty.value,
        maxDifficulty.value,
        selectedCategories.value
    )
);

promise.then((response) => {
    foundQuizzes.value = response.data;
});
</script>

<template>
    <div>
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
                {{ $t("explore.search") }}
            </ButtonComponent>

            <!-- This one is not affected by the transition animation and hides all overflow -->
            <div class="outer-filters-container">
                <Transition>
                    <div v-if="filtersWindowOpen" class="filters-container">
                        <fieldset style="border: none">
                            <legend>Categories</legend>
                            <div class="category-filter-labels">
                                <label
                                    v-for="(category, i) in categories"
                                    :key="i"
                                    :style="getCategoryStyle(category)"
                                    ><input
                                        type="checkbox"
                                        @change="toggleCategory(category)"
                                    />{{ category.name }}</label
                                >
                            </div>
                        </fieldset>
                        <fieldset style="border: none">
                            <legend>Difficulty</legend>
                            <div>
                                <label>
                                    Min ({{ minDifficulty }})
                                    <input
                                        v-model="minDifficulty"
                                        type="range"
                                        step="1"
                                        min="1"
                                        max="10"
                                        value="1"
                                /></label>
                                <br />
                                <label>
                                    Max ({{ maxDifficulty }})
                                    <input
                                        v-model="maxDifficulty"
                                        type="range"
                                        step="1"
                                        min="1"
                                        max="10"
                                        value="10"
                                    />
                                </label>
                            </div>
                        </fieldset>
                    </div>
                </Transition>
            </div>
        </div>
        <div class="found-quizzes-container">
            <div v-if="quizFetchLoading">Loading...</div>
            <div v-else-if="foundQuizzes.length === 0">No quizzes found.</div>
            <div class="found-quizzes-grid" v-else>
                <div
                    v-for="(quiz, i) in foundQuizzes"
                    :key="i"
                    class="quiz-overview-card"
                >
                    <div
                        class="quiz-card-banner"
                        :style="`background-color: hsl(${Math.random() * 360}deg ${60 + Math.random() * 40}% 50%);`"
                    ></div>
                    <div class="quiz-card-content">
                        <h4 style="margin-top: 0">{{ quiz.title }}</h4>
                        <p>{{ quiz.description }}</p>
                    </div>
                </div>
            </div>
        </div>
    </div>
</template>

<style scoped>
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

.filters-container {
    display: flex;
    flex-direction: column;
    padding: 1em 0.5em;
    background-color: var(--primary-400);

    width: 100%;
    height: 100%;
}

.category-filter-labels {
    display: flex;
    flex-wrap: wrap;
    align-items: center;
}

.category-filter-labels label {
    display: flex;
    align-items: center;
    margin: 0.25em;
    padding: 0.5em 1em;
    border: 2px solid;
    border-radius: 2em;
    cursor: pointer;
    user-select: none;
}

.category-filter-labels input {
    display: none;
}

fieldset > legend {
    font-size: 1.2em;
    margin: 0.5em;
    font-weight: bold;
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
}

.found-quizzes-grid {
    display: grid;
    grid-template-columns: repeat(auto-fill, minmax(200px, 1fr));
    gap: 1em;
}

.quiz-overview-card {
    border: 1px solid black;
}

.quiz-overview-card .quiz-card-banner {
    height: 100px;
}

.quiz-overview-card .quiz-card-content {
    padding: 1em;
}
</style>
