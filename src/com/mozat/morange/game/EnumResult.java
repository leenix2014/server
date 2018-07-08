package com.mozat.morange.game;

public class EnumResult {
    public static final int RESULT_UNKNOWN = -2;//鏈煡閿欒
    public static final int RESULT_HACKER = -1;//榛戝
    public static final int RESULT_OK = 0;//鎴愬姛
    public static final int RESULT_FAIL = 1;//澶辫触
    public static final int RESULT_UPGRADING_LIST_ISFULL = 2;//鍗囩骇闃熷垪宸茬粡婊′簡
    public static final int RESULT_GOVERNMENT_LEVEL_NOT_ENOUGH = 3;//鏀垮簻鐨勭瓑绾т笉瓒冲
    public static final int RESULT_NOT_ENOUGH_RESOURCE = 4;//璧勬簮涓嶈冻
    public static final int RESULT_NOT_ENOUGH_DIAMOND = 5;//閽荤煶涓嶈冻
    public static final int RESULT_BATTLE_UNFINISHED = 6;//鎴樻枟杩樻湭缁撴潫
    public static final int RESULT_MSG_TO_LONG = 7;//杈撳叆鐨勫瓧绗﹀お锟
    public static final int RESULT_ITEM_NOT_ENOUGH = 8;//閬撳叿涓嶈冻
    public static final int RESULT_TOO_FAST = 9;//鎿嶄綔澶揩锟
    public static final int RESULT_NOT_ENOUGH_SOLDIERS = 10;//澹叺涓嶈冻
    public static final int RESULT_CITY_HAS_BEED_OCCIPIED = 11;//宸茬粡琚崰锟
    public static final int RESULT_TIME_NOT_LIMIT = 12;//鏃堕棿涓嶅厑锟
    public static final int RESULT_COORD_NOT_LIMIT = 13;//涓栫晫鍦板浘鐨勫潗鏍囬敊锟
    public static final int RESULT_NOT_ENOUGH_BUILDING_LEVEL = 14;//寤虹瓚绛夌骇涓嶈冻
    public static final int RESULT_NOT_MY_PRISONER = 15;//涓嶆槸鎴戞柟鐨勪繕锟
    public static final int RESULT_NEED_TO_PAY_FOR_DUNGEONS = 16;//褰撳ぉ鍓湰娆℃暟杈惧埌涓婇檺
    public static final int RESULT_BOOKMARKS_OVERFLOW = 17;//bookmarks鐨勪釜鏁拌揪鍒颁簡涓婇檺
    public static final int RESULT_ITEM_OVERFLOW = 18;//閬撳叿鐨勪釜鏁拌揪鍒颁簡涓婇檺
    public static final int RESULT_FRIEND_REQUEST_FAIL_IN_BLOCK = 19;//in black list
    public static final int RESULT_FRIEND_REQUEST_FAIL_ITS_FULL = 20;//user's friend is full
    public static final int RESULT_FRIEND_REQUEST_FAIL_IAM_FULL = 21;//my friend is full
    public static final int RESULT_FRIEND_BLOCK_FAIL_IAM_FULL = 22;//my block is full
    public static final int RESULT_DUPLICATE_NAME = 23;//鑱旂洘閲嶅悕
    public static final int RESULT_ALLIANCE_IS_FULL = 24;//鑱旂洘婊′簡
    public static final int RESULT_NOT_ENOUGH_PERMISSIONS = 25;//鎿嶄綔鐨勬潈闄愪笉璁稿彲
    public static final int RESULT_NOT_ENOUGH_CAPACITY = 26;//瓒呰繃瀹归噺涓婇檺
    public static final int RESULT_FUSE_FAIL = 27;//鍚堟垚澶辫触
    public static final int RESULT_DESTROY_EQUIPMENT_FAIL_BY_WARRIORSOUL = 28;//閿€姣佽澶囧洜鏈夐暥宓屽疂鐭宠€屽け锟
    public static final int RESULT_DRESS_EQUIPMENT_FAIL_BY_LEVEL = 29;//绌胯澶囧洜绛夌骇涓嶅鑰屽け锟
    public static final int RESULT_DRESS_EQUIPMENT_FAIL_BY_JOB = 30;//绌胯澶囧洜鏈夎亴涓氫笉绗﹁€屽け锟
    public static final int RESULT_ADD_REQUEST_TO_A_FRIEND = 31;//绌胯澶囧洜鏈夎亴涓氫笉绗﹁€屽け锟
    public static final int RESULT_NOT_IN_AN_ALLIANCE = 32;//涓嶅湪涓€涓仈鐩熼噷

    public static final int RESULT_CITY_IS_PROTECTED = 33;//鍩庡競鍦ㄤ繚鎶ょ姸鎬侊紝涓嶈兘琚敾锟
    public static final int RESULT_APP_LIST_ISNULL = 34; //鑾峰彇app billing list涓虹┖
    public static final int RESULT_IS_ACCEPTED_TASKS = 35; //宸茬粡鎺ュ彈浠诲姟
    public static final int RESULT_ILLEGAL = 36; //illegal

    public static final int RESULT_BAN_CHAT = 37; //绂佹鍙戣█
    public static final int RESULT_NOT_ENOUGH_CONTRIBUTION = 38;
    public static final int RESULT_ALLIANCE_BUF_IS_UNEXPIRED = 39;
    public static final int RESULT_FULL = 40;
    public static final int RESULT_SOLDIER_LEVEL_NOT_ENOUGH = 43;//澹叺绛夌骇涓嶈冻
    public static final int RESULT_LIMITED_LUCKYDRAW_TIMES = 41;
    public static final int RESULT_SOLDIER_HAVE_CHANGE_JOB = 42;//宸茬粡杞亴
    public static final int RESULT_ALLIANCE_DUNGEONS_NO_BATTLE_RECORD = 43;
    public static final int RESULT_ALLIANCE_DUNGEONS_EXCEED_BATTLE_LIMIT = 44;
    public static final int RESULT_ALLIANCE_DUNGEONS_ALLIANCE_DUNGEONS_DONE = 45;
    public static final int RESULT_BOSS_HAS_DIED = 46;
    public static final int RESULT_REDEEM_USED_CODE = 47; // 閲嶅浣跨敤宸插厬鎹㈢殑锟
    public static final int RESULT_REDEEM_INVAILD_CODE = 48; // 鏃犳晥鍏戞崲锟
    public static final int RESULT_REDEEM_EXPIRED_CODE = 49; // 澶辨晥鍏戞崲锟
    public static final int RESULT_CITY_IS_SILENCE = 50;//鍩庡競鍦ㄦ矇榛樼姸锟
    public static final int RESULT_HERO_LEVEL_NOT_ENOUGH = 51;//鑻遍泟绛夌骇涓嶈冻
    
    public static final int RESULT_NOT_ENOUGH_TIME = 52;
    public static final int RESULT_NOT_SAME_RANK = 53;
    public static final int RESULT_DEFENSE_IS_FIGHT = 54;
    public static final int RESULT_NO_HERO = 55;
    public static final int RESULT_NO_SUIT_ARENA_TYPE = 56;
    public static final int RESULT_NO_ARENA = 57;
    

    public static final int RESULT_UPGRADE_TO_ADD_TIME = 58;
    public static final int RESULT_UPGRADE_TO_FINISH = 59;
    
    public static final int RESULT_HAVE_OUT_FIRE_BY_OTHER = 60;
    public static final int RESULT_NOT_ENOUGH_ALLIANCE_MEMBER = 61;
    public static final int RESULT_NO_ALLIANCE_FORT = 62;
    public static final int RESULT_QUEST_MAX_ORDER = 63;
    public static final int RESULT_DEFENSE_NO_TEAM = 64;
   
    public static final int RESULT_TIME_NOT_ALLOWED = 65;
    
    public static final int RESULT_REFINED_EQUIP_LOSS_LEVEL = 66;
    public static final int RESULT_REFINED_EQUIP_KEEP_LEVEL = 67;
    public static final int RESULT_REFINED_EQUIP_ADD_LEVEL = 68;
    		
    public static final int RESULT_STORAGE_CAPACITY_NOT_ENOUGH = 69;
  
  	public static final int RESULT_BUILDING_IS_UPGRADING = 70;
    
    public static final int RESULT_MORE_THAN_ONE_ARENA = 72;
    public static final int RESULT_BUILDING_IS_NOT_UPGRADING = 71;
    public static final int RESULT_ARENA_KICK_OUT = 73;
    
    public static final int RESULT_BOOKMARKS_OVER_VIP_LIMIT = 74;
    
    public static final int RESULT_INVITE_CODE_HAVE_GOT = 75;//鐢ㄦ埛鎴栬澶囧凡缁忛鍙栬繃
    public static final int RESULT_INVITE_INVAILD_CODE = 76;//鏃犳晥閭€璇风爜鎴栭個璇风爜涓嶅瓨鍦
    public static final int RESULT_INVITE_EXPIRED_CODE = 77;//閭€璇风爜宸茶繃鏈
    
	public static final int RESULT_PALAYER_NOT_ONLINE = 78;
    public static final int RESULT_NOT_ENOUGH_CITYWALL_LEVEL = 79;
    
    public static final int RESULT_INVITE_NOT_NEW_USER = 80;//涓嶆槸鏂扮敤鎴凤紝涓嶈兘浣跨敤閭€璇风爜
    
    //鎴樻枟缁撴灉宸茬粡琚鐞嗭紝姝ゆ椂鍙樉绀鸿儨锟
    public static final int RESULT_BATTLE_HAS_ALREADY_HANDLE = 100;

    public static final int RESULT_ISLAND_LANDING_DOCK_FAILED = 201;
    public static final int RESULT_ISLAND_NOT_OPEN = 202;
    public static final int RESULT_ISLAND_NONE_MONSTER = 203;
    public static final int RESULT_ISLAND_LANDING_DOCK_HAD_FULL = 204;//鐮佸ご娌℃湁浣嶇疆
    public static final int RESULT_ISLAND_LANDING_DOCK_HAD_ONE = 205;//璇ョ爜澶存湁浜轰簡
    public static final int RESULT_ISLAND_NONE_EMBATTLE = 206;//杩樻病甯冮樀
    public static final int RESULT_ISLAND_START_BATTLE = 207;//宸茬粡寮€濮嬫垬鏂椾簡
    public static final int RESULT_ISLAND_IN_FAIL_CD = 208;//杩樺湪CD褰撲腑
    public static final int RESULT_ISLAND_LASTONE_ATTACKED = 209;//鏈€鍚庝竴涓€鍦ㄨ鏀诲嚮     
    public static final int RESULT_ISLAND_KILL_MONSTER_NOT_ENOUGH = 210;//鏉€姝荤殑灏忔€暟涓嶅       
    public static final int RESULT_ISLAND_FAILED_FIGHTING_CD = 211;//鎴樻枟澶辫触cd涓
    public static final int RESULT_ISLAND_BOSS_CD = 212;//Boss cdcd        
    public static final int RESULT_ISLAND_NOT_IN_REVICECECD = 213;//涓嶅啀澶嶆椿鏃堕棿    
    public static final int RESULT_ISLAND_DIA_BUFF_MAX = 214;//閽荤煶涔癰uff鍒版渶澶т簡   
    public static final int RESULT_ISLAND_RES_BUFF_MAX = 215;//璧勬簮涔癰uff鍒版渶澶т簡
    public static final int RESULT_MOVED_CITY = 216;
	
	//名人怪活动，分数不达标
    public static final int RESULT_CELEBRITY_CHASE_SCORE_NO_COMPLIANCE = 101;      
}
