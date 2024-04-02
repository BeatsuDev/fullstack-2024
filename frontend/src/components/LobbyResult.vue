<template>
    <div class="user-list">
        <UserResultCard
            v-for="user in multiplayerStore.lobbyUsers"
            :key="user.id"
            :results="props.results"
            :value="user"
        ></UserResultCard>
        <div v-if="!props.results" class="user">
            More friends will show up here!
        </div>
    </div>
</template>
<script lang="ts" setup>
import { useMultiplayerStore } from "@/stores/multiplayer";
import UserResultCard from "@/components/UserResultCard.vue";

const props = defineProps<{
    results?: boolean;
}>();

const multiplayerStore = useMultiplayerStore();
</script>
