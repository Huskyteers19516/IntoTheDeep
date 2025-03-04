# Information

## Driver Flow

1. Driver drives over to the submersible
2. Driver presses `X` to extend the intake
3. Driver presses left and right on the D-Pad to change the intake extension length
4. Driver presses `down` on the D-Pad to lower the claw
5. Driver presses `Y` to grab the sample, and retract the intake into the robot
6. Driver drives over to the basket
7. Driver presses `Y` to extend the outtake to the basket
8. Driver presses `Y` to release the sample, and retract claw

## Robot Claw States

```mermaid
stateDiagram-v2
    direction LR
    classDef edgeLabel color: white, width: 30px, height: 30px, border-radius: 100%, background-color: green
    GoingToTop: Going to top
    Top: At top
    MovingClawDown: Moving claw down
    ClawDown: Claw down
    PickingUp: Picking up
    PickedUp: Picked up
    [*] --> Retracted
    Retracted --> Extending: X
    Extending --> Extended
    Extended --> Retracting: X
    Retracting --> Retracted
    Extended --> MovingClawDown: Down
    MovingClawDown --> ClawDown
    ClawDown --> MovingClawUp: Up
    MovingClawUp --> Extended
    ClawDown --> PickingUp: Y
    PickingUp --> PickedUp
    PickedUp --> Extending: X
    PickedUp --> GoingToTop: Y
    GoingToTop --> Top
    Top --> Dropping: Y
    Dropping --> Retracted
```

## Robot States Without Transitions

```mermaid
stateDiagram-v2
    direction LR
    classDef edgeLabel color: white, width: 30px, height: 30px, border-radius: 100%, background-color: green
    Top: At top
    ClawDown: Claw down
    PickedUp: Picked up
    [*] --> Retracted
    Retracted --> Extended: X
    Extended --> Retracted: X
    Extended --> ClawDown: Down
    ClawDown --> Extended: Up
    ClawDown --> PickedUp: Y
    PickedUp --> Extended: X
    PickedUp --> Top: Y
    note left of Top: When you press Y, it dumps the sample and retracts
    Top --> Retracted: Y
```

## Controls

### Gamepad 1

|     Button     | Action                                                             |
|:--------------:|:-------------------------------------------------------------------|
|     Start      | Reset heading for field centric                                    |
|       A        | Switch between tank and field centric                              |
|       B        | Switch between fast and slow outtake mode                          |
|       X        | Extend or retract intake slide, or cancel pick up from submersible |
|       Y        | Pick up sample, extend outtake slide, or drop sample.              |
| Left Joystick  | Move robot                                                         |
| Right Joystick | Rotate robot                                                       |
|  Left Trigger  | Speed up                                                           |
| Right Trigger  | Slow down                                                          |
|    D-Pad Up    | Intake claw up                                                     |
|   D-Pad Down   | Intake claw down                                                   |
|   D-Pad Left   | Retract intake or outtake                                          |
|  D-Pad Right   | Extend intake or outtake                                           |

### Gamepad 2

No controls

## Wiring

### Motors

| Name                    | Location      | Port |
|-------------------------|---------------|------|
| leftFront               | Expansion Hub | 2    |
| leftBack                | Expansion Hub | 3    |
| rightFront              | Control Hub   | 1    |
| rightBack               | Control Hub   | 0    |
| leftTorqueOuttakeSlide  | Expansion Hub | 1    |
| leftSpeedOuttakeSlide   | Expansion Hub | 0    |
| rightTorqueOuttakeSlide | Control Hub   | 2    |
| rightSpeedOuttakeSlide  | Control Hub   | 3    |

### Servos

| Name                     | Location      | Port |
|--------------------------|---------------|------|
| leftIntakeSlide          | Expansion Hub | 0    |
| rightIntakeSlide         | Control Hub   | 0    |
| leftIntakeClawRotator    | Expansion Hub | 1    |
| rightIntakeClawRotator   | Control Hub   | 1    |
| intakeClawGrabber        | Control Hub   | 2    |
| intakeClawGrabberRotator | Control Hub   | 3    |
| outtakeClawRotator       | Expansion Hub | 2    |
| outtakeClawGrabber       | Control Hub   | 5    |

### Analog Inputs

| Name                    | Location      | Port |
|-------------------------|---------------|------|
| leftIntakeSlideEncoder  | Expansion Hub | 0    |
| rightIntakeSlideEncoder | Control Hub   | 0    |

### Digital Inputs

| Name                | Location    | Port |
|---------------------|-------------|------|
| outtakeBottomSwitch | Control Hub | 0    |

## Class Diagram

![Class Diagram](class_diagram.png)
