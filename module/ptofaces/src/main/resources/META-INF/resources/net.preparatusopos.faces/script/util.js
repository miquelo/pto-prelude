if (typeof net == "undefined")
	net = {};
if (typeof net.preparatusopos == "undefined")
	net.preparatusopos = {};
if (typeof net.preparatusopos.faces == "undefined")
	net.preparatusopos.faces = {};

net.preparatusopos.faces.util = function()
{

function removeClass(target, event, styleClass, targetId)
{
	var resultTarget = typeof targetId == "undefined" ? target
			: document.getElementById(targetId);
	resultTarget.classList.remove(styleClass);
	return true;
};

return {
	"removeClass": removeClass
};
}();
