export const templates: {
    difficulty: number;
    questions: {
        question: string;
        answer: string;
        options: string[];
    }[];
    description: string;
    categories: readonly [];
    title: string;
}[] = [
    {
        title: "Math",
        description: "This is a template",
        difficulty: 5,
        questions: [
            {
                question: "What is 1 + 1?",
                options: ["1", "2", "3", "4"],
                answer: "2",
            },
            {
                question: "What is 2 + 2?",
                options: ["1", "2", "3", "4"],
                answer: "4",
            },
        ],
        categories: [],
    },
    {
        title: "Norsk",
        description: "Norsk template kvizz",
        difficulty: 5,
        questions: [
            {
                question: "Hvordan staver man katt?",
                options: [],
                answer: "katt",
            },
            {
                question: "Hvem er Ivar Åsen?",
                options: [
                    "Norsk dikter",
                    "Norsk forfatter",
                    "Norsk maler",
                    "Norsk skuespiller",
                ],
                answer: "Norsk dikter",
            },
        ],
        categories: [],
    },
];
