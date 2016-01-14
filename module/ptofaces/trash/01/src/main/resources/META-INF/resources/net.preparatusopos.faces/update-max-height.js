if (typeof net == "undefined")
	net = {};
if (typeof net.preparatusopos == "undefined")
	net.preparatusopos = {};
if (typeof net.preparatusopos.faces == "undefined")
	net.preparatusopos.faces = {};

net.preparatusopos.faces.updateMaxHeight = function()
{

function perform(element, event, value)
{
	if (value == null)
		event.source.style.maxHeight = event.source.scrollHeight + "px";
	else
		event.source.style.maxHeight = value;
};

return {
	"perform": perform
};
}();