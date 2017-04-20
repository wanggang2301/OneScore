package com.hhly.mlottery.util;

/**
 * @dec SharedPreferences存储文件的Key
 * @author 107
 *
 */
public interface MyConstants {
	
	String SPFILENAME = "spSettingName";// 设置相关文件
	
	//String VOICEGOALHINT = "voiceGoalHint";// 进球提示声音
	String VIBRATEGOALHINT = "vibrateGoalHint";// 进球提示是否振动boolean
	
	String HOSTTEAMINDEX = "hostTeamIndex";// 主队进球声音下标int
	String GUESTTEAM = "guestTeam";// 客队进球声音下标int
	
	String VOICEREDHINT = "voiceRedHint";// 红牌提示声音int
	String VIBRATEREDHINT = "vibrateRedHint";// 红牌提示是否振动boolean
	
	String GAMEATTENTION = "gameAttention";// 赛事关注boolean
	String FOOTBALL_PUSH_FOCUS="football_push";
	//新版设置
	String GOAL="goal";
	String RED_CARD="red_card";
	String SHAKE="shake";
	String SOUND="sound";
	//String ODDSSHOW = "oddsShow";// 赔率显示
	//String REFRESHTIME = "refreshTime";// 页面刷新时间
	
	String RBSECOND = "rbSecond";// 亚盘boolean
	String rbSizeBall = "RBSIZEBALL";// 大小球boolean
	String RBOCOMPENSATE = "rbOCompensate";// 欧赔 boolean
	String RBNOTSHOW = "rbNotShow";// 不显示boolean
	
	String NUMBEROKS = "numberOks";// 用户已定制彩种
	String NUMBERDEFS = "numberDefs";// 用户未定制彩种
	
	String DEFUALT_SETTING = "DEFUALT_SETTING";//默认设置
	
	String URL_HOME_CONFIG = "URL_HOME_CONFIG";//测试包环境控制
	String WS_HOME_CONFIG = "WS_HOME_CONFIG";//测试包socket环境控制

	//篮球 赛事提示
	String HALF_FULL_SCORE = "Half_full_score"; //半全场比分提示
	String SCORE_DIFFERENCE = "score_difference"; //总分差 提示
	String SINGLE_SCORE = "single_score";//单节得分
	String HOST_RANKING = "Host_ranking";//主客排名
	String BASKETBALL_PUSH_FOCUS ="is_push";

	//篮球 赔率提示
	String BASKETBALL_RBSECOND = "basketball_rbSecond";// 亚盘
	String BASKETBALL_rbSizeBall = "basketball_RBSIZEBALL";// 大小球
	String BASKETBALL_RBOCOMPENSATE = "basketball_rbOCompensate";// 欧赔
	String BASKETBALL_RBNOTSHOW = "basketball_rbNotShow";// 不显示

	//启动页url
	String START_IMAGE_URL="start_image_url";

	//斯洛克 设置页
	String SNOOKER_ALET = "snooker_alet";// 亚盘
	String SNOOKER_EURO = "snooker_euro";// 欧赔
	String SNOOKER_ASIZE = "snooker_asize";// 大小盘
	String SNOOKER_SINGLETWIN = "snooker_single_twin";//单双
	String SNOOKER_NOTSHOW = "snooker_noShow";// 不显示

	//网球 设置页
	String TENNIS_ALET = "tennis_alet";// 亚盘
	String TENNIS_EURO = "tennis_euro";// 欧赔
	String TENNIS_ASIZE = "tennis_asize";// 大小球
	String TENNIS_NOTSHOW = "tennis_noShow";// 不显示
}
