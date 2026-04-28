import request from '@/utils/request'

export function getBannerList() {
  return request({
    url: '/banner/list',
    method: 'get'
  })
}

export function getBannerPage(params) {
  return request({
    url: '/banner/page',
    method: 'get',
    params
  })
}

export function saveBanner(data) {
  return request({
    url: '/banner/save',
    method: 'post',
    data
  })
}

export function deleteBanner(id) {
  return request({
    url: '/banner/delete/' + id,
    method: 'delete'
  })
}
