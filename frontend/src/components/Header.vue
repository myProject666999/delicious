<template>
  <header class="header">
    <div class="container flex-between">
      <div class="logo" @click="$router.push('/')">
        <span class="logo-icon">🍜</span>
        <span class="logo-text">地方特色美食分享系统</span>
      </div>
      <nav class="nav">
        <router-link to="/" exact-active-class="active">首页</router-link>
        <router-link to="/recipe" active-class="active">美食菜谱</router-link>
        <router-link to="/food" active-class="active">美食信息</router-link>
        <router-link to="/forum" active-class="active">美食论坛</router-link>
        <router-link to="/notice" active-class="active">系统公告</router-link>
      </nav>
      <div class="user-actions">
        <template v-if="isLoggedIn">
          <div class="user-info" @click="showUserMenu = !showUserMenu">
            <span class="user-name">{{ userInfo.nickname || userInfo.username }}</span>
            <i class="el-icon-arrow-down"></i>
          </div>
          <el-dropdown :hide-on-click="false" trigger="click" @command="handleCommand">
            <span class="el-dropdown-link">
              <el-avatar :size="36" icon="el-icon-user-solid"></el-avatar>
            </span>
            <el-dropdown-menu slot="dropdown">
              <el-dropdown-item command="profile">个人中心</el-dropdown-item>
              <el-dropdown-item v-if="isAdmin" command="admin">管理后台</el-dropdown-item>
              <el-dropdown-item divided command="logout">退出登录</el-dropdown-item>
            </el-dropdown-menu>
          </el-dropdown>
        </template>
        <template v-else>
          <el-button type="text" @click="$router.push('/login')">登录</el-button>
          <el-button type="text" @click="$router.push('/register')">注册</el-button>
        </template>
      </div>
    </div>
  </header>
</template>

<script>
import { mapState, mapGetters } from 'vuex'

export default {
  name: 'Header',
  data() {
    return {
      showUserMenu: false
    }
  },
  computed: {
    ...mapState(['userInfo', 'role']),
    ...mapGetters(['isLoggedIn', 'isAdmin'])
  },
  methods: {
    handleCommand(command) {
      switch (command) {
        case 'profile':
          this.$router.push('/profile')
          break
        case 'admin':
          this.$router.push('/admin/dashboard')
          break
        case 'logout':
          this.$store.dispatch('logout')
          this.$router.push('/')
          this.$message.success('已退出登录')
          break
      }
    }
  }
}
</script>

<style scoped lang="scss">
.header {
  background: linear-gradient(135deg, #ff6b6b, #ff8e53);
  box-shadow: 0 2px 10px rgba(0, 0, 0, 0.1);
  position: sticky;
  top: 0;
  z-index: 1000;
  
  .container {
    height: 60px;
  }
  
  .logo {
    display: flex;
    align-items: center;
    cursor: pointer;
    
    .logo-icon {
      font-size: 32px;
      margin-right: 10px;
    }
    
    .logo-text {
      font-size: 20px;
      font-weight: bold;
      color: #fff;
      letter-spacing: 1px;
    }
  }
  
  .nav {
    display: flex;
    gap: 30px;
    
    a {
      color: rgba(255, 255, 255, 0.9);
      font-size: 16px;
      transition: all 0.3s;
      padding: 10px 0;
      position: relative;
      
      &:hover, &.active {
        color: #fff;
        
        &::after {
          content: '';
          position: absolute;
          bottom: 0;
          left: 0;
          width: 100%;
          height: 2px;
          background: #fff;
        }
      }
    }
  }
  
  .user-actions {
    display: flex;
    align-items: center;
    gap: 15px;
    
    .el-button {
      color: rgba(255, 255, 255, 0.9);
      font-size: 15px;
      
      &:hover {
        color: #fff;
      }
    }
    
    .user-info {
      color: #fff;
      cursor: pointer;
      display: flex;
      align-items: center;
      gap: 5px;
    }
    
    .el-dropdown-link {
      cursor: pointer;
      display: flex;
      align-items: center;
    }
  }
}
</style>
