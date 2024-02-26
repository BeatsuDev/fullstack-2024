import { ref } from "vue";
import { defineStore } from "pinia";

export type NotificationOptions = {
    message: string;
    type: "error" | "warning" | "info" | "success";
    duration?: number;
};

export type NotificationData = {
    readonly id: number;
    readonly message: string;
    readonly type: "error" | "warning" | "info" | "success";
    readonly duration?: number;
};

export const useNotificationStore = defineStore("notification", () => {
    const DEFAULT_DURATION = 8000;
    const notifications = ref<NotificationData[]>([]);
    const counter = ref(0);

    function addNotification(notification: NotificationOptions) {
        const data = {
            id: counter.value++,
            ...notification,
        } as NotificationData;

        notifications.value.push(data);

        setTimeout(
            removeNotification,
            notification.duration || DEFAULT_DURATION,
            data.id
        );
    }

    function removeNotification(id: number) {
        const index = notifications.value.findIndex((n) => n.id === id);
        if (index !== -1) {
            notifications.value.splice(index, 1);
        }
    }

    return {
        notifications,
        addNotification,
        removeNotification,
    };
});
