import { describe, it, beforeEach, expect } from "vitest";
import { mount } from "@vue/test-utils";

import type { Question } from "@/api";
import QuestionCard from "../QuestionCard.vue";

const question: Question = {
    id: "ae4b1b1e-4b1b-4b1b-4b1b-4b1b4b1b4b1b",
    question: "Test question?",
    answer: "Option 1",
    image: {
        id: "ae4b1b1e-4b1b-4b1b-4b1b-4b1b4b1b4b1b",
        path: "test.jpg",
    },
    options: ["Option 1", "Option 2", "Option 3", "Option 4"],
};

const nonEditableWrapper = mount(QuestionCard, {
    props: {
        value: question,
        editable: false,
    },
});

const wrapper = mount(QuestionCard, {
    props: {
        value: question,
        editable: true,
    },
});

describe("Question Form", () => {
    it("Question Card renders question", () => {
        expect(wrapper.text()).toContain(question.question);
    });

    it("Question card renders question options", () => {
        const questionOptions = wrapper.findAll(".option");
        expect(questionOptions.length).toBe(question.options.length);

        questionOptions.forEach((option, index) => {
            expect(option.text()).toContain(question.options[index]);
        });
    });

    it("Question Card does not render edit and delete buttons if not editable", () => {
        expect(nonEditableWrapper.find(".edit-button").exists()).toBe(false);
        expect(nonEditableWrapper.find(".delete-button").exists()).toBe(false);
    });

    it("QuestionCard renders edit and delete buttons if editable", () => {
        expect(wrapper.find(".edit-button").exists()).toBe(true);
        expect(wrapper.find(".delete-button").exists()).toBe(true);
    });
});
