package com.usian.controller;

import com.usian.pojo.TbItem;
import com.usian.pojo.TbItemDesc;
import com.usian.service.ItemService;
import com.usian.utils.PageResult;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/service/item")
public class ItemController {
    @Autowired
    private ItemService itemService;

    /**查询商品信息
     * 根据商品id
     * @param itemId
     * @return
     */
    @RequestMapping("/selectItemInfo")
    public TbItem selectItemInfo(Long itemId){
        return this.itemService.selectItemInfo(itemId);
    }
 @RequestMapping("selectTbItemAllByPage")
    public PageResult selectTbItemAllByPage(Integer page,Integer rows){

     return this.itemService.selectTbItemAllByPage(page,rows);
 }
@RequestMapping("/insertTbItem")
    public Integer insertTbItem(@RequestBody TbItem tbItem,String desc,String itemParams){
        return this.itemService.insertTbItem(tbItem,desc,itemParams);
}
    @RequestMapping("/preUpdateItem")
    public Map<String,Object> preUpdateItem(Long itemId){
        return this.itemService.preUpdateItem(itemId);
    }
    @RequestMapping("/updateTbItem")
    public Integer updateTbItem(@RequestBody TbItem tbItem,String desc,String itemParams){
        return this.itemService.updateTbItem(tbItem,desc,itemParams);
    }
    @RequestMapping("/deleteItemById")
    public Integer deleteItemById(Long itemId){
        return this.itemService.deleteItemById(itemId);
    }

    @RequestMapping("/selectItemDescByItemId")
    public TbItemDesc selectItemDescByItemId(Long itemId){
        return this.itemService.selectItemDescByItemId(itemId);
    }
}