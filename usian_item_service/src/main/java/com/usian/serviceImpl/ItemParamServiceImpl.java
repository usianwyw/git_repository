package com.usian.serviceImpl;

import com.usian.mapper.TbItemParamMapper;
import com.usian.pojo.TbItemCatExample;
import com.usian.pojo.TbItemParam;
import com.usian.pojo.TbItemParamExample;
import com.usian.service.ItemParamService;
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
}
