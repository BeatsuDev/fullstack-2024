<template>
    <form @submit.prevent ref="form">
        <label for="title">Title</label>
        <ValidatedInput id="title" :validator="v$.title" v-model="editable.title" />
        <label for="Description">Description</label>
        <ValidatedInput
            id="description"
            :validator="v$.description"
            v-model="editable.description"
        />
        <label for="difficulty">Difficulty: {{ editable.difficulty }} </label>
        <input type="range" min="1" max="10" v-model="editable.difficulty" />
        <ButtonComponent @click="submit" :loading="props.loading"
        >Submit
        </ButtonComponent>
    </form>

</template>
<script setup lang="ts">
import ButtonComponent from "@/components/ButtonComponent.vue";
import ValidatedInput from "@/components/ValidatedInput.vue";
import { type Quiz, type QuizCreate } from "@/api";
import { reactive, ref, watchEffect } from "vue";
import { required } from "@vuelidate/validators";
import { useVuelidate } from "@vuelidate/core";

const props = defineProps<{
    value?: QuizCreate | Quiz;
    loading?: boolean;
}>();

const emit = defineEmits<{
    (e: "submit", quiz: QuizCreate | Quiz): void;
}>();

const editable = reactive(props.value || {

    title: "",
    description: "",
    difficulty: 1,

}) as QuizCreate;

watchEffect(() => {
    Object.assign(editable, props.value);
});

const formRules = {
    title: { required },
    description: { required },
    difficulty: { required },
};

const v$ = useVuelidate(formRules, editable);

const form = ref<HTMLFormElement | null>(null);

async function submit() {
    if (!form.value) return;
    if (!(await v$.value.$validate())) {
        return;
    }

    emit("submit", editable);
}

</script>
<style scoped>
form {
    display: flex;
    flex-direction: column;
    gap: 1rem;
    max-width: 20rem;
}
</style>