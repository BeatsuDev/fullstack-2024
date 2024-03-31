<script setup lang="ts">
import { ref } from "vue";
import ButtonComponent from "@/components/ButtonComponent.vue";

const freeTextInput = ref("");

const emit = defineEmits<{
    answerSelected: [option: string];
}>();
</script>

<template>
    <div class="text-answer-container">
        <input
            v-model="freeTextInput"
            type="text"
            @keyup.enter="emit('answerSelected', freeTextInput)"
        />
        <ButtonComponent @click="emit('answerSelected', freeTextInput)" filled>
            Submit Answer
        </ButtonComponent>
    </div>
</template>

<style scoped>
.text-answer-container {
    margin-top: 1em;
    width: 100%;
    height: 100%;
    gap: 0.1em;
    display: flex;
}

input {
    flex: 1;
    font-size: 2em;
    min-width: 0;
}
</style>
