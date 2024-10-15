import {defineStore} from 'pinia'
import {store} from '../index'
import remainingRouter from '@/router/modules/remaining'
import {RouteMeta} from "vue-router";
import {Component} from "vue";

const views = import.meta.glob('@/views/**/**.vue');


export interface AppRouteRecordRaw {
  path: string
  meta: RouteMeta
  component?: Component | string
  children?: AppRouteRecordRaw[]
  fullPath?: string
  keepAlive?: boolean
}

export const usePermissionStore = defineStore('permission', () => {
  let routerMap: any = ref({});

  const routerList = computed(() => {
    return generateRouterList({}, remainingRouter)
  })

  function generateRouterList(parentObj: any, permList: any[]) {
    let result: any[] = [];
    if (!permList || permList.length === 0) {
      return result;
    }

    permList.forEach(permItem => {
      // 补充字段
      if (!permItem.meta) {
        permItem.meta = {};
      }
      if (!permItem.meta.isParentView) {
        permItem.meta.isParentView = false;
      }
      if (!permItem.meta.sort) {
        permItem.meta.sort = 10000;
      }
      //
      let title = permItem.meta.title;
      if (title) {
        if (parentObj.meta) {
          // 面包屑数据
          if (parentObj.meta.breadcrumbItemList) {
            permItem.meta.breadcrumbItemList = parentObj.meta.breadcrumbItemList.concat([title]);
          }
          // 全路径
          permItem.meta.fullPath = parentObj.meta.fullPath + '/' + permItem.path;
        } else {
          // [顶级]
          permItem.meta.breadcrumbItemList = [title];
          permItem.meta.fullPath = permItem.path;
        }
      }
      // 组件页面显示处理
      if (typeof(permItem.component) === 'string') {
        permItem.component = views[`/src/views/${permItem.component}.vue`];
      }

      routerMap.value[permItem.meta.fullPath] = permItem;

      // 递归处理
      if (permItem.children && permItem.children.length > 0) {
        permItem.children = generateRouterList(permItem, permItem.children);
      }

      result.push(permItem);
    })
    // 从小到大 升序
    result = result.sort((a, b) => {
      return a.meta.sort - b.meta.sort;
    });
    return result;
  }

  return { routerList, routerMap };
});

export const usePermissionStoreWithOut = () => {
  return usePermissionStore(store)
}
