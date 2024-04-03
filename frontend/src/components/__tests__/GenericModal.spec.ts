// Copy and paste the following code to the file to test the component

import { describe, expect, it } from "vitest";
import { mount } from "@vue/test-utils";

import GenericModal from "../GenericModal.vue";

describe("Generic Modal", () => {
    const primaryWrapper = mount(GenericModal, {
        props: {
            title: "Test title",
            modelValue: true,
        },
        slots: {
            default: "This is a test slot",
        },
    });

    it("Modal renders with a slot", () => {
        expect(primaryWrapper.html()).toContain("This is a test slot");
    });

    it("Modal renders with a title", () => {
        expect(primaryWrapper.html()).toContain("Test title");
    });

    it("ModelValue changes to false if close button is clicked", async () => {
        await primaryWrapper.find(".close-button").trigger("click");
        expect(primaryWrapper.emitted("update:modelValue")).toBeTruthy();

        // @ts-ignore (orker ikke type shenanigans)
        expect(primaryWrapper.vm.active).toBe(false);
    });
});
