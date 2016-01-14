if (typeof net == "undefined")
	net = {};
if (typeof net.preparatusopos == "undefined")
	net.preparatusopos = {};
if (typeof net.preparatusopos.faces == "undefined")
	net.preparatusopos.faces = {};

net.preparatusopos.faces.fixAjaxViewState = function()
{

function getViewState(responseXML)
{
	var updates = responseXML.getElementsByTagName("update");
	for (var i in updates)
	{
		var update = updates[i];
		if ("id" in update && update.id.match(
					/^([\w]+:)?javax\.faces\.ViewState(:[0-9]+)?$/))
			return update.textContent || updates[i].innerText;
	}
	return null;
};

function hasViewState(form)
{
	for (var i = 0; i < form.elements.length; ++i)
		if (form.elements.item(i).name == "javax.faces.ViewState")
			return true;
	return false;
};

function createViewState(form, viewState)
{
	var stateField = document.createElement("input");
	stateField.name = "javax.faces.ViewState";
	stateField.type = "hidden";
	stateField.value = viewState;
	stateField.autocomplete = "off";
	form.appendChild(stateField);
};

jsf.ajax.addOnEvent(function(data)
{
	if (data.status == "success")
	{
		var viewState = getViewState(data.responseXML);
		for (var i = 0; i < document.forms.length; i++)
		{
			var form = document.forms.item(i);
			if (form.method == "post" && !hasViewState(form))
			{
				console.log("Fixing JSF view state of form", form.id);
				createViewState(form, viewState);
			}
		}
	}
});

return {};
}();
