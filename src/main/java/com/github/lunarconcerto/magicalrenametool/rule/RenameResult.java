package com.github.lunarconcerto.magicalrenametool.rule;

import com.github.lunarconcerto.magicalrenametool.util.FileNode;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class RenameResult {

    protected FileNode source ;

    protected String targetName ;

}
