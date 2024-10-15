<script setup lang="ts">
import Item from './Item.vue';

defineProps({
  routerList: {
    type: Array,
    default: []
  }
})
const isShowFirst = (item: any) => {
  const meta = item.meta || {}
  // 是否隐藏， true：隐藏 返回false， false: 不隐藏
  if (meta.hidden) {
    return false;
  }
  // 如果不存在子目录或者子目录为空，则显示当前目录
  if (!item.children || item.children.length === 0) {
    return true;
  }
  // 否则返回结果
  return false;
}

</script>

<template>
  <div v-for="item in routerList" key="{{item.path}}">
    <el-menu-item v-if="isShowFirst(item)" :index="item.meta.fullPath">
      <item :icon="item.meta.icon" :title="item.meta.title"/>
    </el-menu-item>
    <div v-else>
      <el-sub-menu v-if="!item.meta.hidden" :index="item.meta.fullPath">
        <template #title>
          <item :icon="item.meta.icon" :title="item.meta.title"/>
        </template>
        <!-- 递归 -->
        <sidebarItem :router-list="item.children" />
      </el-sub-menu>
    </div>
  </div>
</template>

<style scoped>
::v-deep .el-scrollbar__thumb {
  display: none;
}
</style>
