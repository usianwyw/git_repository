package com.usian.service;

import com.usian.pojo.TbItemParam;
import com.usian.pojo.TbItemParamItem;
import com.usian.utils.AdNode;
import com.usian.utils.CatResult;
import com.usian.utils.PageResult;

import java.util.List;

public interface ItemParamService {
    TbItemParam selectItemParamByItemCatId(Long itemCatId);

    PageResult selectTbItemAllByPage(Integer page, Integer rows);

    Integer insertItemParam(Long itemCatId, String paramData);

    Integer deleteItemParamById(Long id);

    CatResult selectItemCategoryAll();

    List<AdNode> selectFrontendContentByAD();

    TbItemParamItem selectTbItemParamItemByItemId(Long itemId);
}
