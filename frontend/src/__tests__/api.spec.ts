import { describe, it, expect, vi } from "vitest";
import {
    UserSessionApi,
    UserApi,
    RevisionApi,
    QuizApi,
    QuestionApi,
    FeedbackApi,
    CollaboratorApi,
    CategoryApi,
    AttemptApi,
    type User,
} from "@/api";

describe("Swagger APIs", () => {
    it("User Session API exists", async () => {
        expect(UserSessionApi).toBeDefined();

        vi.mock("axios", () => {
            return {
                default: {
                    request: () =>
                        Promise.resolve({
                            data: {
                                id: "244e4567-4118-4f83-8544-28ebf2cde9a3",
                                name: "John Doe",
                                email: "john.doe@mail.com",
                            } as User,
                        }),
                },
            };
        });

        const api = new UserSessionApi();

        // Logged in user
        const response1 = await api.loggedInUser();
        expect(response1).toBeDefined();
        const user1 = response1.data;
        expect(user1.id).toBe("244e4567-4118-4f83-8544-28ebf2cde9a3");
        expect(user1.name).toBe("John Doe");
        expect(user1.email).toBe("john.doe@mail.com");

        const response2 = await api.login({
            email: "john.doe@mail.com",
            password: "password",
        });
        expect(response2).toBeDefined();
        expect(response2.data).toBeDefined();
        const user2 = response2.data;
        expect(user2.id).toBe("244e4567-4118-4f83-8544-28ebf2cde9a3");
        expect(user2.name).toBe("John Doe");
        expect(user2.email).toBe("john.doe@mail.com");

        const response3 = await api.logout();
        expect(response3).toBeDefined();
        expect(response3.data).toBeDefined();

        const response4 = await api.refreshToken();
        expect(response4).toBeDefined();
        expect(response4.data).toBeDefined();
    });

    it("User API exists", async () => {
        expect(UserApi).toBeDefined();

        const api = new UserApi();

        const response1 = await api.deleteUser(
            "244e4567-4118-4f83-8544-28ebf2cde9a3"
        );
        expect(response1).toBeDefined();
    });

    it("Revision API exists", () => {
        expect(RevisionApi).toBeDefined();
    });

    it("Quiz API exists", () => {
        expect(QuizApi).toBeDefined();
    });

    it("Question API exists", () => {
        expect(QuestionApi).toBeDefined();
    });

    it("Feedback API exists", () => {
        expect(FeedbackApi).toBeDefined();
    });

    it("Collaborator API exists", () => {
        expect(CollaboratorApi).toBeDefined();
    });

    it("Category API exists", () => {
        expect(CategoryApi).toBeDefined();
    });

    it("Attempt API exists", () => {
        expect(AttemptApi).toBeDefined();
    });
});
