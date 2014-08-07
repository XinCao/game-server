package com.xincao.game.server.model;

public class Node {

    private Node owner;

    public Node() {
    }

    public Node(Node owner) {
        this.owner = owner;
    }

    public Node getNode() {
        return this.owner;
    }

    public void setOwner(Node owner) {
        this.owner = owner;
    }
}