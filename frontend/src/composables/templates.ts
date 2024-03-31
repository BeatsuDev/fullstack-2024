export const templates: {
    difficulty: number;
    questions: ({ question: string; answer: string; quizId: number; options: string[] } | {
        question: string;
        answer: string;
        quizId: number;
        options: string[]
    })[];
    description: string;
    title: string
}[] = [
    {
        title: "Math",
        description: "This is a template",
        difficulty: 5,
        questions: [
            {
                quizId: 0,
                question: "What is 1 + 1?",
                options: ["1", "2", "3", "4"],
                answer: "2",
            },
            {
                quizId: 0,
                question: "What is 2 + 2?",
                options: ["1", "2", "3", "4"],
                answer: "4",
            },
        ]
    }
];
