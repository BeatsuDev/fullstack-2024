<script setup lang="ts">
defineProps<{
    small?: boolean;
    large?: boolean;
    roundedSm?: boolean;
    rounded?: boolean;
    roundedLg?: boolean;
    filled?: boolean;
}>();
</script>

<template>
    <button
        :class="{
            small,
            large,
            'rounded-sm': roundedSm,
            rounded,
            'rounded-lg': roundedLg,
            filled,
        }"
    >
        <slot></slot>
    </button>
</template>

<style scoped>
/* Default styling */
button {
    border: 2px solid var(--color-button-border);
    padding: 0.5em 1em;
    background-color: transparent;
    transition: 100ms;
    box-shadow: 2px 2px 0px rgba(0, 0, 0, 0.5);
    cursor: pointer;
}

button:hover {
    background-color: var(--color-button-hover);
    color: white;
}

button:active {
    transform: translate(2px, 2px);
    box-shadow: -2px -2px 0px rgba(0, 0, 0, 0.5);
}

/* Styles for attributes */
button.small {
    padding: 0.25em 0.5em;
}

button.large {
    padding: 1em 1.5em;
}

button.roundedSm {
    border-radius: 0.125em;
}

button.rounded {
    border-radius: 0.25em;
}

button.roundedLg {
    border-radius: 0.5em;
}

button.filled {
    background-color: var(--color-button);
    border: none;
    color: white;
}

button.filled:hover {
    background-color: var(--color-button-hover);
}
</style>
