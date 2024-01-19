<script setup lang="ts">
import { RouterLink, RouterView } from "vue-router";
</script>

<template>
    <header>
        <img
            alt="Mascot"
            class="mascot"
            src="@/assets/mascot.png"
            width="125"
        />
    </header>
    <RouterView />
</template>
