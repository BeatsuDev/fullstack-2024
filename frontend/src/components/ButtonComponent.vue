<template>
    <button>
        <slot></slot>
    </button>
</template>

<style scoped>
button {
    border: none;
    border-radius: 0.25rem;
    padding: 1em 0.5em;
    background-color: var(--gray-700);
    transition: background-color 100ms;
    font-size: 1em;
    color: white;
    cursor: pointer;
}

button:hover {
    background-color: var(--gray-800);
}
</style>
