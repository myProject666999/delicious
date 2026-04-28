<template>
  <div class="profile-page">
    <Header />
    
    <div class="main-content container">
      <div class="profile-container">
        <div class="profile-sidebar">
          <div class="user-info-card card">
            <el-avatar :size="80" :src="userInfo.avatar" icon="el-icon-user-solid"></el-avatar>
            <h3 class="user-name">{{ userInfo.nickname || userInfo.username }}</h3>
            <p class="user-role">普通用户</p>
          </div>
          
          <el-menu
            :default-active="activeMenu"
            class="sidebar-menu"
            router
            background-color="#fff"
            text-color="#666"
            active-text-color="#ff6b6b">
            <el-menu-item index="/profile">
              <i class="el-icon-user"></i>
              <span>个人信息</span>
            </el-menu-item>
            <el-menu-item index="/profile/my-recipes">
              <i class="el-icon-notebook-2"></i>
              <span>我的菜谱</span>
            </el-menu-item>
            <el-menu-item index="/profile/my-foods">
              <i class="el-icon-goods"></i>
              <span>我的美食</span>
            </el-menu-item>
            <el-menu-item index="/profile/my-posts">
              <i class="el-icon-chat-dot-round"></i>
              <span>我的帖子</span>
            </el-menu-item>
            <el-menu-item index="/profile/settings">
              <i class="el-icon-setting"></i>
              <span>账号设置</span>
            </el-menu-item>
          </el-menu>
        </div>
        
        <div class="profile-content">
          <router-view />
        </div>
      </div>
    </div>
    
    <Footer />
  </div>
</template>

<script>
import Header from '@/components/Header.vue'
import Footer from '@/components/Footer.vue'
import { mapState } from 'vuex'

export default {
  name: 'Profile',
  components: { Header, Footer },
  data() {
    return {
      activeMenu: this.$route.path
    }
  },
  computed: {
    ...mapState(['userInfo'])
  },
  watch: {
    '$route.path': {
      handler(newPath) {
        this.activeMenu = newPath
      },
      immediate: true
    }
  }
}
</script>

<style scoped lang="scss">
.profile-page {
  min-height: 100vh;
  background: #f5f5f5;
}

.main-content {
  padding: 30px 0 60px;
}

.profile-container {
  display: flex;
  gap: 20px;
}

.profile-sidebar {
  width: 240px;
  flex-shrink: 0;
  
  .user-info-card {
    padding: 30px 20px;
    text-align: center;
    margin-bottom: 20px;
    
    .el-avatar {
      margin-bottom: 15px;
    }
    
    .user-name {
      font-size: 18px;
      color: #333;
      margin-bottom: 5px;
    }
    
    .user-role {
      font-size: 13px;
      color: #999;
    }
  }
  
  .sidebar-menu {
    border: none;
    
    >>> .el-menu-item {
      height: 50px;
      line-height: 50px;
      
      &.is-active {
        background: #fff5f5;
      }
    }
  }
}

.profile-content {
  flex: 1;
  min-width: 0;
}

@media screen and (max-width: 768px) {
  .profile-container {
    flex-direction: column;
  }
  
  .profile-sidebar {
    width: 100%;
  }
}
</style>
