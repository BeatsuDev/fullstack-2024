import type { User } from "../../src/api";

describe("User login", () => {
    it("Successful login redirects to home page", () => {
        cy.intercept("POST", "/user", {
            statusCode: 201,
            body: {
                id: "4b5047e6-af91-45d2-b9c2-e8e5028787fb",
                name: "test",
                email: "test@test.com",
            } as User,
        });
    });
});

describe("User register", () => {});
