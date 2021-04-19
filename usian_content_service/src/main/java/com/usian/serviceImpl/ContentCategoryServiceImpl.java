package com.usian.serviceImpl;

import com.usian.pojo.TbContentCategory;
import com.usian.service.ContentCategoryService;

import java.util.Date;
import java.util.List;

import com.usian.mapper.TbContentCategoryMapper;
import com.usian.pojo.TbContentCategoryExample;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * 内容分类业务层
 */
@Service
public class ContentCategoryServiceImpl implements ContentCategoryService {
    @Autowired
    private TbContentCategoryMapper tbContentCategoryMapper;

    /**
     * 根据父节点 Id 查询子节点
     *
     * @param parentId
     * @return
     */
    @Override
    public List<TbContentCategory> selectContentCategoryByParentId(Long parentId) {
        TbContentCategoryExample tbContentCategoryExample = new
                TbContentCategoryExample();
        TbContentCategoryExample.Criteria criteria =
                tbContentCategoryExample.createCriteria();
        criteria.andParentIdEqualTo(parentId);
        List<TbContentCategory> list =
                this.tbContentCategoryMapper.selectByExample(tbContentCategoryExample);
        return list;
    }

    @Override
    public Integer insertContentCategory(TbContentCategory tbContentCategory) {
        //1、添加内容分类
        tbContentCategory.setUpdated(new Date());
        tbContentCategory.setCreated(new Date());
        tbContentCategory.setIsParent(false);
        tbContentCategory.setSortOrder(1);
        tbContentCategory.setStatus(1);
        Integer contentCategoryNum =
                this.tbContentCategoryMapper.insert(tbContentCategory);
        //2、如果他爹不是爹，要把他爹改成爹
        //2.1、查询当前新节点的父节点
        TbContentCategory contentCategory =
                this.tbContentCategoryMapper.selectByPrimaryKey(            											 tbContentCategory.getParentId());
        //2.2、判断当前父节点是否是叶子节点
        if (!contentCategory.getIsParent()) {
            contentCategory.setIsParent(true);
            contentCategory.setUpdated(new Date());
            this.tbContentCategoryMapper.updateByPrimaryKey(contentCategory);
        }
        return contentCategoryNum;
    }
}
