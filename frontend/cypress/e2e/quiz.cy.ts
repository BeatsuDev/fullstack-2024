describe("Quiz", () => {
    beforeEach(() => {
        cy.intercept("GET", "/user/session", { fixture: "User.json" });
        cy.intercept("GET", "/quiz?*", { fixture: "QuizOverviewList.json" });
        cy.intercept("GET", "/quiz/*", { fixture: "Quiz.json" });
        cy.intercept("GET", "/quiz/*/feedback", {
            fixture: "FeedbackList.json",
        });
    });

    it("Quiz overview", () => {
        cy.visit("/explore");
        cy.get("a.quiz-overview-card").first().click();
        cy.url().should("include", "/quizzes");
    });
});
