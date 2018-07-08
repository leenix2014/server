package com.mozat.morange.game;


public class EnumAction {

    public static final int ACTION_CREATE_BUILDING = 0;
    public static final int ACTION_MOVE_BUILDING = 1;
    public static final int ACTION_REVERTUPGRADE_BUILDING = 2;
    public static final int ACTION_UPGRADE_BUILDING = 3;
    public static final int ACTION_UPGRADE_BUILDING_BY_DIAMOND = 5;
    //public static final int ACTION_GET_USER_ALL_INFO = 7;
    public static final int ACTION_SYNCH_ALL_BUILDING = 8;
    public static final int ACTION_COLLECT_GOLD = 9;
    public static final int ACTION_COLLECT_CRYSTAL = 10;
    public static final int ACTION_GOVERNMENT_TAXATION = 11;
    public static final int ACTION_GET_BUILDING_INFO = 13;
    public static final int ACTION_GET_GOVERNMENT_BUILDINGS_LIMIT_INFO = 14;
//    public static final int ACTION_GET_BUILDING_CONFIG_INFO = 15;
//    public static final int ACTION_GET_SOLDIERS_CONFIG_INFO = 16;
    public static final int ACTION_PRODUCTION_SOLDIERS = 17;
    public static final int ACTION_CANCEL_PRODUCTION_SOLDIERS = 18;
    public static final int ACTION_USE_DIAMOND_TO_PRODUCTION_SOLDIERS = 19;
    //    public static final int ACTION_GET_ATTACK_AND_DEFENSE_CONFIG = 23;
    public static final int ACTION_CHAT_SEND_MSG_TO_SINGLE = 24;
    public static final int ACTION_CHAT_SEND_MSG_TO_WORLD = 25;
    public static final int ACTION_CHAT_SEND_MSG_TO_GROUP = 26;
    public static final int ACTION_CHAT_SEND_MSG_BY_SYSTEM = 27;
    /**
     * 全服广播
     */
    public static final int ACTION_CHAT_SEND_MSG_BY_BROADCAST = 28;

    //    public static final int ACTION_GET_SKILL_CONFIG = 28;
//    public static final int ACTION_GET_BUFF_CONFIG = 29;
//    public static final int ACTION_GET_BUFFOVERLAY_CONFIG = 30;
    public static final int ACTION_EMBATTLE = 31;
    public static final int ACTION_GET_FRIENDSLIST = 32;

    public static final int ACTION_CHECK_INVADER = 33;
    public static final int ACTION_START_INVADER = 34;
    public static final int ACTION_SYN_EMBATTLE_DATA = 36;
    public static final int ACTION_SYN_WORLD_MAP = 38;
    public static final int ACTION_MOVE_CITY = 39;

	public static final int ACTION_UPGRADE_SOLDIER = 40;

    public static final int ACTION_GET_FRIEND_INFO = 41;

    public static final int ACTION_GET_FRIEND_RELATIONSHIP = 42;

    public static final int ACTION_CHANGE_FRIEND_RELATIONSHIP = 43;

    public static final int ACTION_USE_DIAMOND_TO_UPGRADE_SOLDIERS = 44;
    public static final int ACTION_MAIL = 45;

    public static final int ACTION_ITEM_USE = 46;
    public static final int ACTION_CLIENT_PING_PONG = 48;

    public static final int ACTION_SET_USERNAME = 49;

    public static final int ACTION_DUNGEONS = 50;

    public static final int ACTION_GET_BOOKMARKS_INFO = 51;

    public static final int ACTION_ADD_BOOKMARK = 52;

    public static final int ACTION_DELETE_BOOKMARK = 53;

    public static final int ACTION_CHECK_EXPEDITIONPACK = 54;


    public static final int ACTION_CHECK_RESOURCE_BUY_ITEMS = 56;
    public static final int ACTION_CREATE_ALLIANCE = 57;

    public static final int ACTION_ADD_APPLICATION = 58;

    public static final int ACTION_CANCEL_APPLICATION = 59;

    public static final int ACTION_ACCEPT_APPLICATION = 60;

    public static final int ACTION_REJECT_APPLICATION = 61;

    public static final int ACTION_LEAVE_ALLIANCE = 62;

    public static final int ACTION_DISSOLVE_ALLIANCE = 63;

    public static final int ACTION_ABDICATE = 64;

    public static final int ACTION_KICK = 65;

    public static final int ACTION_SEEKHELP = 66;

    public static final int ACTION_DONATE_SOLDIER = 67;

    public static final int ACTION_CONTRIBUTE = 68;

    public static final int ACTION_GET_ANNOUNCEMENT = 69;

    public static final int ACTION_GET_ALLIANCE_LIST = 70;

    public static final int ACTION_GET_ALLIANCE_INFO = 71;

    public static final int ACTION_GET_ALLIANCE_ID = 72;

//    public static final int ACTION_GET_CONFIG = 73;

    public static final int ACTION_ALLIACNE_PERSONAL_UPGRADE = 74;

    /**
     * 该接口已经不推荐使用
     */
    @Deprecated
    public static final int ACTION_ADD_HERO_EXP = 76;

    public static final int ACTION_DRESS_HERO_EQUIP = 77;

    public static final int ACTION_UNDRESS_HERO_EQUIP = 78;

    public static final int ACTION_CREATE_HERO = 79;

    /**
     * old protocol ,new protocol is : ACTION_GET_BACKPACK_ITEMS_NEW
     */
    public static final int ACTION_GET_BACKPACK_ITEMS = 80;

    public static final int ACTION_ADD_TREPANNING = 81;

    public static final int ACTION_ENCHASE = 82;

//    public static final int ACTION_DESTORY_EQUIPMENT = 83;

    public static final int ACTION_UNENCHASE = 84;

    public static final int ACTION_WARRIORSOUL_FUSE = 85;//需要修改该接口,新接口为525

    public static final int ACTION_ENDBATTLE_AND_OCCUPIED = 86;
    public static final int ACTION_ENDBATTLE_AND_RESIST = 87;

    public static final int ACTION_LEAVE_BATTLE = 88;

    public static final int ACTION_RELEASE = 89;

//    public static final int ACTION_UPLOAD_BATTLE_REPLAY = 90;

    public static final int ACTION_ADD_BACKPACK = 91;

    /**
     * old protocol ,new protocol is : ACTION_BUY_ITEM_NEW
     */
    public static final int ACTION_BUY_ITEM = 92;

    /**
     * old protocol ,new protocol is : ACTION_DESTROY_ITEM_NEW
     */
    public static final int ACTION_DESTROY_ITEM = 93;

    public static final int ACTION_PRISONER_LIST = 94;

    public static final int ACTION_FINISH_GUIDE = 96;
    public static final int ACTION_CANCEL_UPGRADE_SOLDIER = 95;

    /**
     * 用钻石做联盟贡献*
     */
    public static final int ACTION_CONTRIBUTE_BY_DIAMOND = 97;

    /**
     * 拿到世界小地图随机点*
     */
    public static final int ACTION_GET_SMALL_MAP = 98;

    public static final int ACTION_SYNC_RANKING = 99;

    public static final int ACTION_INIT_RANKING = 100;

    public static final int ACTION_BUY_HERO_LIFE = 101;
    public static final int ACTION_BUY_HERO_MAX_LIFE = 102;
    public static final int ACTION_BUY_HERO_MANA = 103;
    public static final int ACTION_HERO_LEARN_MAGIC = 104;
    public static final int ACTION_HERO_EQUIP_MAGIC = 105;
    public static final int ACTION_HERO_UNEQUIP_MAGIC = 106;
    public static final int ACTION_HERO_USE_MAGIC = 107;
    public static final int ACTION_GET_HERO_MAGIC = 108;
    public static final int ACTION_SYNC_WORLD_BUILDING_INFO = 109;


    public static final int ACTION_SKILL = 110;
    public static final int ACTION_WISHINGWELL = 111;
    public static final int ACTION_PROTECTED_CITY = 112;
    public static final int ACTION_GET_LAST_WISHINGWELL_COUNT = 113;
    public static final int ACTION_RANDOM_MOVE_CITY = 114;

    //activity
    public static final int ACTION_GET_ACTIVITY_INFO = 121;
    public static final int ACTION_GET_ACTIVITY_NOTICE = 122;
    public static final int ACTION_GET_ACTIVITY_REWARD_LIST = 123;
    public static final int ACTION_NEW_ACTIVITY_EVENT = 124;
    public static final int ACTION_GET_ACTIVITY_REWARD = 125;
    public static final int ACTION_GET_LAST_OPERATIONS_AWARD = 126;

    public static final int ACTION_BILLING_APPPRICELIST = 131;
    public static final int ACTION_BILLING_PRICELIST = 132;
    public static final int ACTION_BILLING_GETCONFIG = 133;
    public static final int ACTION_BILLING_TOPUP = 134;
    public static final int ACTION_IOS_BILLING = 135;
    public static final int ACTION_ANDROID_BILLING = 136;
    public static final int ACTION_ANDROID_BILLING_TEST = 137;

    public static final int ACTION_SMS_BILLING_RESULT = 138;
    //daily tasks
    public static final int ACTION_GET_DAILY_TASKS_INFO = 139;
    public static final int ACTION_TASKS_REFRESH = 140;


    public static final int ACTION_TASKS_GET_REWARD = 141;
    public static final int ACTION_TASKS_FULL_STAR = 142;
    public static final int ACTION_TASKS_ACCEPT = 143;

    public static final int ACTION_AI_COLLECT = 144;
    public static final int ACTION_AI_RELEASE = 145;

    // World Monster
    public static final int ACTION_UPDATE_WORLD_MONSTER_INFO = 150;
    public static final int ACTION_CHECK_ATTACK_WORLD_MONSTER = 151;
    public static final int ACTION_END_ATTACK_WORLD_MONSTER = 152;
    public static final int ACTION_UPDATE_WORLD_MONSTER_RANKING = 153;
    public static final int ACTION_BROADCAST_WORLD_MONSTER_BEGIN = 154;
    public static final int ACTION_BROADCAST_WORLD_MONSTER_DEAD = 155;
    public static final int ACTION_BROADCAST_WORLD_MONSTER_FINISH = 156;
    
    //ContinuousCheckIn
    public static final int ACTION_IS_CONTINUOUS_CHECK_IN = 157;
    public static final int ACTION_CONTINUOUS_CHECK_IN_PRIZE = 158;
    
    //Ban login
    public static final int ACTION_CHECK_BAN_LOGIN = 159;
    //OnlineAward
    public static final int ACTION_SYNC_ONLINE_AWARD = 161;
    public static final int ACTION_GET_ONLINE_AWARD = 162;
    public static final int ACTION_SYNC_ONLINE_TOTALTIMES = 163;
    //logout
    public static final int ACTION_LOG_OUT = 170;
    public static final int ACTION_LOG_IN = 171;

    //检查活动是否结�
    public static final int ACTION_CHECK_ACTIVITY_STATUS = 180;
    public static final int ACTION_GET_BACKPACK_SPACE = 183;
    public static final int ACTION_BUY_BACKPACK_SPACE_AFTER_GET_REWARD = 184;
    public static final int ACTION_FORUM_GET_BOARD_INFO = 172;
    public static final int ACTION_FORUM_GET_THREADS_BY_BOARD = 173;
    public static final int ACTION_FORUM_GET_POSTS_BY_TOPIC = 174;
    public static final int ACTION_FORUM_CREATE_TOPIC = 175;
    public static final int ACTION_FORUM_CREATE_REPLY = 176;
    /**
     *old protocol ,new protocol is : ACTION_FORUM_GET_POST_NEW
     */
    @Deprecated
    public static final int ACTION_FORUM_GET_POST = 179;

    
    //achievement
    public static final int ACTION_ACHIEVEMENT_INIT = 181;
    public static final int ACTION_ACHIEVEMENT_GET_REWARD = 182;
    
    
    public static final int ACTION_FORUM_CREATE_FAVORITE = 177;
    public static final int ACTION_FORUM_CANCEL_FAVORITE = 178;
    //modify user gender
    public static final int ACTION_SET_USER_GENDER = 185;
    //modify user sign
    public static final int ACTION_SET_USER_SIGIN = 186;

    //use brocast
    public static final int ACTION_USE_BROADCAST = 187;
    
    //alliance buf
    public static final int ACTION_GET_ALLIANCE_BUF_INFO = 190;
    public static final int ACTION_ACTIVATED_ALLIANCE_BUF = 192;
    public static final int ACTION_MSG_HAS_ILLEGAL = 191;
    //use bubble
    public static final int ACTION_USE_BUBBLE = 188;

    public static final int ACTION_USE_SKY_FIRE_CARD = 193;

    public static final int ACTION_SET_USER_HEAD = 194;

    public static final int ACTION_BUY_AND_SET_USER_HEAD = 195;

    public static final int ACTION_GET_MY_ALL_HEADS = 196;

    public static final int ACTION_BUY_AND_USE_BROADCAST = 197;

    public static final int ACTION_BUY_AND_USE_BUBBLE = 198;

    public static final int ACTION_BUY_AND_USE_RENAMECARD = 199;
    public static final int ACTION_BUY_ITEM_NEW = 392;
    public static final int ACTION_DESTROY_ITEM_NEW = 393;
    
    //new get backpack data
    public static final int ACTION_GET_BACKPACK_ITEMS_NEW = 189;
    
    //get single activity info
    public static final int ACTION_GET_SINGLE_ACTIVITY_INFO = 501;//显示奖励列表
    //getTopPrizes reward

    public static final int ACTION_GET_TOPPRIZES_REWARD = 502;//得到奖励
    public static final int ACTION_BROADCAST_WORLD_MONSTER_FORECAST = 200;
    public static final int ACTION_BROADCAST_WORLD_MONSTER_GET_KILL_PRIZE = 201;
    public static final int ACTION_BROADCAST_WORLD_MONSTER_GET_RANKING_PRIZE = 202;
    public static final int ACTION_SHOW_STRONGEST_KING_TOP_CHANNEL = 203;
    public static final int ACTION_BUY_SOLDIER_AND_EMBATTLE = 204;
    public static final int ACTION_CHECK_ALLIANCE_DUNGEONS = 205;
    public static final int ACTION_START_ALLIANCE_DUNGEONS = 206;
    public static final int ACTION_REPLAY_ALLIANCE_DUNGEONS = 207;
    public static final int ACTION_END_ALLIANCE_DUNGEONS = 208;
    public static final int ACTION_REDEEM_CODE = 209;
    public static final int ACTION_GET_ALL_ACTIVITY_SUMMARY = 300;
    public static final int ACTION_GET_ACTIVITY_INFO_NEW = 301;
    public static final int ACTION_GET_ACTIVITY_PRIZES = 302;
    public static final int ACTION_GET_ACTIVITY_RANK = 303;
    public static final int ACTION_GET_ACTIVITY_REWARD_NEW = 304;
    public static final int ACTION_GET_ACTIVITY_PARTICIPATION_REWARD = 305;
    public static final int ACTION_GET_STRONGEST_ALLIANCE_ACTIVITY_ALLIANCE_RANK_REWARD = 306;
    public static final int ACTION_GET_STRONGEST_ALLIANCE_ACTIVITY_PLAYER_RANK_REWARD = 307;
    public static final int ACTION_GET_STRONGEST_ALLIANCE_ACTIVITY_MEMBER_LIST = 308;
    public static final int ACTION_SET_STRONGEST_ALLIANCE_ACTIVITY_ENEMY = 309;
    public static final int ACTION_PAY_FOR_CHANGE_STRONGEST_ALLIANCE_ACTIVITY_ENEMY = 310;
    public static final int ACTION_SHOW_PASSION_LEVELING_TOP_CHANNEL = 311;
    public static final int ACTION_SHOW_REMAINS_ADVENTURE_TOP_CHANNEL = 312;
    public static final int ACTION_GET_NEW_RANKING = 313;
    public static final int ACTION_SHOW_STRONGEST_ALLIANCE_TOP_CHANNEL = 314;
    public static final int ACTION_ONLINE_TIPS = 315;
    public static final int ACTION_ALLIANCE_REJECT_ALL = 316;
    public static final int ACTION_HERO_ARENA_GET_INFO = 317;
    public static final int ACTION_HERO_ARENA_GENERATE_OPPONENTS = 318;
    public static final int ACTION_HERO_ARENA_GET_RANKS = 319;
    public static final int ACTION_HERO_ARENA_CHECK_BATTLE = 320;
    public static final int ACTION_HERO_ARENA_START_BATTLE = 321;
    public static final int ACTION_HERO_ARENA_END_BATTLE = 322;
    public static final int ACTION_HERO_ARENA_CLEAR_COOLDOWN = 323;
    public static final int ACTION_HERO_ARENA_BUY_TIMES = 324;
    public static final int ACTION_HERO_ARENA_GET_PRIZE = 325;
    public static final int ACTION_HERO_ARENA_GET_SEASON_PRIZE = 326;
    public static final int ACTION_HERO_ARENA_SEASON_PRIZE_INFO = 327;
    public static final int ACTION_HERO_ARENA_DAILY_PRIZE_INFO = 328; 
    public static final int ACTION_GET_ACTIVITY_PRIZES_DONE = 330;
    public static final int ACTION_ALLIANCE_WAR_GET_INFO = 349;
    public static final int ACTION_ALLIANCE_WAR_GET_REWARD = 350;
    public static final int ACTION_ALLIANCE_WAR_GET_SCHEDULE = 351;
    public static final int ACTION_ALLIANCE_WAR_GET_DETAIL = 352;
    public static final int ACTION_ALLIANCE_WAR_QUIZ = 353;
    public static final int ACTION_ALLIANCE_WAR_GET_INIT_DATA = 354;
    public static final int ACTION_ALLIANCE_WAR_GET_CURRENT_DATA = 355;
    public static final int ACTION_ALLIANCE_WAR_SET_FIRE = 356;
    public static final int ACTION_ALLIANCE_WAR_GET_FIRE = 357;
    public static final int ACTION_ALLIANCE_WAR_BUY_FIRE = 358;
    public static final int ACTION_ALLIANCE_WAR_CHAT = 359;
    public static final int ACTION_ALLIANCE_WAR_CHECK_BATTLE = 360;
    public static final int ACTION_ALLIANCE_WAR_SET_EMBATTLE = 361;
    public static final int ACTION_ALLIANCE_WAR_GET_REPLAY = 362;
    public static final int ACTION_ALLIANCE_WAR_PAY_FOR_REVIVE = 363;
    public static final int ACTION_ALLIANCE_WAR_CHECK = 364;
    public static final int ACTION_ALLIANCE_WAR_MOVE = 365;
    
    public static final int ACTION_BROADCAST_CC_WORLD_MONSTER_ESCAPE = 366;
    public static final int ACTION_BROADCAST_CC_CATCH_WORLD_MONSTER= 367;
    public static final int ACTION_BROADCAST_CC_CATCH_WORLD_MONSTER2= 368;
    
    /**************************MagicianAdventure****************************/
    public static final int ACTION_START_AIRPLANE_GAME = 339;
    public static final int ACTION_END_AIRPLANE_GAME = 340;
    public static final int ACTION_USE_DIAMOND_TO_RAISEUP = 341;
    public static final int ACTION_USE_DIAMOND_TO_BUYBOMS = 342;
    public static final int ACTION_USE_DIAMOND_TO_BUYTIMES = 343;
    public static final int ACTION_REFRESH_AIRPLANE_GAME = 344;
    public static final int ACTION_GET_MAGIC_POSITION = 345;
    public static final int ACTION_GET_MAGIC_WEEK_RANK =346;
    

    /**************************600-699****************************/
    public static final int ACTION_USE_CITY_PROFILING = 600;
    public static final int ACTION_GET_MY_VALID_CITY_PROFILING = 601;
    public static final int ACTION_BUY_AND_USE_CITY_PROFILING = 602;
    public static final int ACTION_SET_INVITE_FRIENDS_ACTIVITY_MY_INVITE_CODE = 603;
    public static final int ACTION_GET_INVITE_FRIENDS_ACTIVITY_INVITE_REWARD = 605;
    public static final int ACTION_USE_ITEM_BUFF = 604;
    public static final int ACTION_THANKSGIVINGDAY_ACTIVITY_HOLD_A_FEAST = 606;
    public static final int ACTION_USE_ITEM_COMBO_PACK = 607;
    public static final int ACTION_CHECK_ATTACK_HEADLESS_HORSEMAN = 608;
    public static final int ACTION_END_ATTACK_HEADLESS_HORSEMAN = 609;
    public static final int ACTION_SHOW_HEADLESS_HORSEMAN = 610;
    public static final int ACTION_BROADCAST_HEADLESS_HORSEMAN_DEAD = 611;
    public static final int ACTION_BROADCAST_HEADLESS_HORSEMAN_RELIVE = 612;
    public static final int ACTION_BROADCAST_HEADLESS_FIRST_PRIZE = 613;
    public static final int ACTION_CHECK_ITEM_COMBOPACK = 614;
    public static final int ACTION_CHAT_SHIELD_PLAYER = 615;//屏蔽玩家信息
    public static final int ACTION_CHAT_REPORT_PLAYER = 616;//举报玩家
    public static final int ACTION_LIMIT_SHOP_GET_ALL = 617;
    public static final int ACTION_LIMIT_SHOP_BUY_ITEM = 618;
    public static final int ACTION_LIMIT_SHOP_SEND_BROADCAST = 619;
    public static final int ACTION_SYN_ACTIVITY_MAP = 620;
    public static final int ACTION_GET_ACTIVITY_SMALL_MAP = 621;
    public static final int ACTION_REGISTER_TOPONEKING_ACTIVITY = 623;
    public static final int ACTION_ATTACK_CHECK_SEC_WORLD_USER = 624;
    public static final int ACTION_ATTACK_END_SEC_WROLD = 625;
    public static final int ACTION_ATTACK_START_SEC_WORLD = 626;
    public static final int ACTION_QUIT_ATTACK_SEC_WORLD = 627;
    public static final int ACTION_ATTACK_COST_DIAMOND_FOR_REST_TIME = 628;
    public static final int ACTION_ACTIVITY_MAP_GET_ITEM_BUY = 629;
    public static final int ACTION_ACTIVITY_MAP_GET_SCORE_PRIZE = 630;
    public static final int ACTION_ACTIVITY_MAP_GET_RANK_PRIZE = 631;
    public static final int ACTION_ACTIVITY_MAP_COLLOECT_SCORE = 632;
    public static final int ACTION_ACTIVITY_MAP_GET_MY_CITYS = 633;
    public static final int ACTION_ACTIVITY_MAP_DROP_CITY = 634;
    public static final int ACTION_ACTIVITY_MAP_GET_REWARD_FOR_SCORE = 635;
    public static final int ACTION_ACTIVITY_MAP_GET_REWARD_FOR_RANK = 636;
    public static final int ACTION_ATTACK_COST_DIAMOND_FOR_KILL_TIME = 637;
    public static final int ACTION_TOP_ONE_KING_ACTIVITY_GET_RANK_PRIZES_BROCAST = 638;
    public static final int ACTION_ACTIVITY_MAP_NO_DROP_CITY = 639;
    public static final int ACTION_ACTIVITY_MAP_GET_MAIL = 640;
    public static final int ACTION_ACTIVITY_MAP_REPLAY_MAIL = 641;
    public static final int ACTION_TOP_ONE_KING_EVER_LOGIN_BROADCAST = 642;
    public static final int ACTION_ACTIVITY_MAP_ONE_KEY_COLLECT = 643;
    public static final int ACTION_ACTIVITY_MAP_BUY_OCCUPY_CITY = 644;
    public static final int ACTION_ACTIVITY_OCCUPIED_GET_PRIZE = 670;
    public static final int ACTION_ALLIANCE_TECH_GET_TECHS = 671;
    public static final int ACTION_ALLIANCE_TECH_GET_TECH_INFO = 672;
    public static final int ACTION_UPGRADE_ALLIANCE_TECH = 673;
    public static final int ACTION_COST_DIAMOND_BY_TIME_FOR_ALLIANCE_TECH = 674;
    public static final int ACTION_GET_ALLIANCE_EXCHANGE_ITEM_LIST = 675;
    public static final int ACTION_EXCHANE_ALLIANCE_ITEM = 676;
    public static final int ACTION_COST_REFRESH_ALL_ITEM = 677;
    public static final int ACTION_COST_REFRESH_ITEM_BY_CONTRIBUTE = 678;
    public static final int ACTION_UPDATE_ALLIANCE_SIGN = 691;
    public static final int ACTION_SYNC_WILDINVADER_SMALLMAP = 692;
    public static final int ACTION_START_WILDINVADER_BATTLE = 693;
    public static final int ACTION_QUIT_WILDINVADER_BATTLE = 694;
    public static final int ACTION_END_WILDINVADER_BATTLE = 695;
    public static final int ACTION_CHECK_WILDINVADER_BATTLE = 696;
    public static final int ACTION_CHECK_ACTION_POINT_WILDINVADER = 697;
    public static final int ACTION_GET_WILDINVADER_PRIZES = 698;
    public static final int ACTION_SYNC_WILDINVADER_DATE = 699;

    /*********************400--499***************************/
    public static final int ACTION_FINISH_CONDITION_GUIDE = 401;
    public static final int ACTION_GET_ITEM_INFO_BY_ITEMTYPE = 402;
    public static final int ACTION_GET_ACTION_POINT_INFO = 403;
    public static final int ACTION_USE_ITEM_SUPPLY_PACKAGE = 404;
    public static final int ACTION_BUY_ACTION_POINT = 405;
    public static final int ACTION_GOTO_FORM_PERSONAL_RANK = 406;
    public static final int ACTION_Check_Config_Version = 407;
    
    public static final int ACTION_USE_BigDanAlso = 408;
    public static final int ACTION_BUY_AND_USE_BigDanAlso = 409;
    public static final int ACTION_LOAD_SERVER_CONFIG = 410;

    //日历所有奖励的配置（只用一个action，不用多次请求）
    public static final int ACTION_CheckIn_Reward_Config = 428;
    public static final int ACTION_Calendar_Buff_Config = 429;
    
    public static final int ACTION_LaboratoryBuilding_Config = 431;
    
    public static final int ACTION_ItemBigDanAlso_Config = 430;
    public static final int ACTION_SCREEN_SMALLMAP_CONFIG=432;
    public static final int ACTION_MAGIC_CONFIG=433;
    public static final int ACTION_GAMBIT_TYPE_CONFIG=434;
    public static final int ACTION_GAMBIT_HERO_CONFIG=435;
    /************************************************/
    		
    		
    /*********************701--799***************************/

    public static final int ACTION_GET_LUCKY_DRAW_INFO = 701;
    public static final int ACTION_LUCKY_DRAW = 702;
    public static final int ACTION_LUCKY_DRAW_BROADCAST = 703;
    
    public static final int ACTION_GET_FACEBOOK_SHARE_INFO = 704;
    public static final int ACTION_SHARE_FACEBOOK = 705;
    public static final int ACTION_BIND_FACEBOOK = 706;

    public static final int ACTION_GET_DAILY_FACEBOOK_AWARD = 707;
    public static final int ACTION_GET_BIND_FACEBOOK_AWARD = 708;
    
    public static final int ACTION_CHECK_SHARE = 709;
    public static final int ACTION_PICK_LUCKY_GIFT = 710;
    
    
    public static final int ACTION_TOPUP_USER_INFO = 711;
    public static final int ACTION_TOPUP_GET_AWARD=712;

    public static final int ACTION_GET_APP_BILLING_LIST = 713;
    
    public static final int ACTION_CELEBRITY_COMPETITION_JOIN_GROUP = 721;
    public static final int ACTION_CELEBRITY_COMPETITION_CHANGE_GROUP = 722;
    public static final int ACTION_CELEBRITY_COMPETITION_BUY_FLOWERS = 723;
    public static final int ACTION_CELEBRITY_COMPETITION_RANKING = 724;
    public static final int ACTION_CELEBRITY_COMPETITION_GROUP_RANKING = 725;
    public static final int ACTION_CELEBRITY_COMPETITION_BROADCAST = 726;
    
    public static final int ACTION_SMS_BILLING_RESULT_OVER_LIMIT = 727;
    
    public static final int ACTION_STARSTONEMINE_CONFIG = 728;
    public static final int ACTION_SOLDIER_UPGRADE_CONFIG = 729;
    public static final int ACTION_GOVERNMENT_CONFIG = 730;
    
    public static final int ACTION_ENTER_TOPUP_PAGE = 731;
    public static final int ACTION_TRY_TO_TOPUP = 732;
    public static final int ACTION_GET_USER_COUNTRY = 733;
    public static final int ACTION_TOPUP_SUCCESS = 734;
    public static final int ACTION_TOPUP_FAILED = 735;

    public static final int ACTION_AS_CAMP_COMPETITION_GROUP_RANKING = 744;
	public static final int ACTION_AS_CAMP_COMPETITION_PRIZE_INFO = 745;
	public static final int ACTION_AS_CAMP_COMPETITION_RANKING = 746;
	public static final int ACTION_AS_CAMOP_COMPETITION_SPECIAL_INFO = 747;
	public static final int ACTION_AS_CAMP_COMPETITION_PLAYER = 748;

	public static final int ACTION_AS_CAMP_COMPETITION_ADDPIONTBYBLESSING = 749;
	public static final int ACTION_AS_CAMP_COMPETITION_GETPRIZESINFO = 750;
	public static final int ACTION_AS_CAMP_COMPETITION_GETPRIZES = 751;
	public static final int ACTION_AS_CAMP_COMPETITION_GET_DAILY_PRIZES = 752;
	public static final int ACTION_AS_CAMP_COMPETITION_GET_RANKING_PRIZES = 753;
	
	public static final int ACTION_AS_CAMP_COMPETITION_JOIN_TEAM = 754;
    
    
    public static final int ACTION_GET_HEAD_CONFIG = 770;
    
    /*********************701--799***************************/

    /********************506--599***************************/
    public static final int ACTION_UNDRESS_HERO_EQUIP_NEW = 506;
    public static final int ACTION_DRESS_HERO_EQUIP_NEW = 505;
    public static final int ACTION_IS_CHANGE_SOLDIER_JOB = 507;//判断是否转职
    public static final int ACTION_CHANGE_SOLDIER_JOB = 508;//进行转职
    public static final int ACTION_COLLECT_STARSTONE = 509;//收集星玉
    public static final int ACTION_STRENGTHEN_SOLDIER = 510;
    public static final int ACTION_USE_DIAMOND_TO_STRENGTHEN_SOLDIER =511;
    public static final int ACTION_CANCEL_STRENGTHEN_SOLDIER =512;
    public static final int ACTION_USE_DIAMOND_TO_CHANGE_SOLDIER_JOB = 513;
    public static final int ACTION_CANCEL_CHANGE_SOLDIER_JOB = 514;
    public static final int ACTION_FORUM_GET_POST_NEW = 515;
    
    public static final int ACTION_CALENDAR_GET_INFO = 516;
    public static final int ACTION_CALENDAR_GET_CONFIG = 520;
    public static final int ACTION_CALENDAR_GET_BUFFCONFIG = 518;
    public static final int ACTION_CALENDAR_CHECKIN = 519;
    public static final int ACTION_CALENDAR_GETCHECKINREWARD = 517;
    
    public static final int ACTION_BUY_RESOURCE_BY_DIAMOND = 521;
    
    public static final int ACTION_REFINED_EQUIPMENT_GET_CONFIG = 522;
    public static final int ACTION_REFINED_EQUIPMENT = 523;//refined
    
    public static final int ACTION_RESOLVE_EQUIPMENT = 524;//resolve
    
    public static final int ACTION_WARRIORSOUL_FUSE_NEW = 525;//change
    public static final int ACTION_RESOLVE_SPECIAL_EQUIP_GET_CONFIG = 526;
    
    public static final int ACTION_UPGRADE_HERO_MAGIC = 527;
    public static final int ACTION_LIMIT_SHOP_BUY_ITEM_NEW = 529;//limitShop buyItem new
    public static final int ACTION_BUY_MAGIC_MANA = 528;
    
    
    public static final int ACTION_GET_LUCKYDRAW_NEW_CONFIG = 530;//luckyDrawNewConfig
    public static final int ACTION_GET_CASINO_NEW_USER_DATA = 531;
    public static final int ACTION_GET_CASINO_NEW_REWARDS = 532;
    public static final int ACTION_GET_TOP_DIAMOND_NOTIC = 533;
    
    public static final int ACTION_CHECK_HERO_GLORY_BATTLE = 534;
    
    public static final int ACTION_GET_HERO_GLORY_CONFIG = 535;
    
    
    public static final int ACTION_HERO_ARENA_NEW_GET_INTO = 550;
    public static final int ACTION_HERO_ARENA_NEW_CHANGE_OPPONENT = 551;
    public static final int ACTION_HERO_ARENA_NEW_GET_CURRENT_ARENATYPE_RANKS = 552;
    public static final int ACTION_HERO_ARENA_NEW_GET_ALL_RANKS = 553;
    public static final int ACTION_HERO_ARENA_NEW_CHECK_BATTLE = 554;
    public static final int ACTION_HERO_ARENA_NEW_START_BATTLE = 555;
    public static final int ACTION_HERO_ARENA_NEW_END_BATTLE = 556;
    public static final int ACTION_HERO_ARENA_NEW_BUY_CHALLENGE_TIMES = 557;
    public static final int ACTION_HERO_ARENA_NEW_GET_DAILY_PRIZE = 558;
    public static final int ACTION_HERO_ARENA_NEW_GET_HISTORY = 559;
    public static final int ACTION_HERO_ARENA_NEW_USE_CHALLENGE_PACK = 560;
    public static final int ACTION_HERO_ARENA_NEW_PLAY_BACK = 561;
    public static final int ACTION_HERO_ARENA_NEW_CHANGE_ARENATYPE = 562;
    public static final int ACTION_HERO_ARENA_NEW_ENTER_INTO_OTHER_ARENATYPE = 563;
    public static final int ACTION_HERO_ARENA_NEW_QUIT_BATTLE = 564;
    public static final int ACTION_HERO_ARENA_NEW_GET_HELP = 565;
    public static final int ACTION_STRENGTHEN_EQUIPMENT = 566;
    public static final int ACTION_RESOLVE_EQUIPMENT_NEW = 567;
    
    public static final int ACTION_BUILDING_ALLIANCE_FORT = 568;
    public static final int ACTION_UPGRADE_ALLIANCE_FORT = 569;
    public static final int ACTION_SEND_TROOPS_IN_ALLIANCE_FORT = 570;
    public static final int ACTION_REMOVE_TROOPS_BY_ALLIANCE_FORT = 571;
    public static final int ACTION_GET_ALLIANCE_FORT_INFO = 572;
    public static final int ACTION_OUT_FIRE_OF_ALLIANCE_FORT = 573;
    public static final int ACTION_CHECK_BATTLE_ALLIANCE_FORT = 574;
    public static final int ACTION_START_BATTLE_ALLIANCE_FORT = 575;
    public static final int ACTION_END_BATTLE_ALLIANCE_FORT = 576;
    public static final int ACTION_RESET_EMBATTLE_ALLIANCE_FORT = 577;
    public static final int ACTION_REMOVA_ALLIANCE_FORT_FIGHT_STATUS = 578;
    public static final int ACTION_GET_FORT_INFO_IN_WORLD_MAP = 579;
    public static final int ACTION_GET_ALLIANCE_FORT_NORMAL_PRIZE = 580;
    public static final int ACTION_GET_ALLIANCE_FORT_BATTLE_HISTORY = 581;
    public static final int ACTION_ALLIANCE_FORT_BATTLE_PLAY = 582;
    public static final int ACTION_ALLIANCE_FORT_CHANGE_TITLE = 583;
    public static final int ACTION_ALLIANCE_FORT_GET_TEAM_BATTLE = 584;
    public static final int ACTION_ALLIANCE_FORT_QUIT_BATTLE = 585;
    public static final int ACTION_ALLIANCE_FORT_GET_ALL_COORIDS = 586;
    public static final int ACTION_ALLIANCE_FORT_BATTLE_REPLAY = 587;
    public static final int ACTION_ALLIANCE_FORT_UPGRADE_ANIMATION = 588;
    
    public static final int ACTION_SYNC_BLACK_SIMITH_NEED_ITEMS = 589;
    
    public static final int  ACTION_GET_STAR_PRIZE_STATUS_LIST = 590;
    public static final int  ACTION_GET_STAR_PRIZE = 591;
    
    public static final int  ACTION_GUIDE_OPEN_ANIMATION_FINISH = 592;
    public static final int  ACTION_USE_ITEM_BY_ITEM_TYPE = 593;
    
    public static final int  ACTION_CREATE_ALLIANCE_NEW = 594;
    public static final int  ACTION_VIP_UPGRADE_LEVEL = 595;
    public static final int  ACTION_TOP_UP_DIAMONDS_NEED_REFRESH = 596;

    public static final int  ACTION_CAN_GET_CAPTURE_ALLIANCE_FORT_REWARD = 597;
    public static final int  ACTION_GET_CAPTURE_ALLIANCE_FORT_REWARD = 598;
    public static final int  ACTION_ALLIANCE_FORT_FIRE_STATUS_CHANGE = 599;
    

    /*********************1500--1599***************************/
    public static final int ACTION_COLLECT_RESOURCE_COMPETITION_RESOURCE = 1501;
    public static final int ACTION_GET_RESOURCE_BUILDING_POS = 1502;
    public static final int ACTION_GET_RESOURCE_BUILDING_RANK = 1503;
    public static final int ACTION_GET_ALLIANCE_MEMBER_RANK = 1504;
    
    public static final int ACTION_HERO_ARENA_GET_CHALLENGE_PRIZE = 1505;
    
    public static final int ACTION_HERO_ARENA_GET_DAILY_RANK_INFO = 1506;
    public static final int ACTION_HERO_ARENA_GET_MORE_DAILY_RANK_LIST = 1507;
    public static final int ACTION_HERO_ARENA_GET_ALL_DAILY_RANK_PRIZE = 1508;
    
    public static final int ACTION_TOP_ONE_KING_BUY_ITEM = 1509;
    
    public static final int ACTION_BUY_STONE_AND_STRENGTHEN_EQUIPMENT = 1510;
    /*****************************************************/
    
    
    /*********************801--899***************************/
    //副本冲关活动2
    public static final int ACTION_SHOW_REMAINS_SWEEP_TOP_CHANNEL = 801;
    
    
    //获取运营配的特殊士兵配置信息
    public static final int ACTION_GET_OPERATE_CONFIG = 802;
    
    //alliance dungeons action
    public static final int ACTION_GET_ALLIANCE_DUNGEONS_ACTIVITY_CITY_INFO = 803;
    public static final int ACTION_GET_ALLIANCE_DUNGEONS_PASS_PRIZE = 804;
    public static final int ACTION_GET_ALLIANCE_DUNGEONS_CASTLE_POSITION = 805;
    
    public static final int ACTION_BUY_AND_USE_NORMAL_CITY_MOVER = 806;//随机迁城令的直买直用
    public static final int ACTION_BUY_AND_USE_SUPER_CITY_MOVER = 807;//超级迁城令的直买直用
    
    public static final int ACTION_GET_SHOW_SUPER_CITY_MOVER_DATA = 808;
    
    public static final int ACTION_ADD_AND_USE_SUPER_CITY_MOVER = 809;
    
    public static final int ACTION_SHOP_EQUIP_LIST_CONFIG = 810;
    
    public static final int ACTION_BUY_COMBO_EQUIP = 811;
    
    public static final int ACTION_DUNGEONS2_CONFIG = 812;  
    public static final int ACTION_GET_DUNGEONS2_DATA = 813;
    
    public static final int ACTION_CHECK_DUNGEONS2 = 814;
    public static final int ACTION_START_DUNGEONS2 = 815;
    public static final int ACTION_END_DUNGEONS2 = 816;
    
    public static final int ACTION_DUNGEONS2_SET_SIGN = 817;
    
    public static final int EVENT_USE_SKY_FIRE_CARD2 = 818;
    
    public static final int ACTION_GET_BUILDERS = 819;
    public static final int EVENT_USE_CONTRACT = 820;   
    public static final int EVENT_ADD_BUILDER_TIME_BUY_DIAMOND = 821;
    
    public static final int ACTION_START_ATTACK_WORLD_MONSTER = 822;
    
    public static final int ACTION_GET_DAILY_QUEST_INFO = 823;
    public static final int ACTION_GET_DAILY_QUEST_PRIZE = 824;
    
    public static final int ACTION_USE_SOLDIER_JOB_TRANSFER = 825;
    public static final int ACTION_INVITE_CODE = 826;
    
    public static final int ACTION_CC_GET_INFO_DATA = 827;
    public static final int ACTION_CC_GET_CELEBRIT_AND_TEAM_DATA = 828;
    public static final int ACTION_CC_GET_TEAM_RANK = 829;
    public static final int ACTION_CC_GET_HISTORY = 830;
    public static final int ACTION_CC_JOIN_IN_CELEBRITY_TEAM = 831;
    public static final int ACTION_CC_GET_TOP_TEAM_PRIZE = 832;
    public static final int ACTION_CC_GET_MONSTER_INFO_FOR_WORLD = 833;
    public static final int ACTION_CC_CHECK_BATTLE = 834;
    public static final int ACTION_CC_END_BATTLE = 835;
    public static final int ACTION_CC_START_BATTLE = 836;
    
    public static final int ACTION_BUY_AND_USE_SKY_FIRE_CARD2 = 837;
    
    public static final int ACTION_OPEN_ONE_PACK_GRID = 838;
    public static final int ACTION_OPEN_BATCH_PACK_GRID = 839;
    
    public static final int ACTION_BUY_BACKPACK_SPACE_AFTER_GET_REWARD_NEW = 840;
    
    public static final int ACTION_ADD_BACKPACK2 = 841;
    public static final int ACTION_GET_WILDINVADER_REMAIN_COUNT = 842;

    public static final int ACTION_GET_EMPLOY_HERO_INFO = 843;
    public static final int ACTION_ACTIVITY_SEND_STAR_STONE_WAR_NOTICE = 850;
    
    public static final int ACTION_EMPLOY_HERO_DRESS_EQUIP = 844;
    public static final int ACTION_EMPLOY_HERO_UNDRESS_EQUIP = 845;
    public static final int ACTION_EMPLOY_HERO = 846;
    public static final int ACTION_CLICK_EMPLOY_HERO = 847;
    public static final int ACTION_RELIVE_HERO = 848;
    public static final int ACTION_GET_BLACK_SIMITH_NEED_DATA = 849;
    
    public static final int ACTION_UPGRADE_EMPLOY_SOLDIER = 851;
    public static final int ACTION_FINISH_UPGRADING_EMPLOY_SOLDIER = 852;
    public static final int ACTION_GET_ALL_EMPLOY_HERO_DATAS = 853;
    public static final int ACTION_GET_CAMP_INFO = 854;
    public static final int ACTION_GET_CAFE_INFO = 855;
    public static final int ACTION_GET_EMPLOY_SOLDIER_INFO = 856;

    /*********************801--899***************************/
    
    /********************1200--1299***************************/
    public static final int ACTION_NEW_TOPUP_USER_INFO = 1200;
    public static final int ACTION_NEW_TOPUP_GET_AWARD=1201;
    public static final int ACTION_NEW_TOPUP_HAS_AWARD=1202;
    public static final int ACTION_BATTLE_RANDOM_REWARD=1203;
    public static final int ACTION_BATTLE_RANDOM_REWARD_CONFIG=1204;
    public static final int ACTION_BATTLE_ENEMY_RANDOM_REWARD=1205;
    public static final int ACTION_BATTLE_TROOP_EMBATTLE=1206;
    public static final int ACTION_BATTLE_CHECK_REINFORCE_TIME=1207;
    public static final int ACTION_CHECK_EXPEDITION_POINT=1208;
    public static final int ACTION_USE_EXPEDITION_BEFORE_BATTLE=1209;
    public static final int ACTION_GET_BATTLE_RANDOM_REWARD_NEW = 1213;
    public static final int ACTION_TOP_UP_LUCKY_DRAW = 1214;
    public static final int ACTION_UPDATE_FORUM_NEW_ADMIN_READ = 1215;
    public static final int ACTION_TOP_UP_CARNIVAL_CHANNEL = 1216;
    public static final int ACTION_MODIFY_USER_HEAD_FRAME = 1217;
    public static final int ACTION_SET_USER_HEAD_FRAME = 1218;
    public static final int ACTION_BUY_AND_SET_USER_HEAD_FRAME = 1219;
    public static final int ACTION_GET_MY_ALL_HEAD_FRAMES = 1220;
    public static final int ACTION_GET_DUNGEONS_LAYER_INFO = 1221;
    public static final int ACTION_GET_AUTO_QUEST_NEED_DIAMOND = 1222;
    public static final int ACTION_AUTO_QUEST_DUNGEONS = 1223;
    public static final int ACTION_GET_DAILY_QUEST_INFO_NEW = 1224;
    public static final int ACTION_GET_DAILY_QUEST_PRIZE_NEW = 1225;
    public static final int ACTION_GET_ACCRUAL_REWARD = 1226;
    public static final int ACTION_GET_CUMULATIVE_RECHARGE_REWARD = 1227;
    public static final int ACTION_GET_RECHARGE_REWARDS_INFO = 1228;
    public static final int ACTION_GET_RECHARGE_REWARDS_PRIZES = 1229;
    public static final int ACTION_BUY_RECHARGE_REWARDS = 1230;
    /********************1200--1299***************************/
    
    
    /***********************************兼容新旧客户端接******************/
    
    public static final int ACTION_GET_USER_ALL_INFO_New = 1007; // --- 对应旧接口为�ACTION_GET_USER_ALL_INFO = 7
    
    /***********************************兼容新旧客户端接�*/
    public static final int ACTION_Login_Fail = 1008;//登录失败
    
    /*********************1020-1029 Alliance_Append*********************/
    public static final int ACTION_FINISH_APPLICATION = 1020;
	public static final int ACITON_GET_FINISH_COST = 1021;
	public static final int ACTION_CHANGE_ALLIANCE_BADGE = 1022;
	public static final int ACTION_GET_ALLIANCE_ICON_LIST = 1023;
	public static final int ACTION_BUY_NEW_ALLIANCE_ICON = 1024;
    public static final int ACTION_RESEARCH_ALLIANCE_LIST = 1025;
    public static final int ACTION_ALLIANCE_APPLICATION_STATUS = 1026;
    public static final int ACTION_GET_MY_ALLIANCE_ICONS = 1029;
    
    /*********************1001--1199***************************/
    //建筑升级活动
    public static final int ACTION_BUILDING_UP_SHOW_MSG = 1032;
    public static final int ACTION_BUILDING_UP_GET_HAS_PARTICIPATION_REWARD = 1033;
    public static final int ACTION_BUILDING_UP_TOP_CHANNEL = 1034;
    
    //骆驼硬币活动
    public static final int ACTION_GET_COIN_EXCHANGE_LIST = 1050;
    public static final int ACTION_EXCHANGE_COIN_ITEM = 1051;
    public static final int ACITON_PICK_CAMEL_COIN = 1052;
    
    //道具资源�
    public static final int ACTION_GET_ITEM_RESOURCE_CONFIG = 1060;
    public static final int ACTION_USER_ITEM_RESOURCE = 1061;
    
    //1071-1079
    public static final int ACTION_USE_ITEM_BUILDER = 1071;
    
    //获取推荐好友
    public static final int ACTION_GET_COMMEND_FRIENDS_INFO =1080;
    
    //城池占领活动
    public static final int ACTION_SHOW_OCCUPATION_CITY_TOP_CHANNEL = 1099;

 
    //打飞机活�
    public static final int ACTION_SHOW_AIR_PLANE_TOP_CHANNEL = 1189;

    /********************3000--3599***************************/
    public static final int EVENT_NEW_ALLIANCE_SKILL_LIST = 3002;
    public static final int EVENT_NEW_ALLIANCE_ACTIVATE_SKILL = 3003;

    public static final int ACTION_CC_BUY_SCORE = 3004;
    /********************1200--1299***************************/
    
    /******************************************************************************/
    public static final int ACTION_SYNC_USER_INFO = 1100;
    public static final int ACTION_GET_REWARD_FOR_TREE = 1101;
    public static final int ACTION_SYNC_BASIC_INFO = 1130;
    
    public static final int ACTION_BATTLE_VALIDATE = 1110;
    public static final int ACTION_BATTLE_VALIDATE_RESULT = 1120;
    
    /**********************1300-1399************************/
    //任务指引系统
    public static final int ACTION_SYNC_QUEST_DATA = 1300;
    public static final int ACTION_QUEST_GUIDE_GET_REWARD = 1301;
    
    public static final int ACTION_CHECK_ISLAND_EMBATTLE = 1310;
    public static final int ACTION_GET_ISLAND_SCENE_INFO = 1311;
    public static final int ACTION_ISLAND_NOTIFICATION_START = 1313;//进码头的玩家齐了开始战�
    public static final int ACTION_ISLAND_FORMATION_SAVE = 1314;//海盗活动队形保存
    public static final int ACTION_IS_LAND_SET_EMBATTLE = 1320;
    public static final int ACTION_ISLAND_ATTACK_MONSTER_END = 1315;//海盗战斗大小怪结�战斗结算调�
    public static final int ACTION_CHECK_ISLAND_BATTLE_REPLAY = 1321;//攻击boss重播    
    public static final int ACTION_ISLAND_LANDING_DOCK_NOTICATION = 1316;//登录码头通知
    public static final int ACTION_REFRESH_FAIL_CD = 1322;//清空失败的等待CD
    public static final int ACTION_QUIT_ATTACK_SCENE = 1323;//如果玩家中途退�
    public static final int ACTION_ISLAND_LANDING_CLEAN_CD = 1317;//砖石清除cd   
    public static final int ACTION_ISLAND_BUY_BUFF = 1318;//购买buff       
    public static final int ACTION_ISLAND_ATTACK_MONSTER_START = 1324;
    public static final int ACTION_GET_ISLAND_SCORE_BONUS_CONFIG = 1330;
    public static final int ACTION_GET_ISLAND_LAST_ATTACK_REWARD = 1331;
    public static final int ACTION_EVEN_WIN_BROAD_CAST = 1332;
    
    public static final int ACTION_USE_REINFORCE_ITEM = 1340;
    
    /**********************1300-1399************************/
    
    
    //Newbie Guide Dialog
    public static final int ACTION_GUIDE_DIALOG = 1400;
    public static final int ACTION_CONFIG_LOADING_PROGRESS = 1410;
    public static final int ACTION_CONFIG_DOWNLOAD_START = 1420;
    public static final int ACTION_CONFIG_DOWNLOAD_FINISHED = 1430;

    
    // cross
    public static final int ACTION_CROSS_BASE = 100000;
    public static final int ACTION_CROSS_GET_SERVER_LIST = 100001;
    public static final int ACTION_CROSS_SYNC_WORLDMAP = 100002;
    public static final int ACTION_CROSS_GET_SMALL_MAP = 100003;
    
    //消息推�
    public static final int ACTION_NOTIFY_RESET_TOKEN= 112001;
}