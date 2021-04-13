package com.usian.controller;

import com.usian.feign.ItemServiceFeign;
import com.usian.pojo.TbItemParam;
import com.usian.utils.PageResult;
import com.usian.utils.Result;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/backend/itemParam")
public class ItemParamController {
    @Autowired
    private ItemServiceFeign itemServiceFeign;
    @RequestMapping("selectItemParamByItemCatId/{itemCatId}")
    public Result selectItemParamByItemCatId(@PathVariable("itemCatId") Long itemCatId){
        TbItemParam tbItemParam=this.itemServiceFeign.selectItemParamByItemCatId(itemCatId);
        if (tbItemParam!=null){
            return Result.ok(tbItemParam);
        }
        return Result.error("查无结果");
    }
    @RequestMapping("selectItemParamAll")
    public Result selectTbItemAllByPage(@RequestParam(defaultValue = "1") Integer page,
                                        @RequestParam(defaultValue = "10") Integer rows){
        PageResult pageResult= itemServiceFeign.selectItemParamAll(page,rows);
        if (pageResult.getResult()!=null && pageResult.getResult().size()>0){
            return Result.ok(pageResult);
        }
        return Result.error("查无结果");
    }
}
