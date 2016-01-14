if (typeof net == "undefined")
	net = {};
if (typeof net.preparatusopos == "undefined")
	net.preparatusopos = {};
if (typeof net.preparatusopos.faces == "undefined")
	net.preparatusopos.faces = {};

net.preparatusopos.faces.googleOAuth = function()
{

var _auth2 = null;
var _source = null;
var _onSuccess = null;

function definedWait(objectName, retryTime, attempts, onDefined)
{
	try
	{
		onDefined(eval(objectName));
	}
	catch (ex)
	{
		if (attempts > 0)
			setTimeout(definedWait, retryTime, objectName, retryTime,
					--attempts, onDefined);
	}
};

function init(sourceId, clientId, scopes, onSuccess)
{
	definedWait("gapi", 80, 200, function(_gapi)
	{
		_gapi.load("auth2", function()
		{
			_auth2 = gapi.auth2.init({
				"client_id": clientId,
				"scope": scopes
			});
			_source = document.getElementById(sourceId);
			_onSuccess = onSuccess;
		});
	});
};

function request(source, event, inputId)
{
	try
	{
		_auth2.grantOfflineAccess({
			"redirect_uri": "postmessage"
		}).then(grantOfflineAccessSuccess.bind({
			"source": source,
			"event": event,
			"inputId": inputId
		}));
		return true;
	}
	catch (ex)
	{
		console.error(ex);
		return false;
	}
};

function grantOfflineAccessSuccess(resp)
{
	var input = document.getElementById(this.inputId);
	if (input == null)
		throw "Input '" + this.inputId + "' was not found";
	
	input.value = resp.code;
	jsf.ajax.request(this.source, this.event, {
		"execute": this.inputId,
		"onevent": onAjaxRequestEvent.bind(this.event)
	});
};

function onAjaxRequestEvent(data)
{
	if (data.status == "success")
		_onSuccess.bind(_source)(this);
};

var platformScript = document.createElement("script");
platformScript.type = "text/javascript";
platformScript.async = true;
platformScript.src = "https://apis.google.com/js/platform.js";

var firstScript = document.getElementsByTagName("script").item(0);
firstScript.parentNode.insertBefore(platformScript, firstScript);

return {
	"init": init,
	"request": request
};
}();
