import request from '@/utils/request'

export function getRecipeCategoryList() {
  return request({
    url: '/recipe/category/list',
    method: 'get'
  })
}

export function getRecipeCategoryPage(params) {
  return request({
    url: '/recipe/category/page',
    method: 'get',
    params
  })
}

export function saveRecipeCategory(data) {
  return request({
    url: '/recipe/category/save',
    method: 'post',
    data
  })
}

export function deleteRecipeCategory(id) {
  return request({
    url: '/recipe/category/delete/' + id,
    method: 'delete'
  })
}

export function getRecipeList(params) {
  return request({
    url: '/recipe/list',
    method: 'get',
    params
  })
}

export function getRecipeFrontList(params) {
  return request({
    url: '/recipe/front/list',
    method: 'get',
    params
  })
}

export function getRecipeDetail(id, userId) {
  return request({
    url: '/recipe/detail/' + id,
    method: 'get',
    params: { userId }
  })
}

export function getRecipeRecommend(limit) {
  return request({
    url: '/recipe/recommend',
    method: 'get',
    params: { limit }
  })
}

export function saveRecipe(data) {
  return request({
    url: '/recipe/save',
    method: 'post',
    data
  })
}

export function deleteRecipe(id) {
  return request({
    url: '/recipe/delete/' + id,
    method: 'delete'
  })
}

export function toggleRecipeLike(userId, recipeId) {
  return request({
    url: '/recipe/like',
    method: 'post',
    params: { userId, recipeId }
  })
}
