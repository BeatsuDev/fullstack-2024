import { describe, expect, it } from "vitest";
import { mount } from "@vue/test-utils";

import RevisionCard from "../RevisionCard.vue";
import type { Quiz, Category, Question, Feedback, Revision } from "@/api";

const date = new Date();

const testQuiz = {
    id: "1",
    title: "Test Title",
    categories: [] as Category[],
    description: "Test Description",
    questions: [] as Question[],
    createdAt: date,
    creator: {
        id: "1",
        name: "Test User",
        email: "test@test.com",
    },
    feedbacks: [] as Feedback[],
} as Quiz;

const testRevision = {
    revisionId: "1",
    quiz: testQuiz,
} as Revision;

describe("RevisionCard", () => {
    const wrapper = mount(RevisionCard, {
        props: {
            value: testRevision,
        },
    });

    it("Renders correctly revision ID correctly", () => {
        expect(wrapper.html()).toContain("1");
    });

    it("Renders quiz title", () => {
        expect(wrapper.html()).toContain("Test Title");
    });

    it("Renders creation time correctly", () => {
        expect(wrapper.html()).toContain(
            `${date.toDateString()} ${date.toLocaleTimeString()}`
        );
    });
});
