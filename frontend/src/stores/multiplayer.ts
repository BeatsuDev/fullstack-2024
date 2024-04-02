import { ref, computed } from "vue";
import { defineStore } from "pinia";
import type { PrecompetitionInfo } from "@/api";

export const useMultiplayerStore = defineStore("multiplayer", () => {
    const multiplayerData = ref<PrecompetitionInfo | null>(null);

    const multiplayerId = computed(() => multiplayerData.value?.competitionId);
    const players = computed(
        () => multiplayerData.value?.competition.competitors ?? []
    );
    const lobbyCode = ref<number | null>(null);

    function processMessage(message: any): "JOIN" | "PROCEED" | null {
        const data: string = message.body;
        console.log(multiplayerId.value);
        if (!data.startsWith(multiplayerId.value!)) return null;

        const event = data.slice(multiplayerId.value!.toString().length + 1);
        if (["JOIN", "PROCEED"].includes(event)) {
            return event as "JOIN" | "PROCEED";
        } else {
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
