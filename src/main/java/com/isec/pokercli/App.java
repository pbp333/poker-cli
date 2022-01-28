package com.isec.pokercli;

import com.isec.pokercli.constants.Commands;

import java.util.Optional;
import java.util.Scanner;

public class App {

    public static void main(String[] args) {
        System.out.println("Starting poker-cli...");

        boolean running = true;
        Scanner sc = new Scanner(System.in);

        while (running) {
            String input = sc.nextLine();

            String[] inputSplit = input.split(" ");
            if (inputSplit.length == 0) {
                System.out.println("Invalid input");
                continue;
            }

            Optional<Commands> c = Commands.getByCommandString(inputSplit[0]);
            if (c.isEmpty()) {
                continue;
            }

            switch (c.get()) {
                case CREATE_GAME:
                    System.out.println("Game created");
                    break;
                case EXIT:
                    running = false;
                    break;
            }

        }

        System.out.println("Exit");
    }

}
