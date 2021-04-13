package com.usian.serviceImpl;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.usian.mapper.TbItemParamMapper;
import com.usian.pojo.*;
import com.usian.service.ItemParamService;
import com.usian.utils.PageResult;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ItemParamServiceImpl implements ItemParamService {
   @Autowired
   private TbItemParamMapper tbItemParamMapper;
    @Override
    public TbItemParam selectItemParamByItemCatId(Long itemCatId) {
        TbItemParamExample example = new TbItemParamExample();
        TbItemParamExample.Criteria criteria = example.createCriteria();
        criteria.andItemCatIdEqualTo(itemCatId);
        List<TbItemParam> list=tbItemParamMapper.selectByExampleWithBLOBs(example);
        if (list !=null && list.size()>0){
            return list.get(0);
        }
        return null;
    }

    @Override
    public PageResult selectTbItemAllByPage(Integer page, Integer rows) {
        PageHelper.startPage(page,rows);
        TbItemParamExample tbItemParamExample= new TbItemParamExample();
        tbItemParamExample.setOrderByClause("updated DESC");
        List<TbItemParam> tbItemList=tbItemParamMapper.selectByExample(tbItemParamExample);
        PageInfo<TbItemParam> tbItemPageInfo = new PageInfo<>(tbItemList);
        PageResult pageResult = new PageResult();
        pageResult.setResult(tbItemPageInfo.getList());
        pageResult.setTotalPage(Long.valueOf(tbItemPageInfo.getNavigatePages()));
        pageResult.setPageIndex(tbItemPageInfo.getPageNum());
        return pageResult;
    }
}
