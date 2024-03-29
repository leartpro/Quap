package com.quap.controller.vista.main;

import com.quap.client.Client;
import com.quap.client.domain.Chat;
import com.quap.client.domain.Content;
import com.quap.client.domain.Friend;
import com.quap.client.domain.UserContent;
import com.quap.controller.SceneController;
import com.quap.controller.vista.MainVistaObserver;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.control.ContextMenu;
import javafx.scene.control.ListCell;
import javafx.scene.control.ListView;
import javafx.scene.control.MenuItem;
import javafx.stage.Stage;
import javafx.stage.Window;
import javafx.util.Callback;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class ListController extends MainVistaNavigator {
    private Client client;
    private String type;
    private final List<MainVistaObserver> observers = new ArrayList<>();
    private final MenuItem infoItem = new MenuItem("info");

    @FXML
    private ListView<UserContent> listView;

    @FXML
    public void initialize() {
        final MenuItem deleteItem, inviteItem;
        final ContextMenu contextMenu;
        deleteItem = new MenuItem("delete");
        inviteItem = new MenuItem("invite");
        contextMenu = new ContextMenu(infoItem, deleteItem, inviteItem);
        listView.setCellFactory(ContextMenuListCell.forListView(contextMenu, (listView) -> new ChatListCell()));
        infoItem.setOnAction(event -> showInfo(listView.getSelectionModel().getSelectedItem().display(), infoItem));
        deleteItem.setOnAction(event -> {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/quap/desktopapp/popup/requestPopup.fxml"));
            Stage primaryStage = (Stage) deleteItem.getParentPopup().getOwnerWindow();
            switch (type) {
                case "chatrooms" -> {
                    boolean decision = SceneController.submitRequestPopup(loader, primaryStage, Collections.singletonList("Are you sure to leave and delete this chat?"), "Delete this chatroom?");
                    if (decision) {
                        Chat chat = (Chat) listView.getSelectionModel().getSelectedItem();
                        client.deleteChat(chat);
                        System.out.println(observers.size());
                        for (MainVistaObserver c : observers) {
                            c.deleteChatEvent();
                        }
                    }
                }
                case "friends" -> {
                    boolean decision = SceneController.submitRequestPopup(loader, primaryStage, Collections.singletonList("Are you sure to unfriend this user?"), "Unfriend this user?");
                    if (decision) {
                        Friend friend = (Friend) listView.getSelectionModel().getSelectedItem();
                        client.unfriendUser(friend);
                        for (MainVistaObserver c : observers) {
                            System.out.println(c);
                            c.unfriendEvent();
                        }
                    }
                }
            }
        });
        inviteItem.setOnAction(event -> {
            System.out.println("Invite to a chatroom");
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/quap/desktopapp/popup/inputPopup.fxml"));
            Stage primaryStage = (Stage) inviteItem.getParentPopup().getOwnerWindow();
            String input = SceneController.submitInputPopup(loader, primaryStage, "Type the username you want to join your chatroom");
            if (input != null && !input.equals("")) {
                if(!input.equals(client.getUsername())) {
                    int chatId = listView.getSelectionModel().getSelectedItem().id();
                    client.inviteUser(input, chatId);
                } else {
                    Platform.runLater(() -> SceneController.submitPopup(
                            new FXMLLoader(getClass().getResource("/com/quap/desktopapp/popup/popup.fxml")),
                            (Stage) Stage.getWindows().stream().filter(Window::isShowing).findFirst().orElse(null),
                            "You can't invite yourself to a chatroom!",
                            "Information")
                    );
                }
            }
        });
    }

    private void showInfo(String info, MenuItem item) {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/quap/desktopapp/popup/popup.fxml"));
        Stage primaryStage = (Stage) item.getParentPopup().getOwnerWindow();
        SceneController.submitPopup(loader, primaryStage, info, "Information");
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
        if (type.equals("friends")) {
            infoItem.setDisable(true);
        }
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

    @FXML
    public void addUserContent(ActionEvent actionEvent) {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/quap/desktopapp/popup/inputPopup.fxml"));
        Stage primaryStage = (Stage) ((Node) actionEvent.getSource()).getScene().getWindow();
        String header = null;
        if(type.equals("chatrooms")) {
            header = "Type the name of the chatroom you want to create";
        } else if(type.equals("friends")) {
            header = "Type the name of the user you want to become your friend";
        }
        String input = SceneController.submitInputPopup(loader, primaryStage, header);
        if (input != null && !input.equals("")) {
            if (type.equals("chatrooms")) {
                client.addChatroom(input);
            } else if (type.equals("friends")) {
                boolean isFriend = false;
                for(UserContent friend : client.getFriends()) {
                    if(input.equals(friend.content())) {
                        isFriend = true;
                    }
                }
                if(!input.equals(client.getUsername()) && !isFriend) {
                    client.addFriend(input);
                } else {
                    Platform.runLater(() -> SceneController.submitPopup(
                            new FXMLLoader(getClass().getResource("/com/quap/desktopapp/popup/popup.fxml")),
                            (Stage) Stage.getWindows().stream().filter(Window::isShowing).findFirst().orElse(null),
                            "You can't add " + input + " as your friend!",
                            "Information")
                    );
                }
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
    }

    private static class ChatListCell extends ListCell<UserContent> {
        @Override
        protected void updateItem(UserContent content, boolean isEmpty) {
            super.updateItem(content, isEmpty);
            if (content != null && !isEmpty) {
                //setDisabled(false);
                setText(content.content());
            }
        }
    }
}
