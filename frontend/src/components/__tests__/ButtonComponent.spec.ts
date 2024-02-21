import { describe, it, expect } from "vitest";
import { mount } from "@vue/test-utils";

import ButtonComponent from "../ButtonComponent.vue";

describe("ValidatedInput", () => {
    const wrapper = mount(ButtonComponent, {
        slots: {
            default: "This is a test",
        },
    });

    it("Button renders with a slot", () => {
        expect(wrapper.html()).toContain("This is a test");
    });

    it("Button component should contain a single button element as root element", () => {
        expect(wrapper.element instanceof HTMLButtonElement).toBeTruthy();
    });
});
