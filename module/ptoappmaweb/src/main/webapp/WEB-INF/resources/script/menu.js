if (typeof net == "undefined")
	net = {};
if (typeof net.preparatusopos == "undefined")
	net.preparatusopos = {};
if (typeof net.preparatusopos.appmaweb == "undefined")
	net.preparatusopos.appmaweb = {};
if (typeof net.preparatusopos.appmaweb.faces == "undefined")
	net.preparatusopos.appmaweb.faces = {};

net.preparatusopos.appmaweb.faces.menu = function()
{

var EXPANDED_CLASS = "expanded";
var VISIBLE_CLASS = "visible";

function addEventListener(element, type, callback)
{
	var prefix = [ "webkit", "moz", "MS", "o" ];
	element.addEventListener(type.toLowerCase(), callback);
	for (var i in prefix)
		element.addEventListener(prefix[i] + type, callback);
};

function forEachMenuItems(fn)
{
	var menuItems = document.querySelectorAll("nav.menu-items")
	for (var i = 0; i < menuItems.length; ++i)
		fn(menuItems.item(i));
};

function forEachMobileMenuItemsAccount(fn)
{
	if (window.matchMedia("only screen and (max-width: 640px)").matches)
	{
		var menuItemsAccount = document.querySelectorAll(
				"nav.menu-items > ul:nth-of-type(2)");
		for (var i = 0; i < menuItemsAccount.length; ++i)
			fn(menuItemsAccount.item(i));
	}
};

function forEachDesktopMenuItemsAccount(fn)
{
	if (window.matchMedia("only screen and (min-width: 640px)").matches)
	{
		var menuItemsAccount = document.querySelectorAll(
				"nav.menu-items > ul:nth-of-type(2)");
		for (var i = 0; i < menuItemsAccount.length; ++i)
			fn(menuItemsAccount.item(i));
	}
};

window.addEventListener("load", function()
{
	forEachMenuItems(function(menuItems)
	{
		menuItems.addEventListener("click", function(event)
		{
			forEachDesktopMenuItemsAccount(function(menuItemsAccount)
			{
				menuItemsAccount.classList.remove(VISIBLE_CLASS);
			});
			event.stopPropagation();
		});
	});
	forEachMobileMenuItemsAccount(function(menuItemsAccount)
	{
		showAccountMenu(menuItemsAccount);
	});
	forEachDesktopMenuItemsAccount(function(menuItemsAccount)
	{
		addEventListener(menuItemsAccount, "TransitionEnd", function(event) {
			if (!menuItemsAccount.classList.contains(VISIBLE_CLASS))
				menuItemsAccount.style.display = "none";
		});
		menuItemsAccount.addEventListener("click", function(event)
		{
			event.stopPropagation();
		});
	});
});

window.addEventListener("resize", function()
{
	forEachMobileMenuItemsAccount(function(menuItemsAccount)
	{
		showAccountMenu(menuItemsAccount);
	});
	forEachDesktopMenuItemsAccount(function(menuItemsAccount)
	{
		menuItemsAccount.classList.remove(VISIBLE_CLASS);
	});
});

window.addEventListener("click", function()
{
	forEachMenuItems(function(menuItems)
	{
		menuItems.classList.remove(EXPANDED_CLASS);
	});
	forEachDesktopMenuItemsAccount(function(menuItemsAccount)
	{
		menuItemsAccount.classList.remove(VISIBLE_CLASS);
	});
});

window.addEventListener("keyup", function(event)
{
	forEachMenuItems(function(menuItems)
	{
		if (event.keyCode == 27) // ESC
			menuItems.classList.remove(EXPANDED_CLASS);
	});
	forEachDesktopMenuItemsAccount(function(menuItemsAccount)
	{
		if (event.keyCode == 27) // ESC
			menuItemsAccount.classList.remove(VISIBLE_CLASS);
	});
});

function showAccountMenu(menuItemsAccount)
{
	menuItemsAccount.style.display = "flex";
	// TODO Do cancel timeout when needed
	setTimeout(function()
	{
		menuItemsAccount.classList.add(VISIBLE_CLASS);
	},
	80);
};

function show(event)
{
	forEachMenuItems(function(menuItems)
	{
		menuItems.classList.add(EXPANDED_CLASS);
		event.stopPropagation();
	});
};

function account(event)
{
	forEachDesktopMenuItemsAccount(function(menuItemsAccount)
	{
		if (menuItemsAccount.classList.contains(VISIBLE_CLASS))
			menuItemsAccount.classList.remove(VISIBLE_CLASS);
		else
			showAccountMenu(menuItemsAccount);
		event.preventDefault();
		event.stopPropagation();
	});
};

return {
	"show": show,
	"account": account
};
}();
