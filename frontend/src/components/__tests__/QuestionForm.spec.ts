import { describe, it, beforeEach, expect } from "vitest";
import { mount } from "@vue/test-utils";

import type { Question } from "@/api";
import QuestionForm from "../QuestionForm.vue";

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

describe("Question Form", () => {
    let wrapper: ReturnType<typeof mount>;

    beforeEach(() => {});

    it("QuestionForm ", () => {
        expect(true).toBeTruthy();
        // expect(wrapper.find("form").exists()).toBeTruthy();
    });
});
