import { computed, ref } from "vue";
import { defineStore } from "pinia";
import type { Competition } from "@/api";
import { CompetitionApi } from "@/api";
import { useExecutablePromise } from "@/composables/promise";
import { AvatarGenerator } from "random-avatar-generator";

export type Event =
    | { type: "FINISH" }
    | { type: "PROCEED"; questionId: string }
    | { type: "JOIN" };

export const useMultiplayerStore = defineStore("multiplayer", () => {
    const multiplayerId = ref<string | undefined>(undefined);
    const lobbyCode = ref<number | undefined>(undefined);
    const competitionApi = new CompetitionApi();
    const lobby = ref<Competition | undefined>(undefined);
    const { execute: updateLobby } = useExecutablePromise(async () => {
        const code = lobbyCode.value;
        if (!code) return;
        return await competitionApi
            .getCompetition(code)
            .then((r) => (lobby.value = r.data));
    });

    const lobbyUsers = computed(() => {
        return players.value.map((player) => {
            return {
                id: player.user.id,
                name: player.user.name,
                avatar: new AvatarGenerator().generateRandomAvatar(
                    player.user.id
                ),
                score: calculateScore(player.user.id),
            };
        });
    });

    const players = computed(() => lobby.value?.competitors ?? []);

    function processMessage(message: any): Event | null {
        const data: string = message.body;
        const [competitionId, eventName, eventData] = data.split(":");

        if (competitionId !== multiplayerId.value) return null;

        updateLobby();
        switch (eventName) {
            case "FINISH":
                return { type: "FINISH" };
            case "JOIN":
                return { type: "JOIN" };
            case "PROCEED":
                return { type: "PROCEED", questionId: eventData };
            default:
                return null;
        }
    }

    async function joinCompetition(code: number) {
        lobbyCode.value = code;
        const response = await competitionApi.joinCompetition(code);
        lobby.value = response.data.competition;
        multiplayerId.value = response.data.competitionId;
    }

    function reset() {
        lobby.value = undefined;
        lobbyCode.value = undefined;
        multiplayerId.value = undefined;
    }

    function calculateScore(userId: string): number {
        if (!lobby.value) return 0;
        const result = lobby.value.competitors
            .filter((player) => player.user.id === userId)
            .map((player) => {
                return player.attempt.questionAttempts
                    .map((questionAttempt) => {
                        return (questionAttempt.correct ? 1 : 0) as number;
                    })
                    .reduce((a, b) => a + b, 0);
            })
            .reduce((a, b) => a + b, 0);
        return result;
    }

    return {
        reset,
        processMessage,
        joinCompetition,
        multiplayerId,
        lobbyCode,
        players,
        lobby,
        lobbyUsers,
    };
});
