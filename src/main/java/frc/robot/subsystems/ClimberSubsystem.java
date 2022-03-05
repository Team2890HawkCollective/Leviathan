/*----------------------------------------------------------------------------*/
/* Copyright (c) 2019 FIRST. All Rights Reserved.                             */
/* Open Source Software - may be modified and shared by FRC teams. The code   */
/* must be accompanied by the FIRST BSD license file in the root directory of */
/* the project.                                                               */
/*----------------------------------------------------------------------------*/

package frc.robot.subsystems;

import com.ctre.phoenix.motorcontrol.can.WPI_TalonFX;
import edu.wpi.first.wpilibj.Joystick;
import edu.wpi.first.wpilibj.XboxController;
import edu.wpi.first.wpilibj2.command.SubsystemBase;
import frc.robot.Constants;

@SuppressWarnings("unused")
public class ClimberSubsystem extends SubsystemBase {

  /**
   * TalonFX Controllers - for Climber (Winch)
   */
  private WPI_TalonFX leftWinchTalonFX = new WPI_TalonFX(Constants.LEFT_WINCH_TALON_FX_ID);
  private WPI_TalonFX rightWinchTalonFX = new WPI_TalonFX(Constants.RIGHT_WINCH_TALON_FX_ID);
  private boolean hasLifted = false;

  /**
   * Xbox controller object used in the case the driver drives with an Xbox
   * controller
   * XButton for shoot Low
   * YButton for shoot High
   */
  private XboxController assistantDriverController = new XboxController(Constants.XBOX_ASSISTANT_DRIVER_CONTROLLER_ID);

  /**
   * Constructor forShooterSubsystem.
   */
  public ClimberSubsystem() {
    stopClimber(); // default state.
  }

  /**
   * Method for using Xbox Controllers for the Climber.
   * When LeftBumper and RightBumper are both pressed, the Climb
   * process will start.
   */
  public void climb() {

    // If the LeftBumper and RightBumper are pressed,
    // the Climb Left process will begin.
    if (assistantDriverController.getLeftBumperPressed() && !assistantDriverController.getRightBumperPressed()) 
    {
      extend();
      hasLifted = true;
    } 
    else if (assistantDriverController.getRightBumperPressed() && !assistantDriverController.getLeftBumperPressed() && hasLifted)
      contract();
    else if (assistantDriverController.getLeftBumperReleased())
      stopClimber();
    else if (assistantDriverController.getRightBumperReleased())
      stopClimber();
  }

  public void climbManual() {
    // If left trigger is pressed, go down.
    if (assistantDriverController.getLeftTriggerAxis() > Constants.CLIBMER_TRIGGER_THRESHOLD)
      leftWinchTalonFX.set((-1) * Constants.LEFT_CLIMB_SPEED * Constants.LEFT_CLIMBER_POLARITY_MOD);
    // If left bumper is pressed, go up.
    else if (assistantDriverController.getLeftBumper())
      leftWinchTalonFX.set(Constants.LEFT_CLIMB_SPEED * Constants.LEFT_CLIMBER_POLARITY_MOD);
    // If right trigger is pressed, go down.
    else if (assistantDriverController.getRightTriggerAxis() > Constants.CLIBMER_TRIGGER_THRESHOLD)
      rightWinchTalonFX.set((-1) * Constants.RIGHT_CLIMB_SPEED * Constants.LEFT_CLIMBER_POLARITY_MOD);
    // If right bumper is pressed, go up.
    else if (assistantDriverController.getRightBumper())
      rightWinchTalonFX.set(Constants.RIGHT_CLIMB_SPEED * Constants.RIGHT_CLIMBER_POLARITY_MOD);
    // If none of the left buttons are pressed, stop.
    else if (!isLeftPressed())
      leftWinchTalonFX.set(Constants.NO_SPEED);
    // If none of the right buttons are pressed, stop.
    else if (!isRightPressed())
      rightWinchTalonFX.set(Constants.NO_SPEED);
  }

  private void extend() {
    leftWinchTalonFX.set(Constants.LEFT_CLIMB_SPEED * Constants.LEFT_CLIMBER_POLARITY_MOD);
    rightWinchTalonFX.set(Constants.RIGHT_CLIMB_SPEED * Constants.RIGHT_CLIMBER_POLARITY_MOD);
  }

  private void contract() {
    leftWinchTalonFX.set((-1) * Constants.LEFT_CLIMB_SPEED * Constants.LEFT_CLIMBER_POLARITY_MOD);
    rightWinchTalonFX.set((-1) * Constants.RIGHT_CLIMB_SPEED * Constants.LEFT_CLIMBER_POLARITY_MOD);
  }

  private void stopClimber() {
    leftWinchTalonFX.set(Constants.NO_SPEED);
    rightWinchTalonFX.set(Constants.NO_SPEED);
  }

  private boolean isLeftPressed() {
    return assistantDriverController.getLeftBumper() && (assistantDriverController.getLeftTriggerAxis() > 0.2);
  }

  private boolean isRightPressed() {
    return assistantDriverController.getRightBumper() && (assistantDriverController.getRightTriggerAxis() > 0.2);
  }

}
