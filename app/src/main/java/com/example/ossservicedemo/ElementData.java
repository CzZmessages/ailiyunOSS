package com.lenkeng.videoads.test;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author 陈鹏驰$
 * @version 1.0
 * @description: TODO 表示一个 HTML 元素的数据模型，包括其深度、缩进、标签名称、文本内容、属性以及子节点列表。
 * @date $ $
 */
public class ElementData {
    /**
     * HTML 元素在树结构中的深度，从根节点开始计数。
     */
    private int depth;

    /**
     * 用于表示元素在树结构中位置的缩进字符串，通常用于可视化表示层级关系。
     */
    private String indent;

    /**
     * HTML 元素的标签名称，如 "p"、"span"、"strong" 等。
     */
    private String tagName;

    /**
     * HTML 元素的文本内容，不包括其子节点的文本。
     */
    private String textContent;

    /**
     * HTML 元素的所有属性及其对应的值，以键值对的形式存储在一个 HashMap 中。
     */
    private Map<String, String> attributes;

    /**
     * 存储当前元素所有子节点的列表。子节点也是 ElementData 类型的对象，形成一个树状结构。
     */
    private List<ElementData> childNodes;
    /**
     * 标记文本内容是否被<em>标签包裹。
     */
    private boolean isEmphasized;
    /**
     * 标记文本是否被Strong标签包围
     * */
    private  boolean isStrongPhasized;

    public boolean isStrongPhasized() {
        return isStrongPhasized;
    }

    public void setStrongPhasized(boolean strongPhasized) {
        isStrongPhasized = strongPhasized;
    }

    private ElementData parent;
    // 新增的属性，用于存储继承的属性
    private Map<String, String> inheritedAttributes;

    public Map<String, String> getInheritedAttributes() {
        return inheritedAttributes;
    }

    public void setInheritedAttributes(Map<String, String> inheritedAttributes) {
        this.inheritedAttributes = inheritedAttributes;
    }

    public ElementData getParent() {
        return parent;
    }

    public void setParent(ElementData parent) {
        this.parent = parent;
    }

    public boolean isEmphasized() {
        return isEmphasized;
    }

    public void setEmphasized(boolean emphasized) {
        isEmphasized = emphasized;
    }

    /**
     * 构造一个新的 ElementData 对象，初始化属性和子节点列表。
     */
    public ElementData() {
        this.attributes = new HashMap<>();
        this.childNodes = new ArrayList<>();
    }


    public int getDepth() {
        return depth;
    }

    public void setDepth(int depth) {
        this.depth = depth;
    }

    public String getIndent() {
        return indent;
    }

    public void setIndent(String indent) {
        this.indent = indent;
    }

    public String getTagName() {
        return tagName;
    }

    public void setTagName(String tagName) {
        this.tagName = tagName;
    }

    public String getTextContent() {
        return textContent;
    }

    public void setTextContent(String textContent) {
        this.textContent = textContent;
    }

    public Map<String, String> getAttributes() {
        return attributes;
    }

    public void setAttributes(Map<String, String> attributes) {
        this.attributes = attributes;
    }

    public List<ElementData> getChildNodes() {
        return childNodes;
    }

    public void setChildNodes(List<ElementData> childNodes) {
        this.childNodes = childNodes;
    }
}
