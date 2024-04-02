import { describe, it, expect, vi } from "vitest";
import { flushPromises, mount } from "@vue/test-utils";

import type { AxiosResponse } from "axios";
import type { Category } from "@/api";
import FilterOptions from "../FilterOptions.vue";

vi.mock("axios");

// Mock axios
const mockCategories = {
    data: [
        {
            id: "1",
            name: "Test",
            color: "#000000",
        },
        {
            id: "2",
            name: "Test2",
            color: "#FFFFFF",
        },
    ],
} as AxiosResponse<Category[]>;

describe("Filter Options", () => {
    const primaryWrapper: ReturnType<typeof mount> = mount(FilterOptions, {
        props: {
            modelValue: {
                selectedCategories: [
                    {
                        id: "1",
                        name: "Test",
                        color: "#000000",
                    },
                ],
                minDifficulty: 1,
                maxDifficulty: 5,
            } as {
                selectedCategories: Category[];
                minDifficulty: number;
                maxDifficulty: number;
            },
            "onUpdate:modelValue": (e: any) =>
                primaryWrapper.setProps({ modelValue: e }),
        },
    });

    it("FilterOptions renders legends", () => {
        expect(primaryWrapper.html()).toContain("Categories");
        expect(primaryWrapper.html()).toContain("Difficulty");
    });

    it("FilterOptions renders difficulty ranges", () => {
        expect(primaryWrapper.html()).toContain("Min (1)");
        expect(primaryWrapper.html()).toContain("Max (5)");
    });
});
