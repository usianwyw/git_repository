package com.usian.controller;

import com.usian.pojo.TbItemParam;
import com.usian.service.ItemParamService;
import com.usian.utils.PageResult;
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
    @RequestMapping("selectItemParamAll")
    public PageResult selectItemParamAll(Integer page, Integer rows){

        return this.itemParamService.selectTbItemAllByPage(page,rows);
    }
}
