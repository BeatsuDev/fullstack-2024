<script lang="ts" setup>
import type { ErrorObject } from "@vuelidate/core/index.js";

type ParameterValidator = {
    $validate: () => Promise<boolean>;
    $reset: () => void;
    $errors: ErrorObject[];
};

const model = defineModel<string>();
const props = defineProps<{
    id: string;
    validator?: ParameterValidator;
    type?: string;
    label?: string;
    class?: string;
    [key: string]: any;
}>();
</script>

<template>
    <div :class="props.class" class="input-container">
        <label :for="id">{{ label }}</label>
        <input
            :id="id"
            v-model="model"
            :class="{ 'border-red': validator?.$errors.length }"
            :type="type"
            @focusout="validator?.$validate()"
            @input="validator?.$reset()"
        />
        <span v-if="validator?.$errors?.length" class="error-message">{{
            validator.$errors[0].$message
        }}</span>
    </div>
</template>

<style scoped>
.input-container {
    position: relative;
    display: flex;
    flex-direction: column;
    gap: 0.2em;
}

input {
    padding: 0.5rem;
    font-size: 1em;
}

input:focus {
    outline: none;
}

.error-message {
    position: absolute;
    color: red;
    background-color: white;
    padding: 0.1em 0.25em;
    z-index: 10;
    bottom: 0;
    transform: translateY(50%);
    left: 0.25em;
    font-size: 0.8em;
    font-style: italic;
}

.border-red {
    border: 1px solid red;
}
</style>
