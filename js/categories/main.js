exec = function()
{
	setTitle(translate("overview"), translate("overview.text"));
	var b = PageBuilder;
	b.clean();
	b.addLine(translate("overview.desc").replace("erhi34uy89t34g9", getScriptCaller("developers")));
}

exec();