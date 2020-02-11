import request from '@/utils/request'

// 查询apply列表
export function listApply(query) {
  return request({
    url: '/elective/apply/list',
    method: 'get',
    params: query
  })
}

// 查询apply详细
export function getApply(id) {
  return request({
    url: '/elective/apply/' + id,
    method: 'get'
  })
}

// 新增apply
export function addApply(data) {
  return request({
    url: '/elective/apply',
    method: 'post',
    data: data
  })
}

// 修改apply
export function updateApply(data) {
  return request({
    url: '/elective/apply',
    method: 'put',
    data: data
  })
}

// 删除apply
export function delApply(id) {
  return request({
    url: '/elective/apply/' + id,
    method: 'delete'
  })
}

// 导出apply
export function exportApply(query) {
  return request({
    url: '/elective/apply/export',
    method: 'get',
    params: query
  })
}