package net.preparatusopos.app.core.geography.model.internal;

import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.Unmarshaller;

import net.preparatusopos.app.core.geography.model.CountryNode;
import net.preparatusopos.app.core.geography.model.NodeManager;
import net.preparatusopos.app.core.geography.model.RegionNode;
import net.preparatusopos.app.core.geography.model.RouteNode;
import net.preparatusopos.app.domain.CountryRef;
import net.preparatusopos.app.domain.GeographyException;
import net.preparatusopos.app.domain.RegionType;

public class InternalNodeManager
implements NodeManager
{
	private Set<CountryNode> countrySet;
	private Map<String, Set<RegionNode>> regionsMap;
	private Map<String, Set<RouteNode>> routesMap;
	
	public InternalNodeManager()
	{
		countrySet = null;
		regionsMap = new HashMap<>();
		routesMap = new HashMap<>();
	}
	
	@Override
	public Set<CountryNode> countries()
	throws GeographyException
	{
		try
		{
			if (countrySet == null)
			{
				countrySet = new HashSet<>();
				for (CountryData item : loadCountries())
					countrySet.add(item.build(this));
			}
			return countrySet;
		}
		catch (Exception exception)
		{
			throw new GeographyException(exception.getMessage());
		}
	}

	@Override
	public Set<RegionNode> regions(String countryCode)
	throws GeographyException
	{
		try
		{
			Set<RegionNode> regions = regionsMap.get(countryCode);
			if (regions == null)
			{
				regions = new HashSet<>();
				for (RegionType type : RegionType.values())
				{
					for (RegionData item : loadRegions(countryCode, type))
						regions.add(item.build(this, new CountryRef(
								countryCode), type));
				}
				regionsMap.put(countryCode, regions);
			}
			return regions;
		}
		catch (Exception exception)
		{
			throw new GeographyException(exception.getMessage());
		}
	}

	@Override
	public Set<RouteNode> routes(String countryCode)
	throws GeographyException
	{
		try
		{
			Set<RouteNode> routes = routesMap.get(countryCode);
			if (routes == null)
			{
				routes = new HashSet<>();
				for (RouteData item : loadRoutes(countryCode))
					routes.add(item.build(this, new CountryRef(countryCode)));
				routesMap.put(countryCode, routes);
			}
			return routes;
		}
		catch (Exception exception)
		{
			throw new GeographyException(exception.getMessage());
		}
	}
	
	private Set<CountryData> loadCountries()
	throws Exception
	{
		try
		{
			return load(CountriesData.class, "/countries").getItems();
		}
		catch (DataResourceNotFoundException exception)
		{
			return Collections.emptySet();
		}
	}
	
	private Set<RegionData> loadRegions(String countryCode, RegionType type)
	throws Exception
	{
		try
		{
			StringBuilder path = new StringBuilder();
			path.append("/").append(countryCode);
			path.append("/regions-").append(getPath(type));
			return load(RegionsData.class, path.toString()).getItems();
		}
		catch (DataResourceNotFoundException exception)
		{
			return Collections.emptySet();
		}
	}
	
	private Set<RouteData> loadRoutes(String countryCode)
	throws Exception
	{
		try
		{
			StringBuilder path = new StringBuilder();
			path.append("/").append(countryCode);
			path.append("/routes");
			return load(RoutesData.class, path.toString()).getItems();
		}
		catch (DataResourceNotFoundException exception)
		{
			return Collections.emptySet();
		}
	}
	
	private <T> T load(Class<T> type, String path)
	throws Exception
	{
		try (InputStream stream = getDataResourceAsStream(path))
		{
			JAXBContext context = JAXBContext.newInstance(type);
			Unmarshaller unmarshaller = context.createUnmarshaller();
			return type.cast(unmarshaller.unmarshal(stream));
		}
	}
	
	private InputStream getDataResourceAsStream(String path)
	throws DataResourceNotFoundException, IOException
	{
		String dataResBasePath = getDataResourceBasePath(path);
		String dataResFinalPath = String.format("%s.xml", dataResBasePath);
		URL dataRes = getClass().getResource(dataResFinalPath);
		
		if (dataRes == null)
			throw new DataResourceNotFoundException(path);
		return dataRes.openStream();
	}
	
	private String getDataResourceBasePath(String path)
	{
		StringBuilder basePath = new StringBuilder();
		basePath.append("/");
		basePath.append(getClass().getPackage().getName().replace('.', '/'));
		basePath.append(path);
		return basePath.toString();
	}
	
	private String getPath(RegionType type)
	{
		return type.name().toLowerCase().replace('_', '-');
	}
	
	private static class DataResourceNotFoundException
	extends Exception
	{
		private static final long serialVersionUID = 1L;
		
		public DataResourceNotFoundException(String path)
		{
			super(String.format("Data resource not found at %s", path));
		}
	}
}
