package com.ruoyi.project.elective.course.mapper;

import com.ruoyi.project.elective.course.domain.ElectiveCoursePeople;
import com.ruoyi.project.elective.record.domain.ElectiveSelectRecord;

import java.io.Serializable;
import java.util.List;

/**
 * 选课人数Mapper接口
 *
 * @author Sunss
 * @date 2020-02-14
 */
public interface ElectiveCoursePeopleMapper {
    /**
     * 查询选课人数
     *
     * @param id 选课人数ID
     * @return 选课人数
     */
    public ElectiveCoursePeople selectElectiveCoursePeopleById(Long id);

    /**
     * 查询选课人数列表
     *
     * @param electiveCoursePeople 选课人数
     * @return 选课人数集合
     */
    public List<ElectiveCoursePeople> selectElectiveCoursePeopleList(ElectiveCoursePeople electiveCoursePeople);

    /**
     * 新增选课人数
     *
     * @param electiveCoursePeople 选课人数
     * @return 结果
     */
    public int insertElectiveCoursePeople(ElectiveCoursePeople electiveCoursePeople);

    /**
     * 修改选课人数
     *
     * @param electiveCoursePeople 选课人数
     * @return 结果
     */
    public int updateElectiveCoursePeople(ElectiveCoursePeople electiveCoursePeople);

    /**
     * 删除选课人数
     *
     * @param id 选课人数ID
     * @return 结果
     */
    public int deleteElectiveCoursePeopleById(Long id);

    /**
     * 批量删除选课人数
     *
     * @param ids 需要删除的数据ID
     * @return 结果
     */
    public int deleteElectiveCoursePeopleByIds(Long[] ids);

    /**
     * 根据选课记录做加1操作
     *
     * @param electiveSelectRecord
     */
    void addOneSelectNum(ElectiveSelectRecord electiveSelectRecord);

    /**
     * 根据选课记录做减1操作
     *
     * @param electiveSelectRecord
     */
    void minusOneSelectNum(ElectiveSelectRecord electiveSelectRecord);

    /**
     * 统计选课人数
     *
     * @param electiveCoursePeople
     * @return
     */
    List<ElectiveCoursePeople> listStatistic(Serializable electiveCoursePeople);
}
