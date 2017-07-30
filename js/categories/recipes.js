function addRecipe(regName, name)
{
	PageBuilder.addLine(name + ":");
	PageBuilder.constrImg("assets/img/recipes/" + regName + ".png").setId(regName).create().show();
	PageBuilder.addLine("");
}

exec = function()
{
	setTitle("Recipes", "Here you can view every recipe (including specials)");
	
	var b = PageBuilder;
	b.clean();
	
	addRecipe("backpack", "Backpack");
	addRecipe("redstone_teleporter", "Redstone Reciesender");
	addRecipe("shield_totem", "Totem of Shield");
	addRecipe("witherproof_red_stone", "Wither-Proof Red Rock");
	addRecipe("time_bomb", "Time Bomb");
	addRecipe("enhanced_furnace", "Enhanced Furnace");
	addRecipe("fisher", "Auto-Fisher");
}

exec();