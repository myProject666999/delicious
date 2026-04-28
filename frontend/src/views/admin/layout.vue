<template>
  <el-container class="admin-layout">
    <el-aside :width="isCollapse ? '64px' : '200px'" class="admin-aside">
      <div class="logo" @click="$router.push('/')">
        <span class="logo-icon">🍜</span>
        <span class="logo-text" v-show="!isCollapse">美食分享管理</span>
      </div>
      
      <el-menu
        :default-active="activeMenu"
        :collapse="isCollapse"
        :collapse-transition="false"
        router
        background-color="#304156"
        text-color="#bfcbd9"
        active-text-color="#409EFF">
        
        <el-menu-item index="/admin/dashboard">
          <i class="el-icon-s-home"></i>
          <span slot="title">系统首页</span>
        </el-menu-item>
        
        <el-submenu index="user">
          <template slot="title">
            <i class="el-icon-user"></i>
            <span>用户管理</span>
          </template>
          <el-menu-item index="/admin/user">用户列表</el-menu-item>
        </el-submenu>
        
        <el-submenu index="recipe">
          <template slot="title">
            <i class="el-icon-notebook-2"></i>
            <span>美食菜谱</span>
          </template>
          <el-menu-item index="/admin/recipe-category">菜谱分类</el-menu-item>
          <el-menu-item index="/admin/recipe">菜谱列表</el-menu-item>
        </el-submenu>
        
        <el-submenu index="food">
          <template slot="title">
            <i class="el-icon-goods"></i>
            <span>美食信息</span>
          </template>
          <el-menu-item index="/admin/food-category">美食分类</el-menu-item>
          <el-menu-item index="/admin/food">美食列表</el-menu-item>
        </el-submenu>
        
        <el-menu-item index="/admin/forum">
          <i class="el-icon-chat-dot-round"></i>
          <span slot="title">美食论坛</span>
        </el-menu-item>
        
        <el-menu-item index="/admin/notice">
          <i class="el-icon-bell"></i>
          <span slot="title">系统公告</span>
        </el-menu-item>
        
        <el-menu-item index="/admin/banner">
          <i class="el-icon-picture"></i>
          <span slot="title">轮播图管理</span>
        </el-menu-item>
      </el-menu>
    </el-aside>
    
    <el-container class="admin-main">
      <el-header class="admin-header">
        <div class="header-left">
          <i class="el-icon-fold" @click="isCollapse = !isCollapse"></i>
        </div>
        <div class="header-right">
          <el-dropdown @command="handleCommand">
            <span class="user-dropdown">
              <el-avatar :size="32" icon="el-icon-user-solid"></el-avatar>
              <span class="user-name">{{ userInfo.nickname || userInfo.username }}</span>
              <i class="el-icon-arrow-down el-icon--right"></i>
            </span>
            <el-dropdown-menu slot="dropdown">
              <el-dropdown-item command="profile">个人中心</el-dropdown-item>
              <el-dropdown-item command="home">返回前台</el-dropdown-item>
              <el-dropdown-item divided command="logout">退出登录</el-dropdown-item>
            </el-dropdown-menu>
          </el-dropdown>
        </div>
      </el-header>
      
      <el-main class="admin-content">
        <router-view />
      </el-main>
    </el-container>
  </el-container>
</template>

<script>
import { mapState } from 'vuex'

export default {
  name: 'AdminLayout',
  data() {
    return {
      isCollapse: false
    }
  },
  computed: {
    ...mapState(['userInfo']),
    activeMenu() {
      return this.$route.path
    }
  },
  methods: {
    handleCommand(command) {
      switch (command) {
        case 'profile':
          this.$router.push('/admin/profile')
          break
        case 'home':
          this.$router.push('/')
          break
        case 'logout':
          this.$confirm('确定要退出登录吗？', '提示', {
            confirmButtonText: '确定',
            cancelButtonText: '取消',
            type: 'warning'
          }).then(() => {
            this.$store.dispatch('logout')
            this.$router.push('/login')
            this.$message.success('已退出登录')
          }).catch(() => {})
          break
      }
    }
  }
}
</script>

<style scoped lang="scss">
.admin-layout {
  height: 100vh;
}

.admin-aside {
  background-color: #304156;
  transition: width 0.3s;
  
  .logo {
    height: 60px;
    display: flex;
    align-items: center;
    justify-content: center;
    background-color: #2b3a4a;
    cursor: pointer;
    
    .logo-icon {
      font-size: 28px;
    }
    
    .logo-text {
      color: #fff;
      font-size: 16px;
      font-weight: bold;
      margin-left: 10px;
      white-space: nowrap;
    }
  }
  
  >>> .el-menu {
    border-right: none;
  }
}

.admin-main {
  display: flex;
  flex-direction: column;
  background-color: #f0f2f5;
}

.admin-header {
  background: #fff;
  box-shadow: 0 1px 4px rgba(0, 21, 41, 0.08);
  padding: 0 20px;
  display: flex;
  align-items: center;
  justify-content: space-between;
  
  .header-left {
    .el-icon-fold {
      font-size: 20px;
      cursor: pointer;
      color: #666;
      
      &:hover {
        color: #409EFF;
      }
    }
  }
  
  .header-right {
    .user-dropdown {
      display: flex;
      align-items: center;
      gap: 10px;
      cursor: pointer;
      
      .user-name {
        color: #666;
        font-size: 14px;
      }
    }
  }
}

.admin-content {
  padding: 20px;
  overflow-y: auto;
}
</style>
