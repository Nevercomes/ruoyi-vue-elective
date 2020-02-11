// 包含所有的示例函数 方便cv

import request from '@/utils/request'

// 查询配置模板列表
export function listTemplate(query) {
  return request({
    url: '/elective/config/template/list',
    method: 'get',
    params: query
  })
}

// 查询配置模板详细
export function getTemplate(id) {
  return request({
    url: '/elective/config/template/' + id,
    method: 'get'
  })
}

// 新增配置模板
export function addTemplate(data) {
  return request({
    url: '/elective/config/template',
    method: 'post',
    data: data
  })
}

// 修改配置模板
export function updateTemplate(data) {
  return request({
    url: '/elective/config/template',
    method: 'put',
    data: data
  })
}

// 删除配置模板
export function delTemplate(id) {
  return request({
    url: '/elective/config/template/' + id,
    method: 'delete'
  })
}

// 导出配置模板
export function exportTemplate(query) {
  return request({
    url: '/elective/config/template/export',
    method: 'get',
    params: query
  })
}

export function changeTemplateStatus(id, status) {
  const data = {
    id,
    status
  }
  return request({
    url: '/elective/config/template/status',
    method: 'put',
    data: data
  })
}