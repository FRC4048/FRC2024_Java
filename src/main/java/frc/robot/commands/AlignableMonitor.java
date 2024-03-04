package frc.robot.commands;

import edu.wpi.first.wpilibj2.command.Command;
import frc.robot.utils.Alignable;

import java.util.function.Supplier;

public class AlignableMonitor extends Command {
    private final Supplier<Alignable> alignableSupplier;
    private Alignable alignable;
    public AlignableMonitor(Supplier<Alignable> alignableSupplier) {
        this.alignableSupplier = alignableSupplier;
    }

    @Override
    public void initialize() {
        alignable = alignableSupplier.get();
    }

    @Override
    public boolean isFinished() {
        return alignable == null || alignableSupplier.get() == null || !alignable.equals(alignableSupplier.get());
    }
}
