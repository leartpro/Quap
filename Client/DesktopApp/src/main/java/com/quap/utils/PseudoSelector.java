package com.quap.utils;

import javafx.beans.value.WritableValue;
import javafx.css.PseudoClass;
import javafx.scene.Node;
import javafx.scene.Parent;

import java.util.List;

public class PseudoSelector extends Node {

    private static final PseudoClass NTH_OF_TYPE_PSEUDO_CLASS = PseudoClass.getPseudoClass("nth-of-type()");

    WritableValue<List<Node>> nth_of_type;

    public PseudoSelector(Node node) {
        Parent root = node.getScene().getRoot();
        for(int i=0; i<root.getChildrenUnmodifiable().size(); i++)
            if(root.getChildrenUnmodifiable().get(i).equals(node.getClass())) {
                //your code here
            }
    }

}
