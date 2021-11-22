package com.quap.controller.vista.main;

import com.quap.client.Client;
import com.quap.client.domain.UserContent;
import javafx.fxml.FXML;
import javafx.scene.control.ContextMenu;
import javafx.scene.control.ListCell;
import javafx.scene.control.ListView;
import javafx.scene.control.MenuItem;
import javafx.util.Callback;

import java.util.List;

public class ListController extends MainVistaNavigator{
    private Client client;

    @FXML
    private ListView<UserContent> listView = new ListView<UserContent>();

    @FXML
    public void initialize() {
        MenuItem info, chat, delete;
        ContextMenu contextMenu;
        info = new MenuItem("show history");
        chat = new MenuItem("play");
        delete = new MenuItem("delete");
        contextMenu = new ContextMenu(info, chat, delete);
        listView.setCellFactory(ContextMenuListCell.forListView(contextMenu, (listView) -> new ChatListCell()));
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
                setText(content.display());
            }
        }
    }
}
