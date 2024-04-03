import { describe, expect, it } from "vitest";
import { mount } from "@vue/test-utils";

import UserResultCard from "../UserResultCard.vue";

describe("User Result Card", () => {
    const wrapper = mount(UserResultCard, {
        props: {
            value: {
                id: "1",
                name: "test",
                avatar: "avatar.png",
                score: 0,
            },
            results: true,
        },
    });

    it("Shows score if results is true", () => {
        expect(wrapper.html()).toContain("Score ");
    });

    it("Does not show score if results is false", async () => {
        await wrapper.setProps({ results: false });
        expect(wrapper.html()).not.toContain("Score ");
    });
});
