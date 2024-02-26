<script setup lang="ts">
import { computed } from "vue";
import type { NotificationData } from "@/stores/notification";

import SuccessIcon from "@/assets/icons/SuccessIcon.vue";
import InfoIcon from "@/assets/icons/InfoIcon.vue";
import WarningIcon from "@/assets/icons/WarningIcon.vue";
import ErrorIcon from "@/assets/icons/ErrorIcon.vue";

const props = defineProps<NotificationData>();
const emit = defineEmits<{
    close: [id: number];
}>();

const icon = computed(() => {
    switch (props.type) {
        case "success":
            return SuccessIcon;
        case "info":
            return InfoIcon;
        case "warning":
            return WarningIcon;
        case "error":
            return ErrorIcon;
        default:
            return InfoIcon;
    }
});
</script>

<template>
    <div id="notification-box" :class="type">
        <div id="icon">
            <component :is="icon" />
        </div>
        <p>{{ message }}</p>
        <button id="close-notification" @click="emit('close', props.id)">
            {{ $t("notification.close") }}
        </button>
    </div>
</template>

<style scoped>
#notification-box {
    position: relative;
    display: flex;
    justify-content: space-between;
    align-items: center;
    padding: 0.25rem;
    margin: 0.5rem;
    width: 20rem;

    border-radius: 0.5rem;
}

#notification-box svg {
    width: 2.5rem;
    height: auto;
    margin: 0 0.5rem;
    color: white;
}

#notification-box p {
    flex: 1;
}

#notification-box button {
    background-color: transparent;
    border: none;
    color: var(--color-text);
    cursor: pointer;
    font-size: 1rem;

    justify-self: flex-end;
    align-self: flex-start;
}

.success svg {
    color: var(--success-500);
}

.success {
    background-color: var(--success-500);
}

.info svg {
    color: var(--info-500);
}

.info {
    background-color: var(--info-500);
}

.warning svg {
    color: var(--warning-500);
}

.warning {
    background-color: var(--warning-500);
}

.error svg {
    color: var(--error-500);
}

.error {
    background-color: var(--error-500);
}
</style>
