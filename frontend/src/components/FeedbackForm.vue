
<template>
    <form @submit.prevent ref="form">
        <label for="title">Feedback</label>
        <ValidatedInput id="feedback" :validator="v$.feedback" v-model="editable.feedback" />
        <ButtonComponent @click="submit" :loading="props.loading">Create </ButtonComponent>
    </form>
</template>
<script setup lang="ts">
import ButtonComponent from "@/components/ButtonComponent.vue";
import ValidatedInput from "@/components/ValidatedInput.vue";
import type { FeedbackCreate } from "@/api";
import { reactive, ref, watchEffect } from "vue";
import { required } from "@vuelidate/validators";
import { useVuelidate } from "@vuelidate/core";

const props = defineProps<{
    value?: FeedbackCreate;
    loading?: boolean;
}>();

const emit = defineEmits<{
    (e: "submit", quiz: FeedbackCreate): void;
}>();

const editable = reactive(props.value || {
    feedback: "",
}) as FeedbackCreate;

watchEffect(() => {
    if (props.value) Object.assign(editable, props.value);
});

const formRules = {
    feedback: { required },
};

const v$ = useVuelidate(formRules, editable);

const form = ref<HTMLFormElement | null>(null);

async function submit() {
    if (!form.value) return;
    if (!(await v$.value.$validate())) {
        return;
    }

    emit("submit", editable);
    editable.feedback = " ";
    v$.value.$reset();
}


</script>