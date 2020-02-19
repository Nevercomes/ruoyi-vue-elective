import request from '@/utils/request'

// 查询开放选课列表
export function listOpen(query) {
  return request({
    url: '/elective/open/list',
    method: 'get',
    params: query
  })
}

// 查询开放选课详细
export function getOpen(id) {
  return request({
    url: '/elective/open/' + id,
    method: 'get'
  })
}

// 新增开放选课
export function addOpen(data) {
  return request({
    url: '/elective/open',
    method: 'post',
    data: data
  })
}

// 修改开放选课
export function updateOpen(data) {
  return request({
    url: '/elective/open',
    method: 'put',
    data: data
  })
}

// 删除开放选课
export function delOpen(id) {
  return request({
    url: '/elective/open/' + id,
    method: 'delete'
  })
}

// 导出开放选课
export function exportOpen(query) {
  return request({
    url: '/elective/open/export',
    method: 'get',
    params: query
  })
}

export function changeOpenStatus(id, status) {
  const data = {
    id,
    status
  }
  return request({
    url: '/elective/open/status',
    method: 'put',
    data: data
  })
}

export function listStudentOpen() {
  return request({
    url: '/elective/open/student',
    method: 'get'
  })
}
