package com.usian.controller;


import com.usian.feign.ItemServiceFeign;
import com.usian.utils.AdNode;
import com.usian.utils.CatResult;
import com.usian.utils.PageResult;
import com.usian.utils.Result;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * 首页商品分类
 */
@RestController
@RequestMapping("/frontend")
public class ItemCategoryController {
    @Autowired
    private ItemServiceFeign itemServiceFeign;

    /**
     * 查询首页商品分类
     */
    @RequestMapping("/itemCategory/selectItemCategoryAll")
    public Result selectItemCategoryAll() {
        CatResult catResult = itemServiceFeign.selectItemCategoryAll();
        if(catResult.getData().size()>0){
            return Result.ok(catResult);
        }
        return Result.error("查无结果");
    }
    @RequestMapping("/content/selectFrontendContentByAD")
    public Result selectFrontendContentByAD() {
        List<AdNode>list = itemServiceFeign.selectFrontendContentByAD();
        if(list!=null&&list.size()>0){
            return Result.ok(list);
        }
        return Result.error("查无结果");
    }
}
