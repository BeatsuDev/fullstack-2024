<script setup lang="ts">
import ButtonComponent from "@/components/ButtonComponent.vue";

const emit = defineEmits<{
    answerSelected: [option: string];
}>();
</script>

<template>
    <div class="boolean-answer-container">
        <ButtonComponent @click="() => emit('answerSelected', 'true')">
            True
        </ButtonComponent>
        <ButtonComponent @click="() => emit('answerSelected', 'false')">
            False
        </ButtonComponent>
    </div>
</template>

<style scoped>
.boolean-answer-container {
    margin-top: 1em;
    width: 100%;
    height: 100%;
    display: grid;
    grid-template-columns: 1fr 1fr;
    gap: 0.1em;
}

.boolean-answer-container > * {
    font-size: 2em;
    padding: 0.75em;
}
</style>
