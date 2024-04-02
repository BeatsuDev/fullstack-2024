import { describe, expect, it } from "vitest";
import { mount } from "@vue/test-utils";

import ButtonComponent from "../ButtonComponent.vue";

describe("Primary Button", () => {
    const primaryWrapper = mount(ButtonComponent, {
        slots: {
            default: "This is a test",
        },
    });

    it("Button renders with a slot", () => {
        expect(primaryWrapper.html()).toContain("This is a test");
    });

    it("Button component should contain a single button element as root element", () => {
        expect(
            primaryWrapper.element instanceof HTMLButtonElement
        ).toBeTruthy();
    });

    it("Button component should emit click event when clicked", async () => {
        await primaryWrapper.trigger("click");
        expect(primaryWrapper.emitted("click")).toBeTruthy();
    });
});
