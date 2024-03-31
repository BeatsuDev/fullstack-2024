<template>
    <div class="app-container">
        <h3>Results</h3>
        <div class="row">
            <div></div>
        </div>
    </div>

</template>
<script lang="ts" setup>

import { useRoute } from "vue-router";
import { computed } from "vue";

const route = useRoute();

const quizId = computed(() => route.params.id.toString());

</script>
<style scoped>

.row {
    display: flex;
    margin-bottom: 1rem;
}

.small {
    width: 30%;
}

.big {
    width: 70%;
}
</style>