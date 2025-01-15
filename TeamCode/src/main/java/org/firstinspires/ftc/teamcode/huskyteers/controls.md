# Information

## Robot Claw States

```mermaid
stateDiagram-v2
    Ready: Ready to pick up
    GoingToTop: Going to top
    Top: At top
    Ready --> Retracting: Y
    Retracting --> Retracted
    Retracted --> Extending: Y
    Extending --> Ready
    Ready --> GoingToTop: X
    GoingToTop --> Top
    Top --> Releasing: X
    Releasing --> Retracted
```

## Simplified Claw States for Drivers

```mermaid
stateDiagram-v2
    Ready: Ready to pick up
    Top: At top
    Ready --> Retracted: Y
    Retracted --> Ready: Y
    Ready --> Top: X
    Top --> Retracted: X
```

## Controls

### Gamepad 1

|     Button     | Action                                                    |
|:--------------:|:----------------------------------------------------------|
|     Start      | Reset heading for field centric                           |
|       A        | Switch between tank and field centric                     |
|       B        | Nothing                                                   |
|       X        | Grab sample and raise to basket, or release sample at top |
|       Y        | Nothing                                                   |
| Left Joystick  | Move robot                                                |
| Right Joystick | Rotate robot                                              |
|  Left Trigger  | Nothing                                                   |
| Right Trigger  | Nothing                                                   |
|  Right Bumper  | Nothing                                                   |
|  Left Trigger  | Nothing                                                   |

### Gamepad 2

No controls