package com.usian.serviceImpl;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.usian.mapper.TbItemCatMapper;
import com.usian.mapper.TbItemDescMapper;
import com.usian.mapper.TbItemMapper;
import com.usian.mapper.TbItemParamItemMapper;
import com.usian.pojo.*;
import com.usian.redis.RedisClient;
import com.usian.service.ItemService;
import com.usian.utils.IDUtils;
import com.usian.utils.PageResult;
import org.springframework.amqp.core.AmqpTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.beans.factory.annotation.Value;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
@Transactional
public class ItemServiceImpl implements ItemService {

    @Autowired
    private TbItemMapper tbItemMapper;
    @Autowired
    private TbItemDescMapper tbItemDescMapper;
    @Autowired
    private TbItemParamItemMapper tbItemParamItemMapper;
    @Autowired
    private TbItemCatMapper tbItemCatMapper;

    @Autowired
    private AmqpTemplate amqpTemplate;
    @Value("${ITEM_INFO}")
    private String ITEM_INFO;

    @Value("${BASE}")
    private String BASE;

    @Value("${DESC}")
    private String DESC;

    @Value("${ITEM_INFO_EXPIRE}")
    private Integer ITEM_INFO_EXPIRE;

    @Autowired
    private RedisClient redisClient;


    @Override
    public TbItem selectItemInfo(Long itemId) {
        //查询缓存
        TbItem tbItem = (TbItem) redisClient.get(ITEM_INFO + ":" + itemId + ":"+ BASE);
        if(tbItem!=null){
            return tbItem;
        }

        tbItem = tbItemMapper.selectByPrimaryKey(itemId);
        //把数据保存到缓存
        redisClient.set(ITEM_INFO + ":" + itemId + ":"+ BASE,tbItem);
        //设置缓存的有效期
        redisClient.expire(ITEM_INFO + ":" + itemId + ":"+ BASE,ITEM_INFO_EXPIRE);

        return tbItem;
    }

    @Override
    public PageResult selectTbItemAllByPage(Integer page, Integer rows) {
        PageHelper.startPage(page,rows);
        TbItemExample tbItemExample= new TbItemExample();
        tbItemExample.setOrderByClause("updated DESC");
        TbItemExample.Criteria criteria=tbItemExample.createCriteria();
        criteria.andStatusEqualTo((byte)1);
        List<TbItem> tbItemList=tbItemMapper.selectByExample(tbItemExample);
        for (int i=0;i<tbItemList.size();i++){
            TbItem tbItem=tbItemList.get(i);
            tbItem.setPrice(tbItem.getPrice()/100);
        }
        PageInfo<TbItem> tbItemPageInfo = new PageInfo<>(tbItemList);
        PageResult pageResult = new PageResult();
        pageResult.setResult(tbItemPageInfo.getList());
        pageResult.setTotalPage(Long.valueOf(tbItemPageInfo.getNavigatePages()));
        pageResult.setPageIndex(tbItemPageInfo.getPageNum());
        return pageResult;
    }

    @Override
    public Integer insertTbItem(TbItem tbItem, String desc, String itemParams) {
        Long itemId = IDUtils.genItemId();
        Date date = new Date();
        tbItem.setId(itemId);
        tbItem.setStatus((byte)1);
        tbItem.setUpdated(date);
        tbItem.setCreated(date);
        Integer tbItemNum = tbItemMapper.insertSelective(tbItem);

        //补齐商品描述对象
        TbItemDesc tbItemDesc = new TbItemDesc();
        tbItemDesc.setItemId(itemId);
        tbItemDesc.setItemDesc(desc);
        tbItemDesc.setCreated(date);
        tbItemDesc.setUpdated(date);
        Integer tbitemDescNum = tbItemDescMapper.insertSelective(tbItemDesc);

        //补齐商品规格参数
        TbItemParamItem tbItemParamItem = new TbItemParamItem();
        tbItemParamItem.setItemId(itemId);
        tbItemParamItem.setParamData(itemParams);
        tbItemParamItem.setUpdated(date);
        tbItemParamItem.setCreated(date);
        Integer itemParamItmeNum =
                tbItemParamItemMapper.insertSelective(tbItemParamItem);

        //添加商品发布消息到mq
        amqpTemplate.convertAndSend("item_exchage","item.add", itemId);

        return tbItemNum + tbitemDescNum + itemParamItmeNum;

    }

    @Override
    public Map<String, Object> preUpdateItem(Long itemId) {
        Map<String, Object> map = new HashMap<>();
        TbItem item = this.tbItemMapper.selectByPrimaryKey(itemId);
        map.put("item", item);
        //根据商品 ID 查询商品描述
        TbItemDesc itemDesc = this.tbItemDescMapper.selectByPrimaryKey(itemId);
        map.put("itemDesc", itemDesc.getItemDesc());
        //根据商品 ID 查询商品类目
        TbItemCat itemCat = this.tbItemCatMapper.selectByPrimaryKey(item.getCid());
        map.put("itemCat", itemCat.getName());
        //根据商品 ID 查询商品规格信息
        TbItemParamItemExample example = new TbItemParamItemExample();
        TbItemParamItemExample.Criteria criteria = example.createCriteria();
        criteria.andItemIdEqualTo(itemId);
        List<TbItemParamItem> list =
                this.tbItemParamItemMapper.selectByExampleWithBLOBs(example);
        if (list != null && list.size() > 0) {
            map.put("itemParamItem", list.get(0).getParamData());
        }
        return map;
    }

    @Override
    public Integer updateTbItem(TbItem tbItem, String desc, String itemParams) {
        Date date = new Date();
        tbItem.setStatus((byte)1);
        tbItem.setUpdated(date);
        int i1 = tbItemMapper.updateByPrimaryKey(tbItem);

        TbItemDesc tbItemDesc = new TbItemDesc();
        tbItemDesc.setItemDesc(desc);
        tbItemDesc.setCreated(date);
        tbItemDesc.setUpdated(date);
        int i2 = tbItemDescMapper.updateByPrimaryKey(tbItemDesc);

        TbItemParamItem tbItemParamItem = new TbItemParamItem();
        tbItemParamItem.setCreated(date);
        tbItemParamItem.setParamData(itemParams);
        tbItemParamItem.setUpdated(date);
        int i3 = tbItemParamItemMapper.updateByPrimaryKey(tbItemParamItem);
        return i1+i2+i3;
    }
    @Override
    public Integer deleteItemById(Long itemId) {
       TbItem tbItem = tbItemMapper.selectByPrimaryKey(itemId);
        tbItem.setStatus((byte)0);
        int i = tbItemMapper.updateByPrimaryKeySelective(tbItem);
        return i;
    }

    @Override
    public TbItemDesc selectItemDescByItemId(Long itemId) {
        TbItemDesc tbItemDesc = (TbItemDesc) redisClient.get(
                ITEM_INFO + ":" + itemId + ":"+ DESC);
        if(tbItemDesc!=null){
            return tbItemDesc;
        }

        TbItemDescExample example = new TbItemDescExample();
        TbItemDescExample.Criteria criteria = example.createCriteria();
        criteria.andItemIdEqualTo(itemId);
        List<TbItemDesc> itemDescList =
                this.tbItemDescMapper.selectByExampleWithBLOBs(example);
        if(itemDescList!=null && itemDescList.size()>0){
            //把数据保存到缓存
            redisClient.set(ITEM_INFO + ":" + itemId + ":"+ DESC,itemDescList.get(0));
            //设置缓存的有效期
            redisClient.expire(ITEM_INFO + ":" + itemId + ":"+ DESC,ITEM_INFO_EXPIRE);
            return itemDescList.get(0);
        }
        return null;
    }

}