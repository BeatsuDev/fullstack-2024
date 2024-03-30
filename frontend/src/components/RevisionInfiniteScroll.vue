<template>
    <div v-bind="containerProps" style="height: 500px">
        <div v-bind="wrapperProps">
            <RevisionCard
                v-for="revision in list"
                :key="revision.data.revisionId"
                :value="revision.data"
                @click="viewRevision(revision.data)"
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

const { list, containerProps, wrapperProps } = useVirtualList(
  props.value,
  {
    itemHeight: 254
  },
)

function viewRevision(revision: Revision) {
    emit("view", revision);
}
</script>
