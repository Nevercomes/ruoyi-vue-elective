import request from '@/utils/request'

// 查询申请记录列表
export function listApply(query) {
  return request({
    url: '/elective/apply/list',
    method: 'get',
    params: query
  })
}

// 查询申请记录详细
export function getApply(id) {
  return request({
    url: '/elective/apply/' + id,
    method: 'get'
  })
}

// 新增申请记录
export function addApply(data) {
  return request({
    url: '/elective/apply',
    method: 'post',
    data: data
  })
}

// 修改申请记录
export function updateApply(data) {
  return request({
    url: '/elective/apply',
    method: 'put',
    data: data
  })
}

// 删除申请记录
export function delApply(id) {
  return request({
    url: '/elective/apply/' + id,
    method: 'delete'
  })
}

// 导出申请记录
export function exportApply(query) {
  return request({
    url: '/elective/apply/export',
    method: 'get',
    params: query
  })
}