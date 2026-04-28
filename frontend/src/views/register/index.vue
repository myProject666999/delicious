<template>
  <div class="register-page">
    <div class="register-container">
      <div class="register-box">
        <div class="register-header">
          <h1><span class="logo-icon">🍜</span> 用户注册</h1>
          <p>加入我们，分享您的美食故事</p>
        </div>
        
        <el-form ref="registerForm" :model="registerForm" :rules="registerRules" label-width="80px" class="register-form">
          <div class="form-content">
            <el-form-item label="用户名" prop="username">
              <el-input v-model="registerForm.username" placeholder="请输入用户名"></el-input>
            </el-form-item>
            <el-form-item label="密码" prop="password">
              <el-input v-model="registerForm.password" type="password" placeholder="请输入密码"></el-input>
            </el-form-item>
            <el-form-item label="确认密码" prop="confirmPassword">
              <el-input v-model="registerForm.confirmPassword" type="password" placeholder="请再次输入密码"></el-input>
            </el-form-item>
            <el-form-item label="昵称" prop="nickname">
              <el-input v-model="registerForm.nickname" placeholder="请输入昵称"></el-input>
            </el-form-item>
            <el-form-item label="手机号" prop="phone">
              <el-input v-model="registerForm.phone" placeholder="请输入手机号"></el-input>
            </el-form-item>
            <el-form-item label="邮箱" prop="email">
              <el-input v-model="registerForm.email" placeholder="请输入邮箱"></el-input>
            </el-form-item>
          </div>
          <div class="btn-wrapper">
            <el-button type="primary" class="register-btn" :loading="loading" @click="handleRegister">注册</el-button>
          </div>
        </el-form>
        
        <div class="register-footer">
          <p>已有账号？<router-link to="/login">立即登录</router-link></p>
          <p class="back-home"><router-link to="/">返回首页</router-link></p>
        </div>
      </div>
    </div>
  </div>
</template>

<script>
import { userRegister } from '@/api/user'

export default {
  name: 'Register',
  data() {
    const validateConfirmPassword = (rule, value, callback) => {
      if (value !== this.registerForm.password) {
        return callback(new Error('两次输入的密码不一致'))
      }
      callback()
    }
    return {
      loading: false,
      registerForm: {
        username: '',
        password: '',
        confirmPassword: '',
        nickname: '',
        phone: '',
        email: ''
      },
      registerRules: {
        username: [
          { required: true, message: '请输入用户名', trigger: 'blur' },
          { min: 3, max: 20, message: '用户名长度在 3 到 20 个字符', trigger: 'blur' }
        ],
        password: [
          { required: true, message: '请输入密码', trigger: 'blur' },
          { min: 6, max: 20, message: '密码长度在 6 到 20 个字符', trigger: 'blur' }
        ],
        confirmPassword: [
          { required: true, message: '请再次输入密码', trigger: 'blur' },
          { validator: validateConfirmPassword, trigger: 'blur' }
        ],
        nickname: [
          { min: 2, max: 20, message: '昵称长度在 2 到 20 个字符', trigger: 'blur' }
        ],
        phone: [
          { pattern: /^1[3-9]\d{9}$/, message: '请输入正确的手机号', trigger: 'blur' }
        ],
        email: [
          { type: 'email', message: '请输入正确的邮箱地址', trigger: 'blur' }
        ]
      }
    }
  },
  methods: {
    async handleRegister() {
      const valid = await this.$refs.registerForm.validate().catch(() => false)
      if (!valid) return
      
      this.loading = true
      try {
        await userRegister(this.registerForm)
        this.$message.success('注册成功，请登录')
        this.$router.push('/login')
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
.register-page {
  min-height: 100vh;
  background: linear-gradient(135deg, #ff9a9e 0%, #fecfef 50%, #fecfef 100%);
  display: flex;
  align-items: center;
  justify-content: center;
  
  .register-container {
    width: 100%;
    max-width: 400px;
    padding: 20px;
  }
  
  .register-box {
    background: #fff;
    border-radius: 16px;
    box-shadow: 0 10px 40px rgba(0, 0, 0, 0.15);
    overflow: hidden;
    
    .register-header {
      text-align: center;
      padding: 25px 30px 10px;
      
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
    
    >>> .el-form {
      padding: 0 30px 15px;
    }
    
    .register-form {
      .form-content {
        max-width: 280px;
        margin: 0 auto;
        
        >>> .el-form-item {
          margin-bottom: 15px;
        }
        
        >>> .el-form-item__label {
          font-size: 13px;
          color: #666;
          line-height: 40px;
        }
        
        >>> .el-input {
          >>> .el-input__inner {
            height: 40px;
            border-radius: 20px;
            padding: 0 16px;
            font-size: 13px;
          }
        }
      }
      
      .btn-wrapper {
        display: flex;
        justify-content: center;
        margin-top: 5px;
        
        .register-btn {
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
    
    .register-footer {
      padding: 0 30px 25px;
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
