exec = function()
{
	setTitle(translate("devs"), translate("devs.text"));
	
	var b = PageBuilder;
	
	if(!persisted.devtime)
		b.clean();
	
	if(translate("title") != "title")
	{
		if(!persisted.devtime)
		{
			persisted.devtime = true;
			
			b.addLine("<font size = 6>" + translate("devs.desc.1") + "</font>");
			var buildGradle = b.addPane(translate("devs.desc.0"));
			buildGradle.addLine("<pre>repositories { \n	maven { \n		name = \"APengu Maven\" \n		url = \"https://raw.githubusercontent.com/APengu/PenguLibs/gh-pages\" \n	} \n}\n\ndependencies { \n	deobfCompile \"apengu:VanillaTech:MCVERSION-VTVERSION:deobf\" \n}</pre>");
			buildGradle.addLine(translate("devs.desc.2"));
		}
	}
}

exec();