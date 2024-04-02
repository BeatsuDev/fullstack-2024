import { describe, expect, it } from "vitest";
import { mount } from "@vue/test-utils";

import AlertComponent from "../AlertComponent.vue";

describe("Alert Component", () => {
    const primaryWrapper = mount(AlertComponent, {
        props: {
            type: "danger",
        },
        slots: {
            default: "This is a test",
        },
    });

    it("Button renders with a slot", () => {
        expect(primaryWrapper.html()).toContain("This is a test");
    });

    it("Button has 3 types", () => {
        const infoWrapper = mount(AlertComponent, {
            props: {
                type: "info",
            },
        });

        const warningWrapper = mount(AlertComponent, {
            props: {
                type: "warning",
            },
        });

        const dangerWrapper = mount(AlertComponent, {
            props: {
                type: "danger",
            },
        });

        expect(infoWrapper.classes()).toContain("alert-info");
        expect(warningWrapper.classes()).toContain("alert-warning");
        expect(dangerWrapper.classes()).toContain("alert-danger");
    });
});
