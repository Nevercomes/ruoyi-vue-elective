package com.ruoyi.project.elective.config.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.ruoyi.project.elective.config.domain.ElectiveConfigTemplate;

import java.util.List;

/**
 * 配置模板Mapper接口
 *
 * @author Sunss
 * @date 2020-02-08
 */
public interface ElectiveConfigTemplateMapper extends BaseMapper<ElectiveConfigTemplate> {
    /**
     * 查询配置模板
     *
     * @param id 配置模板ID
     * @return 配置模板
     */
    public ElectiveConfigTemplate selectElectiveConfigTemplateById(Long id);

    /**
     * 查询配置模板列表
     *
     * @param electiveConfigTemplate 配置模板
     * @return 配置模板集合
     */
    public List<ElectiveConfigTemplate> selectElectiveConfigTemplateList(ElectiveConfigTemplate electiveConfigTemplate);

    /**
     * 新增配置模板
     *
     * @param electiveConfigTemplate 配置模板
     * @return 结果
     */
    public int insertElectiveConfigTemplate(ElectiveConfigTemplate electiveConfigTemplate);

    /**
     * 修改配置模板
     *
     * @param electiveConfigTemplate 配置模板
     * @return 结果
     */
    public int updateElectiveConfigTemplate(ElectiveConfigTemplate electiveConfigTemplate);

    /**
     * 删除配置模板
     *
     * @param id 配置模板ID
     * @return 结果
     */
    public int deleteElectiveConfigTemplateById(Long id);

    /**
     * 批量删除配置模板
     *
     * @param ids 需要删除的数据ID
     * @return 结果
     */
    public int deleteElectiveConfigTemplateByIds(Long[] ids);
}
