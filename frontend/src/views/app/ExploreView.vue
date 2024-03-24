<script setup lang="ts">
import { ref } from "vue";
import FilterIcon from "@/assets/icons/FilterIcon.vue";
import ButtonComponent from "@/components/ButtonComponent.vue";

// Filter window
const filtersWindowOpen = ref(false);

function toggleFiltersWindow() {
    filtersWindowOpen.value = !filtersWindowOpen.value;
}

// Categories

// TODO: Use the Category type generated from the swagger spec
type TempCategory = {
    name: string;
    color: string;
};

const categories = ref<TempCategory[]>([
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

const selectedCategories = ref<TempCategory[]>([]);

function isCategorySelected(category: TempCategory): boolean {
    return selectedCategories.value.map((c) => c.name).includes(category.name);
}

function toggleCategory(category: TempCategory) {
    if (isCategorySelected(category)) {
        selectedCategories.value = selectedCategories.value.filter(
            (c) => c.name !== category.name
        );
    } else {
        selectedCategories.value = [...selectedCategories.value, category];
    }
}

function getCategoryStyle(category: TempCategory) {
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

// Search
const searchQuery = ref("");

function searchQuizzes() {
    filtersWindowOpen.value = false;
    console.log("Searching for quizzes...");
    alert("Searching for quizzes...");
}
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
                @input="searchQuizzes"
            />
            <ButtonComponent @click="searchQuizzes" rounded large filled>
                {{ $t("explore.search") }}
            </ButtonComponent>
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
                <div>
                    <label>Difficulty</label>
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

.filters-container {
    position: absolute;
    display: flex;
    flex-direction: column;
    align-items: center;
    top: 100%;
    left: 0;
    width: 100%;
    background-color: var(--primary-400);
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

fieldset input {
    display: none;
}
</style>
