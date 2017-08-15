package com.yogu.commons.wx;

import java.util.HashMap;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.fasterxml.jackson.core.type.TypeReference;
import com.yogu.commons.cache.redis.Redis;
import com.yogu.commons.cache.redis.RedisLock;
import com.yogu.commons.server.context.MainframeBeanFactory;
import com.yogu.commons.utils.HttpClientUtils;
import com.yogu.commons.utils.JsonUtils;
import com.yogu.commons.utils.LogUtil;
import com.yogu.commons.utils.cfg.EncryptionPropertyPlaceholderConfigurer;

/**
 * access token 改为缓存读取方式,失效再读取
 * 
 * @author sky
 *
 */
public class KFwxAccessTokenReader {

	private static final Logger logger = LoggerFactory.getLogger(KFwxAccessTokenReader.class);

	/**
	 * 获取间隔
	 */
	private static final int INTERVAL = 2 * 60;

	// access token 在 7200秒过期，
	private static int TOKEN_TIMEOUT = 7200 - INTERVAL;

	private static String hf_appid = null;

	private static String hf_appsecret = null;

	/**
	 * access token
	 */
	public String wxAccessToken = "";

	/**
	 * jsapi_ticket
	 */
	public String wxJsapiTicket = "";

	/**
	 * 卡券api_ticket
	 */
	public String wxCardApiTicket = "";

	private static Redis redis;

	static {

		readConfig();
		// clearCache();
	}

	public static Map<String, String> readAccessToken() {

		Map<String, String> map = new HashMap<>();

		String atKey = getJsAccessTokenCacheKey();
		String accessToken = redis().get(atKey);
		String jsapiTicket = "";
		String cardApiTicket = "";
		if (StringUtils.isBlank(accessToken)) {

			accessToken = readHoifanAccessToken();
			jsapiTicket = readTicket(accessToken);
			cardApiTicket = readCardTicket(accessToken);

			logger.info("kf#wx#accessToken | cache为空,通过接口重新获取数据 | accessToken: {}, jsapiTicket: {},cardApiTicket: {}",
					LogUtil.hidePassport(accessToken), LogUtil.hidePassport(jsapiTicket), LogUtil.hidePassport(cardApiTicket));
		} else {

			jsapiTicket = redis().get(getJsapiTicketCacheKey());
			cardApiTicket = redis().get(getCardApiTicketCacheKey());

			logger.info("kf#wx#accessToken | cache不为空， 获取缓存数据 | accessToken: {}, jsapiTicket: {},cardApiTicket: {}",
					LogUtil.hidePassport(accessToken), LogUtil.hidePassport(jsapiTicket), LogUtil.hidePassport(cardApiTicket));
		}

		map.put(WeixinAccessToken.KF_ACCESS_TOKEN, accessToken);
		map.put(WeixinAccessToken.KF_JSAPI_TICKET, jsapiTicket);
		map.put(WeixinAccessToken.KF_CARD_API_TICKET, cardApiTicket);

		return map;

	}

	private static String getJsAccessTokenCacheKey() {
		return "KF_wxAccessTokenCacheKey";
	}

	/**
	 * jsapi_ticket cache key
	 * 
	 * @return
	 */
	private static String getJsapiTicketCacheKey() {
		return "KF_wxJSapiTicketKey";
	}

	/**
	 * 卡券 api_ticket cache key
	 * 
	 * @return
	 */
	private static String getCardApiTicketCacheKey() {
		return "KF_wxCardApiTicketKey";
	}

	public static void clearCache() {

		redis().del(getJsAccessTokenCacheKey());
		redis().del(getJsapiTicketCacheKey());
		redis().del(getCardApiTicketCacheKey());

		logger.info("清除开饭微信卡券相关缓存....");
	}

	private static Redis redis() {
		if (null == redis)
			redis = (Redis) MainframeBeanFactory.getBean("redis");
		return redis;
	}

	/**
	 * 读取配置，只读一次
	 * 
	 * @throws Exception
	 */
	public static void readConfig() {

		hf_appid = (String) EncryptionPropertyPlaceholderConfigurer.getConfig().get("kf_appid");
		hf_appsecret = (String) EncryptionPropertyPlaceholderConfigurer.getConfig().get("kf_appsecret");
	}

	/**
	 * 通过hoifan 服务器接口获取accessToken
	 * 
	 * @return
	 * @author sky 2016-06-21
	 */
	private static String readHoifanAccessToken() {

		RedisLock lock = new RedisLock(redis, "KFWxAccessTokenKey");
		String wxAccessToken = "";
		if (lock.lock()) {

			wxAccessToken = WxAccessTokenRemoteReader.readAccessToken(WxAccessTokenRemoteReader.hoifan, hf_appid);

			if (StringUtils.isBlank(wxAccessToken))
				logger.error("kf#wx#accessToken | 通过hoifan接口获取token 出错 | wxAccessToken: {}", wxAccessToken);
			else
				// 缓存，并设置过期时间
				redis().set(getJsAccessTokenCacheKey(), wxAccessToken, TOKEN_TIMEOUT);

			lock.unlock();
		}
		return wxAccessToken;
	}

	/**
	 * 读取 access token
	 */
	@Deprecated
	public static String readToken() {

		RedisLock lock = new RedisLock(redis, "KFWxAccessTokenKey");

		String wxAccessToken = "";

		if (lock.lock()) {
			Map<String, String> params = new HashMap<>(4);
			params.put("grant_type", "client_credential");
			params.put("appid", hf_appid);
			params.put("secret", hf_appsecret);
			String json = HttpClientUtils.doGet("https://api.weixin.qq.com/cgi-bin/token", 10000, params);
			if (StringUtils.isBlank(json)) {
				logger.error("读取开饭餐厅微信 access token 错误");
			}
			Map<String, Object> map = JsonUtils.parseObject(json.trim(), new TypeReference<Map<String, Object>>() {
			});
			if (map.containsKey("access_token")) {
				wxAccessToken = (String) map.get("access_token");
				logger.info("读取开饭餐厅微信 access token 成功");
				if (map.containsKey("expires_in")) {
					int expiresIn = ((Number) map.get("expires_in")).intValue();
					logger.info("过期时间 (s)：" + expiresIn);
					TOKEN_TIMEOUT = expiresIn - INTERVAL;
					// 缓存，并设置过期时间
					redis().set(getJsAccessTokenCacheKey(), wxAccessToken, TOKEN_TIMEOUT);
				}

			} else {
				logger.error("读取开饭餐厅微信 access token 错误：" + json);
			}

			lock.unlock();
		}

		return wxAccessToken;
	}

	/**
	 * 读取 jsapi_ticket
	 */
	private static String readTicket(String wxAccessToken) {

		String wxJsapiTicket = "";

		Map<String, String> params = new HashMap<>(4);
		params.put("access_token", wxAccessToken);
		params.put("type", "jsapi");
		String json = HttpClientUtils.doGet("https://api.weixin.qq.com/cgi-bin/ticket/getticket", 10000, params);
		Map<String, Object> map = JsonUtils.parseObject(json.trim(), new TypeReference<Map<String, Object>>() {
		});
		int errcode = -1;
		if (map.get("errcode") != null) {
			errcode = ((Number) map.get("errcode")).intValue();
			logger.info("读取开饭餐厅微信 jsapi_ticket 结果，errcode: {}, errmsg: {}", errcode, map.get("errmsg"));
			if (errcode == 0) {
				wxJsapiTicket = (String) map.get("ticket");

				// set to cache
				redis().set(getJsapiTicketCacheKey(), wxJsapiTicket);

				logger.info("读取开饭餐厅微信 jsapi_ticket 成功!");
			}
		} else {
			logger.error("读取开饭餐厅微信 jsapi_ticket 错误：" + json);
		}

		return wxJsapiTicket;
	}

	/**
	 * 获取卡券 api_ticket
	 */
	private static String readCardTicket(String wxAccessToken) {

		String wxCardApiTicket = "";

		Map<String, String> params = new HashMap<>(4);
		params.put("access_token", wxAccessToken);
		params.put("type", "wx_card");
		String json = HttpClientUtils.doGet("https://api.weixin.qq.com/cgi-bin/ticket/getticket", 10000, params);
		Map<String, Object> map = JsonUtils.parseObject(json.trim(), new TypeReference<Map<String, Object>>() {
		});
		int errcode = -1;
		if (map.get("errcode") != null) {
			errcode = ((Number) map.get("errcode")).intValue();
			logger.info("读取开饭餐厅微信  卡券 api_ticket 结果，errcode: {}, errmsg: {}", errcode, map.get("errmsg"));
			if (errcode == 0) {

				wxCardApiTicket = (String) map.get("ticket");
				// set to cache
				redis().set(getCardApiTicketCacheKey(), wxCardApiTicket);

				logger.info("读取开饭餐厅微信 卡券 api_ticket 成功! ticket: {}", LogUtil.hidePassport(wxCardApiTicket));
			}
		} else {
			logger.error("读取开饭餐厅微信 卡券 api_ticket 错误：" + json);
		}

		return wxCardApiTicket;
	}

}
