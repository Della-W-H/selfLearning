package com.hong.NormalUseOfHttp.springBoot_RestTemplate.module;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

/**
 * @author wanghong
 * @date 2022/9/9
 * @apiNote
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class User{
    private String name;
    private int age;
}
