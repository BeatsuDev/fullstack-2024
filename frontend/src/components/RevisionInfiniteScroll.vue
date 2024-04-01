<template>
    <div v-bind="containerProps" style="max-height: 500px">
        <div v-bind="wrapperProps">
            <RevisionCard
                v-for="revision in list"
                :key="revision.data.revisionId"
                :value="revision.data"
                @click="viewRevision(revision.data)"
                style="margin-bottom: 10px"
            />
        </div>
    </div>
</template>
<script lang="ts" setup>
import type { Revision } from "@/api";
import RevisionCard from "@/components/RevisionCard.vue";
import { useVirtualList } from "@vueuse/core";

const props = defineProps<{
    value: Revision[];
    height: {
        type: Number;
        default: 500;
    };
}>();

const emit = defineEmits<{
    (event: "view", value: Revision): void;
}>();

const { list, containerProps, wrapperProps } = useVirtualList(props.value, {
    itemHeight: 180,
});

function viewRevision(revision: Revision) {
    emit("view", revision);
}
</script>
