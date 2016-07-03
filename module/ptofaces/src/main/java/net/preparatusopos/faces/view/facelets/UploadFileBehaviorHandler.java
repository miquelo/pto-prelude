package net.preparatusopos.faces.view.facelets;

import java.util.ArrayList;
import java.util.Collection;

import javax.el.MethodExpression;
import javax.faces.event.AbortProcessingException;
import javax.faces.view.facelets.BehaviorConfig;
import javax.faces.view.facelets.BehaviorHandler;
import javax.faces.view.facelets.FaceletContext;
import javax.faces.view.facelets.MetaRule;
import javax.faces.view.facelets.MetaRuleset;
import javax.faces.view.facelets.Metadata;
import javax.faces.view.facelets.MetadataTarget;
import javax.faces.view.facelets.TagAttribute;

import net.preparatusopos.faces.component.behavior.UploadFileBehavior;
import net.preparatusopos.faces.event.UploadFileBehaviorEvent;
import net.preparatusopos.faces.event.UploadFileBehaviorListener;

public class UploadFileBehaviorHandler extends BehaviorHandler
{
	public UploadFileBehaviorHandler(BehaviorConfig config)
	{
		super(config);
	}
	
	@SuppressWarnings("rawtypes")
	@Override
	protected MetaRuleset createMetaRuleset(Class type)
	{
		return super.createMetaRuleset(type).addRule(
			new MetaRule()
			{
				@Override
				public Metadata applyRule(String name, TagAttribute attribute,
						MetadataTarget meta)
				{
					switch (name)
					{
						case "listener":
						return new ListenerMetadata(attribute);
						
						case "render":
						return new RenderMetadata(attribute);
						
						default:
						return null;
					}
				}
			});
	}
	
	private static class ListenerMetadata
	extends Metadata
	{
		private TagAttribute attribute;
		
		public ListenerMetadata(TagAttribute attribute)
		{
			this.attribute = attribute;
		}

		@Override
		public void applyMetadata(FaceletContext context, Object instance)
		{
			if (instance instanceof UploadFileBehavior)
				applyMetadata(context, (UploadFileBehavior) instance);
		}
		
		private void applyMetadata(FaceletContext context,
				UploadFileBehavior behavior)
		{
			MethodExpression method = attribute.getMethodExpression(context,
					void.class, new Class<?>[] {
				UploadFileBehaviorEvent.class
			});
			behavior.addUploadFileBehaviorListener(
					new MethodExpressionBehaviorListener(context, method));
		}
	}
	
	private static class RenderMetadata
	extends Metadata
	{
		private TagAttribute attribute;
		
		protected RenderMetadata(TagAttribute attribute)
		{
			this.attribute = attribute;
		}
		
		@Override
		public void applyMetadata(FaceletContext context, Object instance)
		{
			if (instance instanceof UploadFileBehavior)
				applyMetadata(context, (UploadFileBehavior) instance);
		}
		
		private void applyMetadata(FaceletContext context,
				UploadFileBehavior behavior)
		{
			Collection<String> render = new ArrayList<>();
			if (attribute.isLiteral())
			{
				String value = attribute.getValue();
				for (String item : value.split("\\s"))
					render.add(item);
			}
			else
				for (Object item : (Collection<?>) attribute.getObject(context,
						Collection.class))
					render.add(item.toString());
			behavior.setRender(render);
		}
	}
	
	private static class MethodExpressionBehaviorListener
	implements UploadFileBehaviorListener
	{
		private FaceletContext context;
		private MethodExpression method;
		
		public MethodExpressionBehaviorListener(FaceletContext context,
				MethodExpression method)
		{
			this.context = context;
			this.method = method;
		}
		
		@Override
		public void processFileUpload(UploadFileBehaviorEvent event)
		throws AbortProcessingException
		{
			method.invoke(context, new Object[] {
				event
			});
		}
	}
}
