<template>
    <div class="user-card">
        <div class="user-info">
            <div class="user-name">{{ props.value.name }}</div>
            <div class="user-email">{{ props.value.email }}</div>
        </div>
        <div style="display: flex; justify-content: end">
            <ButtonComponent
                id="delete-button"
                v-if="props.editable"
                filled
                @click="emit('delete', value)"
                >Delete
            </ButtonComponent>
        </div>
    </div>
</template>
<script lang="ts" setup>
import ButtonComponent from "./ButtonComponent.vue";
import type { User } from "../api/models/user";

const props = defineProps<{
    value: User;
    editable?: boolean;
}>();

const emit = defineEmits<{
    (event: "delete", value: User): void;
}>();
</script>
<style scoped>
.user-card {
    border: 2px solid var(--color-border);
    padding: 16px;
}

.user-info {
    text-align: center;
}

.user-name {
    font-size: 1.2em;
    font-weight: bold;
}

.user-email {
    color: #666;
    margin-top: 8px;
}
</style>
