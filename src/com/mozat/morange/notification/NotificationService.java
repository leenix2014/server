package com.mozat.morange.notification;

import org.json.JSONArray;
import org.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class NotificationService {

    private static final Logger logger = LoggerFactory.getLogger(NotificationService.class);
    private static final long notification_timeout_limit = (long) 24 * 60 * 60 * 1000;
    private static final long notification_timeout_limit2 = (long) 12 * 60 * 60 * 1000;
    private static final long notification_timeout_limit3 = (long) 168 * 60 * 60 * 1000;
    private static final long TWO_HOURS_LIMIT = (long) 1000 * 60 * 60 * 2;

    public static boolean addNotification(int monetId, EnumNotificationType type) {
    	return addNotification(monetId, type, null);
    }

    public static boolean addNotification(int monetId, EnumNotificationType type, JSONArray parameter) {
//    	long oT = Notification.getLastSendTime(monetId);
//    	long cT = System.currentTimeMillis();
//    	int lServerId = NotificationDataEntity.getLastLoginServerId(monetId);
//    	if(TWO_HOURS_LIMIT > cT - oT || (-1 != lServerId && Global.getServerId() != lServerId) ){
//    		return false;
//    	}
//    	
//        Player player = Player.load(monetId);
//        if (player == null) {
//            TraceLog.info(monetId, "[NotificationService]addNotification", "player is null, type=" + type);
//            return false;
//        }
//        Date lastLogoutTime = player.getLastLogoutTime();
//        if (lastLogoutTime == null) {
//            TraceLog.info(monetId, "[NotificationService]addNotification", "lastLogoutTime is null, type=" + type);
//            return false;
//        }
//        NotificationConfig notificationConfig = NotificationConfig.loadCache().get(type.getIndex());
//        if (notificationConfig == null) {
//            TraceLog.info(monetId, "[NotificationService]addNotification", "notificationConfig is null, type=" + type);
//            return false;
//        }
//        //这个判断条件可能会导致和某些notification的触发条件有冲突,例如产兵时间少于10分钟的话
//        if (System.currentTimeMillis() < (lastLogoutTime.getTime() + notificationConfig.getMinAfterLogout()) || System.currentTimeMillis() > (lastLogoutTime.getTime() + notificationConfig.getMaxAfterLogout())) {
//            //TraceLog.info(monetId, "[NotificationService]addNotification", "logout time is not in range, type=" + type);
//            return false;
//        }
//        int govLevel = GovernmentBuildingService.getGovernmentLevel(monetId);
//        if (govLevel < notificationConfig.getMinGovLv() || govLevel > notificationConfig.getMaxGovLv()) {
//            return false;
//        }
//        List<Notification> notificationList = Notification.load(monetId);
//        if (notificationList == null) {
//            TraceLog.info(monetId, "[NotificationService]addNotification", "notificationList is null, type=" + type);
//            return false;
//        }
//        //Search the previous notification with same type
//        Notification previousNotification = null;
//        for (Notification notification : notificationList) {
//            if (notification.getType() == type.getIndex()) {
//                previousNotification = notification;
//            }
//        }
//        if (previousNotification != null && previousNotification.getHasBeenSent()) {
//            Date sendTime = previousNotification.getSendTime();
//            if (sendTime != null && (sendTime.getTime() + notificationConfig.getInterval() > System.currentTimeMillis())) {
//                //TraceLog.info(monetId, "[NotificationService]addNotification", "within send time cool-down, type=" + type);
//                return false;
//            }
//        }
//
//        String msg = Global.getText(type.getKey(), player.getDeviceLang());
//        try {
//            if (parameter != null) {
//                for (int i = 0, len = parameter.length(); i < len; i++) {
//                    msg = msg.replace("{" + i + "}", parameter.getString(i));
//                }
//            }
//        } catch (JSONException jse) {
//            logger.error("[NotificationService]sendAPNs, monetId=" + monetId + ",type=" + type + ",e:", jse);
//        }
//
//        boolean hasBeenSent = false;
//        if (PlayerService.isIOS(monetId)) {
//            StatLog.writeWithDetail(monetId, "IOS_NOTIFICATION", "deviceToken=" + player.getDeviceToken()
//                    + ",type=" + type + ",deviceLang=" + player.getDeviceLang(), StatLog.GovLevelDetail);
//            try {
//                PushNotificationManager manager = new PushNotificationManager();
//                manager.initializeConnection(new AppleNotificationServerBasicImpl("aps_production.p12", "Xj$nbyndfwlb#C", true));
//                Device device = new BasicDevice();
//                device.setToken(player.getDeviceToken());
//                PushNotificationPayload payload = new PushNotificationPayload();
//                payload.addAlert(msg);
//                payload.addBadge(1);
//                payload.addSound("default");
//                manager.sendNotification(device, payload, true);
//                hasBeenSent = true;
//            } catch (Exception ex) {
//                logger.error("[NotificationService]sendAPNs, monetId=" + monetId + ",type=" + type + ",e:", ex);
//            }
//        } else {
//            try {
//            	 String deviceId = player.getDeviceId();
//                 if (deviceId == null || deviceId.isEmpty()) {
//                     return true;
//                 }
//                 JSONObject jGcmData = new JSONObject();
//                 JSONObject jData = new JSONObject();
//                 jData.put("message", msg);
//                 jGcmData.put("to", player.getDeviceId());
//                 jGcmData.put("data", jData);
//
//                 AndroidNotificationSender.push(jGcmData);
//                 hasBeenSent = true;
//                
//            } catch (JSONException ex) {
//                logger.error("[NotificationService]sendGcm, monetId=" + monetId + ",type=" + type + ",e:", ex);
//            }
//        }
//
//        if (previousNotification == null) {
//            Notification.create(monetId, type.getIndex(), type.getKey(), parameter == null ? null : parameter.toString(), hasBeenSent, hasBeenSent ? new Date() : null);
//        } else {
//            previousNotification.setNotification(type.getKey(), parameter == null ? null : parameter.toString(), hasBeenSent ? new Date() : null, hasBeenSent);
//        }
//
//        StatLog.write(monetId, "NotificationService_addNotification", "type=" + type + ",hasBeenSent=" + hasBeenSent);
        return true;
    }

//    public static void checkAndSendReminder() {
//        TraceLog.info("[NotificationService]checkAndSendReminder, started");
//        final Map<Integer, NotificationConfig> notificationConfigMap = NotificationConfig.loadCache();
//        Map<Integer, EnumNotificationType> sortedReminder = new TreeMap<Integer, EnumNotificationType>(new Comparator<Integer>() {
//            @Override
//            public int compare(Integer o1, Integer o2) {
//                NotificationConfig nc1 = notificationConfigMap.get(o1);
//                NotificationConfig nc2 = notificationConfigMap.get(o2);
//                if (nc1 == null) {
//                    return 1;
//                }
//                if (nc2 == null) {
//                    return -1;
//                }
//                if (nc1.getMaxAfterLogout() < nc2.getMaxAfterLogout()) {
//                    return -1;
//                } else if ((nc1.getMaxAfterLogout() == nc2.getMaxAfterLogout())) {
//                    return nc1.getMinAfterLogout() < nc2.getMinAfterLogout() ? -1 : 1;
//                }
//                return 1;
//            }
//        });
//        sortedReminder.put(EnumNotificationType.reminderMineFull.getIndex(), EnumNotificationType.reminderMineFull);
//        sortedReminder.put(EnumNotificationType.reminderConquer.getIndex(), EnumNotificationType.reminderConquer);
//        sortedReminder.put(EnumNotificationType.reminderComeback.getIndex(), EnumNotificationType.reminderComeback);
//        sortedReminder.put(EnumNotificationType.reminderDefenseSucceed.getIndex(), EnumNotificationType.reminderDefenseSucceed);
//        sortedReminder.put(EnumNotificationType.reminderNewNeighbours.getIndex(), EnumNotificationType.reminderNewNeighbours);
//        sortedReminder.put(EnumNotificationType.reminderComeback2.getIndex(), EnumNotificationType.reminderComeback2);
//        sortedReminder.put(EnumNotificationType.reminderAllianceMember.getIndex(), EnumNotificationType.reminderAllianceMember);
//        sortedReminder.put(EnumNotificationType.reminderNewNeighbours2.getIndex(), EnumNotificationType.reminderNewNeighbours2);
//        sortedReminder.put(EnumNotificationType.reminderDefenseSucceed2.getIndex(), EnumNotificationType.reminderDefenseSucceed2);
//        sortedReminder.put(EnumNotificationType.reminderStarStoneMineFull.getIndex(), EnumNotificationType.reminderStarStoneMineFull);
//        List<Integer> monetIds = PlayerService.getAllPlayersByLastLogoutTime(new Date(System.currentTimeMillis() - notification_timeout_limit3), new Date(System.currentTimeMillis() - notification_timeout_limit2));
//        TraceLog.info("[NotificationService]checkAndSendReminder, get all players finished, count=" + monetIds.size());
//        int checkedCount = 0;
//        for (int monetId : monetIds) {
//            try {
//                int govLevel = GovernmentBuildingService.getGovernmentLevel(monetId);
//                if (govLevel < 1) {
//                    TraceLog.info("[NotificationService]checkAndSendReminder, player is null");
//                    continue;
//                }
//                Player player = Player.load(monetId);
//                if (player == null) {
//                    TraceLog.info("[NotificationService]checkAndSendReminder, player is null");
//                    return;
//                }
//                Date lastLogoutTime = player.getLastLogoutTime();
//                if (lastLogoutTime == null) {
//                    TraceLog.info("[NotificationService]checkAndSendReminder, lastLogoutTime is null");
//                    return;
//                }
//
//                for (EnumNotificationType type : sortedReminder.values()) {
//                    NotificationConfig nc = notificationConfigMap.get(type.getIndex());
//                    if (nc == null) {
//                        continue;
//                    }
//                    if (type == EnumNotificationType.reminderMineFull) {
//                        JSONArray jsonArray = new JSONArray();
//                        jsonArray.put(PlayerService.getNickName(monetId));
//                        if (addNotification(monetId, type, jsonArray)) {
//                            break;
//                        }
//                    } else if (type == EnumNotificationType.reminderAllianceMember) {
//                        if (AllianceService.isMember(monetId) && addNotification(monetId, type)) {
//                            break;
//                        }
//                    } else if (type == EnumNotificationType.reminderNewNeighbours2) {
//                        if (!AllianceService.isMember(monetId) && addNotification(monetId, type)) {
//                            break;
//                        }
//                    } else if (type == EnumNotificationType.reminderStarStoneMineFull) {
//                        Vector<Building> buildings = Building.loadBuildingByUserId(monetId);
//                        boolean send = false;
//                        for (Building building : buildings) {
//                            if (building.getBuildingType() == EnumBuildingType.BUILDING_TYPE_STARSTONEMINER && addNotification(monetId, type)) {
//                                send = true;
//                                break;
//                            }
//                        }
//                        if (send) {
//                            break;
//                        }
//                    } else if (addNotification(monetId, type)) {
//                        break;
//                    }
//                }
//                checkedCount++;
//            } catch (Exception e) {
//                logger.error("[NotificationService]checkAndSendReminder, monetId=" + monetId + ",e:", e);
//            }
//        }
//        TraceLog.info("[NotificationService]checkAndSendReminder, ended, checkedCount=" + checkedCount);
//    }

//    public static void checkSoldierAndBuilding() {
//        TraceLog.info("[NotificationService]checkSoldierAndBuilding, started");
//        List<Integer> monetIds = PlayerService.getAllPlayersByLastLogoutTime(new Date(System.currentTimeMillis() - notification_timeout_limit), new Date());
//        TraceLog.info("[NotificationService]checkSoldierAndBuilding, get all players finished, count=" + monetIds.size());
//        int checkedCount = 0;
//        for (int monetId : monetIds) {
//            try {
//                ArmyService.syncSoldierLevel(monetId);
//                //EmploySoldierBarrackBuildingService.checkUpgradeEmploySoldier(monetId);
//                Vector<Building> buildings = Building.loadBuildingByUserId(monetId);
//                for (Building building : buildings) {
//                    if (building.getBuildingLevelUnCheck() < building.getBuildingLevel()) {
//                        // upgrade notification should be sent, no need to check soldier queue
//                        break;
//                    }
//                    if (building.getBuildingType() == EnumBuildingType.BUILDING_TYPE_GOLDMINER || building.getBuildingType() == EnumBuildingType.BUILDING_TYPE_STARSTONEMINER) {
//                        MineBuilding gm = ((MineBuilding) building);
//                        BuildTypeBase c = BuildTypeBase.getBuildingType(building.getBuildingType(), building.getBuildingLevel());
//                        if (c != null && gm.getProduction() >= c.getCapacity()) {
//                            JSONArray jsonArray = new JSONArray();
//                            jsonArray.put(PlayerService.getNickName(monetId));
//                            if (building.getBuildingType() == EnumBuildingType.BUILDING_TYPE_GOLDMINER) {
//                                if (addNotification(monetId, EnumNotificationType.goldMineFull, jsonArray)) {
//                                    break;
//                                }
//                            } else if (building.getBuildingType() == EnumBuildingType.BUILDING_TYPE_STARSTONEMINER) {
//                                if (addNotification(monetId, EnumNotificationType.starStoneMineFull, jsonArray)) {
//                                    break;
//                                }
//                            }
//                        }
//                    } else if (building.getBuildingType() == EnumBuildingType.BUILDING_TYPE_BARRACK) {
//                        ((BarrackBuilding) building).getAndCheckSoldiersQueue();
//                    }
//                }
//                checkedCount++;
//            } catch (Exception e) {
//                logger.error("[NotificationService]checkSoldierAndBuilding, monetId=" + monetId + ",e:", e);
//            }
//        }
//        TraceLog.info("[NotificationService]checkSoldierAndBuilding, ended, checkedCount=" + checkedCount);
//    }

//    public static void notifyWorldMonsterAppear() {
//        TraceLog.info("[NotificationService]notifyWorldMonsterAppear, started");
//        List<Integer> monetIds = PlayerService.getAllPlayersByLastLogoutTime(new Date(System.currentTimeMillis() - notification_timeout_limit), new Date());
//        TraceLog.info("[NotificationService]notifyWorldMonsterAppear, get all players finished, count=" + monetIds.size());
//        int checkedCount = 0;
//        for (int monetId : monetIds) {
//            try {
//                addNotification(monetId, EnumNotificationType.monsterAppear);
//                checkedCount++;
//            } catch (Exception e) {
//                logger.error("[NotificationService]notifyWorldMonsterAppear, monetId=" + monetId + ",e:", e);
//            }
//        }
//        TraceLog.info("[NotificationService]notifyWorldMonsterAppear, ended, checkedCount=" + checkedCount);
//    }

    public static JSONObject getAndroidPushJson(int monetId) {
        JSONObject ret = new JSONObject();
//        List<Notification> notificationList = Notification.load(monetId);
//        if (notificationList == null) {
//            TraceLog.info("[NotificationService]getAndroidPushJson, failed, notificationList is null");
//            return ret;
//        }
//        Player player = Player.load(monetId);
//        if (player == null) {
//            return ret;
//        }
//        Date lastLogoutTime = player.getLastLogoutTime();
//        Notification latestNotSentNotification = null;
//        for (Notification notification : notificationList) {
//            if (lastLogoutTime != null && lastLogoutTime.getTime() == 0) {
//                break;
//            }
//            if (notification.getHasBeenSent()) {
//                continue;
//            }
//            if (lastLogoutTime != null && notification.getUpdateTime().before(lastLogoutTime)) {
//                continue;
//            }
//            if (latestNotSentNotification == null) {
//                latestNotSentNotification = notification;
//            } else if (notification.getUpdateTime().after(latestNotSentNotification.getUpdateTime())) {
//                latestNotSentNotification = notification;
//            }
//        }
//        if (latestNotSentNotification != null) {
//            try {
//                if (latestNotSentNotification.read()) {
//                    StatLog.writeWithDetail(monetId, "ADNROID_NOTIFICATION", "notification=" + latestNotSentNotification, StatLog.GovLevelDetail);
//                    String parameter = latestNotSentNotification.getParameter();
//                    String msg = Global.getText(latestNotSentNotification.getContent(), player.getDeviceLang());
//                    if (parameter != null) {
//                        JSONArray parameterJson = new JSONArray(parameter);
//                        for (int i = 0, len = parameterJson.length(); i < len; i++) {
//                            msg = msg.replace("{" + i + "}", parameterJson.getString(i));
//                        }
//                    }
//                    ret.put("msg", msg);
//                }
//            } catch (JSONException e) {
//                logger.error("[NotificationService]getAndroidPushJson, monetId=" + monetId + ",e:", e);
//            }
//        }
        return ret;
    }
}
