import { mount } from "@vue/test-utils";
import ErrorIcon from "../ErrorIcon.vue";
import FilterIcon from "../FilterIcon.vue";
import InfoIcon from "../InfoIcon.vue";
import SuccessIcon from "../SuccessIcon.vue";
import WarningIcon from "../WarningIcon.vue";

import AddIcon from "../navbar/AddIcon.vue";
import FolderIcon from "../navbar/FolderIcon.vue";
import ProfileIcon from "../navbar/ProfileIcon.vue";
import SearchIcon from "../navbar/SearchIcon.vue";

import { describe, it, expect } from "vitest";

describe("Icons", () => {
    const icons = [
        mount(ErrorIcon),
        mount(FilterIcon),
        mount(InfoIcon),
        mount(SuccessIcon),
        mount(WarningIcon),
        mount(AddIcon),
        mount(FolderIcon),
        mount(ProfileIcon),
        mount(SearchIcon),
    ];

    it("Icons should have svg root element", () => {
        icons.forEach((icon) => {
            expect(icon.element.tagName).toEqual("svg");
        });
    });

    it("Icons should have fill='currentColor'", () => {
        icons.forEach((icon) => {
            icon.findAll("path").forEach((path) => {
                const fillAttribute = path.attributes("fill");
                const strokeAttribute = path.attributes("stroke");

                if (fillAttribute) {
                    expect(fillAttribute).toEqual("currentColor");
                } else {
                    expect(strokeAttribute).toEqual("currentColor");
                }
            });
        });
    });
});
