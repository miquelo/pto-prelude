if (typeof net == "undefined")
	net = {};
if (typeof net.preparatusopos == "undefined")
	net.preparatusopos = {};
if (typeof net.preparatusopos.faces == "undefined")
	net.preparatusopos.faces = {};

net.preparatusopos.faces.redirect = function()
{
function perform(data)
{
	if (data.status == "success")
	{
		var metaList = document.getElementsByTagName("meta");
		for (var i = 0; i < metaList.length; ++i)
		{
			var meta = metaList.item(i);
			if (meta.name == "faces-redirect")
			{
				if (meta.content == null || meta.content.length == 0)
					window.location.reload();
				else
					window.location.href = meta.content;
				break;
			}
		}
	}
};

return {
	"perform": perform
};
}();