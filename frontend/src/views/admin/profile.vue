<template>
  <div class="admin-profile">
    <div class="page-header">
      <h2>个人中心</h2>
    </div>
    
    <el-card>
      <el-form :model="form" :rules="rules" ref="form" label-width="100px">
        <el-form-item label="用户名">
          <el-input v-model="form.username" disabled></el-input>
        </el-form-item>
        <el-form-item label="昵称" prop="nickname">
          <el-input v-model="form.nickname" placeholder="请输入昵称"></el-input>
        </el-form-item>
        <el-form-item label="头像">
          <el-upload
            class="avatar-uploader"
            :action="uploadUrl"
            :show-file-list="false"
            :on-success="handleUploadSuccess"
            :headers="uploadHeaders">
            <img v-if="form.avatar" :src="form.avatar" class="avatar">
            <i v-else class="el-icon-plus avatar-uploader-icon"></i>
          </el-upload>
        </el-form-item>
        <el-form-item label="手机号">
          <el-input v-model="form.phone" placeholder="请输入手机号"></el-input>
        </el-form-item>
        <el-form-item label="邮箱">
          <el-input v-model="form.email" placeholder="请输入邮箱"></el-input>
        </el-form-item>
        <el-form-item>
          <el-button type="primary" @click="handleSubmit" :loading="loading">保存修改</el-button>
        </el-form-item>
      </el-form>
    </el-card>
    
    <el-card class="password-card" style="margin-top: 20px;">
      <template slot="header">
        <span>修改密码</span>
      </template>
      <el-form :model="passwordForm" :rules="passwordRules" ref="passwordForm" label-width="120px">
        <el-form-item label="原密码" prop="oldPassword">
          <el-input v-model="passwordForm.oldPassword" type="password" placeholder="请输入原密码"></el-input>
        </el-form-item>
        <el-form-item label="新密码" prop="newPassword">
          <el-input v-model="passwordForm.newPassword" type="password" placeholder="请输入新密码"></el-input>
        </el-form-item>
        <el-form-item label="确认新密码" prop="confirmPassword">
          <el-input v-model="passwordForm.confirmPassword" type="password" placeholder="请再次输入新密码"></el-input>
        </el-form-item>
        <el-form-item>
          <el-button type="primary" @click="handlePasswordSubmit" :loading="passwordLoading">修改密码</el-button>
        </el-form-item>
      </el-form>
    </el-card>
  </div>
</template>

<script>
import { mapState } from 'vuex'

export default {
  name: 'AdminProfile',
  data() {
    const validateConfirmPassword = (rule, value, callback) => {
      if (value !== this.passwordForm.newPassword) {
        return callback(new Error('两次输入的密码不一致'))
      }
      callback()
    }
    return {
      loading: false,
      passwordLoading: false,
      uploadUrl: '/api/upload/image',
      uploadHeaders: {
        'Authorization': localStorage.getItem('token') || ''
      },
      form: {
        username: '',
        nickname: '',
        avatar: '',
        phone: '',
        email: ''
      },
      rules: {
        nickname: [{ min: 2, max: 20, message: '昵称长度在 2 到 20 个字符', trigger: 'blur' }]
      },
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
    ...mapState(['userInfo'])
  },
  created() {
    this.initForm()
  },
  methods: {
    initForm() {
      if (this.userInfo) {
        this.form = {
          username: this.userInfo.username || '',
          nickname: this.userInfo.nickname || '',
          avatar: this.userInfo.avatar || '',
          phone: this.userInfo.phone || '',
          email: this.userInfo.email || ''
        }
      }
    },
    handleUploadSuccess(response) {
      if (response.code === 200) {
        this.form.avatar = response.data.url
      }
    },
    async handleSubmit() {
      const valid = await this.$refs.form.validate().catch(() => false)
      if (!valid) return
      
      this.loading = true
      try {
        this.$message.success('保存成功')
      } catch (e) {
        console.error(e)
      } finally {
        this.loading = false
      }
    },
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
.admin-profile {
  .page-header {
    margin-bottom: 20px;
    
    h2 {
      font-size: 20px;
      font-weight: 500;
      color: #333;
    }
  }
  
  .password-card {
    >>> .el-card__header {
      background: #f5f7fa;
    }
  }
  
  .avatar-uploader {
    >>> .el-upload {
      border: 1px dashed #d9d9d9;
      border-radius: 6px;
      cursor: pointer;
      position: relative;
      overflow: hidden;
      
      &:hover {
        border-color: #409EFF;
      }
    }
    
    .avatar {
      width: 100px;
      height: 100px;
      display: block;
      border-radius: 50%;
      object-fit: cover;
    }
    
    .avatar-uploader-icon {
      font-size: 28px;
      color: #8c939d;
      width: 100px;
      height: 100px;
      line-height: 100px;
      text-align: center;
      border-radius: 50%;
    }
  }
}
</style>
