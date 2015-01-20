package anastasoft.rallyvision.command;

import android.app.Application;
import android.content.Context;

import anastasoft.rallyvision.controller.Controller;

public abstract class Command {

    Controller aController;

    public Command(Context context) {
        // TODO Auto-generated constructor stub
        aController = (Controller) context.getApplicationContext();
    }

    public Command(Application controller) {
        aController = (Controller) controller;
    }

    public abstract void Execute();

}
