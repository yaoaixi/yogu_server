package com.yogu.services.store.business.service;

import java.util.List;

import com.yogu.services.store.Goods;

/**
 * 商品信息相关接口
 *
 */
public interface GoodsService {
	
	/**
	 * 通过商品id获取店铺信息
	 * 
	 * @param goodsId - 商品id
	 * @return 商品信息，如果无，返回null
	 */
	Goods getById(long goodsId);
	
	/**
	 * 通过商品key获取店铺信息
	 * 
	 * @param goodsKey - 商品key
	 * @return 商品信息，如果无，返回null
	 */
	Goods getByKey(long goodsKey);
	
	/**
	 * 通过商品key获取店铺信息
	 * 
	 * @param goodsKey - 商品key
	 * @return 商品信息，如果无，返回null
	 */
	Goods getByKey(long goodsKey, Long uid);
	
	/**
	 * 分页获取商品信息，按照创建时间倒序（从小到大）<br>
	 * 方法会区分用户对应的上级批发商装载不同的价格，如果不传用户id，默认为零售价
	 * 
	 * @param uid - 用户id（可以为空）
	 * @param lastId -上一条商品创建时间（首页传0）
	 * @param pageSize - 每页大小
	 * @author qiujun 
	 * @date 2017年9月13日 下午9:42:49 
	 * @return 商品列表，若无，返回empty list
	 */
	List<Goods> listByPage(Long uid, long lastTime, int pageSize);
	
	/**
	 * 通过商品名称分页获取商品信息，按照创建时间倒序（从小到大）<br>
	 * 方法会区分用户对应的上级批发商装载不同的价格，如果不传用户id，默认为零售价
	 * 
	 * @param goodsName - 商品名称（可以为空）
	 * @param uid - 用户id（可以为空）
	 * @param lastId -上一条商品创建时间（首页传0）
	 * @param pageSize - 每页大小
	 * @return 商品列表，若无，返回empty list
	 */
	List<Goods> listByName(String goodsName, Long uid, long lastTime, int pageSize);
	
	/**
	 * 查找指定标签下的商品列表，结果按照价格顺序排序<br>
	 * 方法会区分用户对应的上级批发商装载不同的价格，如果不传用户id，默认为零售价
	 * 
	 * @author qiujun
	 * @date 2017年10月9日 下午12:12:17 
	 * 
	 * @param tagId - 标签id
	 * @param uid - 用户id（可以为空）
	 * @param pageNo - 页码
	 * @param pageSize - 每页大小
	 * @return 商品列表，若无，返回empty list
	 */
	List<Goods> listByTagIdOrderByPriceDesc(long tagId, Long uid, int pageNo, int pageSize);
	
	/**
	 * 查找指定标签下的商品列表，结果按照价格顺序排序<br>
	 * 方法会区分用户对应的上级批发商装载不同的价格，如果不传用户id，默认为零售价
	 * 
	 * @author qiujun
	 * @date 2017年10月9日 下午12:12:17 
	 * 
	 * @param tagId - 标签id
	 * @param uid - 用户id（可以为空）
	 * @param pageNo - 页码
	 * @param pageSize - 每页大小
	 * @return 商品列表，若无，返回empty list
	 */
	List<Goods> listByTagIdOrderByPriceAsc(List<Long> tagIds, Long uid, int pageNo, int pageSize);
	
	/**
	 * 查找指定标签下的商品列表，结果按照价格顺序排序<br>
	 * 方法会区分用户对应的上级批发商装载不同的价格，如果不传用户id，默认为零售价
	 * 
	 * @author qiujun
	 * @date 2017年10月9日 下午12:12:17 
	 * 
	 * @param tagId - 标签id
	 * @param uid - 用户id（可以为空）
	 * @param pageNo - 页码
	 * @param pageSize - 每页大小
	 * @return 商品列表，若无，返回empty list
	 */
	List<Goods> listByTagIdOrderByPriceDesc(List<Long> tagIds, Long uid, int pageNo, int pageSize);
	
	/**
	 * 查找指定标签下的商品列表，结果按照价格顺序排序<br>
	 * 方法会区分用户对应的上级批发商装载不同的价格，如果不传用户id，默认为零售价
	 * 
	 * @author qiujun
	 * @date 2017年10月9日 下午12:12:17 
	 * 
	 * @param tagId - 标签id
	 * @param uid - 用户id（可以为空）
	 * @param pageNo - 页码
	 * @param pageSize - 每页大小
	 * @return 商品列表，若无，返回empty list
	 */
	List<Goods> listByTagIdOrderByPriceAsc(long tagId, Long uid, int pageNo, int pageSize);
	
	/**
	 * 查找指定标签下的商品列表，结果无序<br>
	 * 方法会区分用户对应的上级批发商装载不同的价格，如果不传用户id，默认为零售价
	 * 
	 * @author qiujun
	 * @date 2017年10月9日 下午12:12:17 
	 * 
	 * @param tagId - 标签id
	 * @param uid - 用户id（可以为空）
	 * @param pageNo - 页码
	 * @param pageSize - 每页大小
	 * @return 商品列表，若无，返回empty list
	 */
	List<Goods> listByTagId(long tagId, Long uid, int pageNo, int pageSize);
	
	/**
	 * 查找指定标签下的商品列表，结果无序<br>
	 * 方法会区分用户对应的上级批发商装载不同的价格，如果不传用户id，默认为零售价
	 * 
	 * @author qiujun
	 * @date 2017年10月9日 下午12:12:17 
	 * 
	 * @param tagId - 标签id
	 * @param uid - 用户id（可以为空）
	 * @param pageNo - 页码
	 * @param pageSize - 每页大小
	 * @return 商品列表，若无，返回empty list
	 */
	List<Goods> listByTagId(List<Long> tagIds, Long uid, int pageNo, int pageSize);
	
	/**
	 * 获取商品信息，结果无序<br>
	 * 方法会区分用户对应的上级批发商装载不同的价格，如果不传用户id，默认为零售价
	 * 
	 * @param uid - 用户id（可以为空）
	 * @param goodsKeys - 商品key商品key
	 * @author qiujun 
	 * @date 2017年9月13日 下午9:42:49 
	 * @return 商品列表，若无，返回empty list
	 */
	List<Goods> listBykeys(Long uid, List<Long> goodsKeys);
	
	List<Goods> listByName(String goodsName, int pageIndex, int pageSize);
	
	void addGoods(Goods goods);
	
	/**
	 * 修改菜品的上下架状态，即时生效，但对修改前已经下的订单不会产生影响。 只能修改自己店的数据，角色限制：店主、管理员（待定） 如果角色不对，返回没有权限的错误。
	 * 
	 * @Param uid 用户ID
	 * @param dishId 菜品ID
	 * @param status 需要修改的上下架状态。 1表示正常上架，2表示下架。
	 */
	public void setDishStatus(long goodsId, short status);
}
