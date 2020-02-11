package com.ruoyi.framework.web.service;

import com.baomidou.mybatisplus.core.conditions.Wrapper;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.ruoyi.framework.web.domain.BaseEntity;

import java.util.Collection;

/**
 * @author Sunss
 * @since 2020/2/8
 */
@SuppressWarnings("unchecked")
public class BaseService<M extends BaseMapper<T>, T extends BaseEntity> extends ServiceImpl<M, T> {

    @Override
    public boolean save(T entity) {
        entity.preInsert();
        return super.save(entity);
    }

    @Override
    public boolean saveBatch(Collection<T> entityList, int batchSize) {
        for (T entity : entityList) {
            entity.preInsert();
        }
        return super.saveBatch(entityList, batchSize);
    }

    @Override
    public boolean updateById(T entity) {
        entity.preUpdate();
        return super.updateById(entity);
    }

    @Override
    public boolean update(T entity, Wrapper<T> updateWrapper) {
        entity.preUpdate();
        return super.update(entity, updateWrapper);
    }

    @Override
    public boolean updateBatchById(Collection<T> entityList, int batchSize) {
        for (T entity : entityList) {
            entity.preUpdate();
        }
        return super.updateBatchById(entityList, batchSize);
    }
}
