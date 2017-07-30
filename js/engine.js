var currScript = "";

var scripts = {};
var runtimeScripts = [];
var exec = function()
{
	console.warn("Script not set!");
}

function newPageBuilder(elem)
{
	clean = function()
	{
		document.getElementById(elem).innerHTML = "";
	};
	
	addLine = function(ln)
	{
		document.getElementById(elem).innerHTML += ln + "<br>";
	};
	
	addPane = function(title)
	{
		var uuid = "pane-" + (Math.random() * 46247);
		
		var d = "<div id = \"" + uuid + "\" class = \"panel panel-default\">";
		if(title != undefined)
			d += "<div class=\"panel-heading\">" + title + "</div>";
		d += "</div>";
		this.addLine(d);
		
		var builder = newPageBuilder(uuid);
		
		builder.clean = function()
		{
			var dd = document.getElementById(uuid);
			if(title != undefined)
				dd.innerHTML = "<div class=\"panel-heading\">" + title + "</div>";
			else
				dd.innerHTML = "";
		}
		
		return builder;
	}
	
	addProgressBar = function(uid)
	{
		var ln = "<div class = \"progress progress-striped active\"><div class = \"progress-bar progress-bar-primary\" role = \"progressbar\" id = \"pbar-" + uid + "\" aria-valuenow = \"50\" aria-valuemin = \"0\" aria-valuemax = \"100\" style = \"width: 0%\"><span class = \"sr-only\"></span></div></div>";
		this.addLine(ln);
		var props = {};
		
		props.setNow = function(val)
		{
			document.getElementById("pbar-" + uid)["aria-valuenow"] = val;
			return this;
		};
		
		props.setMin = function(val)
		{
			document.getElementById("pbar-" + uid)["aria-valuemin"] = val;
			return this;
		};
		
		props.setMax = function(val)
		{
			document.getElementById("pbar-" + uid)["aria-valuemax"] = val;
			return this;
		};
		
		return props;
	};
	
	constrImg = function(url)
	{
		var c = {};
		var pb = this;
		
		c.uuid = "page-img-" + (Math.random() * 3674);
		c.url = url;
		c.centered = false;
		
		c.setCentered = function(bool)
		{
			this.centered = bool;
			return this;
		};
		
		c.create = function()
		{
			var prefix = this.centered ? "<center>" : "";
			var postfix = this.centered ? "<center/>" : "";
			pb.addLine(prefix + "<img src = \"" + url + "\" id = \"" + this.uuid + "\" hidden></img>" + postfix);
			return this;
		};
		
		c.show = function()
		{
			var i = document.getElementById(this.uuid);
			i.hidden = false;
			return this;
		};
		
		c.setId = function(id)
		{
			if(document.getElementById(this.uuid) != undefined)
			{
				console.warn("Not setting id");
				return;
			}
			this.uuid = id;
			return this;
		}
		
		c.hide = function()
		{
			var i = document.getElementById(this.uuid);
			i.hidden = true;
			return this;
		};
		
		return c;
	}
	
	return this;
}

var PageBuilder = newPageBuilder("main-page-text");

function getScriptIndex(script)
{
	var i = -1;
	for(var j = 0; j < runtimeScripts.length; ++j)
		if(runtimeScripts[j].name == script)
		{
			i = j;
			break;
		}
	return i;
}

function getScriptCaller(script)
{
	return "runtimeScripts[" + getScriptIndex(script) + "].func();";
}

function addCategory(name, script)
{
	if(document.getElementById("lcat-" + script) != undefined)
	{
		console.error("Attempted to register category for " + script + " but it is already declared");
		return;
	}
	var mm = document.getElementById("main-menu");
	
	scripts[script] = name;
	var f = function() { setScript(script); };
	runtimeScripts.push({ name: script, func: f });
	
	mm.innerHTML += "<li><a id = \"lcat-" + script + "\" onclick = \"" + getScriptCaller(script) + "\" href = \"#\">" + name + "</a></li>";
}

function getScriptName(script)
{
	return scripts[script] != undefined ? scripts[script] : script;
}

var lastChild = undefined;

function setScript(script)
{
	if(script == currScript)
		return;
	
	console.log("set category " + script);
	
	var lc = document.getElementById("lcat-" + script);
	if(lc != undefined)
		lc.hovered = false;
	
	currScript = script;
	
	var ps = document.getElementById("pageScript");
	var nscript = document.createElement("script");
	nscript.src = "js/categories/" + script + ".js";
	ps.appendChild(nscript);
	if(lastChild != undefined)
		ps.removeChild(lastChild);
	lastChild = nscript;
	
	cleanup();
	
	var lc = document.getElementById("lcat-" + script);
	if(lc != undefined)
		lc.hovered = true;
	
	var url = location.search;
	var p = url.substr(url.lastIndexOf('?') + 1, url.length);
	var urls = url.substr(0, url.lastIndexOf('?') + 1);
	url = urls + script;
	
	if(p != script)
		location.search = url;
}

function setTitle(title, description)
{
	document.getElementById("main-title").innerHTML = "<h2>" + title + "</h2>" + (description ? description : "");
}


function cleanup()
{
	var mm = document.getElementById("main-menu");
	mm.innerHTML = "<li class=\"text-center\"></li>";
	PageBuilder.clean();
	runtimeScripts = [];
	setTitle("", "");
	if(addCats != undefined)
		addCats();
}

cleanup();

var url = location.search;
var urls = url.substr(url.lastIndexOf('?') + 1, url.length);
if(getScriptIndex(urls) != -1)
	setScript(urls);