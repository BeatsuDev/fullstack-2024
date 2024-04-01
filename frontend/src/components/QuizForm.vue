<template>
    <form @submit.prevent ref="form">
        <label for="title">Title</label>
        <ValidatedInput
            id="title"
            :validator="v$.title"
            v-model="editable.title"
        />
        <label for="Description">Description</label>
        <ValidatedInput
            id="description"
            :validator="v$.description"
            v-model="editable.description"
        />
        <label for="difficulty">Difficulty: {{ editable.difficulty }} </label>
        <input type="range" min="1" max="10" v-model="editable.difficulty" />
        <div>Categories:</div>

        <div class="category-filter-labels">
            <label
                v-for="(category, i) in categories?.data"
                :key="i"
                :style="getCategoryStyle(category)"
            >
                <input type="checkbox" @change="toggleCategory(category)" />
                {{ category.name }}
            </label>
        </div>
        <ButtonComponent @click="submit" :loading="props.loading"
            >Submit
        </ButtonComponent>
    </form>
</template>
<script setup lang="ts">
import ButtonComponent from "@/components/ButtonComponent.vue";
import ValidatedInput from "@/components/ValidatedInput.vue";
import { type Category, CategoryApi, type Quiz, type QuizCreate } from "@/api";
import { reactive, ref, watch, watchEffect } from "vue";
import { required } from "@vuelidate/validators";
import { useVuelidate } from "@vuelidate/core";
import { usePromise } from "@/composables/promise";

const props = defineProps<{
    value?: QuizCreate | Quiz;
    loading?: boolean;
}>();

const emit = defineEmits<{
    (e: "submit", quiz: QuizCreate | Quiz): void;
}>();

const editable = reactive(
    props.value || {
        title: "",
        description: "",
        difficulty: 1,
    }
) as QuizCreate;

const formRules = {
    title: { required },
    description: { required },
    difficulty: { required },
};

const v$ = useVuelidate(formRules, editable);

const form = ref<HTMLFormElement | null>(null);

async function submit() {
    if (!form.value) return;
    if (!(await v$.value.$validate())) {
        return;
    }
    editable.categories = selectedCategories.value.map((c) => c.id);

    emit("submit", editable);
}

const categoryApi = new CategoryApi();

const selectedCategories = ref<Category[]>([]);
const { data: categories } = usePromise(categoryApi.getCategories());

function isCategorySelected(category: Category): boolean {
    if (!selectedCategories.value) return false;
    return selectedCategories.value.map((c) => c.name).includes(category.name);
}

function getCategoryStyle(category: Category) {
    if (category.name == "string")
        return {
            border: `2px solid red`,
        };
    if (isCategorySelected(category)) {
        return {
            border: `2px solid ${category.color}`,
            backgroundColor: category.color,
            color: "white",
            "box-shadow": "1px 1px 2px rgb(0 0 0 / 50%)",
        };
    }
    return {
        border: `2px solid ${category.color}`,
    };
}

function toggleCategory(category: Category) {
    if (!selectedCategories.value) return;

    if (isCategorySelected(category)) {
        selectedCategories.value = selectedCategories.value.filter(
            (c) => c.name !== category.name
        );
    } else {
        selectedCategories.value = [...selectedCategories.value, category];
    }
}
watchEffect(() => {
    Object.assign(editable, props.value);
    selectedCategories.value = props.value?.categories || [];
});
</script>
<style scoped>
form {
    display: flex;
    flex-direction: column;
    gap: 1rem;
    max-width: 20rem;
}

.category-filter-labels {
    display: flex;
    flex-wrap: wrap;
    align-items: center;
}

.category-filter-labels label {
    display: flex;
    align-items: center;
    margin: 0.25em;
    padding: 0.5em 1em;
    border: 2px solid;
    border-radius: 2em;
    cursor: pointer;
    user-select: none;
    text-transform: uppercase;
}

.category-filter-labels input {
    display: none;
}

fieldset > legend {
    font-size: 1.2em;
    margin: 0.5em;
    font-weight: bold;
}
</style>
