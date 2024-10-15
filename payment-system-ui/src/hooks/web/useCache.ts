/**
 * 配置浏览器本地存储的方式，可直接存储对象数组。
 */

import WebStorageCache from 'web-storage-cache'

type CacheType = 'localStorage' | 'sessionStorage'

const CACHE_KEY_PREFIX = import.meta.env.VITE_CACHE_PREFIX

export const CACHE_KEY = {
  IS_DARK: 'isDark',
  USER: 'user',
  LANG: 'lang',
  THEME: 'theme',
  LAYOUT: 'layout',
  ROLE_ROUTERS: 'roleRouters',
  DICT_CACHE: 'dictCache'
}

export const useCache = (type: CacheType = 'localStorage') => {
  const wsCache: WebStorageCache = new WebStorageCache({
    storage: type
  })

  const temp: WebStorageCache = new WebStorageCache({
    storage: type
  })

  wsCache.set = (key, value, options) => {
    temp.set(CACHE_KEY_PREFIX + key, value, options);
  }
  wsCache.get = (key) => {
    return temp.get(CACHE_KEY_PREFIX + key);
  }
  wsCache.delete = (key) => {
    temp.delete(CACHE_KEY_PREFIX + key);
  }

  return {
    wsCache
  }
}
