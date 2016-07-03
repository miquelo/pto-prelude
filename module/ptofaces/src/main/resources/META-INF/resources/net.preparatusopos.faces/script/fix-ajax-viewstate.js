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
	var updateNodes = responseXML.getElementsByTagName("update");
	for (var i = 0; i < updateNodes.length; ++i)
	{
		var updateNode = updateNodes.item(i);
		var updateId = updateNode.getAttribute("id");
		if (updateId != null && updateId.match(
					/^([\w]+:)?javax\.faces\.ViewState(:[0-9]+)?$/))
			return updateNode.textContent || updateNode.innerText;
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
