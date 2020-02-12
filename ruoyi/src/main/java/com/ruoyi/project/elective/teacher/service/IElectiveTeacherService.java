package com.ruoyi.project.elective.teacher.service;

import com.ruoyi.project.elective.teacher.domain.ElectiveTeacher;
import java.util.List;

/**
 * 教师管理Service接口
 * 
 * @author Sunss
 * @date 2020-02-11
 */
public interface IElectiveTeacherService 
{
    /**
     * 查询教师管理
     * 
     * @param id 教师管理ID
     * @return 教师管理
     */
    public ElectiveTeacher selectElectiveTeacherById(Long id);

    /**
     * 查询教师管理列表
     * 
     * @param electiveTeacher 教师管理
     * @return 教师管理集合
     */
    public List<ElectiveTeacher> selectElectiveTeacherList(ElectiveTeacher electiveTeacher);

    /**
     * 新增教师管理
     * 
     * @param electiveTeacher 教师管理
     * @return 结果
     */
    public int insertElectiveTeacher(ElectiveTeacher electiveTeacher);

    /**
     * 修改教师管理
     * 
     * @param electiveTeacher 教师管理
     * @return 结果
     */
    public int updateElectiveTeacher(ElectiveTeacher electiveTeacher);

    /**
     * 批量删除教师管理
     * 
     * @param ids 需要删除的教师管理ID
     * @return 结果
     */
    public int deleteElectiveTeacherByIds(Long[] ids);

    /**
     * 删除教师管理信息
     * 
     * @param id 教师管理ID
     * @return 结果
     */
    public int deleteElectiveTeacherById(Long id);

    /**
     * 导入教师数据
     * @param teacherList
     * @param updateSupport 是否支持更新
     * @param operName 操作用户
     * @return
     */
    String importTeacher(List<ElectiveTeacher> teacherList, boolean updateSupport, String operName);
}
