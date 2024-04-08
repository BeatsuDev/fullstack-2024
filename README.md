# Kazoot

This is a monorepo for the back-end and front-end of the application
"Kazoot" created for the [NTNU course IDATT2105](https://www.ntnu.no/studier/emner/IDATT210) by
Eric Bieszczad-Stie, Sebastian Bugge and Arunan Gnanasekaran.

In this repo you will find:

`.github`:

- GitHub Actions workflows

`docs`:

- Documentation for the [back-end](./docs/backend.md) and [front-end](./docs/frontend.md)

`/backend`:

- Source code for back-end
- Unit tests for back-end

`/frontend`:

- Source code for front-end
- Unit tests for front-end
- End-to-end for front-end tests that are not reliant on back-end

## Get started

If you just want to test the application out. You may do so by using docker and
docker-compose. Once you have installed docker-compose, you can start the
application by running.

```bash
docker-compose up
```

The application will then be made available on `http://localhost:5173`. You may
test the application by registering an account, or using the existing dev user.
The email and password for this user is `test@example.org` and `password`.

To run the tests for the back-end, you can run the following command.

```bash
docker-compose up test
```

To run the tests for the front-end, you can run the following command.

```bash
cd frontend

npm install

npm run test:e2e

npm run test:unit
```

## Common mistakes:

If you get the error "Could not find ./docker-entrypoint.sh" when running `docker-compose up`
it means that the file format is DOS and not UNIX. Install the dos2unix commandline
tool with `winget install dos2unix`, restart cmd, and then run:

```bash
cd backend
dos2unix ./docker-entrypoint.sh
```
