<script setup lang="ts" generic="ParameterValidator extends { $validate: () => Promise<boolean>, $reset: () => void, $errors: ErrorObject[] }">
import type { ErrorObject } from "@vuelidate/core/index.js";


const model = defineModel<string>();
const props = defineProps<{
    id: string;
    type: string;
    label: string;
    validator: ParameterValidator;
    class?: string;
    [key: string]: any;
}>();
</script>

<template>
    <div class="input-container" :class="props.class">
        <label :for="id">{{ label }}</label>
        <input
            :type="type"
            :id="id"
            :class="{ 'border-red': validator.$errors.length }"
            @input="validator.$reset()"
            @focusout="validator.$validate()"
            v-model="model"
        />
        <span v-if="validator.$errors.length" class="error-message">{{ validator.$errors[0].$message }}</span>
    </div>
</template>

<style scoped>
.input-container {
    display: flex;
    flex-direction: column;
    gap: 0.2em;
}

input {
    padding: 0.5rem;
    font-size: 1em;
}

.error-message {
    color: red;
    margin-left: 0.2em;
    font-size: 0.8em;
    font-style: italic;
}

.border-red {
    border: 1px solid red;
}
</style>