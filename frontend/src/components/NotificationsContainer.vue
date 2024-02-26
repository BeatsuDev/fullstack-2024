<script setup lang="ts">
import { storeToRefs } from "pinia";
import { useNotificationStore } from "@/stores/notification";
import NotificationBox from "./NotificationBox.vue";

const { notifications } = storeToRefs(useNotificationStore());
const { removeNotification } = useNotificationStore();
</script>

<template>
    <div id="notification-container">
        <TransitionGroup name="notification">
            <NotificationBox
                v-for="notification in notifications"
                :key="notification.id"
                v-bind="notification"
                @close="removeNotification(notification.id)"
            />
        </TransitionGroup>
    </div>
</template>

<style scoped>
#notification-container {
    display: flex;
    position: absolute;
    top: 1rem;
    right: 1rem;
    max-height: calc(100% - 2rem);
    flex-direction: column;
    z-index: 100;
    overflow-y: auto;
    overflow-x: hidden;
}

.notification-move,
.notification-enter-active,
.notification-leave-active {
    transition: all 300ms ease;
}

.notification-enter-from,
.notification-leave-to {
    opacity: 0;
    transform: translateX(30px);
}
</style>
