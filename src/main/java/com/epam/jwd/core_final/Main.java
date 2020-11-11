package com.epam.jwd.core_final;

import com.epam.jwd.core_final.context.Application;
import com.epam.jwd.core_final.context.ApplicationMenu;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class Main {
    static final Logger logger = LoggerFactory.getLogger("JWDCourseLogger");

    public static void main(String[] args) {
        try {
            logger.trace("Main: The application has been started.");

            ApplicationMenu mainMenu = Application.start();
            logger.trace("Main: ApplicationMenu has been initialised.");

            while (true) {
                mainMenu.printAvailableOptions();
                if ((Integer)mainMenu.handleUserInput(null) == 1) return;
            }

        } catch (Exception e) {
            logger.error("Main: Exception: " + e.getMessage());
            e.printStackTrace();
        }
    }
}