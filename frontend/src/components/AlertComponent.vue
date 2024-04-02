<template>
    <div :class="['alert', alertType]">
        <slot></slot>
    </div>
</template>

<script lang="ts" setup>
import { computed } from "vue";

const props = defineProps<{
    type: "info" | "warning" | "danger";
}>();

const alertType = computed(() => {
    return "alert-" + props.type;
});
</script>

<style scoped>
.alert {
    padding: 15px;
    border-radius: 4px;
    margin-bottom: 15px;
}

.alert-info {
    background-color: #cce5ff;
    color: #004085;
    border: 1px solid #b8daff;
}

.alert-warning {
    background-color: #fff3cd;
    color: #856404;
    border: 1px solid #ffeeba;
}

.alert-danger {
    background-color: #f8d7da;
    color: #721c24;
    border: 1px solid #f5c6cb;
}

.close-btn {
    cursor: pointer;
    float: right;
    font-weight: bold;
    font-size: 18px;
    line-height: 1;
}
</style>
