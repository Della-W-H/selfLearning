package com.hong.utilsLearning;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class HttpUtilsResult {

    /**
     * http错误码
     */
    private int code;

    /**
     * 最终结果数据
     */
    private String result;

}