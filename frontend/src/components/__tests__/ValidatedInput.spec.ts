import { describe, it, expect, vi, beforeAll, beforeEach } from "vitest";
import { mount, flushPromises, type VueWrapper } from "@vue/test-utils";

import ValidatedInput from "../ValidatedInput.vue";
import type { ErrorObject } from "@vuelidate/core";

import { reactive } from "vue";

describe("ValidatedInput", () => {
    let mockValidator: {
        $validate: () => Promise<boolean>;
        $reset: () => void;
        $errors: ErrorObject[];
    } & any;

    let wrapper: VueWrapper<TODO>;

    beforeAll(() => {
        mockValidator = reactive({
            $validate: vi.fn(),
            $reset: vi.fn(),
            $errors: [],
        });
    });

    beforeEach(() => {
        wrapper = mount(ValidatedInput, {
            props: {
                id: "test",
                validator: mockValidator,
            },
        });
    });

    it("renders an input element", () => {
        expect(wrapper.find("input").exists()).toBe(true);
    });

    it("renders an error message when there is an error", async () => {
        mockValidator.$errors = [{ $message: "Test error" }];
        await flushPromises();
        expect(wrapper.find(".error-message").exists()).toBe(true);
    });

    it("validates on focusout", async () => {
        await wrapper.find("input").trigger("focusout");
        expect(mockValidator.$validate).toHaveBeenCalled();
    });

    it("resets the validation on input", async () => {
        await wrapper.find("input").trigger("input");
        expect(mockValidator.$reset).toHaveBeenCalled();
    });

    it("v-model connects to the input", async () => {
        // Mount with modelValue functionality mocked
        const wrapper = mount(ValidatedInput, {
            props: {
                modelValue: "",
                "onUpdate:modelValue": () =>
                    wrapper.setProps({ modelValue: "test" }),
                id: "test",
                validator: mockValidator,
            },
        });
        await wrapper.find("input").setValue("test");
        expect(wrapper.props("modelValue")).toBe("test");
    });
});
