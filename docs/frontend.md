# Front-end documentation

The tech stack for the front-end is Vue ^3.4 + TypeScript using the composition api
and [setup syntax](https://vuejs.org/api/sfc-script-setup.html). Unit tests use vitest
and end to end tests use cypress. No component libraries or CSS processors were used.

Vue 3.4 or higher is required because of the use of `defineModel()` which was introduced
in Vue 3.3 and became stable in version 3.4.

## Build and preview the project

```bash
cd frontend
npm run build
npm run preview -- --port 8080
```

Other commands:

```bash
npm run test:unit  # Runs unit tests in watch mode
npm run test:unit -- --run  # Runs unit tests once
npm run test:coverage  # Coverage report using vitests
npm run test:e2e  # Runs cypress e2e tests once

npm run lint  # Fix linting errors
npm run format  # Fix formatting errors

# Only checks, no automatic fixes
npm run lint:check
npm run format:check
```

## Running with back-end

Run the server as explain in the [back-end README](/backend/README.md), and take note
of the port number the back-end is listening on.

> More info to come...
