if (typeof net == "undefined")
	net = {};
if (typeof net.preparatusopos == "undefined")
	net.preparatusopos = {};
if (typeof net.preparatusopos.faces == "undefined")
	net.preparatusopos.faces = {};

net.preparatusopos.faces.selectOneMenu = function()
{

var SELECTOR_BASE = "div.select-one-menu";
var SELECTOR_BUTTON = SELECTOR_BASE + " > div:nth-of-type(1)";
var SELECTOR_LIST = SELECTOR_BASE + " > div:nth-of-type(2)";
var SELECTOR_INPUT = SELECTOR_BASE + " > input";

var EXPANDED_CLASS = "expanded";

function getProperty(obj, name)
{
	return "__faces" in obj ? obj.__faces[name] : undefined;
};

function setProperty(obj, name, value)
{
	if (!("__faces" in obj))
		obj.__faces = {};
	obj.__faces[name] = value;
};

function forEachSelectOneMenu(fn)
{
	var nodeList = document.querySelectorAll(SELECTOR_BASE);
	for (var i = 0; i < nodeList.length; ++i)
		fn(nodeList.item(i));
};

function getItems(selectOneMenu)
{
	return selectOneMenu.querySelectorAll(SELECTOR_LIST + " > div > ul > li");
};

function configure()
{
	forEachSelectOneMenu(function(selectOneMenu)
	{
		if (!getProperty(selectOneMenu, "configured"))
		{
			setProperty(selectOneMenu, "preventClose", false);
			
			var button = selectOneMenu.querySelector(SELECTOR_BUTTON);
			button.addEventListener("click", function(event)
			{
				if (!selectOneMenu.isOpened())
					selectOneMenu.open();
				else
					selectOneMenu.close();
			});
			
			var input = selectOneMenu.querySelector(SELECTOR_INPUT);
			var selectedIndex = null;
			
			var listItems = selectOneMenu.querySelectorAll(SELECTOR_LIST +
					" > div > ul > li");
			for (var i = 0; i < listItems.length; ++i)
			{
				var item = listItems.item(i);
				item.addEventListener("click", function(event)
				{
					var index = parseInt(event.target.getAttribute(
							"data-index"));
					selectOneMenu.setSelectedOption(index);
				});
				
				if (input.value == item.getAttribute("data-value"))
					selectedIndex = i;
			}
			
			selectOneMenu.getDisabled = getDisabled.bind(selectOneMenu);
			selectOneMenu.getReadOnly = getReadOnly.bind(selectOneMenu);
			selectOneMenu.setSelectedOption = setSelectedOption.bind(
					selectOneMenu);
			selectOneMenu.isOpened = isOpened.bind(selectOneMenu);
			selectOneMenu.open = open.bind(selectOneMenu);
			selectOneMenu.close = close.bind(selectOneMenu);
			
			setProperty(selectOneMenu, "configured", true);
		}
	});
};

function getDisabled()
{
	return this.querySelector(SELECTOR_INPUT).disabled;
};

function getReadOnly()
{
	return this.querySelector(SELECTOR_INPUT).readOnly;
};

function setSelectedOption(index)
{
	var input = this.querySelector(SELECTOR_INPUT);
	var button = this.querySelector(SELECTOR_BUTTON);
	while (button.firstChild != null)
		button.removeChild(button.firstChild);
	
	
	var items = getItems(this);
	var content = items.item(index);
	for (var i = 0; i < content.childNodes.length; ++i)
		button.appendChild(content.childNodes.item(i).cloneNode(true));
	var newValue = content.getAttribute("data-value");
	if (newValue != input.value)
	{
		input.value = newValue;
		input.dispatchEvent(new Event("change"));
	}
};

function isOpened()
{
	return this.classList.contains(EXPANDED_CLASS);
};

function open()
{
	if (!this.getDisabled() && !this.getReadOnly() && getItems(this).length > 0)
	{
		this.classList.add(EXPANDED_CLASS);
		setProperty(this, "preventClose", true);
	}
};

function close()
{
	this.classList.remove(EXPANDED_CLASS);
};

window.addEventListener("load", function()
{
	configure();
});

window.addEventListener("click", function(event)
{
	forEachSelectOneMenu(function(selectOneMenu)
	{
		if (getProperty(selectOneMenu, "preventClose"))
			setProperty(selectOneMenu, "preventClose", false);
		else
			selectOneMenu.close();
	});
});

window.addEventListener("keyup", function(event)
{
	forEachSelectOneMenu(function(selectOneMenu)
	{
		if (event.keyCode == 27) // ESC
			selectOneMenu.close();
	});
});

jsf.ajax.addOnEvent(function(data)
{
	if (data.status == "success")
		configure();
});

return {};
}();
