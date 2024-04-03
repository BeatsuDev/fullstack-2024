<template>
    <form style="max-width: 300px" @submit.prevent>
        <SelectComponent v-model="questionType">
            <option v-for="type in QuestionTypes" :key="type" :value="type">
                {{ getReadableQuestionType(type) }}
            </option>
        </SelectComponent>
        <h4>Question</h4>
        <ValidatedInput
            id="question"
            v-model="editable.question"
            :validator="v$.question"
            placeholder="Enter your question here"
        >
        </ValidatedInput>
        <div v-if="!editable.image">
            <h4>Image</h4>
            <input
                accept=".jpeg , .png , .jpg"
                type="file"
                @change="uploadImage"
            />
        </div>
        <div v-else style="width: 100%">
            <img
                v-if="editable.image"
                :src="BASE_PATH + '/' + editable.image.path"
                alt="image"
                style="max-width: 100%; margin-top: 10px"
            />
            <ButtonComponent
                block
                filled
                style="margin-top: 10px"
                @click="editable.image = null"
                >Remove image
            </ButtonComponent>
        </div>
        <div
            v-if="questionType == QuestionTypes.BOOLEAN"
            class="boolean-options"
        >
            <input v-model="booleanAnswer" :value="true" type="radio" />
            <label>True</label>
            <input v-model="booleanAnswer" :value="false" type="radio" />
            <label>False</label>
        </div>
        <div v-else-if="questionType == QuestionTypes.MULTIPLE">
            <h4>Options</h4>
            <div
                v-for="(option, index) in editable.options"
                :key="index"
                class="multiple-question-container"
            >
                <ValidatedInput
                    :id="`option-${index}`"
                    v-model="editable.options[index]"
                    placeholder="Enter the option here"
                    style="margin-bottom: 0.5rem"
                />
                <input v-model="selectedOption" :value="index" type="radio" />
            </div>
            <ButtonComponent
                v-if="editable?.options?.length < 4"
                block
                style="margin-top: 20px"
                @click="addAnswer"
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
        <ButtonComponent block filled style="margin-top: 3rem" @click="submit"
            >Submit
        </ButtonComponent>
        <div v-if="error" style="color: var(--error-500); margin-top: 1rem">
            {{ error }}
        </div>
    </form>
</template>
<script lang="ts" setup>
import { defineProps, ref, watchEffect } from "vue";
import { type Question } from "../api/models/question";
import useVuelidate from "@vuelidate/core";
import ValidatedInput from "./ValidatedInput.vue";
import ButtonComponent from "./ButtonComponent.vue";
import { required } from "@vuelidate/validators";
import { type Image, type QuestionCreate } from "@/api";
import {
    getReadableQuestionType,
    QuestionTypes,
    removeFieldsNotInType,
} from "@/composables/useQuestionType";
import SelectComponent from "@/components/SelectComponent.vue";
import { useNotificationStore } from "@/stores/notification";
import axios from "axios";
import { BASE_PATH } from "@/api/base";

const props = defineProps<{
    value?: Pick<Question, "question" | "answer" | "options"> &
        Partial<Question>;
}>();

const emit = defineEmits<{
    (event: "submit", value: QuestionCreate | Question): void;
}>();

const questionType = ref<keyof typeof QuestionTypes>(QuestionTypes.MULTIPLE);
const editable = ref<
    Pick<Question, "question" | "answer" | "options"> & Partial<Question>
>(
    props.value || {
        question: "",
        options: [""],
        answer: "",
    }
);

watchEffect(() => {
    editable.value =
        props.value ||
        ({
            question: "",
            options: [""],
            answer: "",
        } as QuestionCreate);
});

const selectedOption = ref(0);
const booleanAnswer = ref(true);

const formRules = {
    question: { required },
};

const v$ = useVuelidate(formRules, editable);

const error = ref("");

async function submit() {
    if (!(await v$.value.$validate())) {
        return;
    }
    error.value = optionsValidator();
    if (error.value) {
        return;
    }
    if (questionType.value == QuestionTypes.MULTIPLE) {
        editable.value.answer = editable.value.options[selectedOption.value];
    } else if (questionType.value == QuestionTypes.BOOLEAN) {
        editable.value.answer = booleanAnswer.value.toString();
        editable.value.options = ["true", "false"];
    }
    //@ts-ignore
    editable.value.imageId = editable.value.image?.id;

    emit("submit", removeFieldsNotInType(editable.value, questionType.value));
}

function addAnswer() {
    editable.value?.options.push("");
}

function optionsValidator() {
    if (questionType.value == QuestionTypes.MULTIPLE) {
        const empty = editable.value.options.filter(
            (option) => option.trim() == ""
        );
        if (empty.length > 0) {
            return "Some options are empty";
        }
    } else if (questionType.value == QuestionTypes.TEXT) {
        if (!editable.value.answer) {
            return "Answer is empty";
        }
    }
    return "";
}

const notificationStore = useNotificationStore();
const api = axios.create({
    baseURL: BASE_PATH,
});

function uploadImage() {
    const file = (event.target as HTMLInputElement).files?.[0];
    if (!file) {
        return;
    }

    const formData = new FormData();
    formData.append("image", file);
    api.post<Image>("/image", formData)
        .then((response) => {
            editable.value.image = response.data;
            notificationStore.addNotification({
                message: "Image uploaded successfully.",
                type: "success",
            });
        })
        .catch(() => {
            notificationStore.addNotification({
                message: "An unexpected error occurred.",
                type: "error",
            });
        });
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

.boolean-options {
    display: flex;
    flex-direction: row;
    gap: 10px;
    margin-top: 20px;
}
</style>
