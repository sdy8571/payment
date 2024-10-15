import { createApp } from 'vue'
import App from './App.vue'

// 初始化多语言
import {setupI18n} from "./plugins/vueI18n";
// pinia store
import { setupStore } from '@/store'
// 路由
import router, { setupRouter } from '@/router'
// element-plus 组件
import { setupElementPlus } from "./plugins/elementPlus"
// 注册所有图标
import { setupElementPlusIcon } from "./plugins/elementPlusIcon";
// 引入统一样式
import '@/styles/index.scss'
// 权限
import './permission'

const setupAll = async () => {

  const app = createApp(App)
  // i18n
  await setupI18n(app)
  // store
  setupStore(app)
  // element-plus
  setupElementPlus(app)
  // 路由
  setupRouter(app)
  // element-plus-icon
  setupElementPlusIcon(app)

  await router.isReady()
  // 挂载
  app.mount('#app')
}

setupAll()
