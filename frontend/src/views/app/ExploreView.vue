<script setup lang="ts">
import { ref } from "vue";
import FilterIcon from "@/assets/icons/FilterIcon.vue";
import ButtonComponent from "@/components/ButtonComponent.vue";

const searchQuery = ref("");

function searchQuizzes() {
    console.log("Searching for quizzes...");
    alert("Searching for quizzes...");
}
</script>

<template>
    <div>
        <div class="search-container">
            <ButtonComponent rounded-lg class="toggle-filters"
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
        </div>
    </div>
</template>

<style scoped>
.search-container {
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
</style>
