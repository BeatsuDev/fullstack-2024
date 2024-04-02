import { describe, expect, it } from "vitest";
import { mount } from "@vue/test-utils";

import type { QuizOverview, User } from "@/api";
import QuizOverviewCard from "../QuizOverviewCard.vue";

const date = new Date();

const exampleQuiz = {
    id: "1",
    title: "Test Title",
    description: "Test Description",
    categories: [],
    creator: {
        id: "10",
        name: "Test User",
        email: "test@test.com",
    } as User,
    createdAt: date,
} as QuizOverview;

describe("QuizOverviewCard", () => {
    const primaryWrapper = mount(QuizOverviewCard, {
        props: {
            quiz: exampleQuiz,
        },
    });

    it("QuizOverviewCard", () => {
        expect(primaryWrapper.html()).toContain("Test Title");
        expect(primaryWrapper.html()).toContain("Test Description");
        expect(primaryWrapper.html()).toContain("Test User");
    });
});
