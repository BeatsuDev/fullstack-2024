<template>
    <div class="user-list">
        <UserResultCard :key="user.id" :value="user" :results="props.results" v-for="user in multiplayerStore.lobbyUsers"></UserResultCard>
        <div class="user" v-if="!props.results">More friends will show up here!</div>
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