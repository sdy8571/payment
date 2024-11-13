import request from '@/config/axios'

export const getPage = async (data: any) => {
  return await request.post({url: '/pay/channel/page', data})
}

export const detail = async (id: number) => {
  return await request.get({url: '/pay/channel/detail/' + id})
}

export const create = async (data: any) => {
  return await request.post({url: '/pay/channel/create', data})
}

export const edit = async (data: any) => {
  return await request.post({url: '/pay/channel/edit', data})
}

export const remove = async (id: number) => {
  return await request.post({url: '/pay/channel/delete/' + id})
}

export const getTypeList = async () => {
  return await request.get({url: '/pay/channel/type-list'})
}

export const status = async (id: number) => {
  return await request.post({url: '/pay/channel/status/' + id})
}

export const getChannelList = async (appId: number) => {
  return await request.get({url: '/pay/channel/list', params: {appId}})
}
