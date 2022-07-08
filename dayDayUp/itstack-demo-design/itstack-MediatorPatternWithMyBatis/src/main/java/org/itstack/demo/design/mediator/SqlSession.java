package org.itstack.demo.design.mediator;

import java.util.List;

public interface SqlSession/*<T>*/ {

    // TODO: 2022/7/8 还记得 T 泛型 要在类名中申明的 说法吗？

    <T> T selectOne(String statement);

    <T> T selectOne(String statement, Object parameter);

    <T> List<T> selectList(String statement);

    <T> List<T> selectList(String statement, Object parameter);

    void close();
}
