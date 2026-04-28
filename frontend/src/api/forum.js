import request from '@/utils/request'

export function getForumList(params) {
  return request({
    url: '/forum/list',
    method: 'get',
    params
  })
}

export function getForumFrontList(params) {
  return request({
    url: '/forum/front/list',
    method: 'get',
    params
  })
}

export function getForumDetail(id) {
  return request({
    url: '/forum/detail/' + id,
    method: 'get'
  })
}

export function publishForum(data) {
  return request({
    url: '/forum/publish',
    method: 'post',
    data
  })
}

export function saveForum(data) {
  return request({
    url: '/forum/save',
    method: 'post',
    data
  })
}

export function deleteForum(id) {
  return request({
    url: '/forum/delete/' + id,
    method: 'delete'
  })
}

export function updateForumTop(id, isTop) {
  return request({
    url: '/forum/top/' + id,
    method: 'put',
    params: { isTop }
  })
}

export function saveReply(data) {
  return request({
    url: '/forum/reply/save',
    method: 'post',
    data
  })
}

export function getReplyList(params) {
  return request({
    url: '/forum/reply/list',
    method: 'get',
    params
  })
}

export function deleteReply(id) {
  return request({
    url: '/forum/reply/delete/' + id,
    method: 'delete'
  })
}
