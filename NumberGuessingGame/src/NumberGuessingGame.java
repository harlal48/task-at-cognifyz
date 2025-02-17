import javax.swing.*;
import java.util.Random;

class NumberGuessingGame {

    public static void main(String[] args) {
        Random random = new Random();
        int numberToGuess = random.nextInt(10) + 1;
        int numberOfGuesses = 0;
        int playerGuess = -1;

        JOptionPane.showMessageDialog(null, "Welcome to the Number Guessing Game!\nI have selected a number between 1 and 10.\nTry to guess it!", "Number Guessing Game", JOptionPane.INFORMATION_MESSAGE);

        while (playerGuess != numberToGuess) {
            String input = JOptionPane.showInputDialog(null, "Enter your guess:", "Guess the Number", JOptionPane.QUESTION_MESSAGE);
            
            if (input == null) { 
                JOptionPane.showMessageDialog(null, "Game Over! You exited the game.", "Exit", JOptionPane.WARNING_MESSAGE);
                return;
            }

            try {
                playerGuess = Integer.parseInt(input);
                numberOfGuesses++;

                if (playerGuess < numberToGuess) {
                    JOptionPane.showMessageDialog(null, "Your guess is too low. Try again!", "Hint", JOptionPane.WARNING_MESSAGE);
                } else if (playerGuess > numberToGuess) {
                    JOptionPane.showMessageDialog(null, "Your guess is too high. Try again!", "Hint", JOptionPane.WARNING_MESSAGE);
                } else {
                    JOptionPane.showMessageDialog(null, "Congratulations! You've guessed the correct number!\nIt took you " + numberOfGuesses + " guesses.", "Winner!", JOptionPane.INFORMATION_MESSAGE);
                }
            } catch (NumberFormatException e) {
                JOptionPane.showMessageDialog(null, "Invalid input! Please enter a valid number.", "Error", JOptionPane.ERROR_MESSAGE);
            }
        }
    }
}
