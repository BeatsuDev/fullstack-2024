<template>
    <button>
        <slot></slot>
    </button>
</template>

<style scoped>
button {
    border: none;
    border-radius: 0.25rem;
    padding: 1em 1.5em;
    background-color: transparent;
    border: 2px solid var(--color-button);
    box-shadow: 2px 2px 2px rgba(0, 0, 0, 0.5);
    transition-duration: 100ms;
    font-size: 1em;
    cursor: pointer;
}

button:hover {
    background-color: var(--color-button);
    color: white;
}

button:active {
    transform: translate(2px, 2px);
    box-shadow: -2px -2px 2px rgba(0, 0, 0, 0.5);
}
</style>
