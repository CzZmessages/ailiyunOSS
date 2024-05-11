package com.lenkeng.videoads.test;

import android.content.Context;
import android.util.DisplayMetrics;
import android.util.Log;

import com.lenkeng.videoads.util.AcquireTextMessageUtils;

import java.util.ArrayList;
import java.util.List;

/**
 * @author chenpengchi$
 * @version 1.0
 * @description: TODO
 * @date $ $
 */
public class HTMLMessageExtractor {
    /**
     * 从HTML文件中提取带有样式信息的文本内容。
     *
     * @param context         应用上下文
     * @param parentSelector  HTML文件名，位于`assets`目录下
     * @param parentSelector  父元素的CSS选择器
     * @param styleTextUpdate 样式文本更新对象，用于处理文本内容和样式
     * @param displayMetrics  设备的显示指标
     * @return 包含提取信息的日志消息列表
     */


    public static ArrayList<String> getHtmlMessage(Context context, String filePath, String parentSelector, StyleTextUpdate styleTextUpdate, DisplayMetrics displayMetrics) {
        List<ElementData> elementDataList = AcquireTextMessageUtils.parseChildElementsAndAttributes(context, filePath, parentSelector);
        return collectElementDataRecursively(elementDataList, new ArrayList<>(), false, false, styleTextUpdate, displayMetrics);
    }

    public static ArrayList<String> getHtmlMessage(String filePath, String parentSelector, StyleTextUpdate styleTextUpdate, DisplayMetrics displayMetrics) {
        List<ElementData> elementDataList = AcquireTextMessageUtils.parseChildElementsAndAttributes(filePath, parentSelector);
        return collectElementDataRecursively(elementDataList, new ArrayList<>(), false, false, styleTextUpdate, displayMetrics);
    }

    private static ArrayList<String> collectElementDataRecursively(List<ElementData> elementDataList, List<String> accumulatedAttributes, boolean isParentInsideEm, boolean isParentStrong, StyleTextUpdate styleTextUpdate, DisplayMetrics displayMetrics) {
        ArrayList<String> logMessages = new ArrayList<>();

        for (ElementData elementData : elementDataList) {
            // 合并当前元素的属性到累积属性中
            List<String> combinedAttributes = new ArrayList<>(accumulatedAttributes);
            combinedAttributes.add(elementData.getAttributes().get("style"));

            // 获取文本内容，如果为空，则使用"无文本内容"代替
            String textContent = elementData.getTextContent() != null ? elementData.getTextContent() : "";
            // 确定当前节点是否被强调
            boolean isInsideEm = isParentInsideEm || ("em".equalsIgnoreCase(elementData.getTagName()));
            //确定当前的节点是否要被加粗
            boolean isStrong = isParentStrong || ("strong".equalsIgnoreCase(elementData.getTagName()));
            // 构建属性字符串
            StringBuilder attrStrBuilder = new StringBuilder();

            for (String entry : combinedAttributes) {
                attrStrBuilder.append(entry).append("; ");
            }
            if (attrStrBuilder.length() > 0) {
                attrStrBuilder.setLength(attrStrBuilder.length() - 2); // 移除最后一个分号和空格
            }
            String logMessagess = "   深度" + elementData.getDepth() + "   TagName：[" + elementData.getTagName() + "]    文本内容: {" + textContent + "}; 属性: " + attrStrBuilder + (isInsideEm ? "<em>" : "") + "     " + (isStrong ? "<strong>" : "");//+" <--->  "+attrStrBuilder+(isStrong?"<strong>":"")
//            Log.d("AcquireTextMessageUtils", "AcquireTextMessageUtils data:" + logMessagess);
            int startIndex = styleTextUpdate.getLength();
//            styleTextUpdate.appendContent(textContent);
            // 收集逻辑，包括强调状态
            if (!textContent.isEmpty()) {
                String logMessage = "   深度" + elementData.getDepth() + "   TagName：[" + elementData.getTagName() + "]    文本内容: {" + textContent + "}; 属性: " + attrStrBuilder + (isInsideEm ? "<em>" : "") + "     " + (isStrong ? "<strong>" : "");
                Log.d("AcquireTextMessageUtils", "AcquireTextMessageUtils data:" + logMessage);
                styleTextUpdate.enqueueLogContent(textContent, combinedAttributes, isInsideEm, isStrong, displayMetrics);
                logMessages.add(logMessage);
            }

            // 递归调用，传递累积的属性和当前的强调状态，并收集返回的日志消息
            if (elementData.getChildNodes() != null && !elementData.getChildNodes().isEmpty()) {
                ArrayList<String> childMessages = collectElementDataRecursively(elementData.getChildNodes(), combinedAttributes, isInsideEm, isStrong, styleTextUpdate, displayMetrics);
                logMessages.addAll(childMessages);
            }
            int endIndex = styleTextUpdate.getLength();

        }

        return logMessages;
    }

}
