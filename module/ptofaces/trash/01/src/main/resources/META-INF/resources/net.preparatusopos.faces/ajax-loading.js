if (typeof net == "undefined")
	net = {};
if (typeof net.preparatusopos == "undefined")
	net.preparatusopos = {};
if (typeof net.preparatusopos.faces == "undefined")
	net.preparatusopos.faces = {};

net.preparatusopos.faces.ajaxLoading = function()
{

function init(panelId, visibleClass, hiddenClass)
{
	var panel = document.getElementById(panelId);
	jsf.ajax.addOnEvent(function(data)
	{
		switch (data.status)
		{
			case "begin":
			panel.classList.add(visibleClass);
			panel.classList.remove(hiddenClass);
			break;
			
			case "complete":
			panel.classList.add(hiddenClass);
			panel.classList.remove(visibleClass);
			break;
		}
	});
};

return {
	"init": init
};
}();