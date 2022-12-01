package org.firstinspires.ftc.teamcode;

//TODO: make a subclass called Toggle
public class Button {
    private boolean currentlyPressed; // Whether the button was pushed during the most recent call of update()
    private boolean previouslyPressed; // Whether the button was pushed during the previous call of update()

    public Button(boolean firstValue) {
        this.currentlyPressed = firstValue;
        this.previouslyPressed = firstValue;
    }

    // Should be called once right before the while loop in TeleOp
    // and at the beginning of the loop
    public void update(boolean nextValue) {
        previouslyPressed = currentlyPressed;
        currentlyPressed = nextValue;
    }

    public boolean isCurrentlyPressed() {
        return currentlyPressed;
    }

    public boolean wasPreviouslyPressed() {
        return previouslyPressed;
    }

    public boolean isNewlyPressed() {
        return (currentlyPressed && !previouslyPressed);
    }

    public boolean isNewlyReleased() {
        return (previouslyPressed && !currentlyPressed);
    }
}