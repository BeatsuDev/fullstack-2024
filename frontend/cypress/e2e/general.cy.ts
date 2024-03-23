describe("Locale", () => {
    it("default locale is norwegian", () => {
        cy.visit("/");

        cy.get("#contact-router-link").should("contain", "Kontakt");
        cy.get("#login-router-link").should("contain", "Logg inn");
    });

    it("change locale to english", () => {
        cy.visit("/");

        cy.get("#locale-selector").select("en");

        cy.get("#contact-router-link").should("contain", "Contact");
        cy.get("#login-router-link").should("contain", "Log in");
    });
});
