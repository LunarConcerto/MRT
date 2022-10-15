package com.github.lunarconcerto.mrt.gui;

import com.github.lunarconcerto.mrt.MRTStarter;
import com.github.lunarconcerto.mrt.exc.MRTRuntimeException;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.fxml.FXML;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.control.Label;
import javafx.scene.control.Pagination;
import javafx.scene.control.TextArea;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.text.TextFlow;
import javafx.stage.Stage;
import javafx.util.Callback;

import java.io.IOException;
import java.util.Arrays;
import java.util.Objects;

public class MRTHelpPaneController extends AnchorPane {

    static boolean exist ;

    @FXML
    protected Pagination pagination ;

    static final String[] DESCRIPTION = {
            """
             本程序是一款对计算机的文件/文件夹进行特定规则的重命名的程序，因此，使用本程序的第一步
             你需要通过"打开..."按钮来定位到一个某个你想对其中的文件/文件夹进行重命名的路径。
             其他菜单项目解释:
                打开默认的路径 : 在设置中可以设置默认路径，每次打开程序会首先打开默认路径，
                             点击该菜单项可跳转回默认路径
                打开最近的路径 : 在该菜单中可以快速打开前十个最近打开过的路径。
                设置...      : 打开设置菜单
                退出         : 退出程序
            """,
            "",
            "",
            "",
            ""
    };

    void init(){
        pagination.setPageFactory(param -> {
            VBox box = new VBox();
            try {
                Image image = new Image(Objects.requireNonNull(MRTStarter.class.getResourceAsStream("img/help" + (param+1) + ".png")));

                box.setAlignment(Pos.BASELINE_CENTER);
                box.getChildren().add(new ImageView(image));

                VBox textBox = new VBox();
                textBox.setAlignment(Pos.BASELINE_LEFT);
                String[] strings = DESCRIPTION[param].split("\n");
                Arrays.stream(strings).forEachOrdered(string -> textBox.getChildren().add(new Label(string)));

                box.getChildren().add(textBox);
            }catch (Exception e){
                return box ;
            }
            return box ;
        });
    }


    public static void showWindow(){
        if (exist){
            return;
        }
        try {
            MRTHelpPaneController controller = new MRTHelpPaneController();
            Parent parent = ControllerUtil.loadWindow(controller, "mrt.help.fxml");
            controller.init();
            Stage stage = ControllerUtil.newStage(parent, "帮助", 600, 600);
            stage.setOnCloseRequest(e -> exist = false);
            exist = true ;
            stage.show();
        }catch (Exception e){
            throw new MRTRuntimeException(e);
        }
    }

}
