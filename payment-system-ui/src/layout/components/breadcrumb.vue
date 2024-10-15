<script setup lang="ts">
const { currentRoute } = useRouter()

const activeBreadcrumb = computed(() => {
  const {meta} = unref(currentRoute)
  return meta.breadcrumbItemList || [];
})
</script>

<template>
  <div class="breadcrumb">
    <el-breadcrumb separator="/">
      <el-breadcrumb-item v-for="(item, index) in activeBreadcrumb" :key="index">
        <span>{{item}}</span>
      </el-breadcrumb-item>
    </el-breadcrumb>
  </div>
</template>

<style scoped>
.breadcrumb {
  height: 60px;
  display: flex;
  align-items: center;
}
</style>
