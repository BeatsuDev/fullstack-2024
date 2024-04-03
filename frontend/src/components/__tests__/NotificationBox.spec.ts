// Copy and paste the following code to the file to test the component

import { describe, expect, it } from "vitest";
import { mount } from "@vue/test-utils";

import NotificationBox from "../NotificationBox.vue";
import SuccessIcon from "../../assets/icons/SuccessIcon.vue";
import InfoIcon from "../../assets/icons/InfoIcon.vue";
import WarningIcon from "../../assets/icons/WarningIcon.vue";
import ErrorIcon from "../../assets/icons/ErrorIcon.vue";

describe("", () => {
    const successWrapper = mount(NotificationBox, {
        props: {
            id: 1,
            message: "This is a test",
            type: "success",
        },
    });

    const infoWrapper = mount(NotificationBox, {
        props: {
            id: 2,
            message: "This is a test",
            type: "info",
        },
    });

    const warningWrapper = mount(NotificationBox, {
        props: {
            id: 3,
            message: "This is a test",
            type: "warning",
        },
    });

    const errorWrapper = mount(NotificationBox, {
        props: {
            id: 4,
            message: "This is a test",
            type: "error",
        },
    });

    it("Notification box has close button", () => {
        expect(successWrapper.find("button").exists()).toBe(true);
        expect(successWrapper.find("#close-notification").exists()).toBe(true);
    });

    it("Success wrapper uses SuccessIcon", () => {
        expect(successWrapper.findComponent(SuccessIcon).exists()).toBe(true);
    });

    it("Info wrapper uses InfoIcon", () => {
        expect(infoWrapper.findComponent(InfoIcon).exists()).toBe(true);
    });

    it("Warning wrapper uses WarningIcon", () => {
        expect(warningWrapper.findComponent(WarningIcon).exists()).toBe(true);
    });

    it("Error wrapper uses ErrorIcon", () => {
        expect(errorWrapper.findComponent(ErrorIcon).exists()).toBe(true);
    });

    it("Clicking close button emits close event", async () => {
        await successWrapper.find("#close-notification").trigger("click");
        expect(successWrapper.emitted()).toHaveProperty("close");
    });
});
