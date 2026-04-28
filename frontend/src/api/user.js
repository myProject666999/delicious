import request from '@/utils/request'

export function adminLogin(data) {
  return request({
    url: '/admin/login',
    method: 'post',
    data
  })
}

export function userLogin(data) {
  return request({
    url: '/user/login',
    method: 'post',
    data
  })
}

export function userRegister(data) {
  return request({
    url: '/user/register',
    method: 'post',
    data
  })
}

export function getUserList(params) {
  return request({
    url: '/user/list',
    method: 'get',
    params
  })
}

export function saveUser(data) {
  return request({
    url: '/user/save',
    method: 'post',
    data
  })
}

export function updateUser(data) {
  return request({
    url: '/user/update',
    method: 'post',
    data
  })
}

export function deleteUser(id) {
  return request({
    url: '/user/delete/' + id,
    method: 'delete'
  })
}

export function updateUserStatus(id, data) {
  return request({
    url: '/user/status/' + id,
    method: 'put',
    data
  })
}

export function getUserDetail(id) {
  return request({
    url: '/user/detail/' + id,
    method: 'get'
  })
}

export function getUserInfo(userId) {
  return request({
    url: '/user/info',
    method: 'get',
    params: { userId }
  })
}
