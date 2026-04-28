<template>
  <div class="home-page">
    <Header />
    
    <div class="banner-section">
      <el-carousel :interval="4000" type="card" height="400px">
        <el-carousel-item v-for="item in bannerList" :key="item.id">
          <div class="banner-item">
            <img :src="item.image" :alt="item.title" class="banner-image" />
            <div class="banner-info" v-if="item.title">
              <h2>{{ item.title }}</h2>
            </div>
          </div>
        </el-carousel-item>
      </el-carousel>
    </div>
    
    <div class="main-content container">
      <section class="section">
        <h2 class="section-title">推荐菜谱</h2>
        <div class="recipe-grid">
          <div v-for="recipe in recommendRecipes" :key="recipe.id" class="recipe-card card" @click="goToRecipeDetail(recipe.id)">
            <div class="recipe-image">
              <img :src="recipe.cover || defaultImage" :alt="recipe.title" />
              <div class="recipe-category">{{ recipe.categoryName }}</div>
            </div>
            <div class="recipe-info">
              <h3 class="text-ellipsis">{{ recipe.title }}</h3>
              <p class="intro text-ellipsis-2">{{ recipe.introduction }}</p>
              <div class="stats">
                <span><i class="el-icon-view"></i> {{ recipe.viewCount || 0 }}</span>
                <span><i class="el-icon-star-on"></i> {{ recipe.likeCount || 0 }}</span>
              </div>
            </div>
          </div>
        </div>
        <div class="text-center mt-20">
          <el-button type="primary" @click="$router.push('/recipe')">查看更多菜谱</el-button>
        </div>
      </section>
      
      <section class="section">
        <h2 class="section-title">推荐美食</h2>
        <div class="food-grid">
          <div v-for="food in recommendFoods" :key="food.id" class="food-card card" @click="goToFoodDetail(food.id)">
            <div class="food-image">
              <img :src="food.cover || defaultImage" :alt="food.name" />
              <div class="food-origin" v-if="food.origin">{{ food.origin }}</div>
            </div>
            <div class="food-info">
              <h3 class="text-ellipsis">{{ food.name }}</h3>
              <p class="intro text-ellipsis-2">{{ food.introduction }}</p>
              <div class="stats">
                <span><i class="el-icon-view"></i> {{ food.viewCount || 0 }}</span>
                <span><i class="el-icon-star-on"></i> {{ food.likeCount || 0 }}</span>
              </div>
            </div>
          </div>
        </div>
        <div class="text-center mt-20">
          <el-button type="primary" @click="$router.push('/food')">查看更多美食</el-button>
        </div>
      </section>
      
      <section class="section">
        <h2 class="section-title">最新公告</h2>
        <div class="notice-section">
          <div class="notice-list">
            <div v-for="notice in latestNotices" :key="notice.id" class="notice-item" @click="goToNoticeDetail(notice.id)">
              <div class="notice-date">
                <span class="day">{{ notice.createTime | dateFormat('DD') }}</span>
                <span class="month">{{ notice.createTime | dateFormat('MM月') }}</span>
              </div>
              <div class="notice-content">
                <h4 class="text-ellipsis">{{ notice.title }}</h4>
                <p class="text-ellipsis-2">{{ notice.content }}</p>
              </div>
            </div>
          </div>
        </div>
      </section>
    </div>
    
    <Footer />
  </div>
</template>

<script>
import Header from '@/components/Header.vue'
import Footer from '@/components/Footer.vue'
import { getBannerList } from '@/api/banner'
import { getRecipeRecommend } from '@/api/recipe'
import { getFoodRecommend } from '@/api/food'
import { getNoticeLatest } from '@/api/notice'

export default {
  name: 'Home',
  components: { Header, Footer },
  data() {
    return {
      bannerList: [],
      recommendRecipes: [],
      recommendFoods: [],
      latestNotices: [],
      defaultImage: 'https://trae-api-cn.mchost.guru/api/ide/v1/text_to_image?prompt=delicious%20food%20placeholder&image_size=square'
    }
  },
  filters: {
    dateFormat(date, format) {
      if (!date) return ''
      const d = new Date(date)
      const map = {
        'MM': (d.getMonth() + 1).toString().padStart(2, '0'),
        'DD': d.getDate().toString().padStart(2, '0'),
        '月': '月'
      }
      return format.replace(/MM|DD|月/g, matched => map[matched])
    }
  },
  created() {
    this.loadBannerList()
    this.loadRecommendRecipes()
    this.loadRecommendFoods()
    this.loadLatestNotices()
  },
  methods: {
    async loadBannerList() {
      try {
        const res = await getBannerList()
        this.bannerList = res.data || []
      } catch (e) {
        console.error(e)
      }
    },
    async loadRecommendRecipes() {
      try {
        const res = await getRecipeRecommend(6)
        this.recommendRecipes = res.data || []
      } catch (e) {
        console.error(e)
      }
    },
    async loadRecommendFoods() {
      try {
        const res = await getFoodRecommend(6)
        this.recommendFoods = res.data || []
      } catch (e) {
        console.error(e)
      }
    },
    async loadLatestNotices() {
      try {
        const res = await getNoticeLatest(5)
        this.latestNotices = res.data || []
      } catch (e) {
        console.error(e)
      }
    },
    goToRecipeDetail(id) {
      this.$router.push(`/recipe/detail/${id}`)
    },
    goToFoodDetail(id) {
      this.$router.push(`/food/detail/${id}`)
    },
    goToNoticeDetail(id) {
      this.$router.push(`/notice/detail/${id}`)
    }
  }
}
</script>

<style scoped lang="scss">
.home-page {
  min-height: 100vh;
}

.banner-section {
  margin-bottom: 40px;
  
  .banner-item {
    width: 100%;
    height: 400px;
    position: relative;
    overflow: hidden;
    
    .banner-image {
      width: 100%;
      height: 100%;
      object-fit: cover;
    }
    
    .banner-info {
      position: absolute;
      bottom: 0;
      left: 0;
      right: 0;
      background: linear-gradient(transparent, rgba(0, 0, 0, 0.7));
      padding: 40px;
      
      h2 {
        color: #fff;
        font-size: 32px;
      }
    }
  }
}

.main-content {
  padding-bottom: 60px;
}

.section {
  margin-bottom: 50px;
  
  .recipe-grid,
  .food-grid {
    display: grid;
    grid-template-columns: repeat(4, 1fr);
    gap: 25px;
  }
  
  .recipe-card,
  .food-card {
    cursor: pointer;
    
    .recipe-image,
    .food-image {
      height: 180px;
      position: relative;
      overflow: hidden;
      
      img {
        width: 100%;
        height: 100%;
        object-fit: cover;
        transition: transform 0.3s;
      }
      
      &:hover img {
        transform: scale(1.05);
      }
      
      .recipe-category,
      .food-origin {
        position: absolute;
        top: 10px;
        left: 10px;
        background: linear-gradient(135deg, #ff6b6b, #ff8e53);
        color: #fff;
        padding: 4px 10px;
        border-radius: 4px;
        font-size: 12px;
      }
    }
    
    .recipe-info,
    .food-info {
      padding: 15px;
      
      h3 {
        font-size: 16px;
        color: #333;
        margin-bottom: 8px;
        font-weight: 600;
      }
      
      .intro {
        font-size: 13px;
        color: #666;
        line-height: 1.5;
        margin-bottom: 10px;
      }
      
      .stats {
        display: flex;
        gap: 15px;
        font-size: 12px;
        color: #999;
        
        span {
          display: flex;
          align-items: center;
          gap: 3px;
        }
      }
    }
  }
}

.notice-section {
  background: #fff;
  border-radius: 8px;
  padding: 25px;
  box-shadow: 0 2px 12px rgba(0, 0, 0, 0.1);
  
  .notice-item {
    display: flex;
    gap: 20px;
    padding: 15px 0;
    border-bottom: 1px solid #f0f0f0;
    cursor: pointer;
    transition: background 0.3s;
    
    &:last-child {
      border-bottom: none;
    }
    
    &:hover {
      background: #f9f9f9;
    }
    
    .notice-date {
      width: 60px;
      text-align: center;
      background: linear-gradient(135deg, #ff6b6b, #ff8e53);
      border-radius: 8px;
      padding: 10px 0;
      
      .day {
        display: block;
        color: #fff;
        font-size: 24px;
        font-weight: bold;
      }
      
      .month {
        display: block;
        color: rgba(255, 255, 255, 0.9);
        font-size: 12px;
      }
    }
    
    .notice-content {
      flex: 1;
      
      h4 {
        font-size: 16px;
        color: #333;
        margin-bottom: 8px;
        font-weight: 500;
      }
      
      p {
        font-size: 13px;
        color: #666;
        line-height: 1.5;
      }
    }
  }
}

@media screen and (max-width: 1200px) {
  .recipe-grid, .food-grid {
    grid-template-columns: repeat(3, 1fr);
  }
}

@media screen and (max-width: 768px) {
  .recipe-grid, .food-grid {
    grid-template-columns: repeat(2, 1fr);
  }
}
</style>
