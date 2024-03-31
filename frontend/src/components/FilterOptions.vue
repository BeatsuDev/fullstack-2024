<script setup lang="ts">
import { ref } from "vue";
import type { Category } from "@/api";
import { CategoryApi } from "@/api";

const filterOptions = defineModel<{
    selectedCategories: Category[];
    minDifficulty: number;
    maxDifficulty: number;
}>();

// Categories
const categoryApi = new CategoryApi();
const categories = ref<Category[]>();

categoryApi.getCategories().then((response) => {
    categories.value = response.data;
    console.log(categories.value);
});

function isCategorySelected(category: Category): boolean {
    return filterOptions.value.selectedCategories
        .map((c) => c.name)
        .includes(category.name);
}

function toggleCategory(category: Category) {
    if (isCategorySelected(category)) {
        filterOptions.value.selectedCategories =
            filterOptions.value.selectedCategories.filter(
                (c) => c.name !== category.name
            );
    } else {
        filterOptions.value.selectedCategories = [
            ...filterOptions.value.selectedCategories,
            category,
        ];
    }
}

function getCategoryStyle(category: Category) {
    if (category.name == "string")
        return {
            border: `2px solid red`,
        };
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
</script>

<template>
    <div class="filters-container">
        <fieldset style="border: none">
            <legend>Categories</legend>
            <div class="category-filter-labels">
                <label
                    v-for="(category, i) in categories"
                    :key="i"
                    :style="getCategoryStyle(category)"
                >
                    <input type="checkbox" @change="toggleCategory(category)" />
                    {{ category.name }}
                </label>
            </div>
        </fieldset>
        <fieldset style="border: none">
            <legend>Difficulty</legend>
            <div>
                <label>
                    Min ({{ filterOptions.minDifficulty }})
                    <input
                        v-model="filterOptions.minDifficulty"
                        @input="
                            () => {
                                if (
                                    parseInt(filterOptions.minDifficulty, 10) >
                                    parseInt(filterOptions.maxDifficulty, 10)
                                ) {
                                    filterOptions.minDifficulty =
                                        filterOptions.maxDifficulty;
                                }
                            }
                        "
                        type="range"
                        step="1"
                        min="1"
                        max="10"
                        value="1"
                /></label>
                <br />
                <label>
                    Max ({{ filterOptions.maxDifficulty }})
                    <input
                        v-model="filterOptions.maxDifficulty"
                        @input="
                            () => {
                                if (
                                    parseInt(filterOptions.maxDifficulty, 10) <
                                    parseInt(filterOptions.minDifficulty, 10)
                                ) {
                                    filterOptions.maxDifficulty =
                                        filterOptions.minDifficulty;
                                }
                            }
                        "
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
</template>

<style scoped>
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
    text-transform: uppercase;
}

.category-filter-labels input {
    display: none;
}

fieldset > legend {
    font-size: 1.2em;
    margin: 0.5em;
    font-weight: bold;
}
</style>
