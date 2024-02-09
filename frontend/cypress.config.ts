import { defineConfig } from "cypress";
import coverageTask from "@cypress/code-coverage/task";

export default defineConfig({
    e2e: {
        specPattern: "cypress/e2e/**/*.{cy,spec}.{js,jsx,ts,tsx}",
        baseUrl: "http://localhost:4173",
        setupNodeEvents(on, config) {
            coverageTask(on, config);
            // include any other plugin code...

            // It's IMPORTANT to return the config object
            // with any changed environment variables
            return config;
        },
    },
});
