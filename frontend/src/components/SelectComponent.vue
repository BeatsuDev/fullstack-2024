<template>
    <select v-model="value">
        <slot></slot>
    </select>
</template>
<script setup lang="ts">

const value = defineModel({
    required: true,
});
</script>
<style scoped>
select {
    margin-bottom: 20px;
    padding: 10px;
}
</style>
