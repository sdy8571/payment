<script setup lang="ts">

import layout from '@/layout/index.vue';
import { useAppStore } from '@/store/modules/app'

const appStore = useAppStore()
const currentSize = computed(() => appStore.getCurrentSize)

</script>

<template>
  <ConfigGlobal :size="currentSize">
    <layout/>
  </ConfigGlobal>
</template>

<style scoped>
.size {
  width: 100%;
  height: 100%;
}

html,body {
  @extend .size;

  padding: 0 !important;
  margin: 0;
  overflow: hidden;

  #app {
    @extend .size;
  }
}

</style>
