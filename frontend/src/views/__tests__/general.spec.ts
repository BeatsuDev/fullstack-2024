import { describe, it, expect } from "vitest";
import { mount } from "@vue/test-utils";

describe("Shared tests for views", async () => {
    const views = Object.values((await import("../")).default);

    const defaultMockConfig = {
        global: {
            mocks: {
                $t: (msg: string) => msg,
            },
            stubs: {
                RouterLink: { template: "<a />" },
                RouterView: { template: "<div />" },
            },
        },
    };

    it("All views should have a <main> element as root element", () => {
        for (const view of views) {
            console.log(
                "Checking if ",
                view.__file,
                "has a <main> element as root element."
            );
            const wrapper = mount(view, defaultMockConfig);
            expect(wrapper.find("main").exists()).toBe(true);
            console.log("Yes, it has a <main> element as root element.");
        }
    });
});
