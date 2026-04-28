import Vue from 'vue'
import VueRouter from 'vue-router'

Vue.use(VueRouter)

const routes = [
  {
    path: '/',
    name: 'Home',
    component: () => import('@/views/home/index.vue'),
    meta: { title: '首页' }
  },
  {
    path: '/login',
    name: 'Login',
    component: () => import('@/views/login/index.vue'),
    meta: { title: '登录' }
  },
  {
    path: '/register',
    name: 'Register',
    component: () => import('@/views/register/index.vue'),
    meta: { title: '注册' }
  },
  {
    path: '/recipe',
    name: 'Recipe',
    component: () => import('@/views/recipe/index.vue'),
    meta: { title: '美食菜谱' }
  },
  {
    path: '/recipe/detail/:id',
    name: 'RecipeDetail',
    component: () => import('@/views/recipe/detail.vue'),
    meta: { title: '菜谱详情' }
  },
  {
    path: '/food',
    name: 'Food',
    component: () => import('@/views/food/index.vue'),
    meta: { title: '美食信息' }
  },
  {
    path: '/food/detail/:id',
    name: 'FoodDetail',
    component: () => import('@/views/food/detail.vue'),
    meta: { title: '美食详情' }
  },
  {
    path: '/forum',
    name: 'Forum',
    component: () => import('@/views/forum/index.vue'),
    meta: { title: '美食论坛' }
  },
  {
    path: '/forum/detail/:id',
    name: 'ForumDetail',
    component: () => import('@/views/forum/detail.vue'),
    meta: { title: '论坛详情' }
  },
  {
    path: '/notice',
    name: 'Notice',
    component: () => import('@/views/notice/index.vue'),
    meta: { title: '系统公告' }
  },
  {
    path: '/notice/detail/:id',
    name: 'NoticeDetail',
    component: () => import('@/views/notice/detail.vue'),
    meta: { title: '公告详情' }
  },
  {
    path: '/admin',
    name: 'Admin',
    component: () => import('@/views/admin/layout.vue'),
    redirect: '/admin/dashboard',
    children: [
      {
        path: 'dashboard',
        name: 'Dashboard',
        component: () => import('@/views/admin/dashboard.vue'),
        meta: { title: '系统首页', requireAuth: true }
      },
      {
        path: 'user',
        name: 'AdminUser',
        component: () => import('@/views/admin/user/index.vue'),
        meta: { title: '用户管理', requireAuth: true }
      },
      {
        path: 'recipe',
        name: 'AdminRecipe',
        component: () => import('@/views/admin/recipe/index.vue'),
        meta: { title: '美食菜谱管理', requireAuth: true }
      },
      {
        path: 'recipe-category',
        name: 'AdminRecipeCategory',
        component: () => import('@/views/admin/recipe/category.vue'),
        meta: { title: '菜谱分类管理', requireAuth: true }
      },
      {
        path: 'food',
        name: 'AdminFood',
        component: () => import('@/views/admin/food/index.vue'),
        meta: { title: '美食信息管理', requireAuth: true }
      },
      {
        path: 'food-category',
        name: 'AdminFoodCategory',
        component: () => import('@/views/admin/food/category.vue'),
        meta: { title: '美食分类管理', requireAuth: true }
      },
      {
        path: 'forum',
        name: 'AdminForum',
        component: () => import('@/views/admin/forum/index.vue'),
        meta: { title: '美食论坛', requireAuth: true }
      },
      {
        path: 'notice',
        name: 'AdminNotice',
        component: () => import('@/views/admin/notice/index.vue'),
        meta: { title: '系统公告', requireAuth: true }
      },
      {
        path: 'banner',
        name: 'AdminBanner',
        component: () => import('@/views/admin/banner/index.vue'),
        meta: { title: '轮播图管理', requireAuth: true }
      },
      {
        path: 'profile',
        name: 'AdminProfile',
        component: () => import('@/views/admin/profile.vue'),
        meta: { title: '个人中心', requireAuth: true }
      }
    ]
  },
  {
    path: '/profile',
    name: 'Profile',
    component: () => import('@/views/profile/index.vue'),
    meta: { title: '个人中心', requireAuth: true }
  }
]

const router = new VueRouter({
  mode: 'history',
  base: process.env.BASE_URL,
  routes
})

router.beforeEach((to, from, next) => {
  if (to.meta.title) {
    document.title = to.meta.title + ' - 地方特色美食分享系统'
  }
  
  const token = localStorage.getItem('token')
  const userInfo = localStorage.getItem('userInfo')
  
  if (to.meta.requireAuth && !token) {
    next('/login')
  } else if (to.path === '/admin/login' && token) {
    next('/admin/dashboard')
  } else {
    next()
  }
})

export default router
