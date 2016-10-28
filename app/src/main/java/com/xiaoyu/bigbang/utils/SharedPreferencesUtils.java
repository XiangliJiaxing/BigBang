package com.xiaoyu.bigbang.utils;

import android.content.Context;
import android.content.SharedPreferences;

/** 
 * SharedPreferences的一个工具类，调用setParam就能保存String, Integer, Boolean, Float, Long类型的参数 
 * 同样调用getParam就能获取到保存在手机里面的数据
 *
 *  请先使用Init()
 *@author linkeer
 */  
public class SharedPreferencesUtils {  
    /** 
     * 保存在手机里面的文件名 
     */  
    private static final String FILE_NAME = "com.xiaoyu.bigbang";
    /**
     * 保存是否已经开启Service 键名 isOpen
     */
    public static final String KEY = "isOpen";

    private static SharedPreferences sp;


    public static void init(Context context){
        if( sp == null){
            sp = context.getSharedPreferences(FILE_NAME, Context.MODE_PRIVATE);
        }
    }

    /**
     * 保存数据的方法，我们需要拿到保存数据的具体类型，然后根据类型调用不同的保存方法
     * @param key
     * @param object
     */  
    public static void setParam(String key, Object object){

        String type = object.getClass().getSimpleName();  
        SharedPreferences.Editor editor = sp.edit();
          
        if("String".equals(type)){  
            editor.putString(key, (String)object);  
        }  
        else if("Integer".equals(type)){  
            editor.putInt(key, (Integer)object);  
        }  
        else if("Boolean".equals(type)){  
            editor.putBoolean(key, (Boolean)object);  
        }  
        else if("Float".equals(type)){  
            editor.putFloat(key, (Float)object);  
        }  
        else if("Long".equals(type)){  
            editor.putLong(key, (Long)object);  
        }  
          
        editor.commit();
    }
      
      
    /** 
     * 得到保存数据的方法，我们根据默认值得到保存的数据的具体类型，然后调用相对于的方法获取值 
     * @param key
     * @param defaultObject 
     * @return Object
     */  
    public static Object getParam(String key, Object defaultObject){

        String type = defaultObject.getClass().getSimpleName();

        if("String".equals(type)){  
            return sp.getString(key, (String)defaultObject);  
        }  
        else if("Integer".equals(type)){  
            return sp.getInt(key, (Integer)defaultObject);  
        }  
        else if("Boolean".equals(type)){  
            return sp.getBoolean(key, (Boolean)defaultObject);  
        }  
        else if("Float".equals(type)){  
            return sp.getFloat(key, (Float)defaultObject);  
        }  
        else if("Long".equals(type)){  
            return sp.getLong(key, (Long)defaultObject);  
        }  
          
        return null;  
    }  
}  