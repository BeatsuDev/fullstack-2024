<template>
    <div class="card">
        <h3>1. {{ props.value.question }}</h3>
        <ul>
            <li v-for="option in props.value.options" :key="option">
                {{option}}
            </li>
        </ul>
        <div class="action-bar">
            <ButtonComponent
                v-if="props.editable"
                filled
                @click="emit('delete', value)" >Delete</ButtonComponent>
            <ButtonComponent
                v-if="props.editable"
                filled
                @click="emit('edit', value)" >Edit</ButtonComponent>
        </div>
    </div>
</template>
<script lang="ts" setup>
import type { Question } from "@/api";
import { defineProps } from "vue";
import ButtonComponent from "./ButtonComponent.vue";

const props = defineProps<{
    value: Question;
    editable?: boolean;
}>();

const emit = defineEmits<{
    (event: "edit", value: Question): void;
    (event: "delete", value: Question): void;
}>();


</script>
<style scoped>
.card {
    border: 1px solid var(--color-border);
    border-radius: 0.5em;
    padding: 1em;
    margin: 1em 0;
}

.action-bar {
    display: flex;
    justify-content: end;
    margin-top: 1em;
}
</style>
