package com.mabo.entity;

import com.mabo.annotation.Alias;
import lombok.Data;

@Data
public class Dept {
    @Alias("d1")
    private String deptCode;
    @Alias("d2")
    private String deptName;
}
