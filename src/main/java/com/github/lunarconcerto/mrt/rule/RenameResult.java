package com.github.lunarconcerto.mrt.rule;

import com.github.lunarconcerto.mrt.util.FileNode;
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
