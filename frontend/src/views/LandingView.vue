<template>
    <main>
        <div class="hero">
            <div class="hero-text">
                <div class="hero-title">Kazoot!</div>
                <div>
                    <p>{{ randomSubTitle() }}</p>
                    <ButtonComponent
                        filled
                        rounded-lg
                        @click="router.push({ name: 'login' })"
                        >Log in
                    </ButtonComponent>
                </div>
            </div>
        </div>
    </main>
</template>

<script lang="ts" setup>
import ButtonComponent from "@/components/ButtonComponent.vue";
import router from "@/router";

const subtitles = [
    "Did you mean to go to Kahoot?",
    "Kazoot! The best quiz app",
    "Kahoot who? Kazoot!",
    "Let's get quizzical",
    "Learning is fun with Kazoot!",
];

function randomSubTitle() {
    return subtitles[Math.floor(Math.random() * subtitles.length)];
}
</script>

<style scoped>
.hero {
    background-image: url("@/assets/stacked-peaks-haikei.svg");
    background-size: cover;
    background-position: center;
    height: calc(100svh - 4rem);
    overflow: hidden;
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

@media (max-width: 768px) {
    .hero-title {
        font-size: 4rem;
    }
}
</style>
