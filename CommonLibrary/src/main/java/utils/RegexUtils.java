package utils;

import android.text.TextUtils;

public class RegexUtils {
    //判断是否是JSON格式
    public static boolean isJsonString(String json){
        if(TextUtils.isEmpty(json)){
            return false;
        }else if(json.length()<2){
            return false;
        }else{
            return json.startsWith("{")&&json.endsWith("}");
        }
    }
}
