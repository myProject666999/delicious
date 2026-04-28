<template>
  <footer class="footer">
    <div class="container">
      <div class="footer-content">
        <div class="footer-section">
          <h3><span class="logo-icon">🍜</span> 地方特色美食分享系统</h3>
          <p>发现身边的美食，分享你的美食故事</p>
          <p>让美食连接你我，让生活更加美味</p>
        </div>
        <div class="footer-section">
          <h4>快速链接</h4>
          <ul>
            <li><router-link to="/">首页</router-link></li>
            <li><router-link to="/recipe">美食菜谱</router-link></li>
            <li><router-link to="/food">美食信息</router-link></li>
            <li><router-link to="/forum">美食论坛</router-link></li>
          </ul>
        </div>
        <div class="footer-section">
          <h4>帮助中心</h4>
          <ul>
            <li><router-link to="/notice">系统公告</router-link></li>
            <li><a href="#">使用说明</a></li>
            <li><a href="#">关于我们</a></li>
            <li><a href="#">联系我们</a></li>
          </ul>
        </div>
        <div class="footer-section">
          <h4>联系方式</h4>
          <ul>
            <li><i class="el-icon-phone-outline"></i> 400-123-4567</li>
            <li><i class="el-icon-message"></i> contact@delicious.com</li>
            <li><i class="el-icon-location"></i> 北京市朝阳区美食街100号</li>
          </ul>
        </div>
      </div>
      <div class="footer-bottom">
        <p>© 2024 地方特色美食分享系统. All Rights Reserved.</p>
      </div>
    </div>
  </footer>
</template>

<script>
export default {
  name: 'Footer'
}
</script>

<style scoped lang="scss">
.footer {
  background: #333;
  color: #aaa;
  padding: 40px 0 20px;
  
  .footer-content {
    display: grid;
    grid-template-columns: 2fr 1fr 1fr 1fr;
    gap: 40px;
    padding-bottom: 30px;
    border-bottom: 1px solid #444;
  }
  
  .footer-section {
    h3 {
      color: #fff;
      font-size: 18px;
      margin-bottom: 15px;
      
      .logo-icon {
        font-size: 24px;
        margin-right: 5px;
      }
    }
    
    h4 {
      color: #fff;
      font-size: 16px;
      margin-bottom: 15px;
    }
    
    p {
      line-height: 1.8;
      font-size: 14px;
    }
    
    ul {
      li {
        margin-bottom: 10px;
        
        a {
          color: #aaa;
          font-size: 14px;
          transition: color 0.3s;
          
          &:hover {
            color: #ff6b6b;
          }
        }
        
        i {
          margin-right: 8px;
        }
      }
    }
  }
  
  .footer-bottom {
    padding-top: 20px;
    text-align: center;
    font-size: 14px;
    color: #666;
  }
}

@media screen and (max-width: 768px) {
  .footer-content {
    grid-template-columns: 1fr;
  }
}
</style>
