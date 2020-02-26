import Vue from 'vue'
import Router from 'vue-router'

Vue.use(Router)

/* Layout */
import Layout from '@/layout'

/**
 * Note: 路由配置项
 *
 * hidden: true                   // 当设置 true 的时候该路由不会再侧边栏出现 如401，login等页面，或者如一些编辑页面/edit/1
 * alwaysShow: true               // 当你一个路由下面的 children 声明的路由大于1个时，自动会变成嵌套的模式--如组件页面
 *                                // 只有一个时，会将那个子路由当做根路由显示在侧边栏--如引导页面
 *                                // 若你想不管路由下面的 children 声明的个数都显示你的根路由
 *                                // 你可以设置 alwaysShow: true，这样它就会忽略之前定义的规则，一直显示根路由
 * redirect: noRedirect           // 当设置 noRedirect 的时候该路由在面包屑导航中不可被点击
 * name:'router-name'             // 设定路由的名字，一定要填写不然使用<keep-alive>时会出现各种问题
 * meta : {
    roles: ['admin','editor']    // 设置该路由进入的权限，支持多个权限叠加
    title: 'title'               // 设置该路由在侧边栏和面包屑中展示的名字
    icon: 'svg-name'             // 设置该路由的图标，对应路径src/icons/svg
    breadcrumb: false            // 如果设置为false，则不会在breadcrumb面包屑中显示
  }
 */

// 公共路由
export const constantRoutes = [{
    path: '/redirect',
    component: Layout,
    hidden: true,
    children: [{
      path: '/redirect/:path*',
      component: () => import('@/views/redirect')
    }]
  },
  {
    path: '/login',
    component: () => import('@/views/login'),
    hidden: true
  },
  {
    path: '/404',
    component: () => import('@/views/error/404'),
    hidden: true
  },
  {
    path: '/401',
    component: () => import('@/views/error/401'),
    hidden: true
  },
  {
    path: '',
    component: Layout,
    redirect: 'index',
    children: [{
      path: 'index',
      component: () => import('@/views/index'),
      name: '首页',
      meta: {
        title: '首页',
        icon: 'dashboard',
        noCache: true,
        affix: true
      }
    }]
  },
  {
    path: '/user',
    component: Layout,
    hidden: true,
    redirect: 'noredirect',
    children: [{
        path: 'profile',
        component: () => import('@/views/system/user/profile/index'),
        name: 'Profile',
        meta: {
          title: '个人中心',
          icon: 'user'
        }
      },
      {
        path: 'profile/userId/:userId(\\d+)',
        component: () => import('@/views/system/user/profile/index'),
        name: 'Profile',
        meta: {
          title: '用户信息',
          icon: 'user'
        }
      }
    ]
  },
  {
    path: '/dict',
    component: Layout,
    hidden: true,
    children: [{
      path: 'type/data/:dictId(\\d+)',
      component: () => import('@/views/system/dict/data'),
      name: 'Data',
      meta: {
        title: '字典数据',
        icon: ''
      }
    }]
  },
  {
    path: '/gen',
    component: Layout,
    hidden: true,
    children: [{
      path: 'edit',
      component: () => import('@/views/tool/gen/editTable'),
      name: 'GenEdit',
      meta: {
        title: '修改生成配置'
      }
    }]
  },
  {
    path: '/select',
    component: Layout,
    hidden: true,
    children: [{
        path: 'course/list/:openId(\\d+)',
        component: () => import('@/views/elective/select/list'),
        name: 'Select',
        meta: {
          title: '选课列表'
        }
      },
      {
        path: 'record/list/:studentId(\\d+)',
        component: () => import('@/views/elective/record/select/index'),
        name: 'SelectRecord',
        meta: {
          title: '选课记录'
        }
      },
      {
        path: 'record/course/student',
        component: () => import('@/views/elective/record/select/index'),
        name: 'SelectStudent',
        meta: {
          title: '选课学生'
        }
      }
    ]
  },
  {
    path: '/config',
    component: Layout,
    hidden: true,
    children: [{
      path: 'value/:templateId(\\d+)',
      component: () => import('@/views/elective/config/value'),
      name: 'SelectRecord',
      meta: {
        title: '模板数据'
      }
    }]
  },
  {
    path: '/course',
    component: Layout,
    hidden: true,
    children: [{
        path: 'list/:teacherId(\\d+)',
        component: () => import('@/views/elective/course/index'),
        name: 'Course',
        meta: {
          title: '课程列表'
        }
      },
      {
        path: 'info/:courseId(\\d+)',
        component: () => import('@/views/elective/course/info'),
        name: 'Course',
        meta: {
          title: '课程详情'
        }
      }
    ]
  },
  {
    path: '/apply',
    component: Layout,
    hidden: true,
    children: [{
      path: 'list/:teacherId(\\d+)',
      component: () => import('@/views/elective/record/apply/index'),
      name: 'Apply',
      meta: {
        title: '申请列表'
      }
    }]
  },
  {
    path: '/mobile',
    component: Layout,
    hidden: true,
    children: [{
      path: '',
      component: () => import('@/views-mobile/index'),
      name: 'MobileIndex',
      meta: {
        title: '移动端首页'
      }
    },
    {
      path: 'select/student/open',
      component: () => import('@/views-mobile/elective/select/index'),
      name: 'MobileStudentOpen',
      meta: {
        title: '进入选课'
      }
    },
    {
      path: 'select/student/course/:openId(\\d+)',
      component: () => import('@/views-mobile/elective/select/list'),
      name: 'MobileSelectCourse',
      meta: {
        title: '选课列表'
      }
    },
    {
      path: 'select/record',
      component: () => import('@/views-mobile/elective/record/select/index'),
      name: 'MobileSelectRecord',
      meta: {
        title: '选课记录'
      }
    },
    {
      path: 'course',
      component: () => import('@/views-mobile/elective/course/index'),
      name: 'MobileCourse',
      meta: {
        title: '课程列表'
      }
    },
    {
      path: 'course/form',
      component: () => import('@/views-mobile/elective/course/form'),
      name: 'MobileCourseForm',
      meta: {
        title: '课程详情'
      }
    },
    {
      path: 'apply/record',
      component: () => import('@/views-mobile/elective/record/apply/index'),
      name: 'MobileApplyRecord',
      meta: {
        title: '申请记录'
      }
    },
    {
      path: 'select/statistic',
      component: () => import('@/views-mobile/elective/select/statistic'),
      name: 'MobileStatistic',
      meta: {
        title: '选课统计'
      }
    }
    ]
  },
]

export default new Router({
  mode: 'history', // 去掉url中的#
  scrollBehavior: () => ({
    y: 0
  }),
  routes: constantRoutes
})
