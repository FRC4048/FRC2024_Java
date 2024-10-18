package frc.robot.subsystems.swervev3.estimation;

import edu.wpi.first.math.VecBuilder;
import edu.wpi.first.math.Vector;
import edu.wpi.first.math.estimator.SwerveDrivePoseEstimator;
import edu.wpi.first.math.geometry.Pose2d;
import edu.wpi.first.math.geometry.Rotation2d;
import edu.wpi.first.math.geometry.Transform2d;
import edu.wpi.first.math.geometry.Translation2d;
import edu.wpi.first.math.interpolation.TimeInterpolatableBuffer;
import edu.wpi.first.math.kinematics.SwerveDriveKinematics;
import edu.wpi.first.math.kinematics.SwerveModulePosition;
import edu.wpi.first.math.numbers.N3;
import edu.wpi.first.wpilibj.DriverStation;
import edu.wpi.first.wpilibj.smartdashboard.Field2d;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import frc.robot.Robot;
import frc.robot.constants.Constants;
import frc.robot.subsystems.LoggableSystem;
import frc.robot.subsystems.apriltags.ApriltagIO;
import frc.robot.subsystems.apriltags.ApriltagInputs;
import frc.robot.subsystems.swervev3.bags.OdometryMeasurement;
import frc.robot.subsystems.swervev3.bags.VisionMeasurement;
import frc.robot.subsystems.swervev3.io.Module;
import frc.robot.subsystems.swervev3.vision.BasicVisionFilter;
import frc.robot.subsystems.swervev3.vision.BasicVisionFilter2;
import frc.robot.utils.RobotMode;
import frc.robot.utils.advanced.Apriltag;
import frc.robot.utils.math.ArrayUtils;
import org.littletonrobotics.junction.AutoLogOutput;
import org.littletonrobotics.junction.Logger;

public class PoseEstimator {
    private final Field2d field = new Field2d();
    private final Module frontLeft;
    private final Module frontRight;
    private final Module backLeft;
    private final Module backRight;
    private final LoggableSystem<ApriltagIO, ApriltagInputs> apriltagSystem;

    /* standard deviation of robot states, the lower the numbers arm, the more we trust odometry */
    private static final Vector<N3> stateStdDevs1 = VecBuilder.fill(0.05, 0.05, 0.001);

    /* standard deviation of vision readings, the lower the numbers arm, the more we trust vision */
    private static final Vector<N3> visionMeasurementStdDevs1 = VecBuilder.fill(0.5, 0.5, 0.5);
    /* standard deviation of robot states, the lower the numbers arm, the more we trust odometry */
    private static final Vector<N3> stateStdDevs2 = VecBuilder.fill(0.075, 0.075, 0.001);

    /* standard deviation of vision readings, the lower the numbers arm, the more we trust vision */
    private static final Vector<N3> visionMeasurementStdDevs2 = VecBuilder.fill(0.45, 0.45, 0.001);
    private static final Transform2d cameraOneTransform = new Transform2d(Constants.CAMERA_OFFSET_FROM_CENTER_X, Constants.CAMERA_OFFSET_FROM_CENTER_Y, new Rotation2d());
    private static final Transform2d cameraTwoTransform = new Transform2d(Constants.CAMERA_OFFSET_FROM_CENTER_X, Constants.CAMERA_OFFSET_FROM_CENTER_Y, new Rotation2d());
    private final PoseManager poseManager1;
    private final PoseManager poseManager2;

    public PoseEstimator(Module frontLeftMotor, Module frontRightMotor, Module backLeftMotor, Module backRightMotor, ApriltagIO apriltagIO, SwerveDriveKinematics kinematics, double initGyroValueDeg) {
        this.frontLeft = frontLeftMotor;
        this.frontRight = frontRightMotor;
        this.backLeft = backLeftMotor;
        this.backRight = backRightMotor;
        this.apriltagSystem = new LoggableSystem<>(apriltagIO, new ApriltagInputs());
        OdometryMeasurement initMeasurement = new OdometryMeasurement(
                new SwerveModulePosition[]{
                        frontLeft.getPosition(),
                        frontRight.getPosition(),
                        backLeft.getPosition(),
                        backRight.getPosition(),
                }, initGyroValueDeg
        );
        TimeInterpolatableBuffer<Pose2d> m1Buffer = TimeInterpolatableBuffer.createBuffer(Constants.POSE_BUFFER_STORAGE_TIME);
        TimeInterpolatableBuffer<Pose2d> m2Buffer = TimeInterpolatableBuffer.createBuffer(Constants.POSE_BUFFER_STORAGE_TIME);
        this.poseManager1 = new FilterablePoseManager(stateStdDevs1, visionMeasurementStdDevs1, kinematics, initMeasurement, m1Buffer, new BasicVisionFilter(m1Buffer){
            @Override
            public Pose2d getVisionPose(VisionMeasurement measurement) {
                return measurement.measurement().plus(cameraOneTransform);
            }
        });
        this.poseManager2 = new FilterablePoseManager(stateStdDevs2, visionMeasurementStdDevs2, kinematics, initMeasurement, m2Buffer, new BasicVisionFilter2(m2Buffer) {
            @Override
            public Pose2d getVisionPose(VisionMeasurement measurement) {
                return measurement.measurement().plus(cameraOneTransform);
            }
        });
        SmartDashboard.putData(field);
    }

    public void updateInputs() {
        apriltagSystem.updateInputs();
    }

    /**
     * updates odometry, should be called in periodic
     *
     * @see SwerveDrivePoseEstimator#update(Rotation2d, SwerveModulePosition[])
     */
    public void updatePosition(OdometryMeasurement m) {
        if (DriverStation.isEnabled()) {
            poseManager1.addOdomMeasurement(m, Logger.getRealTimestamp());
            poseManager2.addOdomMeasurement(m, Logger.getRealTimestamp());

        }
        field.setRobotPose(poseManager1.getEstimatedPosition());
    }

    private boolean validAprilTagPose(double[] measurement) {
        return !ArrayUtils.allMatch(measurement, -1.0) && measurement.length == 3;
    }

    public void updateVision() {
        if (Robot.getMode().equals(RobotMode.TELEOP) && Constants.ENABLE_VISION) {
            for (int i = 0; i < apriltagSystem.getInputs().timestamp.length; i++) {
                double[] pos = new double[]{
                        apriltagSystem.getInputs().posX[i], apriltagSystem.getInputs().posY[i],
                        apriltagSystem.getInputs().rotationDeg[i]
                };
                if (validAprilTagPose(pos)) {
                    double diff = apriltagSystem.getInputs().serverTime[i] - apriltagSystem.getInputs().timestamp[i];
                    double latencyInSec = diff / 1000;
                    VisionMeasurement measurement = new VisionMeasurement(new Pose2d(pos[0], pos[1], getEstimatedPose1().getRotation()), Apriltag.of(apriltagSystem.getInputs().apriltagNumber[i]), latencyInSec);
                    poseManager1.registerVisionMeasurement(measurement);
                    poseManager2.registerVisionMeasurement(measurement);
                }
            }
        }
    }

    /**
     * @param radians       robot angle to reset odometry to
     * @param translation2d robot field position to reset odometry to
     * @see SwerveDrivePoseEstimator#resetPosition(Rotation2d, SwerveModulePosition[], Pose2d)
     */
    public void resetOdometry(double radians, Translation2d translation2d) {
        OdometryMeasurement initMeasurement = new OdometryMeasurement(
                new SwerveModulePosition[]{
                        frontLeft.getPosition(),
                        frontRight.getPosition(),
                        backLeft.getPosition(),
                        backRight.getPosition(),
                }, (radians*180) / Math.PI
        );
        poseManager1.resetPose(initMeasurement, translation2d);
        poseManager2.resetPose(initMeasurement, translation2d);
        field.setRobotPose(poseManager1.getEstimatedPosition());
    }

    @AutoLogOutput
    public Pose2d getEstimatedPose1() {
        return poseManager1.getEstimatedPosition();
    }
    @AutoLogOutput
    public Pose2d getEstimatedPose2() {
        return poseManager2.getEstimatedPosition();
    }

    public Field2d getField() {
        return field;
    }
}
