package fr.llama.server.dto;

import java.beans.BeanInfo;
import java.beans.IntrospectionException;
import java.beans.Introspector;
import java.beans.PropertyDescriptor;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import java.util.Map.Entry;

import org.json.JSONObject;

public class AbstractDto {
	
	public Map<String, Object> toMap() throws IntrospectionException, IllegalAccessException, IllegalArgumentException, InvocationTargetException {
		Map<String, Object> result = new HashMap<String, Object>();
	    BeanInfo info = Introspector.getBeanInfo(this.getClass());
	    for (PropertyDescriptor pd : info.getPropertyDescriptors()) {
	        Method reader = pd.getReadMethod();
	        if (reader != null && !pd.getName().equals("class")) {
	        	Object val = reader.invoke(this);
	        	if (val instanceof Boolean)
	        		result.put(pd.getName(), (boolean)val == true ? 1 : 0);
	        	else
	        		result.put(pd.getName(), val);
	        }
	    }
	    return result;
	}
	
	public AbstractDto fromMap(Map<String, Object> map) throws IllegalArgumentException, IllegalAccessException, NoSuchFieldException, SecurityException {
		if (map == null)
			return null;
		Set<Entry<String, Object>> entries = map.entrySet();
		for (Entry<String, Object> entry : entries) {
			Field field = getClass().getDeclaredField(entry.getKey());
		    field.set(this, entry.getValue());
		}
		return this;
	}
	
	public String toString() {
		Map<String, Object> map = null;
		try {
			map = this.toMap();
		} catch (IllegalAccessException | IllegalArgumentException
				| InvocationTargetException | IntrospectionException e) {
			e.printStackTrace();
		}
		return (new JSONObject(this, Arrays.copyOf(map.values().toArray(), map.values().toArray().length, String[].class)).toString());
	}
}
