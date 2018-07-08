package com.mozat.morange.game;

public class EnumBuildingType {

	public final static int BUILDING_TYPE_GOLDMINER = 1;
	public final static int BUILDING_TYPE_GOLDSTORAGE = 2;
	public final static int BUILDING_TYPE_CRYSTALMINE = 3;
	public final static int BUILDING_TYPE_CRYSTALSTORAGE = 4;
	public final static int BUILDING_TYPE_GOVERNMENT = 5;
	public final static int BUILDING_TYPE_BARRACK = 6;
	public final static int BUILDING_TYPE_CAMP = 7;
	public final static int BUILDING_TYPE_LABORATORY=8;
	public final static int BUILDING_TYPE_ALLIANCE=9;
	public final static int BUILDING_TYPE_SMITHY=10;
	public final static int BUILDING_TYPE_HEROALTAR=11;
	public final static int BUILDING_TYPE_LOTTERY=12;
	public final static int BUILDING_TYPE_STARSTONEMINER =13;//资源3的矿
	public final static int BUILDING_TYPE_TREE =14;
	public static final int BUILDING_TYPE_WALL = 15;
	public final static int BUILDING_TYPE_HEAVY_FACTORY = 16;
	public final static int BUILDING_TYPE_WISHSPRING = 17;
	public final static int BUILDING_TYPE_CAFE = 18;
	public final static int BUILDING_TYPE_EMPLOY_SOLDIER_BARRACK = 19;
	
	public final static int[] getBuildingTypeArr(){
		return new int[]{
				BUILDING_TYPE_GOLDMINER,
				BUILDING_TYPE_GOLDSTORAGE,
				BUILDING_TYPE_CRYSTALMINE,
				BUILDING_TYPE_CRYSTALSTORAGE,
				BUILDING_TYPE_GOVERNMENT,
				BUILDING_TYPE_BARRACK,
				BUILDING_TYPE_CAMP,
				BUILDING_TYPE_LABORATORY,
				BUILDING_TYPE_ALLIANCE,
				BUILDING_TYPE_SMITHY,
				BUILDING_TYPE_HEROALTAR,
				BUILDING_TYPE_LOTTERY,
				BUILDING_TYPE_STARSTONEMINER,
				BUILDING_TYPE_TREE,
				BUILDING_TYPE_WALL,
				BUILDING_TYPE_HEAVY_FACTORY,
				BUILDING_TYPE_WISHSPRING,
				BUILDING_TYPE_CAFE,
				BUILDING_TYPE_EMPLOY_SOLDIER_BARRACK
		};
	}
	
//	public enum BuildingTypeEnum {
//		BUILDING_TYPE_GOLDMINER(1),
//		BUILDING_TYPE_CRYSTALMine(2);
//		private int value;
//		private BuildingTypeEnum(int value) {
//			this.value = value;
//		}
//		public int getValue() {
//			return this.value;
//		}
//	}
}
