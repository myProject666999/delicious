<template>
  <div class="food-detail-page">
    <Header />
    
    <div class="main-content container">
      <el-breadcrumb separator="/" class="breadcrumb">
        <el-breadcrumb-item :to="{ path: '/' }">首页</el-breadcrumb-item>
        <el-breadcrumb-item :to="{ path: '/food' }">美食信息</el-breadcrumb-item>
        <el-breadcrumb-item>{{ food.name || '美食详情' }}</el-breadcrumb-item>
      </el-breadcrumb>
      
      <div v-if="loading" class="loading-container">
        <i class="el-icon-loading"></i> 加载中...
      </div>
      
      <template v-else-if="food">
        <div class="detail-header">
          <h1 class="title">{{ food.name }}</h1>
          <div class="meta">
            <span class="category">
              <i class="el-icon-folder-opened"></i> {{ food.categoryName || '未分类' }}
            </span>
            <span class="origin" v-if="food.origin">
              <i class="el-icon-location"></i> {{ food.origin }}
            </span>
            <span class="view-count">
              <i class="el-icon-view"></i> {{ food.viewCount || 0 }} 次浏览
            </span>
            <span class="create-time">
              <i class="el-icon-time"></i> {{ formatDate(food.createTime) }}
            </span>
          </div>
        </div>
        
        <div class="detail-content">
          <div class="cover-section">
            <img :src="food.cover || defaultImage" :alt="food.name" class="cover-image" />
            <div class="action-bar">
              <el-button :type="isLiked ? 'danger' : ''" :icon="isLiked ? 'el-icon-star-on' : 'el-icon-star-off'" @click="handleLike">
                {{ isLiked ? '已点赞' : '点赞' }} ({{ food.likeCount || 0 }})
              </el-button>
              <el-button icon="el-icon-share">分享</el-button>
            </div>
          </div>
          
          <div class="info-section">
            <div class="info-item" v-if="food.introduction">
              <h3 class="section-title">简介</h3>
              <div class="section-content">{{ food.introduction }}</div>
            </div>
            
            <div class="info-item" v-if="food.feature">
              <h3 class="section-title">特色</h3>
              <div class="section-content" v-html="formatContent(food.feature)"></div>
            </div>
            
            <div class="info-item" v-if="food.images">
              <h3 class="section-title">图片展示</h3>
              <div class="image-gallery">
                <img v-for="(img, index) in imageList" :key="index" :src="img" :alt="`美食图片${index + 1}`" class="gallery-image" />
              </div>
            </div>
          </div>
        </div>
      </template>
      
      <el-empty v-else description="美食不存在或已被删除"></el-empty>
    </div>
    
    <Footer />
  </div>
</template>

<script>
import Header from '@/components/Header.vue'
import Footer from '@/components/Footer.vue'
import { getFoodDetail, toggleFoodLike } from '@/api/food'
import { mapGetters, mapState } from 'vuex'

export default {
  name: 'FoodDetail',
  components: { Header, Footer },
  data() {
    return {
      loading: false,
      food: null,
      isLiked: false,
      defaultImage: 'https://trae-api-cn.mchost.guru/api/ide/v1/text_to_image?prompt=delicious%20food%20placeholder&image_size=square'
    }
  },
  computed: {
    ...mapGetters(['isLoggedIn']),
    ...mapState(['userInfo']),
    imageList() {
      if (!this.food || !this.food.images) return []
      return this.food.images.split(',').filter(img => img.trim())
    }
  },
  created() {
    this.loadDetail()
  },
  watch: {
    '$route'(to, from) {
      if (to.params.id !== from.params.id) {
        this.loadDetail()
      }
    }
  },
  methods: {
    async loadDetail() {
      this.loading = true
      const id = this.$route.params.id
      const userId = this.userInfo ? this.userInfo.id : null
      
      try {
        const res = await getFoodDetail(id, userId)
        this.food = res.data
        this.isLiked = this.food.isLiked || false
      } catch (e) {
        console.error(e)
      } finally {
        this.loading = false
      }
    },
    formatDate(date) {
      if (!date) return ''
      const d = new Date(date)
      return `${d.getFullYear()}-${(d.getMonth() + 1).toString().padStart(2, '0')}-${d.getDate().toString().padStart(2, '0')}`
    },
    formatContent(content) {
      if (!content) return ''
      return content.replace(/\n/g, '<br>')
    },
    async handleLike() {
      if (!this.isLoggedIn) {
        this.$message.warning('请先登录')
        this.$router.push('/login')
        return
      }
      
      try {
        const res = await toggleFoodLike(this.userInfo.id, this.food.id)
        this.isLiked = res.data
        if (this.isLiked) {
          this.food.likeCount = (this.food.likeCount || 0) + 1
          this.$message.success('点赞成功')
        } else {
          this.food.likeCount = Math.max(0, (this.food.likeCount || 0) - 1)
          this.$message.success('已取消点赞')
        }
      } catch (e) {
        console.error(e)
      }
    }
  }
}
</script>

<style scoped lang="scss">
.food-detail-page {
  min-height: 100vh;
  background: #f5f5f5;
}

.breadcrumb {
  padding: 20px 0;
  font-size: 14px;
}

.loading-container {
  text-align: center;
  padding: 100px 0;
  color: #999;
  font-size: 16px;
  
  i {
    margin-right: 8px;
  }
}

.main-content {
  padding-bottom: 60px;
}

.detail-header {
  background: #fff;
  padding: 30px;
  border-radius: 8px;
  margin-bottom: 20px;
  box-shadow: 0 2px 12px rgba(0, 0, 0, 0.08);
  
  .title {
    font-size: 28px;
    color: #333;
    margin-bottom: 15px;
    font-weight: 600;
  }
  
  .meta {
    display: flex;
    gap: 25px;
    flex-wrap: wrap;
    font-size: 14px;
    color: #999;
    
    span {
      display: flex;
      align-items: center;
      gap: 5px;
    }
    
    .category, .origin {
      color: #667eea;
    }
  }
}

.detail-content {
  background: #fff;
  border-radius: 8px;
  padding: 30px;
  box-shadow: 0 2px 12px rgba(0, 0, 0, 0.08);
}

.cover-section {
  margin-bottom: 30px;
  
  .cover-image {
    width: 100%;
    max-height: 500px;
    object-fit: cover;
    border-radius: 8px;
  }
  
  .action-bar {
    margin-top: 20px;
    display: flex;
    gap: 15px;
    justify-content: center;
  }
}

.info-section {
  .info-item {
    margin-bottom: 30px;
    
    &:last-child {
      margin-bottom: 0;
    }
    
    .section-title {
      font-size: 18px;
      color: #333;
      font-weight: 600;
      margin-bottom: 15px;
      padding-left: 10px;
      border-left: 3px solid #667eea;
    }
    
    .section-content {
      font-size: 15px;
      color: #666;
      line-height: 2;
      padding: 0 13px;
    }
    
    .image-gallery {
      display: grid;
      grid-template-columns: repeat(auto-fill, minmax(200px, 1fr));
      gap: 15px;
      padding: 0 13px;
      
      .gallery-image {
        width: 100%;
        height: 150px;
        object-fit: cover;
        border-radius: 8px;
        cursor: pointer;
        transition: transform 0.3s;
        
        &:hover {
          transform: scale(1.05);
        }
      }
    }
  }
}
</style>
