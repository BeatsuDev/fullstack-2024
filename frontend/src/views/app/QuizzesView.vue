<script lang="ts" setup>
import type { QuizOverview } from "@/api";
import { QuizApi } from "@/api";
import { ref, watch } from "vue";
import { useNotificationStore } from "@/stores/notification";
import { useExecutablePromise } from "@/composables/promise";
import QuizGrid from "@/components/QuizGrid.vue";
import { vInfiniteScroll } from "@vueuse/components";

const pageSize = 10;
let pageNumber = 0;
const data = ref<QuizOverview[]>([]);

// Fetch quizzes from the API
const quizApi = new QuizApi();
const {
    execute,
    data: newestData,
    error,
    loading,
} = useExecutablePromise(
    async (...args: Parameters<typeof quizApi.getQuizzes>) => {
        return (await quizApi.getQuizzes(...args)).data;
    }
);

execute(10, pageNumber);

// Load more
async function onLoadMore() {
    const newData = await execute(pageSize, pageNumber);
    data.value.push(...newData);
    pageNumber++;
}

function canLoadMore() {
    if (error.value != null) return false;
    if (pageNumber === 0 || newestData.value == null) return true;
    if (newestData.value.length === 0) return false;
    return !loading.value;
}

// Error handling
watch(error, () => {
    useNotificationStore().addNotification({
        type: "error",
        message: "Could not load quizzes.",
    });
});
</script>

<template>
    <div
        v-infinite-scroll="[onLoadMore, { canLoadMore }]"
        class="my-quizzes-container"
    >
        <h1>Your Quizzes</h1>
        <QuizGrid :quizzes="data" />
        <div v-if="loading">Loading more data...</div>
    </div>
</template>

<style scoped>
.my-quizzes-container {
    box-sizing: border-box;
    height: 100%;
    padding: 1em;
    overflow: auto;
}

.my-quizzes-container h1 {
    margin-bottom: 1em;
}
</style>
