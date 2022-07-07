package org.itstack.demo.design.test;

import org.itstack.demo.design.group.Employee;
import org.itstack.demo.design.group.GroupStructure;
import org.itstack.demo.design.group.Link;
import org.itstack.demo.design.lang.Iterator;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * 迭代器的设计模式从以上的功能实现可以看到，满⾜了单⼀职责和开闭原则，外界的调⽤⽅也不需
 * 要知道任何⼀个不同的数据结构在使⽤上的遍历差异。可以⾮常⽅便的扩展，也让整个遍历变得更
 * 加⼲净整洁。
 * 但从结构的实现上可以看到，迭代器模式的实现过程相对来说是⽐较负责的，类的实现上也扩增了
 * 需要外部定义的类，使得遍历与原数据结构分开。虽然这是⽐较麻烦的，但可以看到在使⽤java的
 * jdk时候，迭代器的模式还是很好⽤的，可以⾮常⽅便扩展和升级。
 * 以上的设计模式场景实现过程可能对新⼈有⼀些不好理解点，包括；迭代器三个和接⼝的定义、树
 * 形结构的数据关系、树结构深度遍历思路。这些都需要反复实现练习才能深⼊的理解，事必躬亲，
 * 亲历亲为，才能让⾃⼰掌握这些知识。
 */
public class ApiTest {

    private Logger logger = LoggerFactory.getLogger(ApiTest.class);

    @Test
    public void test_iterator() {
        GroupStructure groupStructure = new GroupStructure("1", "小傅哥");
        groupStructure.add(new Employee("2", "花花", "二级部门"));
        groupStructure.add(new Employee("3", "豆包", "二级部门"));
        groupStructure.add(new Employee("4", "蹦蹦", "三级部门"));
        groupStructure.add(new Employee("5", "大烧", "三级部门"));
        groupStructure.add(new Employee("6", "虎哥", "四级部门"));
        groupStructure.add(new Employee("7", "玲姐", "四级部门"));
        groupStructure.add(new Employee("8", "秋雅", "四级部门"));

        groupStructure.addLink("1", new Link("1", "2"));
        groupStructure.addLink("1", new Link("1", "3"));

        groupStructure.addLink("2", new Link("2", "4"));
        groupStructure.addLink("2", new Link("2", "5"));

        groupStructure.addLink("5", new Link("5", "6"));
        groupStructure.addLink("5", new Link("5", "7"));
        groupStructure.addLink("5", new Link("5", "8"));

        Iterator<Employee> iterator = groupStructure.iterator();
        while (iterator.hasNext()) {
            Employee employee = iterator.next();
            logger.info("{}，雇员 Id：{} Name：{}", employee.getDesc(), employee.getuId(), employee.getName());
        }

    }

}
