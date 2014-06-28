package com.hoot.module;

import com.hoot.pojo.Request;

public class RequestInfo {
	public Request reqest;
	public String api;

	public static class Api {
		public static final String createUser = "/user/save/1";
		public static final String AUTH = "/user/auth/";
		public static final String createRouter = "/router/save/1";
		public static final String MATH_ROUTER = "/router/match/";
		public static final String SEARCH = "http://m.baidu.com";
		public static final String NEWS = "http://xw.qq.com/m/news/index.htm";
		public static final String JD = "http://m.JD.com";
		public static final String Tmall = "http://m.tmall.com";
		public static final String Taobao = "http://m.taobao.com";
		public static final String Shiping = "http://m.v.qq.com";
		public static final String Weibo = "http://m.weibo.cn";
		public static final String Dianping = "http://m.dianping.com";
		public static final String Navigation = "http://hao.uc.cn";// 导航
		public static final String Navigation_1 = "http://hao.uc.cn/?uc_param_str=dnfrpfbivecpbtnt";
		public static final String App = "http://m.app.uc.cn";// app下载
		public static final String App_1 = "http://m.app.uc.cn/apk/index.php?system=soft&module=rank&app=1&f=7_1_0_0_0&view=default&uc_param_str=dnfrpfbivesscpmibtbmntnisiei";
		public static final String Mogujie = "http://m.mogujie.com/x5?f=";
		public static final String Jumei = "http://m.jumei.com";
		public static final String Qunar = "http://touch.qunar.com/";
		public static final String Qidian = "http://m.qidian.com/";
		public static final String Autohome = "http://m.autohome.com.cn/";
		public static final String Jiayuan = "http://m.jiayuan.com/";
	}
}
