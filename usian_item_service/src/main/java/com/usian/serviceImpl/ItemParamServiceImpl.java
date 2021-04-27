package com.usian.serviceImpl;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.usian.mapper.TbContentMapper;
import com.usian.mapper.TbItemCatMapper;
import com.usian.mapper.TbItemParamItemMapper;
import com.usian.mapper.TbItemParamMapper;
import com.usian.pojo.*;
import com.usian.redis.RedisClient;
import com.usian.service.ItemParamService;
import com.usian.utils.AdNode;
import com.usian.utils.CatNode;
import com.usian.utils.CatResult;
import com.usian.utils.PageResult;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Service
public class ItemParamServiceImpl implements ItemParamService {
   @Autowired
   private TbItemParamMapper tbItemParamMapper;
   @Autowired
   private TbItemCatMapper tbItemCatMapper;
   @Autowired
   private TbContentMapper tbContentMapper;
    @Autowired
    private RedisClient redisClient;

    @Value("${AD_CATEGORY_ID}")
    private Long AD_CATEGORY_ID;

    @Value("${AD_HEIGHT}")
    private Integer AD_HEIGHT;

    @Value("${AD_WIDTH}")
    private Integer AD_WIDTH;

    @Value("${AD_HEIGHTB}")
    private Integer AD_HEIGHTB;

    @Value("${AD_WIDTHB}")
    private Integer AD_WIDTHB;

    @Value("${PROTAL_CATRESULT_KEY}")
    private String protal_catresult_redis_key;

    @Value("${PORTAL_AD_KEY}")
    private String PORTAL_AD_KEY;

    @Value("${ITEM_INFO}")
    private String ITEM_INFO;

    @Value("${PARAM}")
    private String PARAM;

    @Value("${ITEM_INFO_EXPIRE}")
    private Integer ITEM_INFO_EXPIRE;

    @Autowired
    private TbItemParamItemMapper tbItemParamItemMapper;

    @Override
    public TbItemParam selectItemParamByItemCatId(Long itemCatId) {
        TbItemParamExample example = new TbItemParamExample();
        TbItemParamExample.Criteria criteria = example.createCriteria();
        criteria.andItemCatIdEqualTo(itemCatId);
        List<TbItemParam> list=tbItemParamMapper.selectByExampleWithBLOBs(example);
        if (list !=null && list.size()>0){
            return list.get(0);
        }
        return null;
    }

    @Override
    public PageResult selectTbItemAllByPage(Integer page, Integer rows) {
        PageHelper.startPage(page,rows);
        TbItemParamExample tbItemParamExample= new TbItemParamExample();
        tbItemParamExample.setOrderByClause("updated DESC");
        List<TbItemParam> tbItemList=tbItemParamMapper.selectByExample(tbItemParamExample);
        PageInfo<TbItemParam> tbItemPageInfo = new PageInfo<>(tbItemList);
        PageResult pageResult = new PageResult();
        pageResult.setResult(tbItemPageInfo.getList());
        pageResult.setTotalPage(Long.valueOf(tbItemPageInfo.getNavigatePages()));
        pageResult.setPageIndex(tbItemPageInfo.getPageNum());
        return pageResult;
    }

    @Override
    public Integer insertItemParam(Long itemCatId, String paramData) {

        TbItemParamExample tbItemParamExample = new TbItemParamExample();
        TbItemParamExample.Criteria criteria = tbItemParamExample.createCriteria();
        criteria.andItemCatIdEqualTo(itemCatId);
        List<TbItemParam> itemParamList =
                tbItemParamMapper.selectByExampleWithBLOBs(tbItemParamExample);
        if(itemParamList.size()>0){
            return 0;
        }

        //2、保存规格模板
        Date date = new Date();
        TbItemParam tbItemParam = new TbItemParam();
        tbItemParam.setItemCatId(itemCatId);
        tbItemParam.setParamData(paramData);
        tbItemParam.setUpdated(date);
        tbItemParam.setCreated(date);
        return tbItemParamMapper.insertSelective(tbItemParam);
    }

    @Override
    public Integer deleteItemParamById(Long id) {
        int i = tbItemParamMapper.deleteByPrimaryKey(id);
        return i;
    }

    @Override
    public CatResult selectItemCategoryAll() {
        //查询缓存
        CatResult catResultRedis =
                (CatResult)redisClient.get(protal_catresult_redis_key);
        if(catResultRedis!=null){
            return catResultRedis;
        }
        CatResult catResult = new CatResult();
        //查询商品分类
        catResult.setData(getCatList(0L));

        //添加到缓存
        redisClient.set(protal_catresult_redis_key,catResult);

        return catResult;
    }

    @Override
    public List<AdNode> selectFrontendContentByAD() {
        //查询缓存
        List<AdNode> adNodeListRedis =
                (List<AdNode>)redisClient.hget(PORTAL_AD_KEY,AD_CATEGORY_ID.toString());
        if(adNodeListRedis!=null){
            return adNodeListRedis;
        }
        // 查询数据库
        TbContentExample tbContentExample = new TbContentExample();
        TbContentExample.Criteria criteria = tbContentExample.createCriteria();
        criteria.andCategoryIdEqualTo(AD_CATEGORY_ID);
        List<TbContent> tbContentList =
                tbContentMapper.selectByExample(tbContentExample);
        List<AdNode> adNodeList = new ArrayList<AdNode>();
        for (TbContent tbContent : tbContentList) {
            AdNode adNode = new AdNode();
            adNode.setSrc(tbContent.getPic());
            adNode.setSrcB(tbContent.getPic2());
            adNode.setHref(tbContent.getUrl());
            adNode.setHeight(AD_HEIGHT);
            adNode.setWidth(AD_WIDTH);
            adNode.setHeightB(AD_HEIGHTB);
            adNode.setWidthB(AD_WIDTHB);
            adNodeList.add(adNode);
        }
        //添加到缓存
        redisClient.hset(PORTAL_AD_KEY,AD_CATEGORY_ID.toString(),adNodeList);
        return adNodeList;
    }

    @Override
    public TbItemParamItem selectTbItemParamItemByItemId(Long itemId) {
        TbItemParamItem tbItemParamItem = (TbItemParamItem) redisClient.get(
                ITEM_INFO + ":" + itemId + ":"+ PARAM);
        if(tbItemParamItem!=null){
            return tbItemParamItem;
        }

        TbItemParamItemExample example = new TbItemParamItemExample();
        TbItemParamItemExample.Criteria criteria = example.createCriteria();
        criteria.andItemIdEqualTo(itemId);
        List<TbItemParamItem> tbItemParamItemList =
                tbItemParamItemMapper.selectByExampleWithBLOBs(example);
        if(tbItemParamItemList!=null && tbItemParamItemList.size()>0){
            //把数据保存到缓存
            redisClient.set(ITEM_INFO + ":" + itemId + ":"+
                    PARAM,tbItemParamItemList.get(0));
            //设置缓存的有效期
            redisClient.expire(ITEM_INFO + ":" + itemId + ":"+ PARAM,ITEM_INFO_EXPIRE);
            return tbItemParamItemList.get(0);
        }
        return null;
    }

    private List<?> getCatList(Long parentId){
        //创建查询条件
        TbItemCatExample example = new TbItemCatExample();
        TbItemCatExample.Criteria criteria = example.createCriteria();
        criteria.andParentIdEqualTo(parentId);
        List<TbItemCat> list = this.tbItemCatMapper.selectByExample(example);
        List resultList = new ArrayList();
        int count = 0;
        for(TbItemCat tbItemCat:list){
            //判断是否是父节点
            if(tbItemCat.getIsParent()){
                CatNode catNode = new CatNode();
                catNode.setName(tbItemCat.getName());
                catNode.setItem(getCatList(tbItemCat.getId()));//查询子节点
                resultList.add(catNode);
                count++;
                //只取商品分类中的 18 条数据
                if (count == 18){
                    break;
                }
            }else{
                resultList.add(tbItemCat.getName());
            }
        }
        return resultList;
    }

}
