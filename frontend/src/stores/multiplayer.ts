import { ref, computed } from "vue";
import { defineStore } from "pinia";
import type { PrecompetitionInfo } from "@/api";

export type Event =
    | { type: "FINISH" }
    | { type: "PROCEED"; questionId: string }
    | { type: "JOIN" };

export const useMultiplayerStore = defineStore("multiplayer", () => {
    const multiplayerData = ref<PrecompetitionInfo | null>(null);

    const multiplayerId = computed(() => multiplayerData.value?.competitionId);
    const players = computed(
        () => multiplayerData.value?.competition.competitors ?? []
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
                return { type: "PROCEED", questionId: eventData };
            default:
                return null;
        }
    }

    function reset() {
        multiplayerData.value = null;
        lobbyCode.value = null;
    }

    return {
        reset,
        processMessage,
        multiplayerData,
        multiplayerId,
        lobbyCode,
        players,
    };
});
