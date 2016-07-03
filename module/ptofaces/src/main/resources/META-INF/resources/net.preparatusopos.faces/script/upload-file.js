if (typeof net == "undefined")
	net = {};
if (typeof net.preparatusopos == "undefined")
	net.preparatusopos = {};
if (typeof net.preparatusopos.faces == "undefined")
	net.preparatusopos.faces = {};

net.preparatusopos.faces.uploadFile = function()
{

var NAMESPACE = "net.preparatusopos.faces.uploadFile";

var fileUploadList= [];

function FileUpload(source, event, options, index, file)
{
	this.source = source;
	this.event = event;
	this.options = options || {};
	this.index = index;
	this.file = file;
	this.provider = null;
	this.grant = null;
	this.started = false;
};

FileUpload.prototype.receive = function(properties)
{
	if ("provider" in properties)
		this.provider = eval(NAMESPACE + ".provider." + properties.provider);
	if ("data" in properties)
		this.data = properties.data;
	
	if (!this.started)
	{
		this.started = true;
		this.provider.start(this);
	}
};

FileUpload.prototype.send = function(completedSize)
{
	var properties = {
		"index": this.index,
		"completedSize": completedSize,
		"totalSize": this.file.size,
		"contentType": this.file.type
	};
	
	var requestOptions = {
		"execute": "@none"
	};
	if ("render" in this.options)
		requestOptions.render = this.options.render;
	
	for (var name in properties)
		requestOptions[NAMESPACE + "." + name] = encodeURI(properties[name]);
	jsf.ajax.request(this.source, this.event, requestOptions);
};

FileUpload.prototype.raise = function(completedSize)
{
	this.send(completedSize);
};

function upload(source, event, options)
{
	var resource = options.resource;
	var input = getInput(source, options);
	if (input == null)
		throw "File input was not found";
	
	var fileList = input.files;
	for (var i = 0; i < fileList.length; ++i)
	{
		var file = fileList.item(i);
		var upload = new FileUpload(source, event, options,
				fileUploadList.length, file);
		fileUploadList.push(upload);
		upload.send(0);
	}
};

function getInput(source, options)
{
	if ("input" in options)
	{
		var input = options.input;
		if (input == "@this")
			return source;
		return document.getElementById(input);
	}
	return source;
};

function handleResponse(data, handler)
{
	var extension = data.responseXML.getElementById(NAMESPACE);
	if (extension != null)
	{
		var response = JSON.parse(extension.textContent);
		for (var i in response)
		{
			var item = response[i];
			if (item.index >= fileUploadList.length)
				throw "Bad upload index " + item.index;
			handler(item, fileUploadList[item.index]);
		}
	}
};

jsf.ajax.addOnEvent(function(data)
{
	switch (data.status)
	{
		case "success":
		handleResponse(data, function(response, upload)
		{
			var properties = {
				"provider": response.provider,
				"data": response.data
			};
			upload.receive(properties);
		});
		break;
	}
});

return {
	"upload": upload
};
}();
