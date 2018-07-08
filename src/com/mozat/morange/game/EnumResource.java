package com.mozat.morange.game;

public enum EnumResource {

	RESOURCE_TYPE_GOLD (1 , "gold"),
	RESOURCE_TYPE_CRYSTAL (2 , "crystal"),
	RESOURCE_TYPE_DIAMOND (3 , "diamond"),
	RESOURCE_TYPE_STARSTONE (4 , "starStone"),
	RESOURCE_TYPE_MITHRIL(5, "mithril");
	
	private int index;
	private String name;
	public int getIndex(){
		return this.index;
	}
	public String getName(){
		return this.name;
	}
	private EnumResource(int index , String name){
		this.index = index;
		this.name = name;
	}
	public static EnumResource getByIndex(int index){
		for(EnumResource e : EnumResource.values()){
			if(e.getIndex() == index){
				return e;
			}
		}
		return null;
	}
}
