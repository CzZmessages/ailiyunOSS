package com.lenkeng.videoads.util;

import android.content.Context;
import android.os.Handler;
import android.os.Looper;
import android.util.Log;

import androidx.annotation.NonNull;

import com.blankj.utilcode.util.LogUtils;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.reflect.TypeToken;
import com.google.gson.Gson;
import com.lenkeng.videoads.test.ElementData;
import com.xiao.lib.utils.LogUtil;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Attribute;
import org.jsoup.nodes.Attributes;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.nodes.Node;
import org.jsoup.nodes.TextNode;
import org.jsoup.select.Elements;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.lang.reflect.Type;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Queue;
import java.util.Set;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class AcquireTextMessageUtils {


    /**
     * 表示带有属性的文字的类。
     */
    public static class StyledText {
        private final String text;
        private final Map<String, String> attributes;

        public StyledText(String text, Map<String, String> attributes) {
            this.text = text;
            this.attributes = attributes;
        }

        public String getText() {
            return text;
        }

        public Map<String, String> getAttributes() {
            return attributes;
        }
    }

    /**
     * 解析`assets`目录下HTML文件中指定父元素及其所有后代元素的属性，并返回结构化的元素数据列表。
     *    <即将使用的主要方法>
     * @param context           上下文对象
     * @param assetHtmlFileName `assets`目录下HTML文件名
     * @param parentSelector    父元素的CSS选择器
     * @return 结构化的元素数据列表，每个元素数据为一个`ElementData`对象
     */

    public static List<ElementData> parseChildElementsAndAttributes(Context context, String assetHtmlFileName, String parentSelector) {
        try {
            // 从assets目录读取HTML文件内容
            StringBuilder htmlStringBuilder = new StringBuilder();
            BufferedReader reader = new BufferedReader(new InputStreamReader(context.getAssets().open(assetHtmlFileName), StandardCharsets.UTF_8));
            String line;
            while ((line = reader.readLine()) != null) {
                htmlStringBuilder.append(line).append("\n");
            }
            reader.close();

            String htmlContent = htmlStringBuilder.toString();

            // 解析HTML内容为Document对象
            Document doc = Jsoup.parse(htmlContent);

            // 选取指定父元素
            Element parentElement = doc.getElementById(parentSelector);

            if (parentElement != null) {
                // 递归遍历父元素及其所有后代节点，获取结构化的元素数据列表
                return parseElementAndAttributes(parentElement, 0);
            } else {
                Log.w("AcquireTextMessageUtils", "Parent element not found for selector: " + parentSelector);
                return new ArrayList<>();
            }
        } catch (IOException e) {
            Log.e("AcquireTextMessageUtils", "Error reading or parsing HTML file:", e);
            return new ArrayList<>();
        }
    }

    private static List<ElementData> parseElementAndAttributes(Element element, int depth) {
        List<ElementData> elementDataList = new ArrayList<>();
        StringBuilder indent = new StringBuilder();
        for (int i = 0; i < depth; i++) {
            indent.append("\t");
        }

        ElementData elementData = new ElementData();
        elementData.setDepth(depth);
        elementData.setIndent(indent.toString());
        elementData.setTagName(element.tagName());

        Attributes attributes = element.attributes();
        for (Attribute attribute : attributes) {
            elementData.getAttributes().put(attribute.getKey(), attribute.getValue());
        }
        for (Node child : element.childNodes()) {
            if (child instanceof Element) {
                elementData.getChildNodes().addAll(parseElementAndAttributes((Element) child, depth + 1));
            } else if (child instanceof TextNode) {
                // 处理 TextNode
                ElementData otherNodeData = new ElementData();
                otherNodeData.setDepth(depth + 1);
                otherNodeData.setIndent(indent.toString());
                otherNodeData.setTextContent(((TextNode) child).text());
                elementData.getChildNodes().add(otherNodeData);
            } else {
                // 处理其他非 Element 节点（如 CommentNode 等）
                ElementData otherNodeData = new ElementData();
                otherNodeData.setDepth(depth + 1);
                otherNodeData.setIndent(indent.toString());

                // 修改此处，使用合适的文本表示方法而非 outerHtml()
                otherNodeData.setTextContent(child.toString()); // 或者使用 child.nodeName()、child.baseUri() 等，取决于您希望存储的内容

                elementData.getChildNodes().add(otherNodeData);
            }
        }

        elementDataList.add(elementData);

        return elementDataList;
    }



    /**
     * 增加一个方法，从本地DOWNLOAD解析文件
     * 类似于上述方法，由于不需要从本app的文件读取资源文件，所以剔除了context
     * */
    public static List<ElementData> parseChildElementsAndAttributes(String filePath, String parentSelector) {
        try {
            // 从本地路径读取HTML文件内容
            StringBuilder htmlStringBuilder = new StringBuilder();
            FileInputStream fis = new FileInputStream(filePath);
            BufferedReader reader = new BufferedReader(new InputStreamReader(fis, StandardCharsets.UTF_8));
            String line;
            while ((line = reader.readLine()) != null) {
                htmlStringBuilder.append(line).append("\n");
            }
            reader.close();
            fis.close();

            String htmlContent = htmlStringBuilder.toString();

            // 解析HTML内容为Document对象
            Document doc = Jsoup.parse(htmlContent);

            // 选取指定父元素
            Element parentElement = doc.getElementById(parentSelector);
            //对指定的父元素再缓存或者源文件中插入display：none属性
            if (parentElement != null) {
                // 递归遍历父元素及其所有后代节点，获取结构化的元素数据列表
                return parseElementAndAttributes(parentElement, 0);
            } else {
                Log.w("AcquireTextMessageUtils", "Parent element not found for selector: " + parentSelector);
                return new ArrayList<>();
            }
        } catch (IOException e) {
            Log.e("AcquireTextMessageUtils", "Error reading or parsing HTML file:", e);
            return new ArrayList<>();
        }
    }


}