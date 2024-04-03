import { describe, expect, it } from "vitest";
import { mount } from "@vue/test-utils";
import type { Quiz, Category, Question, Feedback } from "@/api";

import QuizHero from "../QuizHero.vue";

const testQuiz = {
    id: "1",
    title: "Test Title",
    categories: [] as Category[],
    description: "Test Description",
    questions: [] as Question[],
    createdAt: new Date(),
    creator: {
        id: "1",
        name: "Test User",
        email: "test@test.com",
    },
    feedbacks: [] as Feedback[],
} as Quiz;

describe("QuizHero", () => {
    const primaryWrapper = mount(QuizHero, {
        props: {
            playable: true,
            editable: true,
            quiz: testQuiz,
        },
    });

    const secondaryWrapper = mount(QuizHero, {
        props: {
            playable: false,
            quiz: testQuiz,
        },
    });

    it("Renders play and multiplayer buttons", () => {
        expect(primaryWrapper.find("#play-button").exists()).toBe(true);
        expect(primaryWrapper.find("#multiplayer-button").exists()).toBe(true);
    });

    it("Renders edit button if editable", () => {
        expect(primaryWrapper.find("#edit-button").exists()).toBe(true);
    });

    it("Does not render edit button if editable is not set", () => {
        expect(secondaryWrapper.find("#edit-button").exists()).toBe(false);
    });

    it("Does not render play and multiplayer buttons if playable is false", () => {
        expect(secondaryWrapper.find("#play-button").exists()).toBe(false);
        expect(secondaryWrapper.find("#multiplayer-button").exists()).toBe(
            false
        );
    });

    it("Renders quiz title", () => {
        expect(primaryWrapper.html()).toContain("Test Title");
    });

    it("Renders quiz description", () => {
        expect(primaryWrapper.html()).toContain("Test Description");
    });

    it("Renders quiz creator", () => {
        expect(primaryWrapper.html()).toContain("Test User");
    });

    // it("Clicking multiplayer game pushes to quiz-lobby route", async () => {
    //     await primaryWrapper.find("#multiplayer-button").trigger("click");
    //     await flushPromises();
    //     expect(currentRoute).toEqual({
    //         name: "quiz-lobby",
    //         params: { id: "1" },
    //     });
    // });
});
