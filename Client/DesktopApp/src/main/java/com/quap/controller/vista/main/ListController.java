package com.quap.controller.vista.main;

import com.quap.client.Client;
import com.quap.client.domain.UserContent;
import com.quap.controller.SceneController;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.control.ContextMenu;
import javafx.scene.control.ListCell;
import javafx.scene.control.ListView;
import javafx.scene.control.MenuItem;
import javafx.stage.Stage;
import javafx.util.Callback;

import java.util.List;

public class ListController extends MainVistaNavigator {
    private Client client;
    private String type;

    @FXML
    private ListView<UserContent> listView;

    @FXML
    public void initialize() {
        MenuItem info, chat, delete, invite;
        ContextMenu contextMenu;
        info = new MenuItem("info");
        chat = new MenuItem("chat");
        delete = new MenuItem("delete");
        invite = new MenuItem("invite");
        contextMenu = new ContextMenu(info, chat, delete, invite);
        listView.setCellFactory(ContextMenuListCell.forListView(contextMenu, (listView) -> new ChatListCell()));
        listView.setOnMouseClicked(event -> {
            UserContent selectedContent = listView.getSelectionModel().getSelectedItem();
            //TODO: test wich kind of content it is, then jump to it with MaWiCo.selectContent(currentContent)
            //TODO: jump to the specific chat
        });
        //TODO: is null
        info.setOnAction(event -> showInfo(listView.getSelectionModel().getSelectedItem().display(), event));
        invite.setOnAction(new EventHandler<>() {
            @Override
            public void handle(ActionEvent actionEvent) {
                System.out.println("Invite to a chatroom");
                FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/quap/desktopapp/popup/inputPopup.fxml"));
                Stage primaryStage = (Stage) invite.getParentPopup().getOwnerWindow();
                String input = SceneController.submitInputPopup(loader, primaryStage);
                if (input != null && !input.equals("")) {
                    switch (type) {
                        case "chatrooms" -> {
                            int chatId = listView.getSelectionModel().getSelectedItem().getId(); //TODO: is 0
                            client.inviteUser(input, chatId);
                        }
                        case "friends" -> {
                            //TODO: get chatID by the chatroom name as input and the user as selectionModel()
                        }
                    }
                }
            }
        });
    }

    private void showInfo(String info, ActionEvent actionEvent) {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/quap/desktopapp/popup/popup.fxml"));
        Stage primaryStage = (Stage) ((Node) actionEvent.getSource()).getScene().getWindow();
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

    @Override
    public String getType() {
        return type;
    }

    public void addUserContent(ActionEvent actionEvent) {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/quap/desktopapp/popup/inputPopup.fxml"));
        Stage primaryStage = (Stage) ((Node) actionEvent.getSource()).getScene().getWindow();
        String input = SceneController.submitInputPopup(loader, primaryStage);
        if (input != null && !input.equals("")) {
            if (type.equals("chatrooms")) {
                System.out.println("erstelle chatroom: " + input);
                //TODO: erstelle den chatroom anfrage zum server
                // Server Sendet chatroom zurück und UI wird über observer updated
                // add a boolean to the participant table, which shows, if the current user has already joined or is just invited
                client.addChatroom(input);
            } else if (type.equals("friends")) {
                System.out.println("request user: " + input);
                //TODO: erstelle den chatroom anfrage zum server
                // Server Sendet chatroom zurück und UI wird über observer updated
                // check if there if only one entrance(just request) in the friends table or two(solid friends)
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
