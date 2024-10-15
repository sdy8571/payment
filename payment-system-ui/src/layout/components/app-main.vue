<script setup lang="ts">
const props = defineProps({
  height: {
    type: String,
    default: '',
  },
  width: {
    type: String,
    default: '',
  }
})
</script>

<template>
  <el-scrollbar>
    <div class="app_main" :style="{ height: props.height, width: props.width }">
      <router-view />
    </div>
  </el-scrollbar>
</template>

<style scoped>
.app_main {
  font-size: 14px;
}
</style>
