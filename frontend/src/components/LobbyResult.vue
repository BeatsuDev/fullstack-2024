<template>
    <div class="user-list">
        <UserResultCard
            v-for="user in users"
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
import { computed } from "vue";

const users = computed(() => {
    const _users = multiplayerStore.lobbyUsers;
    _users.sort(
        (a, b) =>
            multiplayerStore.calculateScore(b.id) -
            multiplayerStore.calculateScore(a.id)
    );
    return _users;
});

const props = defineProps<{
    results?: boolean;
}>();

const multiplayerStore = useMultiplayerStore();
</script>
