import type { Ref } from "vue";
import { computed, watch } from "vue";
import { AttemptApi } from "@/api";
import { useExecutablePromise } from "@/composables/promise";
import type { QuizAttempt } from "@/api/models/quiz-attempt";

export default function useQuizAttempt(revisionId: Ref<string>) {
    const attemptApi = new AttemptApi();
    const { data, loading, execute, error } = useExecutablePromise(() =>
        attemptApi.getAttempts(revisionId.value)
    );

    watch(
        revisionId,
        () => {
            execute();
        },
        {
            immediate: true,
        }
    );

    const attempts = computed(() => data.value?.data?.length || 0);

    const chartDataBasedOnAttempts = computed(() => {
        const chartData = {
            labels: [],
            datasets: [
                {
                    data: [],
                    label: "Correct Answers",
                    backgroundColor: "lightgreen",
                },
            ],
        };

        data.value?.data?.forEach((attempt: QuizAttempt) => {
            const formattedDate = new Date(
                attempt.attemptedAt
            ).toLocaleString();
            chartData.labels.push(formattedDate);

            const totalCorrectOnAttempt = attempt.questionAttempts
                .map((questionAttempt) => {
                    return (questionAttempt.correct ? 1 : 0) as number;
                })
                .reduce((acc, val) => acc + val, 0);
            chartData.datasets[0].data.push(totalCorrectOnAttempt);
        });

        return chartData;
    });

    const chartDataBasedOnIncorrectAnswerOnQuestion = computed(() => {
        const incorrect = {} as { [key: string]: number };

        data.value?.data?.forEach((attempt: QuizAttempt) => {
            attempt.questionAttempts.forEach((questionAttempt) => {
                if (!incorrect[questionAttempt.question.question]) {
                    incorrect[questionAttempt.question.question] = 0;
                }
                incorrect[questionAttempt.question.question]++;
            });
        });

        const sortedKeys = Object.keys(incorrect).sort(
            (a, b) => incorrect[a] - incorrect[b]
        );
        return {
            labels: sortedKeys,
            datasets: [
                {
                    data: sortedKeys.map((key) => incorrect[key]),
                    label: "Incorrect Answers",
                    backgroundColor: "lightblue",
                },
            ],
        };
    });

    const bestAttempt = computed<QuizAttempt | null>(() => {
        if (!data.value?.data) {
            return null;
        }

        let bestAttempt = data.value.data[0];
        let bestScore = 0;

        data.value.data?.forEach((attempt: QuizAttempt) => {
            const totalCorrectOnAttempt = attempt.questionAttempts
                .map((questionAttempt) => {
                    return (questionAttempt.correct ? 1 : 0) as number;
                })
                .reduce((acc, val) => acc + val, 0);

            if (totalCorrectOnAttempt > bestScore) {
                bestScore = totalCorrectOnAttempt;
                bestAttempt = attempt;
            }
        });

        return bestAttempt as QuizAttempt;
    });

    const bestAttemptScore = computed(() => {
        if (!bestAttempt.value) {
            return 0;
        }

        return bestAttempt.value.questionAttempts
            ?.map((questionAttempt) => {
                return (questionAttempt.correct ? 1 : 0) as number;
            })
            .reduce((acc, val) => acc + val, 0);
    });

    const numOfUncompletedAttempts = computed(() => {
        return data.value?.data?.filter(
            (attempt: QuizAttempt) => !attempt.complete
        ).length;
    });

    const errorMessage = computed(() => {
        if (attempts.value == 0) {
            return "You have not any attempts on this quiz yet.";
        }

        if (!error.value) {
            return "";
        }
        if (
            error.value.response.status == "404" ||
            error.value.response.status == "422"
        ) {
            return "Quiz not found.";
        }
        if (error.value) {
            return "An unexpected error occurred. Please try again later.";
        }
        return "";
    });

    return {
        data,
        loading,
        attempts,
        chartDataBasedOnAttempts,
        bestAttempt,
        bestAttemptScore,
        numOfUncompletedAttempts,
        chartDataBasedOnIncorrectAnswerOnQuestion,
        errorMessage,
    };
}
