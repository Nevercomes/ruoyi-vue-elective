import request from '@/utils/request'

// 查询select列表
export function listSelect(query) {
  return request({
    url: '/elective/select/list',
    method: 'get',
    params: query
  })
}

// 查询select详细
export function getSelect(id) {
  return request({
    url: '/elective/select/' + id,
    method: 'get'
  })
}

// 新增select
export function addSelect(data) {
  return request({
    url: '/elective/select',
    method: 'post',
    data: data
  })
}

// 修改select
export function updateSelect(data) {
  return request({
    url: '/elective/select',
    method: 'put',
    data: data
  })
}

// 删除select
export function delSelect(id) {
  return request({
    url: '/elective/select/' + id,
    method: 'delete'
  })
}

// 导出select
export function exportSelect(query) {
  return request({
    url: '/elective/select/export',
    method: 'get',
    params: query
  })
}

export function listCanSelect(query) {
  return request({
    url: '/elective/select/student',
    method: 'get',
    params: query
  })
}