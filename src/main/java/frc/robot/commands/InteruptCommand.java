package frc.robot.commands;

import edu.wpi.first.wpilibj2.command.Command;

public class InteruptCommand extends Command {
    private final Command command;

    public InteruptCommand(Command command) {
        this.command = command;
    }

    @Override
    public void initialize() {
        command.end(true);
    }

    @Override
    public boolean isFinished() {
        return true;
    }
}
