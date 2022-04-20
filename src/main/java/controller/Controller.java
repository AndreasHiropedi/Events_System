package controller;

import java.util.ArrayList;
import java.util.List;
import command.ICommand;

public class Controller
{

    public List<Context> snapshots;
    private Context context;

    public Controller()
    {
        snapshots = new ArrayList<>();
        context = new Context();
    }

    public void runCommand(ICommand command)
    {
        command.execute(context);
    }

    private void saveSnapshot()
    {
        snapshots.add(context);
    }

    public void restoreSnapshot(int index)
    {
        context = snapshots.get(index);
    }

    public Context getContext()
    {
        return context;
    }

}
