<script lang="ts" setup>
import { computed, reactive, ref, watch } from "vue";
import FilterIcon from "@/assets/icons/FilterIcon.vue";
import ButtonComponent from "@/components/ButtonComponent.vue";
import FilterOptions from "@/components/FilterOptions.vue";
import QuizGrid from "@/components/QuizGrid.vue";

import type { Category, QuizOverview } from "@/api";
import { QuizApi } from "@/api";
import { useExecutablePromise } from "@/composables/promise";
import AlertComponent from "@/components/AlertComponent.vue";
import useDebounceLoading from "@/composables/useDebounceLoading";
import { useNotificationStore } from "@/stores/notification";
import { isAxiosError } from "axios";

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

const quizFetchLoadingDebounced = useDebounceLoading(quizFetchLoading, 200);

const errorMessage = computed(() => {
    if (!error.value) return "";
    if (!isAxiosError(error.value)) return "An unknown error occurred.";
    if (error.value.response?.status === 404) return "No quizzes found.";
    if (error.value.response?.status === 400) return "Invalid search query.";
    return "Failed to search for quizzes.";
});

function searchQuizzes() {
    filtersWindowOpen.value = false;
    nextPage.value = 0;
    loadMore(true);
}

// Quizzes
const foundQuizzes = ref<QuizOverview[]>([]);
const nextPage = ref(0);

function loadMore(reset = false) {
    executeSearch(
        1,
        nextPage.value++,
        searchQuery.value,
        filterOptions.minDifficulty,
        filterOptions.maxDifficulty,
        filterOptions.selectedCategories.map((c) => c.id)
    ).then((response) => {
        if (reset) {
            foundQuizzes.value = response.data;
        } else {
            foundQuizzes.value.push(...response.data);
        }
    });
}
loadMore();

watch(error, () => {
    if (error.value) {
        useNotificationStore().addNotification({
            type: "error",
            message: errorMessage.value,
        });
    }
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
                class="toggle-filters"
                rounded-lg
                @click="toggleFiltersWindow"
            >
                <FilterIcon />
            </ButtonComponent>
            <input
                v-model="searchQuery"
                placeholder="Search for quizzes..."
                type="text"
                @keydown.enter="searchQuizzes"
            />
            <ButtonComponent filled large rounded @click="searchQuizzes">
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
            <AlertComponent v-if="quizFetchLoadingDebounced" type="info">
                Loading...
            </AlertComponent>
            <AlertComponent
                v-else-if="foundQuizzes.length === 0"
                type="warning"
            >
                No quizzes found.
            </AlertComponent>
            <QuizGrid v-else :quizzes="foundQuizzes" />
            <ButtonComponent
                class="load-more-button"
                filled
                large
                @click="loadMore()"
            >
                Load more
            </ButtonComponent>
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

.load-more-button {
    width: 8rem;
    margin: 1em auto 0 calc(50% - 4rem);
}
</style>
