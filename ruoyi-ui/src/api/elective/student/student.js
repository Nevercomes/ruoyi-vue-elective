import request from '@/utils/request'
import { praseStrEmpty } from "@/utils/ruoyi";

// 查询学生列表
export function listStudent(query) {
  return request({
    url: '/elective/student/list',
    method: 'get',
    params: query
  })
}

// 查询学生详细
export function getStudent(userId) {
  return request({
    url: '/elective/student/' + praseStrEmpty(userId),
    method: 'get'
  })
}

// 新增学生
export function addStudent(data) {
  return request({
    url: '/elective/student',
    method: 'post',
    data: data
  })
}

// 修改学生
export function updateStudent(data) {
  return request({
    url: '/elective/student',
    method: 'put',
    data: data
  })
}

// 删除学生
export function delStudent(userId) {
  return request({
    url: '/elective/student/' + userId,
    method: 'delete'
  })
}

// 导出学生
export function exportStudent(query) {
  return request({
    url: '/elective/student/export',
    method: 'get',
    params: query
  })
}

// 学生密码重置
export function resetUserPwd(userId, password) {
  const data = {
    userId,
    password
  }
  return request({
    url: '/system/user/resetPwd',
    method: 'put',
    data: data
  })
}

// 学生状态修改
export function changeUserStatus(userId, status) {
  const data = {
    userId,
    status
  }
  return request({
    url: '/system/user/changeStatus',
    method: 'put',
    data: data
  })
}

// 下载学生导入模板
export function importTemplate() {
  return request({
    url: '/elective/student/importTemplate',
    method: 'get'
  })
}
