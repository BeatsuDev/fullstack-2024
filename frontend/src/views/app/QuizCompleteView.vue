<script setup lang="ts">
import router from "@/router";

const attemptId = router.currentRoute.value.query.attemptId as string;
</script>

<template>Wow! You completed the quiz! Good job!</template>
