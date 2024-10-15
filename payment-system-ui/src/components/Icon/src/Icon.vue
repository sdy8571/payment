<script setup lang="ts">
defineProps({
  icon: {
    type: String,
    default: ''
  },
  size: {
    type: Number || String,
    default: 15,
  },
  color: {
    type: String,
    default: ''
  }
})
</script>

<template>
  <el-icon :size="size" :color="color" class="mr-10px"><component :is="icon" /></el-icon>
</template>

<style scoped>

</style>
