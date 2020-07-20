
package com.intellijcloud.lesscode.mybatis.plugins;


import org.mybatis.generator.api.IntrospectedColumn;
import org.mybatis.generator.api.IntrospectedTable;
import org.mybatis.generator.api.PluginAdapter;
import org.mybatis.generator.api.dom.java.FullyQualifiedJavaType;
import org.mybatis.generator.api.dom.java.Interface;
import org.mybatis.generator.api.dom.java.JavaVisibility;
import org.mybatis.generator.api.dom.java.Method;
import org.mybatis.generator.api.dom.java.Parameter;
import org.mybatis.generator.api.dom.java.TopLevelClass;
import org.mybatis.generator.api.dom.xml.Attribute;
import org.mybatis.generator.api.dom.xml.Document;
import org.mybatis.generator.api.dom.xml.TextElement;
import org.mybatis.generator.api.dom.xml.XmlElement;
import org.mybatis.generator.codegen.mybatis3.ListUtilities;
import org.mybatis.generator.codegen.mybatis3.MyBatis3FormattingUtilities;

import java.util.List;
import java.util.StringJoiner;

public class BatchInsertPlugin extends PluginAdapter {

    private final static String MTD_NAME= "batchInsert";
    public boolean validate(List<String> warnings) {
        return true;
    }
    @Override
    public boolean clientGenerated(Interface interfaze, TopLevelClass topLevelClass, IntrospectedTable introspectedTable) {
        FullyQualifiedJavaType paramType = FullyQualifiedJavaType.getNewListInstance();
        paramType.addTypeArgument(introspectedTable.getRules().calculateAllFieldsClass());
        Method method = new Method(MTD_NAME);
        method.setVisibility(JavaVisibility.PUBLIC);

        method.addParameter(new Parameter(paramType,"list","@Param(\"list\")"));
        method.setReturnType(FullyQualifiedJavaType.getIntInstance());
        interfaze.addImportedType(paramType);
        interfaze.addImportedType(new FullyQualifiedJavaType("org.apache.ibatis.annotations.Param"));
        interfaze.addMethod(method);
        return true;

    }

    @Override
    public boolean sqlMapDocumentGenerated(Document document, IntrospectedTable introspectedTable) {
        XmlElement batchInsertEle = new XmlElement("insert");
        batchInsertEle.addAttribute(new Attribute("id", MTD_NAME));
        batchInsertEle.addAttribute(new Attribute("parameterType", "map"));
        batchInsertEle.addElement(new TextElement("insert into " + introspectedTable.getFullyQualifiedTableNameAtRuntime()));


        List<IntrospectedColumn> introspectedColumns = ListUtilities.removeGeneratedAlwaysColumns(introspectedTable.getAllColumns());

        StringJoiner sjc = new StringJoiner(",","(",")");
        for (IntrospectedColumn introspectedColumn :introspectedColumns ) {
            sjc.add(introspectedColumn.getActualColumnName());
        }
        batchInsertEle.addElement(new TextElement(sjc.toString()));
        batchInsertEle.addElement(new TextElement("values"));

        XmlElement foreachElement = new XmlElement("foreach");
        foreachElement.addAttribute(new Attribute("collection", "list"));
        foreachElement.addAttribute(new Attribute("item", "item"));
        foreachElement.addAttribute(new Attribute("separator", ","));

        StringJoiner sjv = new StringJoiner(",","(",")");
        for (IntrospectedColumn introspectedColumn :introspectedColumns) {
            sjv.add(MyBatis3FormattingUtilities
                    .getParameterClause(introspectedColumn,"item."));
        }
        foreachElement.addElement(new TextElement(sjv.toString()));
        batchInsertEle.addElement(foreachElement);
        document.getRootElement().addElement(batchInsertEle);
        return true;
    }


}
