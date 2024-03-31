import type { Question, QuestionCreate } from "@/api";
import type { Ref } from "vue";
import { computed } from "vue";

export default function useQuestionType(
    question: Ref<QuestionCreate | Question>
) {
    const questionType = computed<keyof typeof QuestionTypes>(() => {
        if (isText(question.value)) {
            return QuestionTypes.TEXT;
        }
        if (isBoolean(question.value)) {
            return QuestionTypes.BOOLEAN;
        }
        return QuestionTypes.MULTIPLE;
    });

    const readableQuestionType = computed(() => {
        return getReadableQuestionType(questionType.value);
    });

    return {
        questionType,
        readableQuestionType,
    };
}

export function getQuestionType(question: QuestionCreate | Question) {
    if (isText(question)) {
        return QuestionTypes.TEXT;
    }
    if (isBoolean(question)) {
        return QuestionTypes.BOOLEAN;
    }
    return QuestionTypes.MULTIPLE;
}

export const QuestionTypes = {
    MULTIPLE: "MULTIPLE",
    TEXT: "TEXT",
    BOOLEAN: "BOOLEAN",
};

export function getReadableQuestionType(type: keyof typeof QuestionTypes) {
    switch (type) {
        case QuestionTypes.MULTIPLE:
            return "Multiple Choice";
        case QuestionTypes.TEXT:
            return "Text";
        case QuestionTypes.BOOLEAN:
            return "True/False";
    }
}

export function isMultipleChoice(question: Question | QuestionCreate) {
    return !(isBoolean(question) || isText(question));
}

export function isText(question: QuestionCreate | Question) {
    return question.options?.length <= 0;
}

export function isBoolean(question: Question | QuestionCreate) {
    if (question.options?.length !== 2) {
        return false;
    }
    return question.options[0] === "true" && question.options[1] === "false";
}

export function removeFieldsNotInType(
    question: QuestionCreate | Question,
    questionType: keyof typeof QuestionTypes
) {
    if (questionType === QuestionTypes.TEXT) {
        question.options = [];
    } else if (questionType === QuestionTypes.BOOLEAN) {
        question.options = ["true", "false"];
    }

    return question;
}
