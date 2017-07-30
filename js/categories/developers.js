exec = function()
{
	setTitle("For Developers", "A quick guide on how to get Vanilla Tech into your workspace");
	
	var b = PageBuilder;
	b.clean();
	
	b.addLine("<font size = 6>How to add to your development environment</font>");
	var buildGradle = b.addPane("Include following code into your build.gradle:");
	buildGradle.addLine("<pre>repositories { \n	maven { \n		name = \"APengu Maven\" \n		url = \"https://raw.githubusercontent.com/APengu/PenguLibs/gh-pages\" \n	} \n}\n\ndependencies { \n	deobfCompile \"apengu:SolarFluxReborn:MCVERSION-VTVERSION:deobf\" \n}</pre>");
}

exec();