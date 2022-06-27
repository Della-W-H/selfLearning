
package com.hong.SomeThingSimpleButDegraded.Four_TypicalMethodOverLoadUsingOnUtils;


import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum BaseOperateType {

    ADD(1, "添加"),
    DELETE(2, "删除"),
    MODIFY(3, "修改"),
    QUERY(4, "查询");

    @EnumId
    private int type;
    private String desc;
}