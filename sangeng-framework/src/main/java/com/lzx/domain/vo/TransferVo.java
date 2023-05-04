package com.lzx.domain.vo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

/**
 * @Author: lin
 * @Description: TODO
 * @Version: 1.0
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class TransferVo {
    private List<AdminAddRoleTreeVo> children;
    private Long id;
    private String label;
    private String menuName;
    //菜单名称
    private Long parentId;
}
