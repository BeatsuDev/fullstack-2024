<template>
    <div
        :key="value.id"
        :style="{
            backgroundColor:
                value.id === value.id ? 'lightblue' : 'var(--primary-200)',
        }"
        class="user"
    >
        <img :src="value.avatar" alt="Avatar" height="100" />
        <p>
            {{ value.name }} <span v-if="results">Score {{ value.score }}</span>
        </p>
    </div>
</template>
<script lang="ts" setup>
const props = defineProps<{
    value: {
        id: string;
        name: string;
        avatar: string;
        score: number;
    };
    results?: boolean;
}>();
</script>
<style scoped>
.user {
    display: flex;
    align-items: center;
    padding: 0.5rem;
    font-size: 1.5em;
    margin-bottom: 0.75rem;
}

.user > img {
    margin-right: 1rem;
}
</style>
