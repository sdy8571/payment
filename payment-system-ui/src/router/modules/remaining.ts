const Layout = () => import('@/layout/index.vue')

const remainingRouter = [
  {
    path: '/403',
    component: () => import('@/views/Error/403.vue'),
    name: 'NoAccess',
    meta: {
      hidden: true,
      title: '403',
      noTagsView: true
    }
  },
  {
    path: '/404',
    component: () => import('@/views/Error/404.vue'),
    name: 'NoFound',
    meta: {
      hidden: true,
      title: '404',
      noTagsView: true
    }
  },
  {
    path: '/500',
    component: () => import('@/views/Error/500.vue'),
    name: 'Error',
    meta: {
      hidden: true,
      title: '500',
      noTagsView: true
    }
  },
  {
    path: '/',
    component: '',
    redirect: 'index',
    meta: { hidden: true }
  },
  {
    path: '/index',
    component: () => import('@/views/Home/index.vue'),
    name: 'Index',
    meta: {
      hidden: false,
      title: '首页',
      icon: 'House',
      noCache: false,
    }
  },
  {
    path: '/payment',
    component: '',
    name: 'Payment',
    meta: {
      hidden: false,
      title: '支付管理',
      icon: 'Menu',
      noCache: false,
    },
    children: [
      {
        path: 'app',
        component: () => import('@/views/payment/app/index.vue'),
        name: 'App',
        meta: {
          hidden: false,
          title: '支付应用',
          noCache: false,
        }
      },
      {
        path: 'channel',
        component: () => import('@/views/payment/channel/index.vue'),
        name: 'Channel',
        meta: {
          hidden: false,
          title: '支付渠道',
          noCache: false,
        }
      },
    ]
  },
  {
    path: '/pay',
    component: '',
    name: 'Pay',
    meta: {
      hidden: false,
      title: '订单管理',
      icon: 'List',
      noCache: false,
    },
    children: [
      {
        path: 'order',
        component: () => import('@/views/payment/order/index.vue'),
        name: 'Order',
        meta: {
          hidden: false,
          title: '订单列表',
          noCache: false,
        }
      },
      {
        path: 'refund',
        component: () => import('@/views/payment/refund/index.vue'),
        name: 'Refund',
        meta: {
          hidden: false,
          title: '退款列表',
          noCache: false,
        }
      },
    ]
  },
  {
    path: '/notify',
    component: '',
    name: 'Notify',
    meta: {
      hidden: false,
      title: '通知管理',
      icon: 'ChatLineSquare',
      noCache: false,
    },
    children: [
      {
        path: 'task',
        component: () => import('@/views/payment/task/index.vue'),
        name: 'Task',
        meta: {
          hidden: false,
          title: '通知任务',
          noCache: false,
        }
      },
    ]
  },
]

export default remainingRouter
