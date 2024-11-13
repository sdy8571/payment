<script setup lang="ts">
import sidebarItem from "./sidebar-item.vue";
import { usePermissionStore } from "@/store/modules/permission";
const routerList = usePermissionStore().routerList
const routerMap = usePermissionStore().routerMap
const { push, currentRoute } = useRouter()

const handleSelect = (index: string) => {
  const to = routerMap[index]
  push(to)
}

const activeMenu = computed(() => {
  const { meta } = unref(currentRoute)
  return meta.fullPath;
})

</script>

<template>
    <el-menu router :defaultActive="activeMenu" @select="handleSelect" unique-opened>
      <el-scrollbar height="100vh" width="200px">
        <sidebar-item :router-list="routerList"/>
      </el-scrollbar>
    </el-menu>
</template>

<style scoped>
::v-deep .el-scrollbar__thumb {
  display: none;
}
</style>
