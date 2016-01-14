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

var menuItems = null;

window.addEventListener("click", function()
{
	if (menuItems != null)
		menuItems.classList.remove(EXPANDED_CLASS);
});

window.addEventListener("keyup", function(event)
{
	if (menuItems != null && event.keyCode == 27) // ESC
		menuItems.classList.remove(EXPANDED_CLASS);
});

function show(event, menuItemsId)
{
	if (menuItems == null)
	{
		menuItems = document.getElementById(menuItemsId);
		menuItems.addEventListener("click", function(event)
		{
			event.stopPropagation();
		});
	}
	menuItems.classList.add(EXPANDED_CLASS);
	event.stopPropagation();
};

return {
	"show": show
};
}();
