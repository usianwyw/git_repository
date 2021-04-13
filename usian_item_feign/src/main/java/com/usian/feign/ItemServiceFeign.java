package com.usian.feign;

import com.usian.pojo.TbItem;
import com.usian.pojo.TbItemCat;
import com.usian.pojo.TbItemParam;
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
}