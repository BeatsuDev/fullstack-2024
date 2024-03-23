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
    background-color: var(--color-button);
    transition: 100ms;
    box-shadow: 2px 2px 2px rgba(0, 0, 0, 0.5);
    font-size: 1em;
    color: white;
    cursor: pointer;
}

button:hover {
    background-color: var(--color-button-hover);
}

button:active {
    transform: translate(2px, 2px);
    box-shadow: -2px -2px 2px rgba(0, 0, 0, 0.5);
}
</style>
