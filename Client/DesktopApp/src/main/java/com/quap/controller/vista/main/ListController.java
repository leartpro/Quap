package com.quap.controller.vista.main;

import com.quap.client.Client;
import com.quap.client.domain.UserContent;
import com.quap.controller.SceneController;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.ContextMenu;
import javafx.scene.control.ListCell;
import javafx.scene.control.ListView;
import javafx.scene.control.MenuItem;
import javafx.stage.Stage;
import javafx.stage.Window;
import javafx.util.Callback;

import java.util.List;

public class ListController extends MainVistaNavigator{
    private Client client;
    private String type;

    @FXML
    private ListView<UserContent> listView;

    @FXML
    public void initialize() {
        MenuItem info, chat, delete;
        ContextMenu contextMenu;
        info = new MenuItem("info");
        chat = new MenuItem("chat");
        delete = new MenuItem("delete");
        contextMenu = new ContextMenu(info, chat, delete);
        listView.setCellFactory(ContextMenuListCell.forListView(contextMenu, (listView) -> new ChatListCell()));
        listView.setOnMouseClicked(event -> {
            UserContent selectedContent = listView.getSelectionModel().getSelectedItem();
            //TODO: test wich kind of content it is, then jump to it with MaWiCo.selectContent(currentContent)
            //TODO: jump to the specific chat
        });
        info.setOnAction(e -> showInfo(listView.getSelectionModel().getSelectedItem().display()));  //TODO:
    }

    private void showInfo(String info) {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/quap/desktopapp/popup/popup.fxml"));
        Stage primaryStage = (Stage) Stage.getWindows().stream().filter(Window::isShowing).findFirst().orElse(null);
        SceneController.submitPopup(loader, primaryStage, info);
    }

    @Override
    public void loadContent(List<UserContent> content) {
        for (UserContent c : content) {
            listView.getItems().add(c);
        }
    }

    @Override
    public void setClient(Client client) {
        this.client = client;
    }

    @Override
    public void setType(String id) {
        this.type = id;
    }

    public void addUserContent(ActionEvent actionEvent) {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/quap/desktopapp/popup/inputPopup.fxml"));
        Stage primaryStage = (Stage) Stage.getWindows().stream().filter(Window::isShowing).findFirst().orElse(null);
        SceneController.submitInputPopup(loader, primaryStage, type);
    }

    public static class ContextMenuListCell<T> extends ListCell<T> {
        public static <T> Callback<ListView<T>, ListCell<T>> forListView(final ContextMenu contextMenu, final Callback<ListView<T>, ListCell<T>> cellFactory) {
            return listView -> {
                ListCell<T> cell = cellFactory.call(listView);
                cell.setContextMenu(contextMenu);
                return cell;
            };
        }
        public ContextMenuListCell(ContextMenu contextMenu) {
            setContextMenu(contextMenu);
        }
    }

    private class ChatListCell extends ListCell<UserContent> {
        @Override
        protected void updateItem(UserContent content, boolean isEmpty) {
            super.updateItem(content, isEmpty);
            if(content != null && !isEmpty) {
                //setDisabled(false);
                setText(content.display());
            }
        }
    }
}
