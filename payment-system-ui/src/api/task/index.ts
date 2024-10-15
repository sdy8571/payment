import request from '@/config/axios'

export const getPage = async (data: any) => {
  return await request.post({url: '/pay/task/page', data})
}

export const detail = async (id: number) => {
  return await request.get({url: '/pay/app/detail/' + id})
}

export const create = async (data: any) => {
  return await request.post({url: '/pay/app/create', data})
}

export const edit = async (data: any) => {
  return await request.post({url: '/pay/app/edit', data})
}

export const remove = async (id: number) => {
  return await request.post({url: '/pay/app/delete/' + id})
}

export const getAllList = async () => {
  return await request.get({url: '/pay/app/getAllList'})
}
