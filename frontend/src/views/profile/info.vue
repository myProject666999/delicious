<template>
  <div class="profile-info">
    <div class="content-card card">
      <div class="content-header">
        <h2>个人信息</h2>
      </div>
      
      <el-form :model="form" :rules="rules" ref="form" label-width="100px">
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
        <el-form-item label="用户名">
          <el-input v-model="form.username" disabled></el-input>
        </el-form-item>
        <el-form-item label="昵称" prop="nickname">
          <el-input v-model="form.nickname" placeholder="请输入昵称"></el-input>
        </el-form-item>
        <el-form-item label="手机号">
          <el-input v-model="form.phone" placeholder="请输入手机号"></el-input>
        </el-form-item>
        <el-form-item label="邮箱">
          <el-input v-model="form.email" placeholder="请输入邮箱"></el-input>
        </el-form-item>
        <el-form-item label="性别">
          <el-radio-group v-model="form.gender">
            <el-radio :label="0">未知</el-radio>
            <el-radio :label="1">男</el-radio>
            <el-radio :label="2">女</el-radio>
          </el-radio-group>
        </el-form-item>
        <el-form-item label="地址">
          <el-input v-model="form.address" type="textarea" :rows="2" placeholder="请输入地址"></el-input>
        </el-form-item>
        <el-form-item>
          <el-button type="primary" @click="handleSubmit" :loading="loading">保存修改</el-button>
        </el-form-item>
      </el-form>
    </div>
  </div>
</template>

<script>
import { mapState } from 'vuex'
import { updateUser, getUserInfo } from '@/api/user'

export default {
  name: 'ProfileInfo',
  data() {
    return {
      loading: false,
      uploadUrl: '/api/upload/image',
      uploadHeaders: {
        'Authorization': localStorage.getItem('token') || ''
      },
      form: {
        username: '',
        nickname: '',
        avatar: '',
        phone: '',
        email: '',
        gender: 0,
        address: ''
      },
      rules: {
        nickname: [{ min: 2, max: 20, message: '昵称长度在 2 到 20 个字符', trigger: 'blur' }]
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
    async initForm() {
      if (this.userInfo && this.userInfo.id) {
        try {
          const res = await getUserInfo(this.userInfo.id)
          if (res.data) {
            this.form = {
              username: res.data.username || '',
              nickname: res.data.nickname || '',
              avatar: res.data.avatar || '',
              phone: res.data.phone || '',
              email: res.data.email || '',
              gender: res.data.gender || 0,
              address: res.data.address || ''
            }
          }
        } catch (e) {
          console.error(e)
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
        const userData = { ...this.form, id: this.userInfo.id }
        await updateUser(userData)
        this.$message.success('保存成功')
        
        if (this.$store.state.userInfo) {
          this.$store.commit('SET_USER_INFO', {
            ...this.$store.state.userInfo,
            nickname: this.form.nickname,
            avatar: this.form.avatar
          })
        }
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
.profile-info {
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
</style>
