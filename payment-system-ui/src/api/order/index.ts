import request from '@/config/axios'

export const getPage = async (data: any) => {
  return await request.post({url: '/pay/order/page', data})
}

export const detail = async (id: number) => {
  return await request.post({url: '/pay/order/get', data: {id}})
}
