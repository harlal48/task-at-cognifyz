import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class Main extends JFrame {
    private JTextField inputField; 
    private JLabel resultLabel;  
    private JRadioButton cToFButton, fToCButton;

    public Main() {
        setTitle("Temperature Converter");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(400, 300);
        setLayout(new FlowLayout());

        // Step 1: Input field for temperature
        add(new JLabel("Enter Temperature:"));
        inputField = new JTextField(10);
        add(inputField);

        // Step 2: Radio buttons for conversion options
        cToFButton = new JRadioButton("Celsius to Fahrenheit");
        fToCButton = new JRadioButton("Fahrenheit to Celsius");

        // Group buttons to allow only one selection
        ButtonGroup group = new ButtonGroup();
        group.add(cToFButton);
        group.add(fToCButton);

        add(cToFButton);
        add(fToCButton);

        // Step 3: Button to trigger conversion
        JButton convertButton = new JButton("Convert");
        add(convertButton);

        // Step 4: Result label
        resultLabel = new JLabel("Result: ");
        add(resultLabel);

        // Action listener for the convert button
        convertButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                convertTemperature();
            }
        });

        setVisible(true);
    }

    // Conversion logic
    private void convertTemperature() {
        try {
            double temperature = Double.parseDouble(inputField.getText());

            if (cToFButton.isSelected()) {
                // Celsius to Fahrenheit
                double result = (temperature * 9 / 5) + 32;
                resultLabel.setText(String.format("Result: %.2f Celsius = %.2f Fahrenheit", temperature, result));
            } else if (fToCButton.isSelected()) {
                // Fahrenheit to Celsius
                double result = (temperature - 32) * 5 / 9;
                resultLabel.setText(String.format("Result: %.2f Fahrenheit = %.2f Celsius", temperature, result));
            } else {
                resultLabel.setText("Please select a conversion direction.");
            }
        } catch (NumberFormatException ex) {
            resultLabel.setText("Invalid input. Please enter a valid number.");
        }
    }

    public static void main(String[] args) {
        new Main();
    }
}
