import { describe, expect, it } from "vitest";
import { mount } from "@vue/test-utils";

import ButtonComponent from "../ButtonComponent.vue";

describe("Button Component", () => {
    const primaryWrapper = mount(ButtonComponent, {
        props: {},
        slots: {
            default: "This is a test",
        },
    });

    it("Button renders with a slot", () => {
        expect(primaryWrapper.html()).toContain("This is a test");
    });

    it("small attribute", () => {
        const smallWrapper = mount(ButtonComponent, {
            props: {
                small: true,
            },
        });

        expect(smallWrapper.classes()).toContain("small");
    });

    it("large attribute", () => {
        const largeWrapper = mount(ButtonComponent, {
            props: {
                large: true,
            },
        });

        expect(largeWrapper.classes()).toContain("large");
    });

    it("rounded-sm attribute", () => {
        const roundedSmWrapper = mount(ButtonComponent, {
            props: {
                roundedSm: true,
            },
        });

        expect(roundedSmWrapper.classes()).toContain("rounded-sm");
    });

    it("rounded-lg attribute", () => {
        const roundedLgWrapper = mount(ButtonComponent, {
            props: {
                roundedLg: true,
            },
        });

        expect(roundedLgWrapper.classes()).toContain("rounded-lg");
    });

    it("rounded attribute", () => {
        const roundedWrapper = mount(ButtonComponent, {
            props: {
                rounded: true,
            },
        });

        expect(roundedWrapper.classes()).toContain("rounded");
    });

    it("filled attribute", () => {
        const filledWrapper = mount(ButtonComponent, {
            props: {
                filled: true,
            },
        });

        expect(filledWrapper.classes()).toContain("filled");
    });
});
