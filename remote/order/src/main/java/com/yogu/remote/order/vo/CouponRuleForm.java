package com.yogu.remote.order.vo;

import java.util.Date;

import javax.ws.rs.FormParam;

/**
 * 新增、编辑优惠券的时候，admin接口接受参数的表单
 * 
 * @author ten 2015/12/26.
 */
public class CouponRuleForm {

	/** 优惠券规则主键id */
	@FormParam("couponRuleId")
	private long couponRuleId;

	/** 券的显示名称 */
	@FormParam("couponName")
	private String couponName = "";

	/** 券的显示描述 */
	@FormParam("description")
	private String description = "";

	/** 券的责任方类型 1:平台型 2:商家性 */
	@FormParam("couponBearType")
	private short couponBearType;

	/** 优惠券类型 1：现金抵用券 2：折扣券 3：满减券 */
	@FormParam("couponType")
	private short couponType;

	/** 满多少钱(该值大于0表示使用该券订单应该满足的额度) */
	@FormParam("enoughMoney")
	private long enoughMoney = 0;

	/** 规则表达式 (如: faceValue=30;discount=0.5; )目前该字段只存值如：30，0.5 */
	@FormParam("regExpression")
	private String regExpression = "";

	/** 每人最高可领次数 */
	@FormParam("gainTotal")
	private int gainTotal;

	/** 使用时的手机尾号限制 */
	@FormParam("phoneSuffix")
	private String phoneSuffix = "";

	/** 有效期开始时间 */
	@FormParam("startTime")
	private String startTimeStr;

	/** 有效期截止时间 */
	@FormParam("endTime")
	private String endTimeStr;
	
	private Date startTime;

	/** 有效期截止时间 */
	private Date endTime;

	/** 修改人 */
	@FormParam("adminId")
	private long adminId;

	/** 是否启用 1:启用 0:不启用 */
	@FormParam("isEnable")
	private short isEnable;

	/** 是否终止发放 1: 是 0: 否 */
	@FormParam("isStop")
	private short isStop;

	/**
	 * 发放总量
	 */
	@FormParam("createTotal")
	private int createTotal;

	/** 折扣券最高优惠金额 **/
	private long mostOffer = 0;
	
	/** 优惠券使用渠道，0:所有渠道 1:线上订餐 2:mazing pay */
	private short usingChannel = 0;

	public long getCouponRuleId() {
		return couponRuleId;
	}

	public void setCouponRuleId(long couponRuleId) {
		this.couponRuleId = couponRuleId;
	}

	public String getCouponName() {
		return couponName;
	}

	public void setCouponName(String couponName) {
		this.couponName = couponName;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public short getCouponBearType() {
		return couponBearType;
	}

	public void setCouponBearType(short couponBearType) {
		this.couponBearType = couponBearType;
	}

	public short getCouponType() {
		return couponType;
	}

	public void setCouponType(short couponType) {
		this.couponType = couponType;
	}

	public long getEnoughMoney() {
		return enoughMoney;
	}

	public void setEnoughMoney(long enoughMoney) {
		this.enoughMoney = enoughMoney;
	}

	public String getRegExpression() {
		return regExpression;
	}

	public void setRegExpression(String regExpression) {
		this.regExpression = regExpression;
	}

	public int getGainTotal() {
		return gainTotal;
	}

	public void setGainTotal(int gainTotal) {
		this.gainTotal = gainTotal;
	}

	public String getPhoneSuffix() {
		return phoneSuffix;
	}

	public void setPhoneSuffix(String phoneSuffix) {
		this.phoneSuffix = phoneSuffix;
	}

	public String getStartTimeStr() {
		return startTimeStr;
	}

	public void setStartTimeStr(String startTimeStr) {
		this.startTimeStr = startTimeStr;
	}

	public String getEndTimeStr() {
		return endTimeStr;
	}

	public void setEndTimeStr(String endTimeStr) {
		this.endTimeStr = endTimeStr;
	}

	public long getAdminId() {
		return adminId;
	}

	public void setAdminId(long adminId) {
		this.adminId = adminId;
	}

	public short getIsEnable() {
		return isEnable;
	}

	public void setIsEnable(short isEnable) {
		this.isEnable = isEnable;
	}

	public short getIsStop() {
		return isStop;
	}

	public void setIsStop(short isStop) {
		this.isStop = isStop;
	}

	public int getCreateTotal() {
		return createTotal;
	}

	public void setCreateTotal(int createTotal) {
		this.createTotal = createTotal;
	}

	public long getMostOffer() {
		return mostOffer;
	}

	public void setMostOffer(long mostOffer) {
		this.mostOffer = mostOffer;
	}

	public short getUsingChannel() {
		return usingChannel;
	}

	public void setUsingChannel(short usingChannel) {
		this.usingChannel = usingChannel;
	}

	public Date getStartTime() {
		return startTime;
	}

	public void setStartTime(Date startTime) {
		this.startTime = startTime;
	}

	public Date getEndTime() {
		return endTime;
	}

	public void setEndTime(Date endTime) {
		this.endTime = endTime;
	}
	
	

	
}
