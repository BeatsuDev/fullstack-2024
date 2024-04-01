import { ref } from "vue";
import { defineStore } from "pinia";
import type { Competitor } from "@/api";

export const useMultiplayerStore = defineStore("multiplayer", () => {
    const players = ref<Competitor[]>([]);
    const lobbyCode = ref<number | null>(null);
    const isInMultiplayerGame = ref(false);

    return {
        isInMultiplayerGame,
        lobbyCode,
        players,
    };
});
