// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.

package frc.robot.subsystems;

import edu.wpi.first.wpilibj.Timer;
import edu.wpi.first.wpilibj2.command.SubsystemBase;
import frc.robot.Robot;
import frc.robot.utils.diag.DiagBoolean;

public class ExampleSubsystem extends SubsystemBase {
    public ExampleSubsystem() {
        Robot.getDiagnostics().addDiagnosable(new DiagBoolean("Diags", "TEST DIAG") {
            @Override
            protected boolean getValue() {
                return Timer.getFPGATimestamp() > 10;
            }
        });
    }
}
