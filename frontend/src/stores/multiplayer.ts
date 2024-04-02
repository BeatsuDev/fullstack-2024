import { computed, ref, watch, watchEffect } from "vue";
import { defineStore } from "pinia";
import type { Competition, PrecompetitionInfo } from "@/api";
import { CompetitionApi } from "@/api";
import { useExecutablePromise } from "@/composables/promise";
import { AvatarGenerator } from "random-avatar-generator";

export type Event =
    | { type: "FINISH" }
    | { type: "PROCEED"; questionId: string }
    | { type: "JOIN" };

export const useMultiplayerStore = defineStore("multiplayer", () => {
    const multiplayerData = ref<PrecompetitionInfo | null>(null);

    const multiplayerId = computed(() => multiplayerData.value?.competitionId);
    const players = computed(
        () => multiplayerData.value?.competition.competitors ?? [],
    );
    const lobbyCode = ref<number | null>(null);

    function processMessage(message: any): Event | null {
        const data: string = message.body;
        const [competitionId, eventName, eventData] = data.split(":");

        if (competitionId !== multiplayerId.value) return null;

        switch (eventName) {
            case "FINISH":
                return { type: "FINISH" };
            case "JOIN":
                return { type: "JOIN" };
            case "PROCEED":
                console.log("FAREN DIN")
                execute(
                    lobbyCode.value,
                    undefined
                );
                return { type: "PROCEED", questionId: eventData };
            default:
                return null;
        }
    }

    function reset() {
        multiplayerData.value = null;
        lobbyCode.value = null;
    }

    const competitionApi = new CompetitionApi();
    const { data: lobby, execute } = useExecutablePromise(async () => {
        return await competitionApi.getCompetition(lobbyCode.value);
    });

    const lobbyUsers = computed(() => {
        return players.value.map((player) => {
            return {
                id: player.user.id,
                name: player.user.name,
                avatar: new AvatarGenerator().generateRandomAvatar(player.user.id),
                score: lobby.value?.data ? calculateScore(lobby.value.data, player.user.id) : 0
            };
        });
    });
    function calculateScore(lobby: Competition, userId: string): number {
        const result = lobby.competitors.filter((player) => player.user.id === userId).map((player) => {
            return player.attempt.questionAttempts.map((questionAttempt) => { return questionAttempt.correct ? 1 : 0; }).reduce((a, b) => a + b, 0);
        }).reduce((a, b) => a + b, 0);
        return result;
    }

    return {
        reset,
        processMessage,
        multiplayerData,
        multiplayerId,
        lobbyCode,
        players,
        lobby,
        lobbyUsers,
    };
});
