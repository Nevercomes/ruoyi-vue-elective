import request from '@/utils/request'

// 查询check列表
export function listCheck(query) {
  return request({
    url: '/elective/check/list',
    method: 'get',
    params: query
  })
}

// 查询check详细
export function getCheck(id) {
  return request({
    url: '/elective/check/' + id,
    method: 'get'
  })
}

// 新增check
export function addCheck(data) {
  return request({
    url: '/elective/check',
    method: 'post',
    data: data
  })
}

// 修改check
export function updateCheck(data) {
  return request({
    url: '/elective/check',
    method: 'put',
    data: data
  })
}

// 删除check
export function delCheck(id) {
  return request({
    url: '/elective/check/' + id,
    method: 'delete'
  })
}

// 导出check
export function exportCheck(query) {
  return request({
    url: '/elective/check/export',
    method: 'get',
    params: query
  })
}