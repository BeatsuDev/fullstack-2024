import { describe, it, expect } from "vitest";
import { mount } from "@vue/test-utils";

import DialogModal from "../DialogModal.vue";

describe("Primary Button", () => {
    it("Dialog modal renders correctly", () => {
        const wrapper = mount(DialogModal, {
            props: {
                modelValue: true, // Show the dialog
                title: "Title",
                message: "Message",
                confirmText: "Confirm",
                cancelText: "Cancel",
            },
        });

        console.log(wrapper.html());
        expect(wrapper.html()).contains("Title");
        expect(wrapper.html()).contains("Message");
        expect(wrapper.html()).contains("Confirm");
        expect(wrapper.html()).contains("Cancel");
    });

    it("Clicking on confirm emits confirm event", async () => {
        const wrapper = mount(DialogModal, {
            props: {
                modelValue: true, // Show the dialog
                title: "Title",
                message: "Message",
                confirmText: "Confirm",
                cancelText: "Cancel",
            },
        });

        await wrapper.find("button#confirm-button").trigger("click");
        expect(wrapper.emitted("confirm")).toBeTruthy();
    });

    it("Clicking on cancel emits cancel event", async () => {
        const wrapper = mount(DialogModal, {
            props: {
                modelValue: true, // Show the dialog
                title: "Title",
                message: "Message",
                confirmText: "Confirm",
                cancelText: "Cancel",
            },
        });

        await wrapper.find("button#cancel-button").trigger("click");
        expect(wrapper.emitted("cancel")).toBeTruthy();
    });

    it("Clicking on confirm closes the dialog", async () => {
        const wrapper = mount(DialogModal, {
            props: {
                modelValue: true, // Show the dialog
                title: "Title",
                message: "Message",
                confirmText: "Confirm",
                cancelText: "Cancel",
            },
        });

        await wrapper.find("button#confirm-button").trigger("click");
        expect(wrapper.emitted("update:modelValue")).toBeTruthy();
    });

    it("Clicking on cancel closes the dialog", async () => {
        const wrapper = mount(DialogModal, {
            props: {
                modelValue: true, // Show the dialog
                title: "Title",
                message: "Message",
                confirmText: "Confirm",
                cancelText: "Cancel",
            },
        });

        await wrapper.find("button#cancel-button").trigger("click");
        expect(wrapper.emitted("update:modelValue")).toBeTruthy();
    });
});
