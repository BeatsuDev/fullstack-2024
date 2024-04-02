<template>
    <div class="category-filter-labels">
        <label
            v-for="(category, i) in categories"
            :key="i"
            :style="getCategoryStyle(category)"
            ><input type="checkbox" @change="toggleCategory(category)" />{{
                category.name
            }}</label
        >
    </div>
</template>
<script lang="ts" setup>
import type { Category } from "@/api";

function toggleCategory(category: Category) {
    if (isCategorySelected(category)) {
        selectedCategories.value = selectedCategories.value.filter(
            (c) => c.name !== category.name
        );
    } else {
        selectedCategories.value = [...selectedCategories.value, category];
    }
}
</script>
