function addRecipe(regName)
{
	PageBuilder.addLine(translate("recipe." + regName) + ":");
	PageBuilder.constrImg("assets/img/recipes/" + regName + ".png").setId(regName).create().show();
	PageBuilder.addLine("");
}

exec = function()
{
	setTitle(translate("recipes"), translate("recipes.text"));
	
	var b = PageBuilder;
	b.clean();
	
	addRecipe("backpack");
	addRecipe("redstone_teleporter");
	addRecipe("shield_totem");
	addRecipe("witherproof_red_stone");
	addRecipe("time_bomb");
	addRecipe("enhanced_furnace");
	addRecipe("fisher");
}

exec();