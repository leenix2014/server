package com.mozat.morange.notification;

import java.util.HashMap;
import java.util.Map;

public enum EnumNotificationType {
	beInvaded(0, "msg.beInvaded.new"),
	soldierProduced(1, "msg.soldierFinished.new"),
	soldierUpgraded(2, "msg.soldierFinished.new"),
	buildingUpgraded(3, "msg.buildingUpgraded"),
	goldMineFull(4, "msg.goldMineFull"),
	starStoneMineFull(5, "msg.starStoneMineFull"),
	monsterAppear(6, "msg.monsterAppear"),
	reminderMineFull(7, "msg.reminderMineFull"),
	reminderConquer(8, "msg.reminderConquer"),
	reminderComeback(9, "msg.reminderComeback"),
	reminderDefenseSucceed(10, "msg.reminderDefenseSucceed"),
	reminderNewNeighbours(11, "msg.reminderNewNeighbours"),
	reminderComeback2(12, "msg.reminderComeback2"),
	reminderAllianceMember(13, "msg.reminderAllianceMember"),
	reminderNewNeighbours2(14, "msg.reminderNewNeighbours"),
	reminderDefenseSucceed2(15, "msg.reminderDefenseSucceed"),
	reminderStarStoneMineFull(16, "msg.starStoneMineFull");

	private int index;
	private String key;
	private static Map<Integer, EnumNotificationType> map = new HashMap<Integer, EnumNotificationType>();

	static {
		for (EnumNotificationType legEnum : EnumNotificationType.values()) {
			map.put(legEnum.getIndex(), legEnum);
		}
	}

	public int getIndex() {
		return index;
	}

	public String getKey() {
		return key;
	}

	private EnumNotificationType(int index, String key) {
		this.index = index;
		this.key = key;
	}

	public static EnumNotificationType getEnumByIndex(int index) {
		return map.get(index);
	}
}