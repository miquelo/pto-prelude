if (typeof net == "undefined")
	net = {};
if (typeof net.preparatusopos == "undefined")
	net.preparatusopos = {};
if (typeof net.preparatusopos.faces == "undefined")
	net.preparatusopos.faces = {};
if (typeof net.preparatusopos.faces.uploadFile == "undefined")
	net.preparatusopos.faces.uploadFile = {};
if (typeof net.preparatusopos.faces.uploadFile.provider == "undefined")
	net.preparatusopos.faces.uploadFile.provider = {};

net.preparatusopos.faces.uploadFile.provider.googleDrive = function()
{

var GOOGLE_RECOMMENDED_CHUNK_SIZE = 262144;
var MAX_CHUNK_SIZE = 2 * GOOGLE_RECOMMENDED_CHUNK_SIZE;

function start(upload)
{
	var req = new XMLHttpRequest();
	req.open("PATCH", "https://www.googleapis.com" +
			"/upload/drive/v3/files/" + upload.data.id +
			"?uploadType=resumable");
	req.setRequestHeader("Host", "www.googleapis.com");
	req.setRequestHeader("Accept", "application/json");
	req.setRequestHeader("Authorization", "Bearer " + upload.data.accessToken);
	req.setRequestHeader("Content-Type", "application/json; charset=UTF-8");
	req.setRequestHeader("X-Upload-Content-Type", upload.file.type);
	req.setRequestHeader("X-Upload-Content-Length", upload.file.size);
	
	req.addEventListener("load", function()
	{
		if (req.status == 200)
		{
			upload.__location = req.getResponseHeader("Location");
			writeToDrive(upload);
		}
	});
	req.send();
};

function writeToDrive(upload)
{
	var start = 0;
	var chunk = upload.file.slice(start, MAX_CHUNK_SIZE);
	writeChunkToDrive(upload, 0, chunk);
};

function writeChunkToDrive(upload, start, chunk)
{
	var req = new XMLHttpRequest();
	
	req.open("PUT", upload.__location);
	req.setRequestHeader("Host", "www.googleapis.com");
	req.setRequestHeader("Accept", "application/json");
	req.setRequestHeader("Content-Type", upload.file.type);
	req.setRequestHeader("Content-Length", chunk.size);
	req.setRequestHeader("Content-Range", "bytes " + start + "-" +
			(chunk.size - 1) + "/" + upload.file.size);
	
	req.addEventListener("load", function()
	{
		switch (req.status)
		{
			case 200:
			upload.send(upload.file.size);
			break;
			
			case 308:
			var rangeValue = req.getRequestHeader("Range")
			var range = rangeValue.match(/^bytes=(\\d)-(\\d)$/);
			var size = parseInt(range[1]) - parseInt(range[0]) + 1;
			var next = start + size;
			var chunk = upload.file.slice(next, next + MAX_CHUNK_SIZE);
			writeChunkToDrive(upload, next, chunk);
			break;
			
			default:
			console.error("Unhandled upload status ", req.status);
		}
	});
	req.send(chunk);
};

return {
	"start": start
};
}();
