<script setup lang="ts">
import { QuizApi } from "@/api";
import { usePromise } from "@/composables/promise";
import router from "@/router";

const quizApi = new QuizApi();
const {
    promise,
    data: response,
    loading,
    error,
} = usePromise(
    quizApi.quizIdGet(
        (router.currentRoute.value.params.id as unknown as string) ?? ""
    )
);

promise.then((response) => {
    console.log(response.data);
});
</script>

<template>
    <main>
        <div v-if="loading">Loading...</div>
        <div v-else-if="error">{{ error }}</div>
        <div v-else-if="response">
            <h1>{{ response.data.title }}</h1>
        </div>
    </main>
</template>

<style scoped>
main {
    height: calc(100vh - 66px);
}
</style>
