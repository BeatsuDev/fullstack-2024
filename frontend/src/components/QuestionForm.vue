<template>
    <form @submit.prevent="true">
        <select v-model="questionType">
            <option v-for="type in QuestionTypes" :key="type" :value="type">
                {{ getReadableQuestionType(type) }}
            </option>
        </select>
        <h4>Question</h4>
        <ValidatedInput
            id="question"
            v-model="editable.question"
            :validator="v$.question"
            placeholder="Enter your question here"
        >
        </ValidatedInput>
        <div v-if="questionType == QuestionTypes.BOOLEAN">
            <label>True</label>
            <input type="radio" v-model="booleanAnswer" :value="true" />

            <label>False</label>
            <input type="radio" v-model="booleanAnswer" :value="false" />
        </div>
        <div v-else-if="questionType == QuestionTypes.MULTIPLE">
            <h4>Options</h4>
            <div
                v-for="(option, index) in editable.options"
                class="multiple-question-container"
                :key="index"
            >
                <ValidatedInput
                    v-model="editable.options[index]"
                    :id="`option-${index}`"
                    :validator="v$.question"
                    placeholder="Enter the option here"
                />
                <input type="radio" v-model="selectedOption" :value="index" />
            </div>
            <ButtonComponent
                filled
                @click="addAnswer"
                v-if="editable?.options?.length < 4"
                style="margin-top: 20px"
                >Add option
            </ButtonComponent>
        </div>
        <div v-else-if="questionType == QuestionTypes.TEXT">
            <h4>Answer</h4>
            <ValidatedInput
                id="question"
                v-model="editable.answer"
                :validator="v$.question"
                placeholder="Enter your answer here"
            />
        </div>
        <ButtonComponent filled style="margin-top: 20px" @click="submit"
            >Submit</ButtonComponent
        >
    </form>
</template>
<script lang="ts" setup>
import { defineProps, type Ref, ref, watchEffect } from "vue";
import { type Question } from "../api/models/question";
import useVuelidate from "@vuelidate/core";
import ValidatedInput from "./ValidatedInput.vue";
import ButtonComponent from "./ButtonComponent.vue";
import { required } from "@vuelidate/validators";
import type { QuestionCreate } from "@/api";
import {
    getReadableQuestionType,
    QuestionTypes,
    removeFieldsNotInType,
} from "@/composables/useQuestionType";

const props = defineProps<{
    value?: QuestionCreate | Question;
}>();

const emit = defineEmits<{
    (event: "submit", value: QuestionCreate | Question): void;
}>();

const questionType = ref<QuestionTypes>(QuestionTypes.MULTIPLE);
const editable = ref(props.value|| {
    question: "",
    options: [""],
    answer: "",

}) as Ref<QuestionCreate | Question>;

watchEffect(() => {
    editable.value = props.value || {
        question: "",
        options: [""],
        answer: "",
    } as QuestionCreate;
});

const selectedOption = ref(0);
const booleanAnswer = ref(true);

const formRules = {
    question: {
        required: required,
    },
};

const v$ = useVuelidate(formRules, editable);

function submit() {
    if (v$.value.$invalid) {
        return;
    }
    if (questionType.value == QuestionTypes.MULTIPLE) {
        editable.value.answer = editable.value.options[selectedOption.value];
    } else if (questionType.value == QuestionTypes.BOOLEAN) {
        editable.value.answer = booleanAnswer.value.toString();
        editable.value.options = ["true", "false"];
    }

    emit("submit", removeFieldsNotInType(editable.value, questionType.value));
}

function addAnswer() {
    editable.value?.options.push("");
}
</script>
<style scoped>
.multiple-question-container {
    display: flex;
    flex-direction: row;
}
</style>
