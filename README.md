# Kazoot

This is a monorepo for the back-end and front-end of the application
"Kazoot" created for the [NTNU course IDATT2105](https://www.ntnu.no/studier/emner/IDATT210).

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
