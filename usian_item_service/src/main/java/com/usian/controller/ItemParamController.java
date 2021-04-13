package com.usian.controller;

import com.usian.pojo.TbItemParam;
import com.usian.service.ItemParamService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/service/itemParam")
public class ItemParamController {
    @Autowired
    private ItemParamService itemParamService;
@RequestMapping("/selectItemParamByItemCatId")
    public TbItemParam selectItemParamByItemCatId(Long itemCatId){
    return this.itemParamService.selectItemParamByItemCatId(itemCatId);
}
}
