var onLangLoad = function() {};
var langMap = {};

var userLang = navigator.language || navigator.userLanguage;

function getText(url, id, varMap)
{
    var request = new XMLHttpRequest();
    request.open('GET', url, true);
	request.onloadend = function()
	{
		if(request.status == 404) 
		{
			console.error("Warning: " + url + " not found!");
			varMap[id] = undefined;
			varMap["onAdded"](id);
		}
	}
	request.send(null);
	request.onreadystatechange = function()
	{
        if(request.readyState === 4 && request.status === 200)
		{
            if(request.responseText)
			{
				varMap[id] = request.responseText;
				varMap["onAdded"](id);
			}
        }
    }
}

var langMaps = {};
var lll = 0;
var LANG = undefined;

langMaps.onAdded = function(id)
{
	lll++;
	if(lll == 2)
	{
		LANG = makeLang(langMaps[userLang], langMaps["en"]);
		onLangLoad();
	}
}

getText("assets/lang/en.lang", "en", langMaps);
getText("assets/lang/" + userLang + ".lang", userLang, langMaps);

function makeLang(main, fallback)
{
	var mapping = {};
	
	var m1 = null;
	
	if(fallback)
	{
		m1 = fallback.split("\n");
		for(var i in m1)
		{
			var v = m1[i];
			if(v.indexOf("=") == -1)
				continue;
			var k = v.substr(0, v.indexOf("="));
			var v = v.substr(v.indexOf("=") + 1);
			mapping[k] = v;
		}
	}
	
	if(main)
	{
		m1 = main.split("\n");
		for(var i in m1)
		{
			var v = m1[i];
			if(v.indexOf("=") == -1)
				continue;
			var k = v.substr(0, v.indexOf("="));
			var v = v.substr(v.indexOf("=") + 1);
			mapping[k] = v;
		}
	}
	
	return mapping;
}

function translate(key)
{
	if(LANG && LANG[key])
		return LANG[key];
	return key;
}