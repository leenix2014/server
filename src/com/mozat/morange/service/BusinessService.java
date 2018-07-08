package com.mozat.morange.service;

import game.baccarat.model.BaccaratRoomService;
import game.coinroom.model.CoinRoomService;
import game.common.CommonConfig;
import game.live.model.LiveGiftMgr;
import game.live.model.LiveRoomService;
import game.live.robot.LiveNameMgr;
import game.live.robot.LiveWordMgr;
import game.loginReward.LoginRewardService;
import game.lottery.PrizeService;
import game.roulette.model.RouletteRoomService;
import game.task.TaskService;

public class BusinessService{
	public static void init(){
		//公共业务
		CommonConfig.init();
		
		//功能业务
		LoginRewardService.init();
		TaskService.init();
		PrizeService.init();
		LiveGiftMgr.init();
		LiveWordMgr.init();
		LiveNameMgr.init();
		CoinRoomService.init();
		RouletteRoomService.init();
		BaccaratRoomService.init();
		LiveRoomService.init();
	}
}
