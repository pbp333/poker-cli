package com.isec.pokercli.cli;

import com.isec.pokercli.cli.actions.ExitAction;
import com.isec.pokercli.cli.controller.CliController;
import com.isec.pokercli.cli.controller.CliControllerImpl;

import java.util.Scanner;

public class App {

    public static void main(String[] args) {
        System.out.println("Starting poker-cli...");

        var sc = new Scanner(System.in);

        CliController controller = new CliControllerImpl();

        var running = true;
        while (running) {
            var input = sc.nextLine();

            try {
                var inputSplit = input.split(" ");

                controller.handleAction(inputSplit);

            } catch (ExitAction e) {
                running = false;
                System.out.println("Exit");
            } catch (Throwable e) {
                System.out.println("User action invalid - " + input);
                System.out.println(e.getMessage());
            }
        }
    }
}
