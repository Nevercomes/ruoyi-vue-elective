package com.ruoyi.project.elective.student.service;

import com.ruoyi.project.elective.student.domain.ElectiveStudent;

import java.util.List;

/**
 * studentService接口
 *
 * @author Sunss
 * @date 2020-02-11
 */
public interface IElectiveStudentService {
    /**
     * 查询student
     *
     * @param id studentID
     * @return student
     */
    public ElectiveStudent selectElectiveStudentById(Long id);

    /**
     * 查询student列表
     *
     * @param electiveStudent student
     * @return student集合
     */
    public List<ElectiveStudent> selectElectiveStudentList(ElectiveStudent electiveStudent);

    /**
     * 新增student
     *
     * @param electiveStudent student
     * @return 结果
     */
    public int insertElectiveStudent(ElectiveStudent electiveStudent);

    /**
     * 修改student
     *
     * @param electiveStudent student
     * @return 结果
     */
    public int updateElectiveStudent(ElectiveStudent electiveStudent);

    /**
     * 批量删除student
     *
     * @param ids 需要删除的studentID
     * @return 结果
     */
    public int deleteElectiveStudentByIds(Long[] ids);

    /**
     * 删除student信息
     *
     * @param id studentID
     * @return 结果
     */
    public int deleteElectiveStudentById(Long id);

    /**
     * 导入学生数据
     * @param studentList
     * @param updateSupport
     * @param username
     * @return
     */
    String importStudent(List<ElectiveStudent> studentList, boolean updateSupport, String username);

    /**
     * 查询导出数据时的学生数据
     * @param electiveStudent
     * @return
     */
    List<ElectiveStudent> selectExportList(ElectiveStudent electiveStudent);
}
