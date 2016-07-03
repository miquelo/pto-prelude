if (typeof net == "undefined")
	net = {};
if (typeof net.preparatusopos == "undefined")
	net.preparatusopos = {};
if (typeof net.preparatusopos.faces == "undefined")
	net.preparatusopos.faces = {};

net.preparatusopos.faces.ajaxLoading = function()
{

var AJAX_LOADING_CLASS = "ajax-loading";
var ACTIVE_CLASS = "active";

function forEachAjaxLoading(fn)
{
	var ajaxLoading = document.querySelectorAll("div.ajax-loading");
	for (var i = 0; i < ajaxLoading.length; ++i)
		fn(ajaxLoading.item(i));
};

window.addEventListener("load", function()
{
	forEachAjaxLoading(function(elem)
	{
		jsf.ajax.addOnEvent(function(data)
		{
			switch (data.status)
			{
				case "begin":
				elem.classList.add(ACTIVE_CLASS);
				break;
				
				case "complete":
				elem.classList.remove(ACTIVE_CLASS);
				break;
			}
		});
	});
});

return {};
}();
