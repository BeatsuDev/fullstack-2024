<script setup lang="ts">
withDefaults(
    defineProps<{
        loading: boolean;
        outerColor?: string;
        innerColor?: string;
        thickness?: string;
    }>(),
    {
        thickness: "5px",
    }
);
</script>

<template>
    <Transition mode="out-in">
        <div
            v-if="loading"
            class="loading-container"
            style="transition-delay: 200ms"
        >
            <div class="loader" :class="$attrs.class">
                <div
                    :style="{
                        borderColor: outerColor,
                        borderWidth: thickness,
                        width: `100%`,
                        aspectRatio: '1/1',
                    }"
                    class="outer-circle"
                ></div>
                <div
                    :style="{
                        borderColor: innerColor,
                        borderWidth: thickness,
                        width: `calc(100% - 2*${thickness} - 4px)`,
                        aspectRatio: '1/1',
                    }"
                    class="inner-circle"
                ></div>
            </div>
        </div>
    </Transition>
</template>

<style scoped>
.loading-container {
    width: 100%;
    height: 100%;
    background-color: rgba(0, 0, 0, 0.5);
    position: absolute;
    display: flex;
    justify-content: center;
    align-items: center;
    z-index: 40;
}

.loader {
    position: relative;
    display: flex;
    justify-content: center;
    align-items: center;
    width: 100px;
    height: 100px;
}

.outer-circle {
    position: absolute;
    border-radius: 50%;
    border: solid var(--primary-300);
    border-top: solid transparent !important;
    border-left: solid transparent !important;
    animation: spin 0.5s linear infinite;
}

.inner-circle {
    position: absolute;
    border-radius: 50%;
    border: solid var(--primary-100);
    border-top: solid transparent !important;
    border-left: solid transparent !important;
    border-right: solid transparent !important;
    animation: spin 0.77s linear infinite;
}

@keyframes spin {
    0% {
        transform: rotate(0deg);
    }
    100% {
        transform: rotate(360deg);
    }
}

.v-enter-active,
.v-leave-active {
    transition: opacity 300ms;
}

.v-enter-from,
.v-leave-to {
    opacity: 0;
}
</style>
