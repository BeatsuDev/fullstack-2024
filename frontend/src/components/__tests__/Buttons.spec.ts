import { describe, it, expect } from "vitest";
import { mount } from "@vue/test-utils";

import PrimaryButton from "../buttons/PrimaryButton.vue";
import SecondaryButton from "../buttons/SecondaryButton.vue";

describe("Primary Button", () => {
    const primaryWrapper = mount(PrimaryButton, {
        slots: {
            default: "This is a test",
        },
    });

    const secondaryWrapper = mount(SecondaryButton, {
        slots: {
            default: "This is a test",
        },
    });

    it("Button renders with a slot", () => {
        expect(primaryWrapper.html()).toContain("This is a test");
        expect(secondaryWrapper.html()).toContain("This is a test");
    });

    it("Button component should contain a single button element as root element", () => {
        expect(
            primaryWrapper.element instanceof HTMLButtonElement
        ).toBeTruthy();

        expect(
            secondaryWrapper.element instanceof HTMLButtonElement
        ).toBeTruthy();
    });

    it("Button component should emit click event when clicked", async () => {
        await primaryWrapper.trigger("click");
        expect(primaryWrapper.emitted("click")).toBeTruthy();

        await secondaryWrapper.trigger("click");
        expect(secondaryWrapper.emitted("click")).toBeTruthy();
    });
});
