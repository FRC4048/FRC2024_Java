package frc.robot.commands;

import frc.robot.utils.Alignable;
import frc.robot.utils.loggingv2.LoggableCommand;

import java.util.function.Supplier;

public class AlignableMonitor extends LoggableCommand {
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
