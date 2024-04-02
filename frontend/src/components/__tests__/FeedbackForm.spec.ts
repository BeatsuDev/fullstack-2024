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
        shallow: false,
    });

    it("Feedback renders submit new feedback text", () => {
        expect(primaryWrapper.html()).toContain("Submit new Feedback");
    });

    it("Feedback renders feedback content", () => {
        primaryWrapper.element.querySelector("input")!.value = "test feedback";
    });

    it("Submitting form emits submit event", async () => {
        await primaryWrapper.find("form").trigger("submit");
        await flushPromises();
        console.log(primaryWrapper);
        expect(primaryWrapper.emitted("submit")).toBeTruthy();
    });
});
