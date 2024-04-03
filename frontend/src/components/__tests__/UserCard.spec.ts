import { describe, expect, it } from "vitest";
import { mount } from "@vue/test-utils";

import UserCard from "../UserCard.vue";

describe("User Card", () => {
    const wrapper = mount(UserCard, {
        props: {
            value: {
                id: "1",
                name: "test",
                email: "test@test.com",
            },
            editable: true,
        },
    });

    const nonEditableWrapper = mount(UserCard, {
        props: {
            value: {
                id: "1",
                name: "test",
                email: "test@test.com",
            },
            editable: false,
        },
    });

    it("Shows delete button if editable", () => {
        expect(wrapper.html()).toContain("Delete");
    });

    it("Does not show delete button if not editable", async () => {
        expect(nonEditableWrapper.html()).not.toContain("Delete");
    });

    it("Shows name and email", () => {
        expect(wrapper.html()).toContain("test");
        expect(wrapper.html()).toContain("test@test.com");
    });

    it("Emits delete event when delete button is clicked", async () => {
        await wrapper.find("#delete-button").trigger("click");
        expect(wrapper.emitted("delete")).toBeTruthy();
    });
});
