<template>
    <div class="feedback-item">
        <div class="feedback-header">
            <p class="username">{{ props.feedback.username }}</p>
            <p class="created-at">{{ props.feedback.createdAt }}</p>
        </div>
        <div class="feedback-content">
            <p class="feedback">{{ props.feedback.feedback }}</p>
        </div>
    </div>
</template>
<script lang="ts" setup>
import type { Feedback } from "../api/models/feedback";

const props = defineProps<{
    feedback: Feedback;
}>();
</script>
<style scoped>
.feedback-item {
    background-color: var(--color-background-mute);
    padding: 16px;
    margin-bottom: 16px;
    border: 2px solid var(--color-border);
}

.feedback-header {
    display: flex;
    justify-content: space-between;
    align-items: center;
    margin-bottom: 8px;
}

.username {
    font-weight: bold;
}

.created-at {
    font-size: 12px;
    color: #666;
}

.feedback-content {
    border-left: 4px solid var(--primary-400);
    padding-left: 12px;
}
</style>
