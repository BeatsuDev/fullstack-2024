describe("Quiz", () => {
    beforeEach(() => {
        cy.intercept("GET", "/user/session", { fixture: "User.json" });
        cy.intercept("GET", "/quiz?*", { fixture: "QuizOverviewList.json" });
        cy.intercept("GET", "/quiz/*", { fixture: "Quiz.json" });
        cy.intercept("GET", "/category", { fixture: "CategoryList.json" });
        cy.intercept("POST", "/quiz", { fixture: "Quiz.json" });
        cy.intercept("GET", "/quiz/*/feedback", {
            fixture: "FeedbackList.json",
        });
        cy.intercept("POST", "/quiz/*/attempt", {
            fixture: "QuizAttempt.json",
        });
        cy.intercept("POST", "/quiz/*/attempt/*");
    });

    it("Quiz overview", () => {
        cy.visit("/explore");
        cy.get("a.quiz-overview-card").first().click();
        cy.url().should("include", "/quizzes");
    });

    it("Play quiz", { scrollBehavior: false }, () => {
        cy.visit("/explore");
        cy.get("a.quiz-overview-card").first().click();
        cy.get("button#play-button").click();
    });

    it("Create quiz", () => {
        cy.visit("/create");
        cy.get("input#title").type("Cypress Quiz");
        cy.get("input#description").type("This is a quiz created by Cypress");
        cy.get(".category-filter-labels label").first().click();
        cy.get("button#quiz-submit-button").click();

        cy.url().should("include", "/quizzes");
    });
});
