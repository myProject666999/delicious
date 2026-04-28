<template>
  <div class="profile-settings">
    <div class="content-card card">
      <div class="content-header">
        <h2>账号设置</h2>
      </div>
      
      <el-tabs v-model="activeTab">
        <el-tab-pane label="修改密码" name="password">
          <el-form :model="passwordForm" :rules="passwordRules" ref="passwordForm" label-width="120px">
            <el-form-item label="原密码" prop="oldPassword">
              <el-input v-model="passwordForm.oldPassword" type="password" placeholder="请输入原密码" show-password></el-input>
            </el-form-item>
            <el-form-item label="新密码" prop="newPassword">
              <el-input v-model="passwordForm.newPassword" type="password" placeholder="请输入新密码" show-password></el-input>
            </el-form-item>
            <el-form-item label="确认新密码" prop="confirmPassword">
              <el-input v-model="passwordForm.confirmPassword" type="password" placeholder="请再次输入新密码" show-password></el-input>
            </el-form-item>
            <el-form-item>
              <el-button type="primary" @click="handlePasswordSubmit" :loading="passwordLoading">保存修改</el-button>
            </el-form-item>
          </el-form>
        </el-tab-pane>
        
        <el-tab-pane label="账号安全" name="security">
          <div class="security-list">
            <div class="security-item">
              <div class="security-info">
                <h4>绑定手机号</h4>
                <p class="status" v-if="phoneStatus">
                  <i class="el-icon-circle-check" style="color: #67c23a"></i>
                  已绑定
                </p>
                <p class="status" v-else>
                  <i class="el-icon-circle-close" style="color: #f56c6c"></i>
                  未绑定
                </p>
              </div>
              <el-button type="text">修改</el-button>
            </div>
            
            <div class="security-item">
              <div class="security-info">
                <h4>绑定邮箱</h4>
                <p class="status" v-if="emailStatus">
                  <i class="el-icon-circle-check" style="color: #67c23a"></i>
                  已绑定
                </p>
                <p class="status" v-else>
                  <i class="el-icon-circle-close" style="color: #f56c6c"></i>
                  未绑定
                </p>
              </div>
              <el-button type="text">修改</el-button>
            </div>
          </div>
        </el-tab-pane>
      </el-tabs>
    </div>
  </div>
</template>

<script>
import { mapState } from 'vuex'

export default {
  name: 'ProfileSettings',
  data() {
    const validateConfirmPassword = (rule, value, callback) => {
      if (value !== this.passwordForm.newPassword) {
        return callback(new Error('两次输入的密码不一致'))
      }
      callback()
    }
    
    return {
      activeTab: 'password',
      passwordLoading: false,
      passwordForm: {
        oldPassword: '',
        newPassword: '',
        confirmPassword: ''
      },
      passwordRules: {
        oldPassword: [{ required: true, message: '请输入原密码', trigger: 'blur' }],
        newPassword: [
          { required: true, message: '请输入新密码', trigger: 'blur' },
          { min: 6, max: 20, message: '密码长度在 6 到 20 个字符', trigger: 'blur' }
        ],
        confirmPassword: [
          { required: true, message: '请再次输入新密码', trigger: 'blur' },
          { validator: validateConfirmPassword, trigger: 'blur' }
        ]
      }
    }
  },
  computed: {
    ...mapState(['userInfo']),
    phoneStatus() {
      return this.userInfo?.phone
    },
    emailStatus() {
      return this.userInfo?.email
    }
  },
  methods: {
    async handlePasswordSubmit() {
      const valid = await this.$refs.passwordForm.validate().catch(() => false)
      if (!valid) return
      
      this.passwordLoading = true
      try {
        this.$message.success('密码修改成功')
        this.passwordForm = {
          oldPassword: '',
          newPassword: '',
          confirmPassword: ''
        }
      } catch (e) {
        console.error(e)
      } finally {
        this.passwordLoading = false
      }
    }
  }
}
</script>

<style scoped lang="scss">
.profile-settings {
  .content-card {
    padding: 30px;
    
    .content-header {
      margin-bottom: 30px;
      padding-bottom: 20px;
      border-bottom: 1px solid #f0f0f0;
      
      h2 {
        font-size: 18px;
        color: #333;
        font-weight: 500;
      }
    }
    
    >>> .el-tabs__header {
      margin-bottom: 25px;
    }
  }
  
  .security-list {
    .security-item {
      display: flex;
      justify-content: space-between;
      align-items: center;
      padding: 20px 0;
      border-bottom: 1px solid #f0f0f0;
      
      &:last-child {
        border-bottom: none;
      }
      
      .security-info {
        h4 {
          font-size: 15px;
          color: #333;
          margin-bottom: 5px;
        }
        
        .status {
          font-size: 13px;
          color: #999;
        }
      }
    }
  }
}
</style>
