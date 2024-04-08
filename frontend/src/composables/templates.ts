export const templates: {
    difficulty: number;
    questions: (
        | {
              question: string;
              answer: string;
              quizId: number;
              options: string[];
          }
        | {
              question: string;
              answer: string;
              quizId: number;
              options: string[];
          }
    )[];
    description: string;
    title: string;
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
        ],
    },
    {
        title: "Norsk",
        description: "Norsk template kvizz",
        difficulty: 5,
        questions: [
            {
                quizId: 0,
                question: "Hvordan staver man katt?",
                options: [],
                answer: "katt",
            },
            {
                quizId: 0,
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
    },
];
