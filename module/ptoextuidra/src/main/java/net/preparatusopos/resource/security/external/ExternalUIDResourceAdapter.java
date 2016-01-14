package net.preparatusopos.resource.security.external;

import java.io.Serializable;

import javax.resource.ResourceException;
import javax.resource.spi.ActivationSpec;
import javax.resource.spi.BootstrapContext;
import javax.resource.spi.Connector;
import javax.resource.spi.ResourceAdapter;
import javax.resource.spi.ResourceAdapterInternalException;
import javax.resource.spi.TransactionSupport.TransactionSupportLevel;
import javax.resource.spi.endpoint.MessageEndpointFactory;
import javax.transaction.xa.XAResource;

@Connector(
	displayName="ExternalIDResourceAdapter",
	description="PTO External ID Adapter",
	vendorName="PTO",
	eisType="PTOEXTID",
	version="0.1",
	transactionSupport=TransactionSupportLevel.XATransaction
)
public class ExternalUIDResourceAdapter
implements ResourceAdapter, Serializable
{
	private static final long serialVersionUID = 1L;

	public ExternalUIDResourceAdapter()
	{
	}
	
	@Override
	public void start(BootstrapContext ctx)
	throws ResourceAdapterInternalException
	{
	}

	@Override
	public void stop()
	{
	}

	@Override
	public XAResource[] getXAResources(ActivationSpec[] specs)
	throws ResourceException
	{
		return null;
	}
	
	@Override
	public void endpointActivation(MessageEndpointFactory endpointFactory,
			ActivationSpec spec)
	throws ResourceException
	{
	}

	@Override
	public void endpointDeactivation(MessageEndpointFactory endpointFactory,
			ActivationSpec spec)
	{
	}
}