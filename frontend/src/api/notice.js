import request from '@/utils/request'

export function getNoticeList(params) {
  return request({
    url: '/notice/list',
    method: 'get',
    params
  })
}

export function getNoticeFrontList(params) {
  return request({
    url: '/notice/front/list',
    method: 'get',
    params
  })
}

export function getNoticeDetail(id) {
  return request({
    url: '/notice/detail/' + id,
    method: 'get'
  })
}

export function getNoticeLatest(limit) {
  return request({
    url: '/notice/latest',
    method: 'get',
    params: { limit }
  })
}

export function saveNotice(data) {
  return request({
    url: '/notice/save',
    method: 'post',
    data
  })
}

export function deleteNotice(id) {
  return request({
    url: '/notice/delete/' + id,
    method: 'delete'
  })
}
