import { describe, it, expect } from "vitest";
import { flushPromises, mount } from "@vue/test-utils";

import type { FeedbackCreate } from "@/api";
import FeedbackForm from "../FeedbackForm.vue";

describe("Feedback Form", () => {
    const primaryWrapper = mount(FeedbackForm, {
        props: {
            value: {
                feedback: "test feedback",
            } as FeedbackCreate,
        },
    });

    it("Feedback renders submit new feedback text", () => {
        expect(primaryWrapper.html()).toContain("Submit new Feedback");
    });

    it("Feedback renders feedback content", () => {
        primaryWrapper.element.querySelector("input")!.value = "test feedback";
    });

    it("Submitting form emits submit event", async () => {
        await primaryWrapper.find("#submit-feedback-button").trigger("click");
        await flushPromises();
        expect(primaryWrapper.emitted("submit")).toBeTruthy();
    });

    it("Submitting a form without feedback content does not emit submit event", async () => {
        // Mount with no value prop
        const primaryWrapper = mount(FeedbackForm);
        await primaryWrapper.find("#submit-feedback-button").trigger("click");
        await flushPromises();
        expect(primaryWrapper.emitted("submit")).toBeFalsy();
    });

    it("Changing value prop updates feedback content", async () => {
        const newFeedback = "new feedback";
        await primaryWrapper.setProps({
            value: {
                feedback: newFeedback,
            } as FeedbackCreate,
        });
        expect(primaryWrapper.element.querySelector("input")!.value).toBe(
            newFeedback
        );
    });
});
