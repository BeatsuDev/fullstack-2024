<script setup lang="ts">
import { ref } from "vue";
import FilterIcon from "@/assets/icons/FilterIcon.vue";
import ButtonComponent from "@/components/ButtonComponent.vue";

const filtersWindowOpen = ref(false);

function toggleFiltersWindow() {
    filtersWindowOpen.value = !filtersWindowOpen.value;
}

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
                &lt;insert filters here&gt;
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
</style>
