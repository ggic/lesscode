package com.intellijcloud.lesscode.mybatis.plugins;

import java.util.List;
import org.mybatis.generator.api.IntrospectedColumn;
import org.mybatis.generator.api.IntrospectedTable;
import org.mybatis.generator.api.PluginAdapter;
import org.mybatis.generator.api.dom.java.FullyQualifiedJavaType;
import org.mybatis.generator.api.dom.java.Method;
import org.mybatis.generator.api.dom.java.TopLevelClass;
import java.util.*;


public class LombokPlugin extends PluginAdapter {

    private final Collection<Annotations> annotations =new LinkedHashSet<Annotations>(Annotations.values().length);

    /**
     * @param warnings list of warnings
     * @return always true
     */
    public boolean validate(List<String> warnings) {
        return true;
    }

    /**
     * Intercepts base record class generation
     *
     * @param topLevelClass     the generated base record class
     * @param introspectedTable The class containing information about the table as
     *                          introspected from the database
     * @return always true
     */
    @Override
    public boolean modelBaseRecordClassGenerated(
            TopLevelClass topLevelClass,
            IntrospectedTable introspectedTable
    ) {
        addAnnotations(topLevelClass);
        return true;
    }

    /**
     * Intercepts primary key class generation
     *
     * @param topLevelClass     the generated primary key class
     * @param introspectedTable The class containing information about the table as
     *                          introspected from the database
     * @return always true
     */
    @Override
    public boolean modelPrimaryKeyClassGenerated(
            TopLevelClass topLevelClass,
            IntrospectedTable introspectedTable
    ) {
        addAnnotations(topLevelClass);
        return true;
    }

    /**
     * Intercepts "record with blob" class generation
     *
     * @param topLevelClass     the generated record with BLOBs class
     * @param introspectedTable The class containing information about the table as
     *                          introspected from the database
     * @return always true
     */
    @Override
    public boolean modelRecordWithBLOBsClassGenerated(
            TopLevelClass topLevelClass,
            IntrospectedTable introspectedTable
    ) {
        addAnnotations(topLevelClass);
        return true;
    }

    @Override
    public boolean modelGetterMethodGenerated(
            Method method,
            TopLevelClass topLevelClass,
            IntrospectedColumn introspectedColumn,
            IntrospectedTable introspectedTable,
            ModelClassType modelClassType
    ) {
        return false;
    }


    @Override
    public boolean modelSetterMethodGenerated(
            Method method,
            TopLevelClass topLevelClass,
            IntrospectedColumn introspectedColumn,
            IntrospectedTable introspectedTable,
            ModelClassType modelClassType
    ) {
        return false;
    }

    /**
     * Adds the lombok annotations' imports and annotations to the class
     *
     * @param topLevelClass the partially implemented model class
     */
    private void addAnnotations(TopLevelClass topLevelClass) {
        for (Annotations annotation : annotations) {
            topLevelClass.addImportedType(annotation.javaType);
            topLevelClass.addAnnotation(annotation.name);
        }
    }


    @Override
    public void setProperties(Properties properties) {
        super.setProperties(properties);
        //@Data is default annotation
        for (String annotationName : properties.stringPropertyNames()) {
            String value = properties.getProperty(annotationName);
            if (!Boolean.parseBoolean(value)) {
                continue;
            }
            Annotations annotation = Annotations.getValueOf(annotationName);
            if (annotation == null) {
                continue;
            }
            annotations.add(annotation);
        }
    }

    private enum Annotations {
        DATA("@Data", "lombok.Data"),
        BUILDER("@Builder", "lombok.Builder"),
        ALL_ARGS_CONSTRUCTOR( "@AllArgsConstructor", "lombok.AllArgsConstructor"),
        NO_ARGS_CONSTRUCTOR("@NoArgsConstructor", "lombok.NoArgsConstructor"),
        ACCESSORS("@Accessors", "lombok.experimental.Accessors"),
        TO_STRING("@ToString", "lombok.ToString");

        private final String name;
        private final FullyQualifiedJavaType javaType;

        Annotations(String name, String className) {
            this.name = name;
            this.javaType = new FullyQualifiedJavaType(className);
        }

        private static Annotations getValueOf(String name) {
            for (Annotations annotation : Annotations.values())
                if (String.CASE_INSENSITIVE_ORDER.compare(name, annotation.name) == 0)
                    return annotation;

            return null;
        }
    }
}