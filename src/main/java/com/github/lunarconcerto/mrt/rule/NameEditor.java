package com.github.lunarconcerto.mrt.rule;

import com.github.lunarconcerto.mrt.component.RenameTargetContainer;

public interface NameEditor {

    void doEdit(RenameTargetContainer builder);

    default EditorRuntime getEditorRuntime(){
        return EditorRuntime.POST ;
    }

    enum EditorRuntime {

        PRE ,

        POST

    }

}
