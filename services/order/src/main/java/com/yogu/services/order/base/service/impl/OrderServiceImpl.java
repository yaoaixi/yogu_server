package com.yogu.services.order.base.service.impl;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;
import javax.inject.Inject;
import javax.inject.Named;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.yogu.CommonConstants;
import com.yogu.commons.cache.redis.Redis;
import com.yogu.commons.concurrent.ExecutorFactory;
import com.yogu.commons.utils.CollectionUtils;
import com.yogu.commons.utils.DateUtils;
import com.yogu.commons.utils.HttpClientUtils;
import com.yogu.commons.utils.PageUtils;
import com.yogu.commons.utils.StringUtils;
import com.yogu.commons.utils.VOUtil;
import com.yogu.core.consumer.messageBean.AutoDeliverOrderBO;
import com.yogu.core.consumer.messageBean.ConfirmReceiptBO;
import com.yogu.core.enums.BooleanConstants;
import com.yogu.core.enums.Role;
import com.yogu.core.enums.ThirdExpressCode;
import com.yogu.core.enums.order.ExpressStatus;
import com.yogu.core.enums.order.OrderStatus;
import com.yogu.core.enums.order.OrderStatusUtils;
import com.yogu.core.enums.pay.PayType;
import com.yogu.core.remote.config.ConfigGroupConstants;
import com.yogu.core.remote.config.ConfigRemoteService;
import com.yogu.core.sms.SmsServiceFactory;
import com.yogu.core.web.OrderErrorCode;
import com.yogu.core.web.ParameterUtil;
import com.yogu.core.web.ResultCode;
import com.yogu.core.web.exception.ServiceException;
import com.yogu.language.OrderMessages;
import com.yogu.remote.user.dto.UserProfile;
import com.yogu.remote.user.provider.UserRemoteService;
import com.yogu.services.order.base.dao.OrderDao;
import com.yogu.services.order.base.dto.Order;
import com.yogu.services.order.base.entry.OrderPO;
import com.yogu.services.order.base.service.OrderService;

/**
 * OrderService 的实现类
 * 
 * @author JFan 2015-07-13
 */
@Named
public class OrderServiceImpl implements OrderService {

	private static final Logger logger = LoggerFactory.getLogger(OrderServiceImpl.class);

	@Inject
	private OrderDao orderDao;

	@Resource(name = "redis")
	private Redis redis;

	@Override
	public Order getByOrderNo(long uid, long orderNo) {
		OrderPO order = orderDao.getByOrderNoUid(uid, orderNo);
		if (order == null) {
			return null;
		}
		return VOUtil.from(order, Order.class);
	}

	@Override
	public Order getByOrderNo(long orderNo) {
		OrderPO order = orderDao.getByOrderNo(orderNo);
		if (order == null) {
			return null;
		}
		return VOUtil.from(order, Order.class);
	}

	@Override
	public Order getById(long orderId) {
		OrderPO order = orderDao.getById(orderId);
		if (order == null) {
			return null;
		}
		return VOUtil.from(order, Order.class);
	}


	@Override
	@Transactional(propagation = Propagation.REQUIRED)
	public void userReceiveConfirm(long uid, long orderNo) {

		logger.info("order#service#receiveConfirm | 用户确认收货start | uid: {}, orderNo: {}", uid, orderNo);
		OrderPO order = orderDao.getByOrderNo(orderNo);

		if (null == order)
			throw new ServiceException(OrderErrorCode.ORDER_NOT_EXIST, OrderMessages.ORDER_ORDERADMINAPI_ORDERDETAIL_ORDER_NOTEXIST());

		// 2016-10-26 add by hins 内容：新增订单状态校验
		if (order.getStatus() != OrderStatus.PENDING_ACCEPT.getValue()) {
			logger.error("order#service#userReceiveConfirm | 订单状态不是配送中，无法确认送达 | orderNo: {}, status: {}", orderNo, order.getStatus());
			throw new ServiceException(ResultCode.REJECTED_OPERATION, OrderMessages.ORDER_ORDER_USERRECEIVECONFIRM_ORDER_CONFIRM_NOTSATISFY());
		}
		
		updateOrderToUserConfirm(order, uid);

		logger.info("order#service#receiveConfirm | 用户确认收货成功 | uid: {}, orderNo: {}", uid, orderNo);

	}

	/**
	 * 修改订单状态为用户确认收货，增加订单轨迹，发送MQ消息（更新门店交易中余额 并 记录交易流水明细）
	 * 
	 * @author Hins
	 * @date 2015年10月9日 下午4:41:45
	 * 
	 * @param order - 订单内容
	 * @param uid - 操作人（用户ID/定时任务时，管理员ID）
	 */
	private void updateOrderToUserConfirm(OrderPO order, long uid) {
		long orderId = order.getOrderId();
		// 当前订单状态
		short oldStatus = order.getStatus();
		// 确认收货后 订单的状态
		short nextStatus = OrderStatus.CONFIRM_RECEIPT_USER.getValue();
		// 订单是否完成标志 hins 2015/10/22 修改 内容：根据新需求，用户确认收货后，订单就“完成”，跟商家是否确认送达不影响。
		Date now = new Date();
//		// 修改订单状态
//		// Felix 2015/11/13 修改定时任务更新的标识
//		int rows = orderDao.updateUserConfirm(orderId, oldStatus, nextStatus, now, now);// (orderId, oldStatus, nextStatus, now,
//		// end hins 2015/10/22 修改 end
//
//		if (rows <= 0) {
//			logger.error("order#service#updateOrderToUserConfirm | 确认收货失败 | orderNo: {}, 订单状态: {}", order.getOrderNo(), order.getStatus());
//			throw new ServiceException(ResultCode.UNKNOWN_ERROR, OrderMessages.ORDER_ORDER_UPDATEUSERCONFIRM_FAILURE());
//		}

		// #TODO 更新门店交易中余额 并 记录交易流水明细 (同步操作, 考虑下是否用MQ来写)
		

	}


}
