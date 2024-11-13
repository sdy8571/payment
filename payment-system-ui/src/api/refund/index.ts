import request from '@/config/axios'

export const getPage = async (data: any) => {
  return await request.post({url: '/pay/refund/page', data})
}

export const detail = async (id: number) => {
  return await request.post({url: '/pay/refund/get', data: {id}})
}
