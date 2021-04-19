package com.usian.controller;

import com.usian.pojo.TbItem;
import com.usian.pojo.TbItemParam;
import com.usian.service.ItemParamService;
import com.usian.utils.AdNode;
import com.usian.utils.CatResult;
import com.usian.utils.PageResult;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

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
    @RequestMapping("/insertItemParam")
    public Integer insertItemParam(Long itemCatId,String paramData){
        return this.itemParamService.insertItemParam(itemCatId,paramData);
    }
    @RequestMapping("deleteItemParamById")
    public Integer deleteItemParamById(Long id){
    return this.itemParamService.deleteItemParamById(id);
    }
    @RequestMapping("selectItemCategoryAll")
    public CatResult selectItemCategoryAll(){
        return this.itemParamService.selectItemCategoryAll();
    }
    @RequestMapping("selectFrontendContentByAD")
    public List<AdNode> selectFrontendContentByAD(){
        return this.itemParamService.selectFrontendContentByAD();
    }
}
