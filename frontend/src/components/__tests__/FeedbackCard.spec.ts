import type { Feedback } from "@/api";
import { describe, expect, it } from "vitest";
import { mount } from "@vue/test-utils";

import FeedbackCard from "../FeedbackCard.vue";

describe("Feedback Card", () => {
    const date = new Date();
    const primaryWrapper = mount(FeedbackCard, {
        props: {
            feedback: {
                id: 1,
                quizId: "1",
                username: "username",
                feedback: "test feedback",
                createdAt: date,
            } as Feedback,
        },
    });

    it("Feedback card renders feedback content", () => {
        expect(primaryWrapper.html()).toContain("username");
        expect(primaryWrapper.html()).toContain("test feedback");
        expect(primaryWrapper.html()).toContain(date.toDateString());
    });
});
