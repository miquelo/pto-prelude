if (typeof net == "undefined")
	net = {};
if (typeof net.preparatusopos == "undefined")
	net.preparatusopos = {};
if (typeof net.preparatusopos.faces == "undefined")
	net.preparatusopos.faces = {};

net.preparatusopos.faces.toggleClass = function()
{

function init(select, styleClass, delay, onBeforeToggle, onAfterToggle,
		onTransitionEnd)
{
	function addEventListener(element, type, callback)
	{
		var prefix = [ "webkit", "moz", "MS", "o" ];
		element.addEventListener(type.toLowerCase(), callback);
		for (var i in prefix)
			element.addEventListener(prefix[i] + type, callback);
	};
	
	jsf.ajax.addOnEvent(function(data)
	{
		switch (data.status)
		{
			case "success":
			var elems = document.querySelectorAll(select);
			for (var i = 0; i < elems.length; ++i)
			{
				var elem = elems.item(i);
				if (onTransitionEnd != null)
					addEventListener(elem, "TransitionEnd", function(event) {
						onTransitionEnd(elem, {
							"source": elem
						});
					});
				if (onBeforeToggle != null)
					onBeforeToggle(elem, {
						"source": elem
					});
			}
			setTimeout(function()
			{
				for (var i = 0; i < elems.length; ++i)
				{
					var elem = elems.item(i);
					elem.classList.toggle(styleClass);
					if (onAfterToggle != null)
						onAfterToggle(elem, {
							"source": elem
						});
				}	
			},
			delay);
			break;
		}
	});
};

return {
	"init": init
};
}();