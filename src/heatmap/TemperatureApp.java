package heatmap;

import java.io.FileNotFoundException;
import java.util.Scanner;
import javax.swing.*;
import java.awt.BorderLayout;
import java.awt.FlowLayout;

public class TemperatureApp {

    public static void main(String[] args) {
        Scanner input = new Scanner(System.in);
        TemperatureDisplay tempDisplay;
        String option;

        JToggleButton colorSwitch;

        // gets user input for the filename, handles FileNotFoundException
        while (true) {
            try {
                System.out.println("Enter filename: ");
                System.out.println("Type exit to leave the program. ");
                option = input.nextLine();
                if (option.equalsIgnoreCase("exit")) {
                    return;
                }
                TemperatureData tempData = new TemperatureData(option);
                colorSwitch = new JToggleButton();
                tempDisplay = new TemperatureDisplay(tempData, colorSwitch);
                break;
            } catch (FileNotFoundException e) {
                System.out.println("File not found");
            } catch (ArrayIndexOutOfBoundsException | NumberFormatException e) {
                System.out.println("File in incorrect format");
            }
        }

        // creates a frame with a Temperature display object
        JFrame frame = new JFrame("Heatmap for London");
        frame.setSize(600, 600);
        frame.setResizable(false);
        frame.add(colorSwitch);
        frame.addMouseListener(tempDisplay);
        frame.add(tempDisplay);
        frame.setVisible(true);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        // sets initial attributes for the button
        colorSwitch.setBounds(10, 10, 100, 40);
        colorSwitch.setText("No Color");
        colorSwitch.addActionListener(tempDisplay);
    }
}
