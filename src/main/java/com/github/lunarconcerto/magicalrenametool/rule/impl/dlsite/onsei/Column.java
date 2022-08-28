package com.github.lunarconcerto.magicalrenametool.rule.impl.dlsite.onsei;

import javafx.scene.control.ComboBox;
import javafx.scene.control.TextField;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class Column {

    private TextField prefix , suffix ;

    private ComboBox<Element> element ;

}
