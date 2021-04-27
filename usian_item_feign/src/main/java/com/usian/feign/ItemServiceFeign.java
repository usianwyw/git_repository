package com.usian.feign;

import com.usian.pojo.*;
import com.usian.utils.AdNode;
import com.usian.utils.CatResult;
import com.usian.utils.PageResult;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@FeignClient(value = "usian-item-service")
public interface ItemServiceFeign {

    @RequestMapping("/service/item/selectItemInfo")
    TbItem selectItemInfo(@RequestParam("itemId") Long itemId);
    @RequestMapping("/service/item/selectTbItemAllByPage")
    PageResult selectTbItemAllByPage(@RequestParam Integer page, @RequestParam Integer rows);
    @RequestMapping("/service/itemCategory/selectItemCategoryByParentId")
    List<TbItemCat> selectItemCategoryByParentId(@RequestParam Long id);
    @PostMapping("/service/itemParam/selectItemParamByItemCatId")
    TbItemParam selectItemParamByItemCatId(@RequestParam("itemCatId") Long itemCatId);
    @GetMapping("/service/item/insertTbItem")
    Integer insertTbItem(@RequestBody TbItem tbItem, @RequestParam String desc, @RequestParam String itemParams);
@RequestMapping("/service/item/preUpdateItem")
    Map<String, Object> preUpdateItem(@RequestParam("itemId") Long itemId);
    @GetMapping("/service/item/updateTbItem")
    Integer updateTbItem(@RequestBody TbItem tbItem,@RequestParam String desc,@RequestParam String itemParams);
    @RequestMapping("/service/item/deleteItemById")
    Integer deleteItemById(@RequestParam("itemId") Long itemId);
    @RequestMapping("/service/itemParam/selectItemParamAll")
    PageResult selectItemParamAll(@RequestParam Integer page,@RequestParam Integer rows);
    @RequestMapping("/service/itemParam/insertItemParam")
    Integer insertItemParam(@RequestParam Long itemCatId,@RequestParam String paramData);
    @RequestMapping("/service/itemParam/deleteItemParamById")
    Integer deleteItemParamById(@RequestParam Long id);
    @RequestMapping("/service/itemParam/selectItemCategoryAll")
    CatResult selectItemCategoryAll();
    @RequestMapping("/service/itemParam/selectFrontendContentByAD")
    List<AdNode> selectFrontendContentByAD();
    @RequestMapping("/service/itemParam/selectItemDescByItemId")
    TbItemDesc selectItemDescByItemId(Long itemId);
    @RequestMapping("/service/itemParam/selectTbItemParamItemByItemId")
    TbItemParamItem selectTbItemParamItemByItemId(Long itemId);
}