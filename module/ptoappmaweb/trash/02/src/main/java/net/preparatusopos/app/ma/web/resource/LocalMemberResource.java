package net.preparatusopos.app.ma.web.resource;

import java.io.IOException;
import java.util.Map.Entry;

import javax.annotation.Resource;
import javax.ejb.EJB;
import javax.enterprise.context.RequestScoped;
import javax.json.Json;
import javax.json.JsonObjectBuilder;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.Status;

import net.preparatusopos.app.domain.Membership;
import net.preparatusopos.app.domain.MembershipException;
import net.preparatusopos.tools.file.FileStorageConnection;
import net.preparatusopos.tools.file.FileStorageConnectionFactory;
import net.preparatusopos.tools.file.FileStorageGrant;

@RequestScoped
@Path("/")
public class LocalMemberResource
{
	@EJB(
		name="ejb/MembershipBean"
	)
	private Membership membership;
	
	@Resource(
		lookup="file/PTOGoogleDriveFileStorage"
	)
	private FileStorageConnectionFactory fileStgGoogleDriveFact;
	
	public LocalMemberResource()
	{
	}
	
	@Path("/photo")
	@POST
	@Produces(MediaType.APPLICATION_JSON)
	public Response doPostPhoto()
	{
		try (FileStorageConnection conn =
				fileStgGoogleDriveFact.getConnection())
		{
			FileStorageGrant grant = conn.store();
			JsonObjectBuilder dataBuilder = Json.createObjectBuilder();
			for (Entry<String, Object> entry : grant.getData().entrySet())
				dataBuilder.add(entry.getKey(), String.valueOf(
						entry.getValue()));
			
			JsonObjectBuilder builder = Json.createObjectBuilder();
			builder.add("member", getMemberRef());
			if (grant.getRef() == null)
				builder.addNull("ref");
			else
				builder.add("ref", grant.getRef().toASCIIString());
			builder.add("data", dataBuilder.build());
			
			return Response.ok(builder.build()).build();
		}
		catch (MembershipException | IOException exception)
		{
			return Response
				.status(Status.CONFLICT)
				.entity(exception.getMessage())
				.build();
		}
	}
	
	private String getMemberRef()
	throws MembershipException
	{
		return membership.fetchMemberInfo().getRef();
	}
}
