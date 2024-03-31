<template>
    <main>
        <div class="hero">
            <div class="hero-text">
                <div class="hero-title">Kazoot!</div>
                <div>
                    <p>{{ randomSubTitle() }} </p>
                    <button-component @click="router.push('/login')" filled rounded-lg>Log in</button-component>
                </div>
            </div>
        </div>
    </main>
</template>
<script setup>

import ButtonComponent from "@/components/ButtonComponent.vue";
import { useRouter } from "vue-router";

const router = useRouter();

const subtitles = [
    "Did you mean to go to Kahoot?",
    "Kazoot! The best quiz app",
    "Kahoot who? Kazoot!",
    "Let's get quizzical",
    "Learning is fun with Kazoot!",
]

function randomSubTitle() {
    return subtitles[Math.floor(Math.random() * subtitles.length)];

}
</script>
<style scoped>
    .hero {
        background-image: url("src/assets/stacked-peaks-haikei.svg");
        background-size: cover;
        background-position: center;
        height: calc(100svh - 68px);
        display: flex;
        color: white;
    }

    .hero-text {
        margin-top: 10rem;
        margin-left: 2rem;
        font-size: 1.5rem;
    }

    .hero-title {
        font-size: 6rem;

    }
</style>