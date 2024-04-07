describe("User login", () => {
    it("Successful login redirects to home page", () => {
        cy.visit("/login");
        cy.get("input#email").type("cypress@test.com");
        cy.get("input#password").type("password");

        cy.intercept("POST", "/user/session", { fixture: "User.json" }).then(
            () => {
                cy.intercept("GET", "/user/session", { fixture: "User.json" });
            }
        );
        cy.intercept("GET", "/quiz?*", { fixture: "QuizOverviewList.json" });

        cy.get("button#login-button").click();

        // Check if routed to /explore page
        cy.url().should("include", "/explore");
    });

    it("Register new user", () => {
        cy.visit("/register");
        cy.get("input#name").type("Cypress Test");
        cy.get("input#email").type("cypress@test.com");
        cy.get("input#password").type("password");
        cy.get("input#repeat-password").type("password");

        cy.intercept("POST", "/user", { fixture: "User.json" });
        cy.intercept("GET", "/quiz?*", { fixture: "QuizOverviewList.json" });

        cy.get("button#register-button").click();
    });

    it("Edit user profile", () => {
        cy.intercept("GET", "/user/session", { fixture: "User.json" });
        cy.intercept("PUT", "/user", { fixture: "User.json" });

        cy.visit("/profile");
        cy.get("input#name").clear();
        cy.get("input#name").type("Cypress Test");

        cy.get("input#email").clear();
        cy.get("input#email").type("changed@email.com");
        cy.get("button#change-values-button").click();

        cy.contains("Profile updated successfully");
    });
});
