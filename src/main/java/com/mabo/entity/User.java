package com.mabo.entity;

import com.mabo.annotation.Alias;
import lombok.Data;
import java.util.List;

@Data
public class User {
    @Alias("u1")
    private String name;
    @Alias("u2")
    private String age;
    @Alias("u3")
    private com.mabo.entity.Dept dept;
    @Alias("u4")
    private List<com.mabo.entity.Score> scores;
}
