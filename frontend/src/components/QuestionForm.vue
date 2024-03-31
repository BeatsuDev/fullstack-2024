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
        <div v-if="questionType == QuestionTypes.BOOLEAN" class="boolean-options">
            <input type="radio" v-model="booleanAnswer" :value="true" />
            <label>True</label>
            <input type="radio" v-model="booleanAnswer" :value="false" />
            <label>False</label>
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
                    style="margin-bottom: 0.5rem;"
                    :validator="v$.question"
                    placeholder="Enter the option here"
                />
                <input type="radio" v-model="selectedOption" :value="index" />
            </div>
            <ButtonComponent
                @click="addAnswer"
                v-if="editable?.options?.length < 4"
                style="margin-top: 20px"
                block
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
        <ButtonComponent filled block style="margin-top: 3rem" @click="submit"
        >Submit
        </ButtonComponent
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
import { QuestionApi, type QuestionCreate, type QuizCreate } from "@/api";
import { getReadableQuestionType, QuestionTypes, removeFieldsNotInType } from "@/composables/useQuestionType";

const props = defineProps<{
    value?: QuestionCreate | Question;
}>();

const emit = defineEmits<{
    (event: "submit", value: QuestionCreate | Question): void;
}>();

const questionType = ref<QuestionTypes>(QuestionTypes.MULTIPLE);
const editable = ref(props.value || {
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
/* Make radio buttons square */
.multiple-question-container input[type="radio"] {
    appearance: none;
    -webkit-appearance: none;
    -moz-appearance: none;
    width: 36px; /* Set the width and height */
    height: 36px;
    border: 1px solid #999; /* Add a border */
    border-radius: 4px; /* Optional: Add rounded corners */
    outline: none; /* Remove outline */
}

/* Customize the appearance of checked state */
input[type="radio"]:checked {
    background-color: var(--primary-500); /* Change the background color */
}

/* Adjust height of ValidatedInput component */
.ValidatedInput input {
    height: 40px; /* Adjust this value according to your preference */
}

/* Additional styling for form layout */
.multiple-question-container {
    display: flex;
    flex-direction: row;
}

form {
    display: flex;
    flex-direction: column;
}

select {
    margin-bottom: 20px;
    padding: 10px;
}

.boolean-options {
    display: flex;
    flex-direction: row;
    gap: 10px;
    margin-top: 20px;

}
</style>

