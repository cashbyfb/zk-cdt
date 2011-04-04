package org.zkoss.eclipse.template;

import java.io.StringWriter;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;

import org.apache.velocity.Template;
import org.apache.velocity.VelocityContext;
import org.apache.velocity.app.Velocity;
import org.apache.velocity.exception.ParseErrorException;
import org.apache.velocity.exception.ResourceNotFoundException;

public abstract class AbstractTemplate implements ITemplateBuilder {

	@SuppressWarnings({ "rawtypes" })
	protected String generator(HashMap args) {

		VelocityContext context = new VelocityContext();

		if (args != null) {
			for (Object o : args.entrySet()) {
				Map.Entry e = (Map.Entry) o;
				context.put((String) e.getKey(), e.getValue());
			}
		}

		try {
			return fillTemplate(context, getTemplateName());
		} catch (ResourceNotFoundException rnfe) {
			throw new IllegalArgumentException(
					"Example : error : cannot find template ", rnfe);
		} catch (ParseErrorException pee) {
			throw new IllegalArgumentException(
					"Example : Syntax error in template:" + pee, pee);
		} catch (Exception pee) {
			throw new IllegalArgumentException("Error when reading template:"
					+ pee, pee);
		}

	}

	@SuppressWarnings({ "rawtypes", "unchecked" })
	protected void putWithoutNull(HashMap map, String key, Object value) {
		if (value != null)
			map.put(key, value);
	}

	/**
	 * Since we would never change the properties , I think we don't need to
	 * wrtie a config file for it, use proprties to config it directly.
	 */
	private static Properties prop = null;
	static {
		prop = new Properties();
		prop.put("resource.loader", "class");
		prop.put("class.resource.loader.class",
				"org.apache.velocity.runtime.resource.loader.ClasspathResourceLoader");
	}

	protected String fillTemplate(VelocityContext context, String templateName)
			throws Exception {

		Velocity.init(prop);

		Template template = Velocity.getTemplate(templateName);

		StringWriter writer = new StringWriter();

		if (template != null)
			template.merge(context, writer);

		writer.flush();
		return writer.getBuffer().toString();
	}

	public abstract String getTemplateName();

	/**
	 * @throws IllegalArgumentException
	 *             throws up when invoking a illegal template or file not found
	 */
	public String build() {
		return generator(getPreparedVariables());
	}

}
