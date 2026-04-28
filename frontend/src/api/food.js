import request from '@/utils/request'

export function getFoodCategoryList() {
  return request({
    url: '/food/category/list',
    method: 'get'
  })
}

export function getFoodCategoryPage(params) {
  return request({
    url: '/food/category/page',
    method: 'get',
    params
  })
}

export function saveFoodCategory(data) {
  return request({
    url: '/food/category/save',
    method: 'post',
    data
  })
}

export function deleteFoodCategory(id) {
  return request({
    url: '/food/category/delete/' + id,
    method: 'delete'
  })
}

export function getFoodList(params) {
  return request({
    url: '/food/list',
    method: 'get',
    params
  })
}

export function getFoodFrontList(params) {
  return request({
    url: '/food/front/list',
    method: 'get',
    params
  })
}

export function getFoodDetail(id, userId) {
  return request({
    url: '/food/detail/' + id,
    method: 'get',
    params: { userId }
  })
}

export function getFoodRecommend(limit) {
  return request({
    url: '/food/recommend',
    method: 'get',
    params: { limit }
  })
}

export function saveFood(data) {
  return request({
    url: '/food/save',
    method: 'post',
    data
  })
}

export function deleteFood(id) {
  return request({
    url: '/food/delete/' + id,
    method: 'delete'
  })
}

export function toggleFoodLike(userId, foodId) {
  return request({
    url: '/food/like',
    method: 'post',
    params: { userId, foodId }
  })
}
