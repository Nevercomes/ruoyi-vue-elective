package com.ruoyi.project.elective.open.mapper;

import com.ruoyi.project.elective.open.domain.ElectiveOpenSelect;

import java.util.List;

/**
 * 开放选课Mapper接口
 *
 * @author Sunss
 * @date 2020-02-15
 */
public interface ElectiveOpenSelectMapper {
    /**
     * 查询开放选课
     *
     * @param id 开放选课ID
     * @return 开放选课
     */
    public ElectiveOpenSelect selectElectiveOpenSelectById(Long id);

    /**
     * 查询开放选课列表
     *
     * @param electiveOpenSelect 开放选课
     * @return 开放选课集合
     */
    public List<ElectiveOpenSelect> selectElectiveOpenSelectList(ElectiveOpenSelect electiveOpenSelect);

    /**
     * 新增开放选课
     *
     * @param electiveOpenSelect 开放选课
     * @return 结果
     */
    public int insertElectiveOpenSelect(ElectiveOpenSelect electiveOpenSelect);

    /**
     * 修改开放选课
     *
     * @param electiveOpenSelect 开放选课
     * @return 结果
     */
    public int updateElectiveOpenSelect(ElectiveOpenSelect electiveOpenSelect);

    /**
     * 删除开放选课
     *
     * @param id 开放选课ID
     * @return 结果
     */
    public int deleteElectiveOpenSelectById(Long id);

    /**
     * 批量删除开放选课
     *
     * @param ids 需要删除的数据ID
     * @return 结果
     */
    public int deleteElectiveOpenSelectByIds(Long[] ids);
}
