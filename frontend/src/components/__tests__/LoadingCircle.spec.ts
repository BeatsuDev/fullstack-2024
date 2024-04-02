import { describe, expect, it } from "vitest";
import { mount } from "@vue/test-utils";

import LoadingCircle from "../LoadingCircle.vue";

describe("Loading Circle", () => {
    it("Loading Button renders if loading", () => {
        const primaryWrapper = mount(LoadingCircle, {
            props: {
                loading: true,
            },
        });
        expect(primaryWrapper.find(".loader").exists()).toBeTruthy();
    });

    it("Loading Button does not render if not loading", () => {
        const secondaryWrapper = mount(LoadingCircle, {
            props: {
                loading: false,
            },
        });
        expect(secondaryWrapper.find(".loader").exists()).toBeFalsy();
    });
});
