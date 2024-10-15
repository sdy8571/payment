import * as RegionApi from '@/api/region'
import {useCache} from "@/hooks/web/useCache";
import {defaultProps, handleTree} from '@/utils/tree'

const {wsCache} = useCache()

export const REGION_CACHE_KEY = 'NIUMO_REGION_CACHE';

export const initRegionTree = async () => {
  const region = await RegionApi.getRegionAll()
  wsCache.set(REGION_CACHE_KEY, JSON.stringify(region))
}

export const getRegionTree = async () => {
  const regions = await RegionApi.getRegionListWithChildren()
  return handleTree(regions)
}

export const getRegionText2 = (val: string) => {
  if (!val) {
    return ''
  }

  const regions = wsCache.get(REGION_CACHE_KEY)
  let regionList = [];
  if (regions) {
    regionList = JSON.parse(regions)
  }

  const arr = val.split('/')
  let res = ''
  for (let i = 0; i < arr.length; i++) {
    const code = arr[i]
    const node = regionList.find(item => item.id === code)
    if (i > 0) {
      res += '/'
    }
    if (node) {
      res += node.name
    } else {
      res += code
    }
  }
  return res
}
