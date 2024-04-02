<script setup lang="ts">
import type { Question } from "@/api";
import ButtonComponent from "@/components/ButtonComponent.vue";

defineProps<{
    question: Question;
}>();

const emit = defineEmits<{
    answerSelected: [option: string];
}>();

</script>

<template>
    <div class="multiple-choice-container">
        <div v-for="option in question.options" :key="option">
            <ButtonComponent
                class="option"
                @click="() => emit('answerSelected', option)"
            >
                {{ option }}
            </ButtonComponent>
        </div>
    </div>
</template>

<style scoped>
.multiple-choice-container {
    margin-top: 1em;
    width: 100%;
    height: 100%;
    display: grid;
    grid-template-columns: 1fr 1fr;
    gap: 0.1em;
}

@media (max-width: 900px) {
    .multiple-choice-container {
        grid-template-columns: 1fr;
    }
}

.option {
    width: 100%;
    font-size: 2em;
}
</style>
