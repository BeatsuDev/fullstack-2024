import { fileURLToPath } from "node:url";
import { mergeConfig, defineConfig, configDefaults } from "vitest/config";
import viteConfig from "./vite.config";

export default mergeConfig(
    viteConfig,
    defineConfig({
        test: {
            environment: "jsdom",
            exclude: [...configDefaults.exclude, "e2e/*"],
            root: fileURLToPath(new URL("./", import.meta.url)),
            coverage: {
                // Views are tested by cypress and dont make sense to unit test
                exclude: ["**/*.ts", "src/views/**/*.vue", "**/__tests__/**/*"],
            },
        },
    })
);
