function addCats()
{
	addCategory(translate("overview"), "main");
	addCategory(translate("recipes"), "recipes");
	addCategory(translate("devs"), "developers");
}

onLangLoad = function()
{
	document.getElementById("site-label").innerHTML = translate("title");
	document.getElementById("togglenav").innerHTML = translate("togglenav");
	resetPage();
}