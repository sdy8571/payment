import router from './router';
import {usePermissionStoreWithOut} from "./store/modules/permission";
import { usePageLoading } from "./hooks/web/usePageLoading";

// 进度条
import NProgress from 'nprogress';
import 'nprogress/nprogress.css';
NProgress.configure({ showSpinner: true });

const { loadStart, loadDone } = usePageLoading()

const whiteList = ['/login'];

let hasRouter = false;

router.beforeEach(async (to, from, next)=> {
  // 开启进度条
  NProgress.start();
  loadStart()
  // 处理菜单
  try {
    if (hasRouter) {
      next();
    } else {
      const permssionStore = usePermissionStoreWithOut()
      permssionStore.routerList.forEach(e => router.addRoute(e));
      hasRouter = true
      next({ ...to, replace: true });
    }
  } catch (ex) {
    next({ path: '/' }); // 跳转到首页
  }
})

router.afterEach(() => {
  NProgress.done();
  loadDone()
})
