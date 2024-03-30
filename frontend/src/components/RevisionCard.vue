<template>
    <div class="revision-card">
        <h3>Revision Card</h3>
        <div class="card-content">
            <p><strong>Revision ID:</strong> {{ props.value.revisionId }}</p>
            <p><strong>Title:</strong> {{ props.value.quiz?.title }}</p>
            <p>
                <strong>Creator:</strong> {{ props.value.quiz?.creator.name }}
            </p>
            <p><strong>Created At:</strong> {{ formatCreatedAt }}</p>
        </div>
    </div>
</template>
<script lang="ts" setup>
import type { Revision } from "@/api";
import { computed } from "vue";

const props = defineProps<{
    value: Revision;
    editable?: boolean;
}>();

const formatCreatedAt = computed(() => {
    const createdAt = new Date(props.value.quiz?.createdAt);
    return `${createdAt.toDateString()} ${createdAt.toLocaleTimeString()}`;
});
</script>

<style scoped>
.revision-card {
    background-color: #fff;
    border: 1px solid #ccc;
    border-radius: 8px;
    box-shadow: 0 2px 4px rgba(0, 0, 0, 0.1);
    padding: 20px;
    margin: 0 auto;
}

.card-content {
    margin-top: 20px;
}

.card-content p {
    margin: 10px 0;
}

.card-content strong {
    font-weight: bold;
}
</style>
