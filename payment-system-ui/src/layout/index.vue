<script setup lang="ts">

defineOptions({ name: 'Layout' })

import sidebar from './components/sidebar/sidebar.vue';
import navbar from './components/navbar.vue'
import appMain from './components/app-main.vue';

let appMainWidth = ref(0);
let appMainHeight = ref(0);

onMounted(() => {
  window.onresize = function windowResize() {
    calWidthAndHeight();
  };
});

// 注册一个回调函数，在组件因为响应式状态变更而更新其 DOM 树之后调用。
onUpdated(() => {
  calWidthAndHeight();
});

const calWidthAndHeight = () => {
  let sidebar = document.getElementById('sidebar');
  let sidebarW = sidebar ? sidebar.offsetWidth : 0;
  appMainWidth.value = window.innerWidth - sidebarW;

  let top = document.getElementById('top');
  let topH = top ? top.offsetHeight : 0;
  appMainHeight.value = window.innerHeight - topH;
}

</script>

<template>
  <div class="full">
    <el-container>
      <sidebar id="sidebar" class="sidebar w-200"/>
      <el-container>
        <el-header id="top" class="navbar">
          <navbar />
        </el-header>
        <el-main>
          <app-main class="app_main"/>
        </el-main>
      </el-container>
    </el-container>
  </div>
</template>

<style scoped>
.full {
  width: 100%;
  height: 100%;
}
.sidebar {
  box-shadow: 1px 0 5px rgba(0, 0, 0, 0.2);
}
.w-200 {
  width: 200px;
}
.navbar {
  box-shadow: 0 1px 5px rgba(0, 0, 0, 0.2);
}
.app_main {
  width: 100%;
  height: 100%;
}
</style>
