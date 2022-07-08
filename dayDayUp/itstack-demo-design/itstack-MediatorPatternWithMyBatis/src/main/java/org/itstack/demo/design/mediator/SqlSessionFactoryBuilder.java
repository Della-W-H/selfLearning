package org.itstack.demo.design.mediator;

import org.apache.ibatis.builder.xml.XMLMapperEntityResolver;
import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.Element;
import org.dom4j.io.SAXReader;
import org.xml.sax.InputSource;

import java.io.Reader;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

// TODO: 2022/7/8 真的这些就是绝好的学习资料啊 伙计 要烂熟于心啊 这可是你以后 在这个社会上 立足之本啊！！！！！ 、

public class SqlSessionFactoryBuilder {

    /**
     *
     * @param reader 这个reader即 Resources对象根据 classLoader创建出来的 字节流转化后的 字符输入流
     * @return
     */
    public DefaultSqlSessionFactory build(Reader reader) {
        SAXReader saxReader = new SAXReader();
        try {
            //是为了保证在不联网的情况下 可以解析XML 否者 会需要从互联网获得dtd文件
            saxReader.setEntityResolver(new XMLMapperEntityResolver());
            //依据dtd文件标准解析xml文件的结果 即document对象 层层进入源码 发现是 这个对象 又是多态的形式DefaultDocument
            Document document = saxReader.read(new InputSource(reader));

            Configuration configuration = parseConfiguration(document.getRootElement());
            return new DefaultSqlSessionFactory(configuration);
        } catch (DocumentException e) {
            e.printStackTrace();
        }
        return null;
    }

    private Configuration parseConfiguration(Element root) {
        Configuration configuration = new Configuration();
        //Element 解析 dataSource标签 “//”即表示 此不为 类根标签 解析结果为dataSource集合意味着可能有多个dataSource对象可以被解析出来 todo 这即为多个数据源的配置提供了可能？
        configuration.setDataSource(dataSource(root.selectNodes("//dataSource")));
        //根据上一步解析出来的 连接信息 创建连接对象信息
        configuration.setConnection(connection(configuration.dataSource));
        //解析出 mybatis-config-datasource 中说明的 mapper文件信息
        configuration.setMapperElement(mapperElement(root.selectNodes("mappers")));
        return configuration;
    }

    // 获取数据源配置信息
    private Map<String, String> dataSource(List<Element> list) {
        Map<String, String> dataSource = new HashMap<>(4);
        Element element = list.get(0);
        List content = element.content();
        for (Object o : content) {
            Element e = (Element) o;
            String name = e.attributeValue("name");
            String value = e.attributeValue("value");
            dataSource.put(name, value);
        }
        return dataSource;
    }
    //根据获取的数据源配置信息设置 连接创建连接对象
    private Connection connection(Map<String, String> dataSource) {
        try {
            //这个class文件对象 用去哪了？显式的 这个只是将 文件二进制数据另类的读取到内存中了
            Class.forName(dataSource.get("driver"));

            return DriverManager.getConnection(dataSource.get("url"), dataSource.get("username"), dataSource.get("password"));
        } catch (ClassNotFoundException | SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    // 获取SQL语句信息
    private Map<String, XNode> mapperElement(List<Element> list) {
        Map<String, XNode> map = new HashMap<>();

        Element element = list.get(0);
        List content = element.content();
        for (Object o : content) {
            Element e = (Element) o;
            String resource = e.attributeValue("resource");

            try {
                Reader reader = Resources.getResourceAsReader(resource);
                SAXReader saxReader = new SAXReader();
                Document document = saxReader.read(new InputSource(reader));
                Element root = document.getRootElement();
                //命名空间
                String namespace = root.attributeValue("namespace");

                // SELECT
                List<Element> selectNodes = root.selectNodes("select");
                for (Element node : selectNodes) {
                    String id = node.attributeValue("id");
                    String parameterType = node.attributeValue("parameterType");
                    String resultType = node.attributeValue("resultType");
                    //获得select标签中的 sql语句
                    String sql = node.getText();

                    // ? 匹配
                    Map<Integer, String> parameter = new HashMap<>();
                    Pattern pattern = Pattern.compile("(#\\{(.*?)})");
                    Matcher matcher = pattern.matcher(sql);
                    for (int i = 1; matcher.find(); i++) {
                        String g1 = matcher.group(1);
                        String g2 = matcher.group(2);
                        parameter.put(i, g2);
                        sql = sql.replace(g1, "?");

                        System.out.println(sql);
                    }

                    XNode xNode = new XNode();
                    xNode.setNamespace(namespace);
                    xNode.setId(id);
                    xNode.setParameterType(parameterType);
                    xNode.setResultType(resultType);
                    xNode.setSql(sql);
                    xNode.setParameter(parameter);

                    map.put(namespace + "." + id, xNode);
                }
            } catch (Exception ex) {
                ex.printStackTrace();
            }

        }
        return map;
    }

}
