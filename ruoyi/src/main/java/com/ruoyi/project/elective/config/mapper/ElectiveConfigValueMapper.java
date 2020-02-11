package com.ruoyi.project.elective.config.mapper;

import com.ruoyi.project.elective.config.domain.ElectiveConfigValue;

import java.util.List;

/**
 * 模板内容Mapper接口
 *
 * @author Sunss
 * @date 2020-02-08
 */
public interface ElectiveConfigValueMapper {
    /**
     * 查询模板内容
     *
     * @param id 模板内容ID
     * @return 模板内容
     */
    public ElectiveConfigValue selectElectiveConfigValueById(Long id);

    /**
     * 查询模板内容列表
     *
     * @param electiveConfigValue 模板内容
     * @return 模板内容集合
     */
    public List<ElectiveConfigValue> selectElectiveConfigValueList(ElectiveConfigValue electiveConfigValue);

    /**
     * 新增模板内容
     *
     * @param electiveConfigValue 模板内容
     * @return 结果
     */
    public int insertElectiveConfigValue(ElectiveConfigValue electiveConfigValue);

    /**
     * 修改模板内容
     *
     * @param electiveConfigValue 模板内容
     * @return 结果
     */
    public int updateElectiveConfigValue(ElectiveConfigValue electiveConfigValue);

    /**
     * 删除模板内容
     *
     * @param id 模板内容ID
     * @return 结果
     */
    public int deleteElectiveConfigValueById(Long id);

    /**
     * 批量删除模板内容
     *
     * @param ids 需要删除的数据ID
     * @return 结果
     */
    public int deleteElectiveConfigValueByIds(Long[] ids);
}
