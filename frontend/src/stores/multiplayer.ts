import { ref } from "vue";
import { defineStore } from "pinia";
import type { User } from "@/api";

export const useMultiplayerStore = defineStore("multiplayer", () => {
    const players = ref<User[]>([]);
    const lobbyCode = ref<number | null>(null);

    return {
        players,
    };
});
