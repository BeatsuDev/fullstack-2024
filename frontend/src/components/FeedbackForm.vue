<template>
    <form ref="form" @submit.prevent>
        <label for="title">
            <h4 style="margin-top: 3rem">Submit new Feedback</h4>
        </label>
        <ValidatedInput
            id="feedback"
            v-model="editable.feedback"
            :validator="v$.feedback"
        />
        <div style="display: flex; justify-content: end; margin-top: 10px">
            <ButtonComponent
                id="submit-feedback-button"
                type="submit"
                @click="submit"
            >
                Submit
            </ButtonComponent>
        </div>
    </form>
</template>

<script lang="ts" setup>
import ButtonComponent from "@/components/ButtonComponent.vue";
import ValidatedInput from "@/components/ValidatedInput.vue";
import type { FeedbackCreate } from "@/api";
import { reactive, ref, watchEffect } from "vue";
import { required } from "@vuelidate/validators";
import { useVuelidate } from "@vuelidate/core";

const props = defineProps<{
    value?: FeedbackCreate;
}>();

const emit = defineEmits<{
    submit: [quiz: FeedbackCreate];
}>();

const editable = reactive(
    props.value || {
        feedback: "",
    }
) as FeedbackCreate;

watchEffect(() => {
    if (props.value) Object.assign(editable, props.value);
});

const formRules = {
    feedback: { required },
};

const v$ = useVuelidate(formRules, editable);

const form = ref<HTMLFormElement | null>(null);

async function submit() {
    if (!(await v$.value.$validate())) {
        return;
    }

    emit("submit", editable);
    editable.feedback = " ";
    v$.value.$reset();
}
</script>
