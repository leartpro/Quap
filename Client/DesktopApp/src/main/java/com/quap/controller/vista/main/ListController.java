package com.quap.controller.vista.main;

import com.quap.client.Client;
import com.quap.client.domain.Chat;
import com.quap.client.domain.Content;
import com.quap.client.domain.UserContent;
import com.quap.controller.SceneController;
import com.quap.controller.vista.MainVistaObserver;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.control.ContextMenu;
import javafx.scene.control.ListCell;
import javafx.scene.control.ListView;
import javafx.scene.control.MenuItem;
import javafx.stage.Stage;
import javafx.util.Callback;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class ListController extends MainVistaNavigator {
    private Client client;
    private String type;
    private final List<MainVistaObserver> observers = new ArrayList<>();


    @FXML
    private ListView<UserContent> listView;

    @FXML
    public void initialize() {
        MenuItem infoItem, chatItem, deleteItem, inviteItem;
        ContextMenu contextMenu;
        infoItem = new MenuItem("info");
        chatItem = new MenuItem("chat");
        deleteItem = new MenuItem("delete");
        inviteItem = new MenuItem("invite");
        contextMenu = new ContextMenu(infoItem, chatItem, deleteItem, inviteItem);
        listView.setCellFactory(ContextMenuListCell.forListView(contextMenu, (listView) -> new ChatListCell()));
        listView.setOnMouseClicked(event -> {
            UserContent selectedContent = listView.getSelectionModel().getSelectedItem();
            //TODO: test wich kind of content it is, then jump to it(specific chat) with MaWiCo.selectContent(currentContent)
        });
        infoItem.setOnAction(event -> {
            showInfo(listView.getSelectionModel().getSelectedItem().display(), infoItem);
        });
        chatItem.setOnAction(event -> {
            //TODO:
        });
        deleteItem.setOnAction(event -> {
            //TODO: listview has ui bugs on delete
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/quap/desktopapp/popup/requestPopup.fxml"));
            Stage primaryStage = (Stage) deleteItem.getParentPopup().getOwnerWindow();
            boolean decision = SceneController.submitRequestPopup(loader, primaryStage, Collections.singletonList("Are you sure to leave and delete this chat?"));
            if (decision) {
                //TODO: leave chat
                //TODO: confirm popup
                //TODO: popups should contain theme specific header label
                Chat chat = (Chat) listView.getSelectionModel().getSelectedItem(); //TODO: is 0
                client.deleteChat(chat);
                //listView.getItems().clear();
                System.out.println("leave chat action...");
                System.out.println(observers.size());
                for (MainVistaObserver c : observers) { //TODO: next index of elemtent is bigger than the total amount of elements
                    System.out.println(c);
                    c.deleteChatEvent(chat);
                }
            }
        });
        inviteItem.setOnAction(event -> {
            System.out.println("Invite to a chatroom");
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/quap/desktopapp/popup/inputPopup.fxml"));
            Stage primaryStage = (Stage) inviteItem.getParentPopup().getOwnerWindow();
            String input = SceneController.submitInputPopup(loader, primaryStage);
            if (input != null && !input.equals("")) {
                switch (type) {
                    case "chatrooms" -> {
                        int chatId = listView.getSelectionModel().getSelectedItem().id(); //TODO: is 0
                        client.inviteUser(input, chatId);
                    }
                    case "friends" -> {
                        //TODO: get chatID by the chatroom name as input and the user as selectionModel()
                    }
                }
            }
        });
    }

    private void showInfo(String info, MenuItem item) {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/quap/desktopapp/popup/popup.fxml"));
        Stage primaryStage = (Stage) item.getParentPopup().getOwnerWindow();
        SceneController.submitPopup(loader, primaryStage, info);
    }

    @Override
    public void loadContent(List<Content> content) {
        for (Content c : content) {
            listView.getItems().add((UserContent) c);
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

    @Override
    public String getType() {
        return type;
    }

    @Override
    public void addObserver(MainVistaObserver observer) {
        observers.add(observer);
    }

    @Override
    public void removeObserver(MainVistaObserver observer) {
        observers.remove(observer);
    }

    public void addUserContent(ActionEvent actionEvent) {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/quap/desktopapp/popup/inputPopup.fxml"));
        Stage primaryStage = (Stage) ((Node) actionEvent.getSource()).getScene().getWindow();
        String input = SceneController.submitInputPopup(loader, primaryStage);
        if (input != null && !input.equals("")) {
            if (type.equals("chatrooms")) {
                System.out.println("erstelle chatroom: " + input);
                client.addChatroom(input);
            } else if (type.equals("friends")) {
                System.out.println("request user: " + input);
                //TODO: check if there if only one entrance(just request) in the friends table or two(solid friends)
                client.addFriend(input);
            }
        }
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

    private static class ChatListCell extends ListCell<UserContent> {
        @Override
        protected void updateItem(UserContent content, boolean isEmpty) {
            super.updateItem(content, isEmpty);
            if (content != null && !isEmpty) {
                //setDisabled(false);
                setText(content.toString());
            }
        }
    }
}
