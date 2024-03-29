import type { Question, QuestionCreate } from "@/api";
import type { Ref } from "vue";
import { computed } from "vue";

export default function useQuestionType(
    question: Ref<QuestionCreate | Question>
) {
    const questionType = computed(() => {
        if (isText(question.value)) {
            return QuestionTypes.TEXT;
        }
        if (isBoolean(question.value)) {
            return QuestionTypes.BOOLEAN;
        }
        return QuestionTypes.MULTIPLE;
    });

    return {
        questionType,
    };
}

export const QuestionTypes = {
    MULTIPLE: "multiple",
    TEXT: "text",
    BOOLEAN: "boolean",
};

export function getReadableQuestionType(type) {
    switch (type) {
        case QuestionTypes.MULTIPLE:
            return "Multiple Choice";
        case QuestionTypes.TEXT:
            return "Text";
        case QuestionTypes.BOOLEAN:
            return "True/False";
    }
}

export function isMultipleChoice(question) {
    return !(isBoolean(question) || isText(question));
}

export function isText(question: QuestionCreate | Question) {
    return question.options?.length <= 0;
}

export function isBoolean(question) {
    if (question.options?.length !== 2) {
        return false;
    }
    return (
        question.options[0].text === "True" &&
        question.options[1].text === "False"
    );
}

export function removeFieldsNotInType(
    question: QuestionCreate | Question,
    questionType: keyof typeof QuestionTypes
) {
    if (questionType === QuestionTypes.TEXT) {
        delete question.options;
    }
    if (questionType === QuestionTypes.BOOLEAN) {
        question.options = ["True", "False"];
    }

    return question;
}
