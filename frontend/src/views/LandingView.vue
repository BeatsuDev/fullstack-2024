<script setup lang="ts">
import { useNotificationStore } from "@/stores/notification";

const { addNotification } = useNotificationStore();

function notify() {
    addNotification({
        message: "This is a notification!",
        type: ["info", "warning", "error", "success"][
            Math.floor(Math.random() * 4)
        ] as "info" | "warning" | "error" | "success",
    });
}
</script>

<template>
    <div>
        <button @click="notify">Notification!</button>
    </div>
</template>
