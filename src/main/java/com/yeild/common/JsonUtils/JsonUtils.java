package com.yeild.common.JsonUtils;

import java.io.IOException;

import org.apache.logging.log4j.Logger;
import org.apache.logging.log4j.LogManager;

import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.yeild.common.Utils.CommonUtils;

public class JsonUtils {
	private static Logger logger = LogManager.getLogger(JsonUtils.class);

    public static String objToJson(Object obj) {
        try {
            ObjectMapper mapper = new ObjectMapper();
            mapper.disable(SerializationFeature.FAIL_ON_EMPTY_BEANS);
    		mapper.setSerializationInclusion(Include.NON_DEFAULT);
            return mapper.writeValueAsString(obj);
        } catch (JsonProcessingException e) {
			logger.debug(CommonUtils.getExceptionInfo(e));
        }
        return null;
    }

    public static <T> T jsonToObj(String json, Class<T> cls) {
        Exception exception = null;
        try {
            ObjectMapper mapper = new ObjectMapper();
            mapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
            mapper.enable(JsonParser.Feature.ALLOW_COMMENTS);
            return mapper.readValue(json, cls);
        } catch (JsonParseException e) {
            exception = e;
        } catch (JsonMappingException e) {
            exception = e;
        } catch (IOException e) {
            exception = e;
        }
        if (exception != null) {
			logger.debug(CommonUtils.getExceptionInfo(exception));
        }
        return null;
    }

    public static <T> T jsonToObj(String json, JavaType javaType) {
        Exception exception = null;
        try {
            ObjectMapper mapper = new ObjectMapper();
            mapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
            mapper.enable(JsonParser.Feature.ALLOW_COMMENTS);
            return mapper.readValue(json, javaType);
        } catch (JsonParseException e) {
            exception = e;
        } catch (JsonMappingException e) {
            exception = e;
        } catch (IOException e) {
            exception = e;
        }
        if (exception != null) {
			logger.debug(CommonUtils.getExceptionInfo(exception));
        }
        return null;
    }

    /**
     * json to Map or List
     * @param json  json data
     * @param collectionClass   the type of Map or List
     * @param elementClass the type of element within Map or List
     * @return
     */
    public static <T> T jsonToObj(String json, Class<?> collectionClass, Class<?>... elementClass) {
        return jsonToObj(json, getCollectionJsonType(collectionClass, elementClass));
    }

    public static JavaType getCollectionJsonType(Class<?> collectionClass, Class<?>... elementClass) {
        return new ObjectMapper().getTypeFactory().constructParametricType(collectionClass, elementClass);
    }

    public static <T> T castTo(Object source, Class<?> collectionClass, Class<?>... elementClass) {
        ObjectMapper mapper = new ObjectMapper();
        return mapper.convertValue(source, getCollectionJsonType(collectionClass, elementClass));
    }
    
    public static <T> T castTo(Object source, Class<T> destination) {
    	ObjectMapper mapper = new ObjectMapper();
    	return mapper.convertValue(source, destination);
    }
}
