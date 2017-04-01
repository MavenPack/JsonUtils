package com.yeild.common.JsonUtils;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.PrintStream;
import java.util.List;

import org.apache.log4j.Logger;

import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;

public class JsonUtils {
	private static Logger logger = Logger.getLogger(JsonUtils.class.getSimpleName());
	
	public static int parseInt(String numStr) {
		return parseInt(numStr, 0);
	}
	
	public static int parseInt(String numStr, int defaultValue) {
		try {
			return Integer.parseInt(numStr);
		} catch (Exception e) {
			return defaultValue;
		}
	}
	
	public static long parseLong(String numStr) {
		return parseLong(numStr, 0);
	}
	
	public static long parseLong(String numStr, long defaultValue) {
		try {
			return Long.parseLong(numStr);
		} catch (Exception e) {
			return defaultValue;
		}
	}
	
	public static boolean parseBoolean(String numStr) {
		return parseBoolean(numStr, false);
	}
	
	public static boolean parseBoolean(String numStr, boolean defaultValue) {
		try {
			return Boolean.parseBoolean(numStr);
		} catch (Exception e) {
			return defaultValue;
		}
	}
	
    //高位在前，低位在后
    public static long bytesToInt(byte[] bytes){
    	long result=0;
		for(int i=0;i<bytes.length;i++) {
			result |= (long)((bytes[i]&0xff))<<(8*(bytes.length-i-1));
		}
        return result;
    }
    public static long bytesToInt(List<Byte> bytes){
    	long result=0;
		for(int i=0;i<bytes.size();i++) {
			result |= (long)((bytes.get(i)&0xff))<<(8*(bytes.size()-i-1));
		}
        return result;
    }
	
	/**
	 * long 转换为4字节byte
	 * @param num
	 * @return
	 */
    public static byte[] int4ToBytes(long num){
        byte[] result = new byte[4];
        result[0] = (byte)((num >>> 24) & 0xff);
        result[1] = (byte)((num >>> 16)& 0xff );
        result[2] = (byte)((num >>> 8) & 0xff );
        result[3] = (byte)((num >>> 0) & 0xff );
        return result;
    }
	
	/**
	 * long 转换为2字节byte
	 * @param num
	 * @return
	 */
    public static byte[] int2ToBytes(long num){
        byte[] result = new byte[2];
        result[0] = (byte)((num >>> 8) & 0xff );
        result[1] = (byte)((num >>> 0) & 0xff );
        return result;
    }
	
	/**
	 * long 转换为1字节byte
	 * @param num
	 * @return
	 */
    public static byte intToBytes(long num){
        byte result = (byte)((num >>> 0) & 0xff );
        return result;
    }
    
    public static byte[] listToArray(List<Byte> datas) {
    	if(datas == null) {
    		return null;
    	}
    	byte[] result = new byte[datas.size()];
    	for(int i=0;i<datas.size();i++) {
    		result[i] = (byte)(datas.get(i)&0xff);
    	}
    	return result;
    }
	
	public static String BinaryToHexString(List<Byte> bytes) {
        StringBuffer result = new StringBuffer();
		for(byte b:bytes) {
			if(result.length() > 0) {
				result.append(" ");
			}
			String hex = Integer.toHexString(b&0xff);
			result.append((hex.length()<2?"0":"")+hex);
		}
        return result.toString();
    }
	
	public static String BinaryToHexString(byte[] bytes) {
        StringBuffer result = new StringBuffer();
		for(byte b:bytes) {
			if(result.length() > 0) {
				result.append(" ");
			}
			String hex = Integer.toHexString(b&0xff);
			result.append((hex.length()<2?"0":"")+hex);
		}
        return result.toString();
    }
	
	public static String getExceptionInfo(Throwable e){
		return getExceptionInfo(e, true);
	}
	public static String getExceptionInfo(Throwable e, boolean byDetail){
		String msg;
		if(byDetail) {
			ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
			e.printStackTrace(new PrintStream(outputStream));
			msg = outputStream.toString();
		}
		else {
			msg = e.getMessage();
		}
		return msg;
	}
	
	public static String getAppHomePath() {
		String appPath = System.getProperty("workHome");
		File appHomeFile = null;
		if(appPath != null) {
			appHomeFile = new File(appPath);
			if(appHomeFile.exists()) {
				return appPath;
			}
		}
		appHomeFile = new File("../");
		try {
			return appHomeFile.getCanonicalFile().getAbsolutePath();
		} catch (IOException e) {
		}
		return null;
	}

    public static String objToJson(Object obj) {
        try {
            ObjectMapper mapper = new ObjectMapper();
            mapper.disable(SerializationFeature.FAIL_ON_EMPTY_BEANS);
            return mapper.writeValueAsString(obj);
        } catch (JsonProcessingException e) {
			logger.debug(getExceptionInfo(e));
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
			logger.debug(getExceptionInfo(exception));
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
			logger.debug(getExceptionInfo(exception));
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
}
