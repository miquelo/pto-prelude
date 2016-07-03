if (typeof net == "undefined")
	net = {};
if (typeof net.preparatusopos == "undefined")
	net.preparatusopos = {};
if (typeof net.preparatusopos.appmaweb == "undefined")
	net.preparatusopos.appmaweb = {};

net.preparatusopos.appmaweb.animations = function()
{

var TRANSITION_END_EVENT_TYPES = [
	"transitionend",
	"mozTransitionEnd",
	"webkitTransitionEnd",
	"oTranstionEnd",
	"MSTransitionEnd"
];

function addTransitionEndEventListener(target, listener)
{
	TRANSITION_END_EVENT_TYPES.forEach(function(eventType)
	{
		target.addEventListener(eventType, listener);
	});
};

function removeTransitionEndEventListener(target, listener)
{
	TRANSITION_END_EVENT_TYPES.forEach(function(eventType)
	{
		target.removeEventListener(eventType, listener);
	});
};

function executeLater(callback)
{
	setTimeout(callback, 0);
};

HTMLElement.prototype.replaceChild = function(newChild, oldChild)
{
	// Must be in document tree unless its is not shown
	this.insertBefore(newChild, oldChild);
	var newChildScrollWidth = newChild.scrollWidth;
	var newChildScrollHeight = newChild.scrollHeight;
	var newChildDefaultDisplay = newChild.style.display;
	var newChildDefaultOpacity = newChild.style.opacity;
	var newChildDefaultTransition = newChild.style.transition;
	newChild.style.display = "none";
	newChild.style.opacity = "0";
	newChild.style.transition = "opacity 120ms ease-in";
	
	var propertiesToChange = [];
	propertiesToChange.push("opacity");
	if (oldChild.scrollWidth != newChildScrollWidth)
		propertiesToChange.push("width");
	if (oldChild.scrollHeight != newChildScrollHeight)
		propertiesToChange.push("height");
	function allPropertiesChanged(propertyName)
	{
		for (var i = propertiesToChange.length - 1; i >= 0; --i)
			if (propertiesToChange[i] == propertyName)
				propertiesToChange.splice(i, 1);
		return propertiesToChange.length == 0;
	};
	
	var oldChildRemoved = false;
	var newChildAdded = false;
	addTransitionEndEventListener(oldChild, function(event)
	{
		if (!oldChildRemoved && allPropertiesChanged(event.propertyName))
		{
			this.removeChild(oldChild);
			newChild.style.display = newChildDefaultDisplay;
			
			executeLater(function()
			{
				addTransitionEndEventListener(newChild, function(event)
				{
					if (!newChildAdded && event.propertyName == "opacity")
					{
						newChild.style.transition = newChildDefaultTransition;
						newChildAdded = true;
					}
				});
				newChild.style.opacity = newChildDefaultOpacity;
			});
			oldChildRemoved = true;
		}
	}.bind(this));
	
	oldChild.style.width = oldChild.scrollWidth + "px";
	oldChild.style.height = oldChild.scrollHeight + "px";
	oldChild.style.overflow = "hidden";
	
	executeLater(function()
	{
		oldChild.style.transition = ""
			+ "opacity 120ms ease-in,"
			+ "width 120ms ease-in 120ms,"
			+ "height 120ms ease-in 120ms";
		oldChild.style.opacity = "0";
		oldChild.style.width = newChildScrollWidth + "px";
		oldChild.style.height = newChildScrollHeight + "px";
	});
};

return {};
}();
