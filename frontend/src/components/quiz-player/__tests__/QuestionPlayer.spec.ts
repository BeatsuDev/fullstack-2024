import { describe, it, expect } from "vitest";
import { mount } from "@vue/test-utils";

import QuestionPlayer from "../QuestionPlayer.vue";

describe("Question player", () => {
    const multipleChoiceWrapper = mount(QuestionPlayer, {
        props: {
            question: {
                id: "1",
                question: "What is the capital of France?",
                image: {
                    id: "1",
                    path: "https://example.com/image.jpg",
                },
                options: ["Paris", "London", "Berlin", "Madrid"],
            },
            countdown: 20,
        },
    });

    const trueFalseWrapper = mount(QuestionPlayer, {
        props: {
            question: {
                id: "2",
                question: "Is the capital of France Paris?",
                image: {
                    id: "2",
                    path: "https://example.com/image.jpg",
                },
                options: ["true", "false"],
            },
            countdown: 10,
        },
    });

    const openWrapper = mount(QuestionPlayer, {
        props: {
            question: {
                id: "3",
                question: "What is the capital of France?",
                image: {
                    id: "3",
                    path: "https://example.com/image.jpg",
                },
                options: [],
            },
            countdown: 5,
        },
    });

    const noImageWrapper = mount(QuestionPlayer, {
        props: {
            question: {
                id: "4",
                question: "What is the capital of France?",
                image: null,
                options: [],
            },
            countdown: 3,
        },
    });

    it("Question player renders question", () => {
        expect(multipleChoiceWrapper.html()).toContain(
            "What is the capital of France?"
        );
    });

    it("Multiple choice question player renders options", () => {
        expect(multipleChoiceWrapper.findAll("button").length).toBe(4);
    });

    it("True/false question player renders options", () => {
        expect(trueFalseWrapper.findAll("button").length).toBe(2);
    });

    it("Open question player renders input field", () => {
        expect(openWrapper.find("input").exists()).toBe(true);
    });

    it("Question player renders image", () => {
        expect(multipleChoiceWrapper.find("img").exists()).toBe(true);
    });

    it("Question player does not render image", () => {
        expect(noImageWrapper.find("img").exists()).toBe(false);
    });

    it("Question player renders countdown", () => {
        expect(multipleChoiceWrapper.html()).toContain("20");
    });

    it("Emits answer event on option click", async () => {
        await multipleChoiceWrapper.findAll("button")[0].trigger("click");
        expect(multipleChoiceWrapper.emitted()).toHaveProperty(
            "answerSelected"
        );
    });
});
