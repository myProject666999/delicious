<template>
  <div class="login-page">
    <div class="login-container">
      <div class="login-box">
        <div class="login-header">
          <h1><span class="logo-icon">🍜</span> 地方特色美食分享系统</h1>
          <p>欢迎回来，请登录您的账号</p>
        </div>
        
        <el-tabs v-model="activeTab" @tab-click="handleTabClick">
          <el-tab-pane label="用户登录" name="user">
            <el-form ref="userForm" :model="userForm" :rules="loginRules" label-width="0" class="login-form">
              <div class="form-content">
                <el-form-item prop="username">
                  <el-input v-model="userForm.username" placeholder="请输入用户名" prefix-icon="el-icon-user">
                  </el-input>
                </el-form-item>
                <el-form-item prop="password">
                  <el-input v-model="userForm.password" type="password" placeholder="请输入密码" prefix-icon="el-icon-lock" @keyup.enter.native="handleUserLogin">
                  </el-input>
                </el-form-item>
              </div>
              <div class="btn-wrapper">
                <el-button type="primary" class="login-btn" :loading="loading" @click="handleUserLogin">登录</el-button>
              </div>
            </el-form>
          </el-tab-pane>
          
          <el-tab-pane label="管理员登录" name="admin">
            <el-form ref="adminForm" :model="adminForm" :rules="loginRules" label-width="0" class="login-form">
              <div class="form-content">
                <el-form-item prop="username">
                  <el-input v-model="adminForm.username" placeholder="请输入管理员账号" prefix-icon="el-icon-user">
                  </el-input>
                </el-form-item>
                <el-form-item prop="password">
                  <el-input v-model="adminForm.password" type="password" placeholder="请输入密码" prefix-icon="el-icon-lock" @keyup.enter.native="handleAdminLogin">
                  </el-input>
                </el-form-item>
              </div>
              <div class="btn-wrapper">
                <el-button type="primary" class="login-btn" :loading="loading" @click="handleAdminLogin">登录</el-button>
              </div>
            </el-form>
          </el-tab-pane>
        </el-tabs>
        
        <div class="login-footer">
          <p>还没有账号？<router-link to="/register">立即注册</router-link></p>
          <p class="back-home"><router-link to="/">返回首页</router-link></p>
        </div>
      </div>
    </div>
  </div>
</template>

<script>
import { userLogin, adminLogin } from '@/api/user'

export default {
  name: 'Login',
  data() {
    const validateUsername = (rule, value, callback) => {
      if (!value) {
        return callback(new Error('请输入用户名'))
      }
      callback()
    }
    const validatePassword = (rule, value, callback) => {
      if (!value) {
        return callback(new Error('请输入密码'))
      }
      callback()
    }
    return {
      activeTab: 'user',
      loading: false,
      userForm: {
        username: '',
        password: ''
      },
      adminForm: {
        username: '',
        password: ''
      },
      loginRules: {
        username: [{ required: true, validator: validateUsername, trigger: 'blur' }],
        password: [{ required: true, validator: validatePassword, trigger: 'blur' }]
      }
    }
  },
  methods: {
    handleTabClick() {
      this.$refs.userForm && this.$refs.userForm.resetFields()
      this.$refs.adminForm && this.$refs.adminForm.resetFields()
    },
    async handleUserLogin() {
      const valid = await this.$refs.userForm.validate().catch(() => false)
      if (!valid) return
      
      this.loading = true
      try {
        const res = await userLogin(this.userForm)
        this.$store.dispatch('login', {
          token: res.data.token,
          userInfo: res.data.userInfo,
          role: 'user'
        })
        this.$message.success('登录成功')
        this.$router.push('/')
      } catch (e) {
        console.error(e)
      } finally {
        this.loading = false
      }
    },
    async handleAdminLogin() {
      const valid = await this.$refs.adminForm.validate().catch(() => false)
      if (!valid) return
      
      this.loading = true
      try {
        const res = await adminLogin(this.adminForm)
        this.$store.dispatch('login', {
          token: res.data.token,
          userInfo: res.data.userInfo,
          role: 'admin'
        })
        this.$message.success('登录成功')
        this.$router.push('/admin/dashboard')
      } catch (e) {
        console.error(e)
      } finally {
        this.loading = false
      }
    }
  }
}
</script>

<style scoped lang="scss">
.login-page {
  min-height: 100vh;
  background: linear-gradient(135deg, #ff9a9e 0%, #fecfef 50%, #fecfef 100%);
  display: flex;
  align-items: center;
  justify-content: center;
  
  .login-container {
    width: 100%;
    max-width: 360px;
    padding: 20px;
  }
  
  .login-box {
    background: #fff;
    border-radius: 16px;
    box-shadow: 0 10px 40px rgba(0, 0, 0, 0.15);
    overflow: hidden;
    
    .login-header {
      text-align: center;
      padding: 30px 30px 10px;
      
      h1 {
        font-size: 20px;
        color: #333;
        margin-bottom: 10px;
        
        .logo-icon {
          font-size: 24px;
          margin-right: 6px;
        }
      }
      
      p {
        color: #999;
        font-size: 13px;
      }
    }
    
    >>> .el-tabs {
      padding: 0 30px 30px;
      
      >>> .el-tabs__header {
        margin-bottom: 25px;
      }
      
      >>> .el-tabs__item {
        font-size: 14px;
        height: 40px;
        line-height: 40px;
      }
      
      >>> .el-tabs__active-bar {
        background: linear-gradient(135deg, #ff6b6b, #ff8e53);
      }
    }
    
    .login-form {
      .form-content {
        max-width: 260px;
        margin: 0 auto;
        
        >>> .el-form-item {
          margin-bottom: 18px;
        }
        
        >>> .el-input {
          >>> .el-input__inner {
            height: 40px;
            border-radius: 20px;
            padding: 0 16px;
            font-size: 14px;
          }
        }
        
        >>> .el-input-group__prepend {
          padding: 0 12px;
          border-radius: 20px 0 0 20px !important;
          
          >>> .el-input__inner {
            border-radius: 20px 0 0 20px !important;
          }
        }
      }
      
      .btn-wrapper {
        display: flex;
        justify-content: center;
        margin-top: 10px;
        
        .login-btn {
          width: 160px;
          height: 40px;
          font-size: 14px;
          border-radius: 20px;
          background: linear-gradient(135deg, #ff6b6b, #ff8e53);
          border: none;
          
          &:hover, &:focus {
            background: linear-gradient(135deg, #ff5252, #ff7043);
          }
        }
      }
    }
    
    .login-footer {
      padding: 0 30px 30px;
      text-align: center;
      
      p {
        color: #999;
        font-size: 13px;
        margin-bottom: 6px;
        
        a {
          color: #ff6b6b;
        }
      }
      
      .back-home {
        margin-bottom: 0;
      }
    }
  }
}
</style>
