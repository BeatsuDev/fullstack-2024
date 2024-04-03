import type { User } from "../../src/api";

describe("User login", () => {
    it("Successful login redirects to home page", () => {
        cy.visit("/login");
        cy.get("input#email").type("test@example.org");
        cy.get("input#password").type("password");

        cy.intercept("POST", "/user/session", {
            statusCode: 200,
            body: {
                id: "4b5047e6-af91-45d2-b9c2-e8e5028787fb",
                email: "test@example.org",
                name: "test",
            } as User,
        });

        cy.intercept("GET", "/quiz", {
            data: {
                id: "133a5fd5-216a-4b75-ae6a-fd2068eac185",
                title: "Balle",
                description: "Stein",
                difficulty: 10,
                creator: {
                    id: "1e79b33f-1a13-4182-b124-e78b662c8b41",
                    email: "test@example.org",
                    name: 'John "Mother Fucking" Doe',
                },
                createdAt: "2024-04-01T20:54:13.038+00:00",
                categories: [
                    {
                        id: "534a3c5a-2cd8-4fce-89c6-7eeb3f8b8598",
                        name: "Geography",
                        color: "#FFFF00",
                    },
                ],
            },
        });
        cy.get("button#login-button").click();

        // Check if routed to /explore page
        cy.url().should("include", "/explore");
    });
});
