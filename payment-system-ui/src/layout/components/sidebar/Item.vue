<script setup lang="ts">
defineProps({
  icon: {
    type: String,
    default: undefined,
  },
  title: {
    type: String,
    default: '',
  },
})
</script>

<template>
  <div>
    <el-icon v-if="icon"><component :is="icon" /></el-icon>
    <div v-else style="width: 30px"></div>
    <span>{{ title }}</span>
  </div>
</template>

<style scoped>

</style>
