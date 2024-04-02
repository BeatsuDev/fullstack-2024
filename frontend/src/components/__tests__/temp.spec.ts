// Copy and paste the following code to the file to test the component

import { describe, it, expect } from "vitest";
import { mount } from "@vue/test-utils";

import AlertComponent from "../AlertComponent.vue";

describe("", () => {
    const primaryWrapper = mount(AlertComponent, {
        props: {},
        slots: {
            default: "This is a test",
        },
    });

    it("Button renders with a slot", () => {
        expect(primaryWrapper.html()).toContain("This is a test");
    });
});
