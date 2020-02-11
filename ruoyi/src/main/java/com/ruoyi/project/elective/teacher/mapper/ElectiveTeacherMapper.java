package com.ruoyi.project.elective.teacher.mapper;

import com.ruoyi.project.elective.teacher.domain.ElectiveTeacher;

import java.util.List;

/**
 * 教师管理Mapper接口
 *
 * @author Sunss
 * @date 2020-02-11
 */
public interface ElectiveTeacherMapper {
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
     * 删除教师管理
     *
     * @param id 教师管理ID
     * @return 结果
     */
    public int deleteElectiveTeacherById(Long id);

    /**
     * 批量删除教师管理
     *
     * @param ids 需要删除的数据ID
     * @return 结果
     */
    public int deleteElectiveTeacherByIds(Long[] ids);
}
