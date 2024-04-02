<script lang="ts" setup>
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
    height: calc(100% - 2rem);
    width: 100%;
    pointer-events: none;
    flex-direction: column;
    align-items: flex-end;
    z-index: 100;
    overflow-y: auto;
    overflow-x: hidden;
}

#notification-container > * {
    pointer-events: auto;
}

.notification-move,
.notification-enter-active,
.notification-leave-active {
    transition: all 300ms ease;
}

.notification-leave-active {
    position: absolute !important;
}

.notification-enter-from,
.notification-leave-to {
    opacity: 0;
    transform: translateX(30px);
}
</style>
