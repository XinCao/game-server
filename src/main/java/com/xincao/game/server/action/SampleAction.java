package com.xincao.game.server.action;

import com.xincao.action_container.AbstractAction;
import com.xincao.action_container.Action;

@Action(option = "sample")
public class SampleAction extends AbstractAction {

    private Integer id;
    private String name;
    private Integer age;

    @Override
    public void setArguments(Object... args) throws NumberFormatException {
        id = (Integer) args[0];
        name = (String) args[1];
        age = (Integer) args[2];
    }

    @Override
    public boolean canPerform(Object... args) {
        return true;
    }

    @Override
    public void perform(Object... args) {
    	logger.info(this.toString());
    }

    @Override
    public String toString() {
        return "SampleAction{" + "id=" + id + ", name=" + name + ", age=" + age + '}';
    }
}
