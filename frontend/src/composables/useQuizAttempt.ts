import type { Ref } from "vue";
import { computed, watch } from "vue";
import { AttemptApi } from "@/api";
import { useExecutablePromise } from "@/composables/promise";
import type { QuizAttempt } from "@/api/models/quiz-attempt";

export default function useQuizAttempt(revisionId: Ref<string>) {
    const attemptApi = new AttemptApi();
    const { data, loading, execute, error } = useExecutablePromise(() => attemptApi.getAttempts(revisionId.value));

    watch(revisionId, () => {
        execute();

    }, {
        immediate: true,
    });

    const attempts = computed(() => data.value?.data?.length || 0);

    const chartDataBasedOnAttempts = computed(() => {
        const chartData = {
            labels: [],
            datasets: [{ data: [] }],
        };

        data.value?.data?.forEach((attempt: QuizAttempt) => {
            chartData.labels.push(attempt.id);

            const totalCorrectOnAttempt = attempt.questionAttempts.map((questionAttempt) => {
                return questionAttempt.correct ? 1 : 0;

            }).reduce((acc, val) => acc + val, 0);
            chartData.datasets[0].data.push(totalCorrectOnAttempt);
        });

        console.log(chartData);
        return chartData;
    });

    const chartDataBasedOnIncorrectAnswerOnQuestion = computed(() => {

        const incorrect = {} as { [key: string]: number };

        data.value?.data?.forEach((attempt: QuizAttempt) => {
            attempt.questionAttempts.forEach((questionAttempt) => {
                if (!incorrect[questionAttempt.question.id]) {
                    incorrect[questionAttempt.question.id] = 0;
                }
                incorrect[questionAttempt.question.id]++;
            });
        });

        const sortedKeys = Object.keys(incorrect).sort((a, b) => incorrect[a] - incorrect[b]);
        return  {
            labels: sortedKeys,
            datasets: sortedKeys.map((key) => incorrect[key]),
        }
    });

    const bestAttempt = computed<QuizAttempt | null>(() => {
        if (!data.value?.data) {
            return null;
        }

        let bestAttempt = data.value.data[0];
        let bestScore = 0;

        data.value.data?.forEach((attempt: QuizAttempt) => {
            const totalCorrectOnAttempt = attempt.questionAttempts.map((questionAttempt) => {
                return questionAttempt.correct ? 1 : 0;
            }).reduce((acc, val) => acc + val, 0);

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

        return bestAttempt.value.questionAttempts?.map((questionAttempt) => {
            return questionAttempt.correct ? 1 : 0;
        }).reduce((acc, val) => acc + val, 0);
    });

    const numOfUncompletedAttempts = computed(() => {
        return data.value?.data?.filter((attempt: QuizAttempt) => !attempt.complete).length;
    });

    const errorMessage = computed(() => {
        if (!error.value) {
            return "";
        }
        if (error.value.response.status == "404" || error.value.response.status == "422") {
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
        errorMessage
    };

}